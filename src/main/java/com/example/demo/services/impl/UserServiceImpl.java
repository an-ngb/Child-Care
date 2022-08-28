package com.example.demo.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dtos.*;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Post;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import com.sun.jdi.AbsentInformationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.demo.utils.Utils.loadProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;
    private final PostServiceImpl postServiceImpl;
    private final SessionServiceImpl sessionService;

    public static String generateToken(Map<String, Object> payload, org.springframework.security.core.userdetails.User user) {
        Properties prop = loadProperties("jwt.setting.properties");
        assert prop != null;
        String key = prop.getProperty("key");
        String accessExpired = prop.getProperty("access_expired");
        assert key != null;
        assert accessExpired != null;
        long expiredIn = Long.parseLong(accessExpired);
        Algorithm algorithm = Algorithm.HMAC256(key);

        return JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + expiredIn)).withClaim("user", payload).sign(algorithm);
    }

    @Override
    public AbstractResponse login(LoginRequestDto loginRequestDto) throws RuntimeException {

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INCORRECT_EMAIL_OR_PASSWORD", e);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        User detectedUser = userRepository.findByEmail(email);

        if (detectedUser == null || detectedUser.getDisable()) {
            return new AbstractResponse("FAILED", "FORBIDDEN", 400);
        }

        if (detectedUser.getToken() != null) {
            return new AbstractResponse("FAILED", "ALREADY_LOGGED_IN", 400, detectedUser.getToken());
        }

        Map<String, Object> payload = new HashMap<>();

        payload.put("id", detectedUser.getId());
        payload.put("email", detectedUser.getEmail());
        payload.put("role", detectedUser.getRole().getRoleName());

        Properties prop = loadProperties("jwt.setting.properties");

        String token = generateToken(payload, new org.springframework.security.core.userdetails.User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()));

        detectedUser.setToken(token);

        userRepository.save(detectedUser);

        return new AbstractResponse(new LoginDto("Bearer", token, prop.getProperty("access_expired")));
    }

    @Override
    public AbstractResponse logout(String token) {
        if (!token.startsWith("Bearer")) {
            throw new IllegalArgumentException("Missing Bearer prefix");
        }
        token = token.split(" ")[1];
        User user = userRepository.findByToken(token);
        if (user == null) {
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        } else if (user.getToken() == null) {
            return new AbstractResponse("FAILED", "TOKEN_EXPIRED", 400);
        }
        user.setToken(null);
        userRepository.save(user);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse register(RegisterRequestDto registerRequestDto) {

        User foundUser = userRepository.findByEmail(registerRequestDto.getEmail().trim());

        if (foundUser != null) {
            return new AbstractResponse("FAILED", "EMAIL_EXISTED", 400);
        }

        User user = new User();

        user.setEmail(registerRequestDto.getEmail());
        user.setFullName(registerRequestDto.getFullName());
        user.setPhone(registerRequestDto.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setAge(registerRequestDto.getAge());
        user.setGender(registerRequestDto.getGender());
        user.setRole(roleRepository.findRoleById(registerRequestDto.getRole()));
        user.setDisable(registerRequestDto.getDisable());
        user = userRepository.save(user);

        return new AbstractResponse();
    }

    @Override
    public AbstractResponse getUserProfile(String id) {
        if (sessionService.isTokenExpire()) {
            return new AbstractResponse("FAILED", "TOKEN EXPIRED", 400);
        }
        User user = userRepository.findUserById(Long.valueOf(id));
        if (user == null) {
            throw new NotFoundException();
        }
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getId());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setFullName(user.getFullName());
        userProfileDto.setAge(user.getAge());
        userProfileDto.setGender(user.getGender());
        userProfileDto.setPhone(user.getPhone());
        List<Post> postList = postRepository.findAllByCreatedBy(user.getEmail());
        int totalLike = 0;
        int totalDislike = 0;
        int totalComment = 0;
        for (Post post : postList) {
            if (post.getTotalLike() != null) {
                totalLike += post.getTotalLike();
            } else {
                totalLike = 0;
            }
            if (post.getTotalDislike() != null) {
                totalDislike += post.getTotalDislike();
            } else {
                totalDislike = 0;
            }
            List<Comment> commentList = post.getComment();
            if (commentList != null) {
                totalComment += commentList.size();
            } else {
                totalComment = 0;
            }
        }
        userProfileDto.setTotalLike(totalLike);
        userProfileDto.setTotalDislike(totalDislike);
        userProfileDto.setTotalPost(postList.size());
        userProfileDto.setTotalComment(totalComment);
        List<PostSearchResultDto> postSearchResultDtoList;
        postSearchResultDtoList = postServiceImpl.convertPostToPostDto(postList);
        userProfileDto.setPostSearchResultDtoList(postSearchResultDtoList);
        return new AbstractResponse(userProfileDto);
    }
}

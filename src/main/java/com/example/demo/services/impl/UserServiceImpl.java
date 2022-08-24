package com.example.demo.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dtos.AbstractResponse;
import com.example.demo.dtos.LoginDto;
import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.RegisterRequestDto;
import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.ErrorCode;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
            throw new UnauthorizedException("UNAUTHORIZED", "USER_NOT_FOUND_OR_DISABLED");
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
            throw new UnauthorizedException("401", "Token expired.");
        }
        user.setToken(null);
        userRepository.save(user);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse register(RegisterRequestDto registerRequestDto) {

        User foundUser = userRepository.findByEmail(registerRequestDto.getEmail().trim());

        if (foundUser != null) {
            throw new ConflictException(ErrorCode.EMAIL_EXIST, "Email existed.");
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
}

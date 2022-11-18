package com.example.demo.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.dtos.*;
import com.example.demo.entities.*;
import com.example.demo.repositories.*;
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
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.example.demo.utils.Utils.loadProperties;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final GroupPostRepository groupPostRepository;
    private final ReactionRepository reactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
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
            return new AbstractResponse("FAILED", "INCORRECT_EMAIL_OR_PASSWORD", 400);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        User detectedUser = userRepository.findByEmail(email);
        if (detectedUser == null || !detectedUser.getIsActive()) {
            return new AbstractResponse("FAILED", "FORBIDDEN", 400);
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
            return new AbstractResponse("FAILED", "MISSING_BEARER_PREFIX", 400);
        }
        token = token.split(" ")[1];
        User user = userRepository.findByToken(token);
        user.setToken(null);
        userRepository.save(user);
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse register(RegisterRequestDto registerRequestDto) {
        User foundUser = userRepository.findByEmail(registerRequestDto.getEmail());
        if (foundUser != null) {
            return new AbstractResponse("FAILED", "EMAIL_EXISTED", 400);
        }
        User user = new User();
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRole(roleRepository.findRoleById(3));
        user = userRepository.save(user);
        UserProfile userProfile = new UserProfile(user, registerRequestDto.getFullName(), registerRequestDto.getAddress(), registerRequestDto.getAge(), registerRequestDto.getGender(), registerRequestDto.getPhone());
        userProfileRepository.save(userProfile);
        return new AbstractResponse(login(new LoginRequestDto(registerRequestDto.getEmail(), registerRequestDto.getPassword())).getData());
    }

    @Override
    public AbstractResponse getUserProfile(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
        }
        UserProfile userProfile = userProfileRepository.findByUser(user);
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getId());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setRole(user.getRole().getRoleName());
        userProfileDto.setFullName(userProfile.getFullName());
        userProfileDto.setAge(userProfile.getAge());
        userProfileDto.setGender(userProfile.getGender());
        userProfileDto.setPhone(userProfile.getPhone());
        List<Follow> followList = followRepository.findAllByTargetUser(user);
        userProfileDto.setTotalFollower(followList.size());
        List<Follow> followList2 = followRepository.findAllByFollowedByUser(user);
        userProfileDto.setTotalFollowing(followList2.size());
        List<GroupPost> groupPostList = groupPostRepository.findAllByCreatedBy(user.getEmail());
        List<PostSearchResultDto> postSearchResultDtoList;
        postSearchResultDtoList = postServiceImpl.convertPostToPostDto(groupPostList);
        userProfileDto.setPostSearchResultDtoList(postSearchResultDtoList);
        DoctorProfile doctorProfile = doctorProfileRepository.findByUser(user);
        if (doctorProfile == null) {
            return new AbstractResponse(userProfileDto);
        } else {
            userProfileDto.setCertificate(doctorProfile.getCertificate());
            userProfileDto.setDegree(doctorProfile.getDegree());
            userProfileDto.setExpYear(doctorProfile.getExpYear());
            userProfileDto.setSpecialist(doctorProfile.getSpecialist().getSpecialistName());
            userProfileDto.setWorkingAt(doctorProfile.getWorkingAt());
            userProfileDto.setPrivateWeb(doctorProfile.getPrivateWeb());
            userProfileDto.setStartWorkAtTime(doctorProfile.getStartWorkAtTime());
            userProfileDto.setEndWorkAtTime(doctorProfile.getEndWorkAtTime());
            userProfileDto.setWorkAt(doctorProfile.getWorkAt());
        }
        return new AbstractResponse(userProfileDto);
    }

    @Override
    public AbstractResponse getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        if (user == null) {
            return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
        }
        UserProfile userProfile = userProfileRepository.findByUser(user);
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setId(user.getId());
        userProfileDto.setEmail(user.getEmail());
        userProfileDto.setRole(user.getRole().getRoleName());
        userProfileDto.setFullName(userProfile.getFullName());
        userProfileDto.setAge(userProfile.getAge());
        userProfileDto.setGender(userProfile.getGender());
        userProfileDto.setPhone(userProfile.getPhone());
        List<Follow> followList = followRepository.findAllByTargetUser(user);
        userProfileDto.setTotalFollower(followList.size());
        List<Follow> followList2 = followRepository.findAllByFollowedByUser(user);
        userProfileDto.setTotalFollowing(followList2.size());
        List<GroupPost> groupPostList = groupPostRepository.findAllByCreatedBy(user.getEmail());
        List<PostSearchResultDto> postSearchResultDtoList;
        postSearchResultDtoList = postServiceImpl.convertPostToPostDto(groupPostList);
        userProfileDto.setPostSearchResultDtoList(postSearchResultDtoList);
        DoctorProfile doctorProfile = doctorProfileRepository.findByUser(user);
        if (doctorProfile == null) {
            return new AbstractResponse(userProfileDto);
        } else {
            userProfileDto.setCertificate(doctorProfile.getCertificate());
            userProfileDto.setDegree(doctorProfile.getDegree());
            userProfileDto.setExpYear(doctorProfile.getExpYear());
            userProfileDto.setSpecialist(doctorProfile.getSpecialist().getSpecialistName());
            userProfileDto.setWorkingAt(doctorProfile.getWorkingAt());
            userProfileDto.setPrivateWeb(doctorProfile.getPrivateWeb());
            userProfileDto.setStartWorkAtTime(doctorProfile.getStartWorkAtTime());
            userProfileDto.setEndWorkAtTime(doctorProfile.getEndWorkAtTime());
            userProfileDto.setWorkAt(doctorProfile.getWorkAt());
        }
        return new AbstractResponse(userProfileDto);
    }

    @Override
    public AbstractResponse getDoctorList() {
        List<DoctorProfile> doctorProfileList = doctorProfileRepository.findAll();
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        for (DoctorProfile doctorProfile : doctorProfileList) {
            UserProfileDto userProfileDto = new UserProfileDto();
            userProfileDto.setId(doctorProfile.getId());
            userProfileDto.setFullName(doctorProfile.getFullName() == null ? null : doctorProfile.getFullName());
            userProfileDto.setSpecialist(doctorProfile.getSpecialist() == null ? null : doctorProfile.getSpecialist().getSpecialistName());
            userProfileDto.setAvatar(doctorProfile.getAvatar() == null ? null : doctorProfile.getAvatar());
            userProfileDto.setWorkingAt(doctorProfile.getWorkingAt() == null || "exampleWorkAt".equals(doctorProfile.getWorkingAt()) ? "Child Care Center - Ho Chi Minh City" : doctorProfile.getWorkingAt());
            userProfileDtos.add(userProfileDto);
        }
        return new AbstractResponse(userProfileDtos);
    }

    @Override
    public AbstractResponse followUser(Integer targetUserId) {
        User user = userRepository.findUserById(targetUserId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(authentication.getName());

        if (user != null) {

            if (user.equals(currentUser)) {
                return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
            }

            List<Follow> followList = followRepository.findFollowByFollowedByUserAndTargetUser(currentUser, user);
            if (!CollectionUtils.isEmpty(followList)) {
                followRepository.deleteAll(followList);
            } else {
                Follow follow1 = new Follow(user, currentUser);
                followRepository.save(follow1);
            }
        } else {
            DoctorProfile doctorProfile = doctorProfileRepository.findById(targetUserId).orElse(null);
            if (doctorProfile != null) {
                List<Follow> followList = followRepository.findFollowByFollowedByUserAndTargetUser(currentUser, doctorProfile.getUser());
                if (!CollectionUtils.isEmpty(followList)) {
                    followRepository.deleteAll(followList);
                } else {
                    Follow follow1 = new Follow(doctorProfile.getUser(), currentUser);
                    followRepository.save(follow1);
                }
            } else {
                UserProfile userProfile = userProfileRepository.findById(targetUserId).orElse(null);
                if (userProfile != null) {
                    List<Follow> followList = followRepository.findFollowByFollowedByUserAndTargetUser(currentUser, userProfile.getUser());
                    if (!CollectionUtils.isEmpty(followList)) {
                        followRepository.deleteAll(followList);
                    } else {
                        Follow follow1 = new Follow(userProfile.getUser(), currentUser);
                        followRepository.save(follow1);
                    }
                } else {
                    return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
                }
            }
        }
        return new AbstractResponse();
    }

    @Override
    public AbstractResponse checkFollow(Integer targetUserId) {
        User user = userRepository.findUserById(targetUserId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(authentication.getName());
        if (user != null) {
            List<Follow> followList = followRepository.findFollowByFollowedByUserAndTargetUser(currentUser, user);
            if (!CollectionUtils.isEmpty(followList)) {
                return new AbstractResponse(true);
            } else {
                return new AbstractResponse(false);
            }
        } else {
            DoctorProfile doctorProfile = doctorProfileRepository.findById(targetUserId).orElse(null);
            if (doctorProfile != null) {
                List<Follow> followList = followRepository.findFollowByFollowedByUserAndTargetUser(currentUser, doctorProfile.getUser());
                if (!CollectionUtils.isEmpty(followList)) {
                    return new AbstractResponse(true);
                } else {
                    return new AbstractResponse(false);
                }
            } else {
                UserProfile userProfile = userProfileRepository.findById(targetUserId).orElse(null);
                if (userProfile != null) {
                    List<Follow> followList = followRepository.findFollowByFollowedByUserAndTargetUser(currentUser, userProfile.getUser());
                    if (!CollectionUtils.isEmpty(followList)) {
                        return new AbstractResponse(true);
                    } else {
                        return new AbstractResponse(false);
                    }
                } else {
                    return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
                }
            }
        }
    }

    @Override
    public AbstractResponse getFollowListOfLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByEmail(authentication.getName());
        List<Follow> followList = followRepository.findAllByTargetUser(currentUser);
        List<FollowedByDto> followedByDtoList = new ArrayList<>();
        followList.forEach(item -> {
            String fullName;
            if (Objects.nonNull(item.getFollowedByUser())) {
                UserProfile userProfile = userProfileRepository.findByUser(item.getFollowedByUser());
                if (userProfile == null) {
                    DoctorProfile doctorProfile = doctorProfileRepository.findByUser(item.getFollowedByUser());
                    fullName = doctorProfile.getFullName();
                } else {
                    fullName = userProfile.getFullName();
                }
                FollowedByDto followedByDto = new FollowedByDto(item.getFollowedByUser().getId(), fullName);
                followedByDtoList.add(followedByDto);
            }
        });

        List<Follow> followList2 = followRepository.findAllByFollowedByUser(currentUser);
        List<FollowingDto> followingDtoList = new ArrayList<>();
        followList2.forEach(item -> {
            String fullName;
            if (Objects.nonNull(item.getFollowedByUser())) {
                UserProfile userProfile = userProfileRepository.findByUser(item.getTargetUser());
                if (userProfile == null) {
                    DoctorProfile doctorProfile = doctorProfileRepository.findByUser(item.getTargetUser());
                    fullName = doctorProfile.getFullName();
                } else {
                    fullName = userProfile.getFullName();
                }
                FollowingDto followingDto = new FollowingDto(item.getTargetUser().getId(), fullName);
                followingDtoList.add(followingDto);
            }
        });

        FollowDto followDto = new FollowDto(followedByDtoList, followingDtoList);

        return new AbstractResponse(followDto);
    }

    @Override
    public AbstractResponse getFollowListOfUser(Integer id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            UserProfile userProfile = userProfileRepository.findById(id).orElse(null);
            if (userProfile != null) {
                user = userProfile.getUser();
            } else {
                DoctorProfile doctorProfile = doctorProfileRepository.findById(id).orElse(null);
                if (doctorProfile != null) {
                    user = doctorProfile.getUser();
                } else {
                    return new AbstractResponse("FAILED", "USER_NOT_FOUND", 404);
                }
            }
        }
        List<Follow> followList = followRepository.findAllByTargetUser(user);
        List<FollowedByDto> followedByDtoList = new ArrayList<>();
        followList.forEach(item -> {
            String fullName;
            if (Objects.nonNull(item.getFollowedByUser())) {
                UserProfile userProfile = userProfileRepository.findByUser(item.getFollowedByUser());
                if (userProfile == null) {
                    DoctorProfile doctorProfile = doctorProfileRepository.findByUser(item.getFollowedByUser());
                    fullName = doctorProfile.getFullName();
                } else {
                    fullName = userProfile.getFullName();
                }
                FollowedByDto followedByDto = new FollowedByDto(item.getFollowedByUser().getId(), fullName);
                followedByDtoList.add(followedByDto);
            }
        });

        List<Follow> followList2 = followRepository.findAllByFollowedByUser(user);
        List<FollowingDto> followingDtoList = new ArrayList<>();
        followList2.forEach(item -> {
            String fullName;
            if (Objects.nonNull(item.getFollowedByUser())) {
                UserProfile userProfile = userProfileRepository.findByUser(item.getTargetUser());
                if (userProfile == null) {
                    DoctorProfile doctorProfile = doctorProfileRepository.findByUser(item.getTargetUser());
                    fullName = doctorProfile.getFullName();
                } else {
                    fullName = userProfile.getFullName();
                }
                FollowingDto followingDto = new FollowingDto(item.getTargetUser().getId(), fullName);
                followingDtoList.add(followingDto);
            }
        });

        FollowDto followDto = new FollowDto(followedByDtoList, followingDtoList);

        return new AbstractResponse(followDto);
    }
}

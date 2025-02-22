package planpad.planpadapp.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.SocialUserDto;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final JwtTokenProvider jwtTokenProvider;

    public String join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("getUserById 실패 userId = " + id));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUserById(String socialId) {
        userRepository.deleteBySocialId(socialId);
    }

    public void deleteUserByAccessToken(String accessToken) {
        userRepository.deleteByAccessToken(accessToken);
    }

    public User getUserByBearerToken(String userToken) {
        String userId = jwtTokenProvider.getUserIdFromToken(userToken);
        return getUserById(userId);
    }

    public UserDetails loadUserById(String id) {

        User user = getUserById(id);

        if (user == null) {
            throw new UsernameNotFoundException("회원을 찾을 수 없습니다. id = " + id);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "SOCIAL_LOGIN",
                Collections.singletonList(() -> "ROLE_USER")
        );
    }

    @Transactional
    public User kakaoNaverLoginOrJoin(String socialType, String code) {

        String socialAccessToken = new String();
        SocialUserDto socialUser = new SocialUserDto();

        if ("kakao".equalsIgnoreCase(socialType)) {
            socialAccessToken = kakaoService.kakaoGetAccessToken(code);
            socialUser = kakaoService.kakaoGetUserInfo(socialAccessToken);
        } else if ("naver".equalsIgnoreCase(socialType)) {
            socialAccessToken = naverService.naverGetAccessToken(code);
            socialUser = naverService.naverGetUserInfo(socialAccessToken);
        }

        String userEmail = socialUser.getEmail();
        Optional<User> existingUserOptional = getUserByEmail(userEmail);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setAccessToken(socialAccessToken);

            return existingUser;

        } else {
            User newUser = new User(socialUser);
            join(newUser);

            return newUser;
        }
    }

    @Transactional
    public void kakaoNaverUnLink(String socialType, String bearerToken) {

        String userToken = bearerToken.replace("Bearer ", "");
        String userId = jwtTokenProvider.getUserIdFromToken(userToken);
        String accessToken = getUserById(userId).getAccessToken();

        if ("kakao".equalsIgnoreCase(socialType)) {
            String socialId = kakaoService.kakaoUnLink(accessToken);
            deleteUserById(socialId);

        } else if ("naver".equalsIgnoreCase(socialType)) {
            naverService.naverUnLink(accessToken);
            deleteUserByAccessToken(accessToken);
        }
    }
}

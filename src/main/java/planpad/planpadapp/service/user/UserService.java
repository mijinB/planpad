package planpad.planpadapp.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;
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
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDetails loadUserById(Long id) {

        User user = userRepository.findOne(id);

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
    public User kakaoLoginOrJoin(String code) {

        String kakaoAccessToken = kakaoService.kakaoGetAccessToken(code);
        KakaoUserInfoDto kakaoUserInfo = kakaoService.kakaoGetUserInfo(kakaoAccessToken);

        String userEmail = kakaoUserInfo.kakao_account.email;
        Optional<User> existingUserOptional = getUserByEmail(userEmail);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setAccessToken(kakaoAccessToken);

            return existingUser;
        } else {
            User newUser = new User();
            newUser.setKakaoId(kakaoUserInfo.id);
            newUser.setAccessToken(kakaoAccessToken);
            newUser.setEmail(userEmail);
            newUser.setUserName(kakaoUserInfo.properties.nickname);
            newUser.setAvatar(kakaoUserInfo.properties.thumbnail_image);

            join(newUser);
            return newUser;
        }
    }

    public void kakaoUnLink(String bearerToken) {

        String userToken = bearerToken.replace("Bearer ", "");
        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(userToken));
        String accessToken = getUserById(userId).getAccessToken();

        kakaoService.kakaoUnLink(accessToken);
    }
}

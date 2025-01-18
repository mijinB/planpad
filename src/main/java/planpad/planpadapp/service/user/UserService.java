package planpad.planpadapp.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;
import planpad.planpadapp.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoService kakaoService;

    @Transactional
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User kakaoLoginOrJoin(String code) {
        String kakaoAccessToken = kakaoService.kakaoGetAccessToken(code);
        KakaoUserInfoDto kakaoUserInfo = kakaoService.kakaoGetUserInfo(kakaoAccessToken);

        String userEmail = kakaoUserInfo.kakao_account.email;
        Optional<User> existingUserOptional = findByEmail(userEmail);

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
}

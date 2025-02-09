package planpad.planpadapp.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.kakao.KakaoUserInfoDto;
import planpad.planpadapp.dto.user.naver.NaverUserInfoDto;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KakaoLoginService kakaoLoginService;
    private final NaverLoginService naverLoginService;
    private final JwtTokenProvider jwtTokenProvider;

    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public User getUserById(Long id) {
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
        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(userToken));
        return getUserById(userId);
    }

    public UserDetails loadUserById(Long id) {

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
    public User kakaoLoginOrJoin(String code) {

        String kakaoAccessToken = kakaoLoginService.kakaoGetAccessToken(code);
        KakaoUserInfoDto kakaoUserInfo = kakaoLoginService.kakaoGetUserInfo(kakaoAccessToken);

        String userEmail = kakaoUserInfo.kakaoAccount.email;
        Optional<User> existingUserOptional = getUserByEmail(userEmail);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setAccessToken(kakaoAccessToken);

            return existingUser;

        } else {
            User newUser = new User();
            newUser.setSocialId(kakaoUserInfo.id.toString());
            newUser.setSocialType("kakao");
            newUser.setAccessToken(kakaoAccessToken);
            newUser.setEmail(userEmail);
            newUser.setName(kakaoUserInfo.properties.nickname);
            newUser.setAvatar(kakaoUserInfo.properties.thumbnailImage);

            join(newUser);
            return newUser;
        }
    }

    @Transactional
    public void kakaoUnLink(String bearerToken) {

        String userToken = bearerToken.replace("Bearer ", "");
        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(userToken));
        String accessToken = getUserById(userId).getAccessToken();

        String socialId = kakaoLoginService.kakaoUnLink(accessToken);
        deleteUserById(socialId);
    }

    @Transactional
    public User naverLoginOrJoin(String code) {
        String naverAccessToken = naverLoginService.naverGetAccessToken(code);
        NaverUserInfoDto naverUserInfo = naverLoginService.naverGetUserInfo(naverAccessToken);

        String userEmail = naverUserInfo.response.email;
        Optional<User> existingUserOptional = getUserByEmail(userEmail);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.setAccessToken(naverAccessToken);

            return existingUser;

        } else {
            try {
                User newUser = new User();
                newUser.setSocialId(naverUserInfo.response.id);
                newUser.setSocialType("naver");
                newUser.setAccessToken(naverAccessToken);
                newUser.setEmail(userEmail);
                newUser.setName(URLDecoder.decode(naverUserInfo.response.name, "UTF-8"));
                newUser.setAvatar(naverUserInfo.response.profileImage);

                join(newUser);
                return newUser;

            } catch (UnsupportedEncodingException e) {  // URLDecoder.decode 예외 처리
                e.printStackTrace();
                return null;
            }
        }
    }

    @Transactional
    public void naverUnLink(String bearerToken) {

        String userToken = bearerToken.replace("Bearer ", "");
        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(userToken));
        String accessToken = getUserById(userId).getAccessToken();

        naverLoginService.naverUnLink(accessToken);
        deleteUserByAccessToken(accessToken);
    }
}

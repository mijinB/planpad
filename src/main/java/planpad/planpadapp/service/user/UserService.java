package planpad.planpadapp.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.dto.user.UserDto;
import planpad.planpadapp.provider.JwtTokenProvider;
import planpad.planpadapp.repository.UserRepository;
import planpad.planpadapp.service.calendar.GroupService;
import planpad.planpadapp.service.memo.FolderService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final GoogleService googleService;
    private final FolderService folderService;
    private final GroupService groupService;

    public String join(User user) {
        userRepository.save(user);
        return user.getUserId();
    }

    public User getUserByBearerToken(String bearerToken) {
        String userToken = bearerToken.replace("Bearer ", "");
        String userId = jwtTokenProvider.getUserIdFromToken(userToken);

        return getUserOrThrow(userId);
    }

    public UserDetails loadUser(String id) {
        User user = getUserOrThrow(id);

        if (user == null) {
            throw new UsernameNotFoundException("회원을 찾을 수 없습니다. id = " + id);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                "SOCIAL_LOGIN",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Transactional
    public User socialLoginOrJoin(String socialType, String code) {

        validateSocialType(socialType);
        String socialAccessToken = "";
        UserDto socialUser = new UserDto();

        if ("kakao".equalsIgnoreCase(socialType)) {
            socialAccessToken = kakaoService.kakaoGetAccessToken(code);
            socialUser = kakaoService.kakaoGetUserInfo(socialAccessToken);

        } else if ("naver".equalsIgnoreCase(socialType)) {
            socialAccessToken = naverService.naverGetAccessToken(code);
            socialUser = naverService.naverGetUserInfo(socialAccessToken);

        } else if ("google".equalsIgnoreCase(socialType)) {
            socialAccessToken = googleService.googleGetAccessToken(code);
            socialUser = googleService.googleGetUserInfo(socialAccessToken);
        }

        String userEmail = socialUser.getEmail();
        Optional<User> existingUserOptional = userRepository.findByEmail(userEmail);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.updateAccessToken(socialAccessToken);

            return existingUser;

        } else {
            User newUser = new User(socialUser);
            join(newUser);
            folderService.createDefaultFolder(newUser);
            groupService.createDefaultGroup(newUser);

            return newUser;
        }
    }

    @Transactional
    public void socialUnLink(User user, String socialType) {

        validateSocialType(socialType);
        String accessToken = user.getAccessToken();

        if ("kakao".equalsIgnoreCase(socialType)) {
            String socialId = kakaoService.kakaoUnLink(accessToken);
            userRepository.deleteBySocialId(socialId);

        } else if ("naver".equalsIgnoreCase(socialType)) {
            naverService.naverUnLink(accessToken);
            userRepository.deleteByAccessToken(accessToken);

        } else if ("google".equalsIgnoreCase(socialType)) {
            googleService.googleUnLink(accessToken);
            userRepository.deleteByAccessToken(accessToken);
        }
    }

    public User getUserOrThrow(String id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    private void validateSocialType(String socialType) {
        if (!List.of("kakao", "naver", "google").contains(socialType.toLowerCase())) {
            throw new IllegalArgumentException("지원하지 않는 소셜 타입입니다.");
        }
    }
}

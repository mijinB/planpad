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
import planpad.planpadapp.service.memo.FolderService;
import planpad.planpadapp.service.memo.MemoService;
import planpad.planpadapp.service.memo.TagService;

import java.util.Collections;
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
    private final MemoService memoService;
    private final TagService tagService;

    public String join(User user) {
        userRepository.save(user);
        return user.getUserId();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("getUserById 실패 userId = " + id));
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUserToken(String userToken) {
        String userId = jwtTokenProvider.getUserIdFromToken(userToken);
        return getUserById(userId);
    }

    public void deleteUserById(String socialId) {
        userRepository.deleteBySocialId(socialId);
    }

    public void deleteUserByAccessToken(String accessToken) {
        userRepository.deleteByAccessToken(accessToken);
    }

    public void deleteFolderMemoTag(User user) {
        folderService.deleteFolderByUser(user);
        memoService.deleteMemoByUser(user);
        tagService.deleteTagByUser(user);
    }

    public UserDetails loadUser(String id) {
        User user = getUserById(id);

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
        String socialAccessToken = new String();
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
        Optional<User> existingUserOptional = getUserByEmail(userEmail);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            existingUser.updateAccessToken(socialAccessToken);

            return existingUser;

        } else {
            User newUser = new User(socialUser);
            join(newUser);
            folderService.saveDefaultFolder(newUser);

            return newUser;
        }
    }

    @Transactional
    public void socialUnLink(String socialType, String bearerToken) {
        String userToken = bearerToken.replace("Bearer ", "");
        String userId = jwtTokenProvider.getUserIdFromToken(userToken);
        User user = getUserById(userId);
        String accessToken = user.getAccessToken();

        if ("kakao".equalsIgnoreCase(socialType)) {
            String socialId = kakaoService.kakaoUnLink(accessToken);
            deleteFolderMemoTag(user);
            deleteUserById(socialId);

        } else if ("naver".equalsIgnoreCase(socialType)) {
            naverService.naverUnLink(accessToken);
            deleteFolderMemoTag(user);
            deleteUserByAccessToken(accessToken);

        } else if ("google".equalsIgnoreCase(socialType)) {
            googleService.googleUnLink(accessToken);
            deleteFolderMemoTag(user);
            deleteUserByAccessToken(accessToken);
        }
    }
}

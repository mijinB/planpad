package planpad.planpad_app.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import planpad.planpadapp.PlanpadAppApplication;
import planpad.planpadapp.domain.User;
import planpad.planpadapp.repository.UserRepository;
import planpad.planpadapp.service.UserService;

@SpringBootTest(classes = PlanpadAppApplication.class)
@Transactional
public class UserServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;

    @Test
    public void 회원가입() {
        // given
        User user = new User();
        user.setUserName("userA");

        // when
        Long savedId = userService.join(user);
        log.info("savedId={}", savedId);

        // then
        Assertions.assertEquals(user, userRepository.findOne(savedId));
    }
}

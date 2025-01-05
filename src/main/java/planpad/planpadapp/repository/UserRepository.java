package planpad.planpadapp.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.User;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email=:email";
        return em.createQuery(jpql, User.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}

package planpad.planpadapp.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.User;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public Optional<User> findByEmail(String email) {
        try {
            String jpql = "SELECT u FROM User u WHERE u.email=:email";
            User user = em.createQuery(jpql, User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

package planpad.planpadapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import planpad.planpadapp.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(String userId);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM User u WHERE u.socialId = :socialId")
    void deleteBySocialId(@Param("socialId") String socialId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.accessToken = :accessToken")
    void deleteByAccessToken(@Param("accessToken") String accessToken);
}

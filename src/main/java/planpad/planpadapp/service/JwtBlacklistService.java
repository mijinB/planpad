package planpad.planpadapp.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JdbcTemplate jdbcTemplate;

    @Scheduled(fixedRate = 21600000)
    public void cleanUpExpiredTokens() {
        String query = "DELETE FROM jwt_blacklist WHERE expiration_date < ?";
        jdbcTemplate.update(query, System.currentTimeMillis());
    }

    @PostConstruct
    public void initCleanup() {
        cleanUpExpiredTokens();
    }

    public void addToBlacklist(String userToken, Long expirationTimestamp) {
        String query = "INSERT INTO jwt_blacklist (token, expiration_date) VALUES (?, ?)";
        jdbcTemplate.update(query, userToken, expirationTimestamp);
    }

    public boolean isBlacklisted(String userToken) {
        String query = "SELECT COUNT(*) FROM jwt_blacklist WHERE token = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, userToken);

        return count != null && count > 0;
    }
}

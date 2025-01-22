package planpad.planpadapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import planpad.planpadapp.provider.JwtTokenProvider;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final JdbcTemplate jdbcTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    public void addToBlacklist(String userToken, Date expirationDate) {
        String query = "INSERT INTO jwt_blacklist (token, expiration_date) VALUES (?, ?)";
        jdbcTemplate.update(query, userToken, expirationDate);
    }

    public boolean isBlacklisted(String userToken) {
        String query = "SELECT COUNT(*) FROM jwt_blacklist WHERE token = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, userToken);
        return count != null && count > 0;
    }
}

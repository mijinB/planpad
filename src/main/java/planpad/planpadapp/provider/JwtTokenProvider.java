package planpad.planpadapp.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import planpad.planpadapp.service.JwtBlacklistService;

import java.util.Date;

@Slf4j
@Service
public class JwtTokenProvider {

    private final JwtBlacklistService jwtBlacklistService;

    public JwtTokenProvider(@Lazy JwtBlacklistService jwtBlacklistService) {
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Value("${jwt.secret_key}")
    private String SECRET_KEY;
    private final long EXPIRATION_TIME = 86400000;  // = 24시간 = 1000 * 60 * 60 * 24

    public String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims getClaimsFromToken(String userToken) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(userToken)
                .getBody();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    public Date getExpirationFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    public boolean validateToken(String userToken) {

        if (jwtBlacklistService.isBlacklisted(userToken)) {
            return false; // 블랙리스트에 있으면 유효하지 않음
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(userToken);
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

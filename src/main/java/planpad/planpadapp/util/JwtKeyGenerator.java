package planpad.planpadapp.util;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Base64;

public class JwtKeyGenerator {

    public static void main(String[] args) {
        // HS256을 위한 안전한 비밀 키 생성
        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Base64로 인코딩하여 출력
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // 생성된 키 출력
        System.out.println("Generated Base64 Encoded Key: " + base64Key);
    }
}

package controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;


public class AuthenticationHelper {
    public String hashPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean passwordMatches(String password, String passwordHash) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.matches(password, passwordHash);
    }

    public String generateJWTForUser(String userEmail) throws InvalidKeyException, NoSuchAlgorithmException {
        HashMap header = new HashMap();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        HashMap payload = new HashMap();
        payload.put("iss", "iemdb");
        payload.put("iat", new Date().getTime());
        payload.put("exp", new Date().getTime()+86400000);
        payload.put("userEmail", userEmail);

        String encodedHeader = Base64.getUrlEncoder().encodeToString(header.toString().getBytes());
        String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.toString().getBytes());
        String valueToDigest = encodedHeader + "." + encodedPayload;
        String key = "iemdb1401";
        String signature = HMACSHA256(valueToDigest, key);
        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public String HMACSHA256(String message, String key) throws InvalidKeyException, NoSuchAlgorithmException {
        byte[] bytes = hmac("HmacSHA256", key.getBytes(), message.getBytes());
        return bytesToHex(bytes);
    }

    byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }

    String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public Map<String, String> validateJWT(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor("iemdb1401".getBytes(StandardCharsets.UTF_8));
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);

        Map<String, String> payload = new HashMap<>();
        claims.getBody().forEach((key, value) -> {
            payload.put(key, value.toString());
        });

        return payload;
    }
}

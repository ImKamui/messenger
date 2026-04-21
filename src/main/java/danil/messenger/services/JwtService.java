package danil.messenger.services;

import danil.messenger.models.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String generateToken(User user);
    //String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String extractUsername(String token);
    boolean isTokenValid(String token);
    <T> T extractClaim(String token, Function<Claims, T> resolver);
    Claims extractAllClaims(String token);
    boolean isTokenExpired(String token);
    SecretKey getSigningKey();
    String extractRole(String token);
    int extractUserId(String token);
}

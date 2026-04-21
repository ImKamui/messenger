package danil.messenger.services.impls;

import danil.messenger.models.User;
import danil.messenger.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final UsersDetailsServiceImpl usersDetailsServiceImpl;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

//    @Override
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts.builder()
//                .claims(extraClaims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSigningKey(), Jwts.SIG.HS256)
//                .compact();
//    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String extractRole(String token)
    {
        return extractAllClaims(token).get("role", String.class);
    }

    @Override
    public int extractUserId(String token) {
        return extractAllClaims(token).get("userId", Integer.class);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e)
        {
            return false;
        }
    }


    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

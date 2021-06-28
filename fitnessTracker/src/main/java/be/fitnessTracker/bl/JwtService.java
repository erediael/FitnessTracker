package be.fitnessTracker.bl;

import be.fitnessTracker.dal.async.IdentityRepositoryAsync;
import be.fitnessTracker.models.db.User;
import be.fitnessTracker.models.exceptions.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
    @Value("${be.fitnessTracker.jwt.secret}")
    private String secret;

    @Value("${be.fitnessTracker.jwt.expiration}")
    private String expirationTimeInHours;

    private static final String CLAIM_USERNAME = "username";
    private static final String BEARER = "Bearer ";
    private static final Integer TOKEN_SUBSTRING = 7;

    @Autowired
    private IdentityRepositoryAsync identityRepository;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Boolean isExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generate(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_USERNAME, user.getUsername());
        return create(claims, user.getId());
    }

    private String create(Map<String, Object> claims, String userId) {
        long exp = Long.parseLong(expirationTimeInHours);
        Date createdDate = new Date();
        Date expirationDate = new Date(createdDate.getTime() + TimeUnit.MINUTES.toMillis(exp));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new UnauthorizedException();
        }
    }

    public String getUserId(String token) {
        return extractClaims(token).getSubject();
    }

    public Mono<User> getUserFromDb(String bearer) {
        String token = getToken(bearer);
        String userId = getUserId(token);

        return identityRepository.findById(userId);
    }

    public String getToken(String bearer) {
        return bearer.substring(TOKEN_SUBSTRING);
    }

    public boolean isBearer(String token) {
        return token.startsWith(BEARER);
    }

    private Date getExpirationDateFromToken(String token) {
        return extractClaims(token).getExpiration();
    }
}
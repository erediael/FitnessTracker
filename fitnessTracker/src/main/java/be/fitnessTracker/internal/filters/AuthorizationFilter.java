package be.fitnessTracker.internal.filters;

import be.fitnessTracker.bl.IdentityService;
import be.fitnessTracker.dal.async.IdentityRepositoryAsync;
import be.fitnessTracker.internal.Constants.Routes;
import be.fitnessTracker.bl.JwtService;
import be.fitnessTracker.models.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Order(1)
public class AuthorizationFilter implements WebFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IdentityRepositoryAsync identityRepository;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.equals(Routes.LOGIN) || path.equals(Routes.REGISTER) || path.equals(Routes.FORGOT_PASSWORD)) {
            return chain.filter(exchange);
        }

        String bearer = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (bearer == null || !jwtService.isBearer(bearer)) {
            logger.info("AuthorizationFilter no bearer present in authorization header: {}", bearer);
            throw new UnauthorizedException();
        }

        String token = jwtService.getToken(bearer);
        if (jwtService.isExpired(token)) {
            logger.info("AuthorizationFilter token is expired: {}", token);
            throw new UnauthorizedException();
        }

        return chain.filter(exchange);
    }
}
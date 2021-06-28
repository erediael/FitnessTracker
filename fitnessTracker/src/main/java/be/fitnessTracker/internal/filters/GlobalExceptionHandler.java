package be.fitnessTracker.internal.filters;

import be.fitnessTracker.bl.IdentityService;
import be.fitnessTracker.internal.Constants.ExceptionMessages;
import be.fitnessTracker.models.exceptions.*;
import be.fitnessTracker.models.responses.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2) // go before default error web exception handler
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ObjectMapper objectMapper;

    private GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        logger.error(throwable.getMessage());

        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer dataBuffer;

        if (throwable instanceof ValidationException) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            dataBuffer = getErrorResponse(bufferFactory, throwable.getMessage());
        } else if (throwable instanceof NotFoundException) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            dataBuffer = getErrorResponse(bufferFactory, throwable.getMessage());
        } else if (throwable instanceof UnauthorizedException) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            dataBuffer = getErrorResponse(bufferFactory, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        } else if (throwable instanceof ForbiddenException) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            dataBuffer = getErrorResponse(bufferFactory, HttpStatus.FORBIDDEN.getReasonPhrase());
        } else {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            dataBuffer = getErrorResponse(bufferFactory, ExceptionMessages.SYSTEM_ERROR);
        }

        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    private DataBuffer getErrorResponse(DataBufferFactory bufferFactory, String errorMessage) throws JsonProcessingException {
        return bufferFactory.wrap(objectMapper.writeValueAsBytes(new ErrorResponse(errorMessage)));
    }
}
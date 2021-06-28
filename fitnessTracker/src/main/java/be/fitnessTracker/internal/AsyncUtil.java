package be.fitnessTracker.internal;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;

public class AsyncUtil {

    public static <T> Mono<T> runOnBoundedElasticScheduler(Callable<T> callable) {
        return Mono.just(Boolean.TRUE).publishOn(Schedulers.boundedElastic())
                .flatMap((ignore) -> Mono.fromCallable(callable));
    }
}

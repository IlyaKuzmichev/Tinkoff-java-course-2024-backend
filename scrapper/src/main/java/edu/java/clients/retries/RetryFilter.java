package edu.java.clients.retries;

import edu.java.clients.retries.backoff.Backoff;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class RetryFilter implements ExchangeFilterFunction {
    private final Backoff backoff;
    private final Set<Integer> statusCodes;
    private final Integer attempts;

    @Override
    public @NotNull Mono<ClientResponse> filter(@NotNull ClientRequest request, @NotNull ExchangeFunction next) {
        return retry(request, next, 1);
    }

    private Mono<ClientResponse> retry(ClientRequest request, ExchangeFunction next, int attempt) {
        return next.exchange(request)
            .flatMap(resp -> {
                Integer httpStatus = resp.statusCode().value();
                if (statusCodes.contains(httpStatus) && attempt <= attempts) {
                    Duration timeToWait = backoff.calculateWaitingTime(attempt);
                    log.debug("%s Attempt number: %d, time: %d".formatted(
                        request.getClass().toString(),
                        attempt,
                        OffsetDateTime.now().getSecond()
                    ));
                    Mono<ClientResponse> response = Mono.defer(
                        () -> retry(request, next, attempt + 1)
                    );
                    return Mono.delay(timeToWait).then(response);
                }
                return Mono.just(resp);
            });
    }
}

package demo.stockdashboard;

import java.util.Optional;
import reactor.core.publisher.Mono;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;

public class Optional3Test {

    public static void main(String[] args) {
        System.out.println(company("Some payload").blockOptional().get().statusCode());
        System.out.println(company("").blockOptional().get().statusCode());
        System.out.println(company("Not").blockOptional().get().statusCode());
    }

    static Mono<ServerResponse> company(String request) {
        return request.isEmpty()
                ? getBadRequest(Mono.empty())
                : companyRepo(request)
                    .map(req -> ok().bodyValue(req).log(req))
                    .orElse(notFound().build());
    }

    static Optional<String> companyRepo(String request) {
        return !"Not".equals(request)
                ? Optional.of(request)
                : Optional.empty();
    }

    static Mono<ServerResponse> getBadRequest(Mono request) {
        return request.switchIfEmpty(badRequest().bodyValue("parameter symbol is missing").log("parameter symbol is missing"));
    }
}

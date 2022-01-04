//package demo.stockdashboard;
//
//import demo.stockdashboard.repo.CompanyProfileRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
//
//public class OptionalTest {
//
//    public static void main(String[] args) {
////        System.out.println(badRequest.blockOptional().get().statusCode());
//
//
////        System.out.println(company("symbol").blockOptional().get().statusCode()); // 200 OK
////        System.out.println(company("").blockOptional().get().statusCode()); // 200 OK
//////        System.out.println(company(null).blockOptional().get().statusCode()); // 200 OK
//
//        System.out.println(getAll("symbol").blockOptional().get().statusCode()); // 200 OK
//        System.out.println(getAll("").blockOptional().get().statusCode());       // 400 BAD_REQUEST
//
//
//
////        Mono.just("test1")
//////        Mono.just("")
////                .flatMap(val -> {
////                    if (val.isEmpty()) return Mono.empty();
////                    else return Mono.just(val);
////                })
////                .switchIfEmpty(method1());
//////                .subscribe(s -> System.out.println(s));
//
//        Mono result =
////        Mono.just("Some payload")
//        Mono.just("")
//                .flatMap(v -> v.isEmpty() ? Mono.empty() : Mono.just(v))
//                .switchIfEmpty(Mono.just("empty"));
//        System.out.println(result.blockOptional().get());
//
//        System.out.println(some("Some payload").blockOptional().get());
////        System.out.println(some("").blockOptional().get());
//
////        System.out.println(some(Mono.just("Some payload")).blockOptional().get());
//        System.out.println(some(Mono.empty()).blockOptional().get());
//    }
//
//    static Mono some(String req) {
//        return Mono.just(req)
//                .flatMap(v -> v.isEmpty() ? Mono.empty() : Mono.just(v))
//                .switchIfEmpty(Mono.just("empty"));
//    }
//
//    static Mono some(Mono<String> req) {
//        return req.switchIfEmpty(Mono.just("empty"));
//    }
//
////    private static Mono<String> method1() {
////        System.out.println("method1");
////        return Mono.empty();
////    }
//
//    static Mono<ServerResponse> badRequest = ServerResponse
//            .status(HttpStatus.BAD_REQUEST)
//            .bodyValue("parameter symbol is missing");
//
//    //
////    static Mono<ServerResponse> company(String request) {
//////        return ok().contentType(MediaType.APPLICATION_JSON).body(request, String.class)
////        return ok().contentType(MediaType.APPLICATION_JSON).bodyValue(request)
////                .switchIfEmpty(ServerResponse.notFound().build())
////                .or(ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue("parameter symbol is missing"));
////    }
//
////    Mono<ServerResponse> company(String request) {
////        return companyRepo.findBySymbol(request)
////                .
////    }
//
////    static Mono<ServerResponse> getAll(String request) {
////        return ServerResponse.ok()
//////                .contentType(MediaType.APPLICATION_JSON)
////                .bodyValue(request)
////                .log(request);
////    }
//
//    //
//    // OK
//    static Mono<ServerResponse> getAll(String request) {
//        return request.isEmpty()
//                ? badRequest().bodyValue("parameter symbol is missing").log("parameter symbol is missing")
//                : ok().bodyValue(request).log(request);
//    }
//
////    static Mono<ServerResponse> getAll(String request, Optional<String> req2) {
////        ServerResponse.
////
////        return request.isEmpty()
////                ? badRequest().bodyValue("parameter symbol is missing").log("parameter symbol is missing")
////                : req2.flatMap(r -> r).
////    }
//}

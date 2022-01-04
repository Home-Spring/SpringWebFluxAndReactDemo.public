//package demo.stockdashboard;
//
//import org.springframework.web.reactive.function.server.ServerResponse;
//import reactor.core.publisher.Mono;
//
//import static org.springframework.web.reactive.function.server.ServerResponse.ok;
//import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
//import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
//
//public class Optional2Test {
//
//    public static void main(String[] args) {
//        System.out.println(getBadRequest("").blockOptional().get().statusCode());
//        System.out.println(getBadRequest("Some payload").blockOptional().get().statusCode());
//        System.out.println(getBadRequest("Not").blockOptional().get().statusCode());
//    }
//
////    static Mono<ServerResponse> getBadRequest(String request) {
////        return Mono.just(request)
////                .flatMap(req -> req.isEmpty() ? Mono.empty() : companyResponse(req).log(req))
////                .switchIfEmpty(badRequest().bodyValue("parameter symbol is missing").log("parameter symbol is missing"));
////    }
//
//    static Mono<ServerResponse> companyResponse(String request) {
//        return getBadRequest(request);
//    }
//
//    static Mono<ServerResponse> getBadRequest(String request) {
//        return Mono.just(request)
//                .flatMap(req -> response(req, company(req)))
//                .switchIfEmpty(badRequest().bodyValue("parameter symbol is missing").log("parameter symbol is missing"));
//    }
//
//    static Mono<ServerResponse> response(String request, Mono<ServerResponse> obj) {
//        return request.isEmpty() ? Mono.empty() : obj.log(request);
//    }
//
//    static Mono<ServerResponse> company(String request) {
//        return !"Not".equals(request)
//                ? ok().bodyValue(request)
//                : notFound().build();
//    }
//}

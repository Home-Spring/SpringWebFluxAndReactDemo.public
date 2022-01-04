//package demo.stockdashboard;
//
//import java.util.Arrays;
//import java.util.Optional;
//import java.util.stream.Stream;
//
///**
// * Concatenate two or more optional string in Java 8
// * *************************************************
// * @see https://stackoverflow.com/questions/46473098/concatenate-two-or-more-optional-string-in-java-8
// */
//public class ConcatenateOptionalTest {
//
//    public static void main(String[] args) {
//        Optional<String> optSymbol = Optional.of("symbol");
//        Optional<String> optDate = Optional.of("date");
//        Stream.of(optSymbol, optDate)
//                .filter(Optional::isPresent);
//
//
////                .reduce((ff, ss) -> ff + '.' + ss);
//////                .flatMap(Optional::stream)
//////                .reduce(String::concat);
//    }
//
//    @SafeVarargs
//    public final Optional<String> concat(Optional<String>... inputs) {
//        return Arrays.stream(inputs)
//                .reduce((left, right) -> left.flatMap(leftValue -> right.map(rightValue -> leftValue + rightValue)))
//                .orElseThrow()
//    }
//
//    class MyRuntimeException extends RuntimeException {
//        public MyRuntimeException(String value) {}
//
//    }
//}

package demo.stockdashboard;

import java.util.Optional;
import java.util.stream.Stream;

public class Optional4Test {

    public static void main(String[] args) {
        Optional<String> optSymbol = Optional.of("symbol");
//        Optional<String> optDate = Optional.of("date");
        Optional<String> optDate = Optional.empty();

        boolean result = Stream.of(optSymbol, optDate)
                .map(v -> v.isPresent())
                .reduce((first, second) -> first && second)
                .orElse(false);

        System.out.println( result );
    }
}

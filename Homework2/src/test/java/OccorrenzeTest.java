import org.example.Occorrenze;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.params.provider.Arguments.of;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class OccorrenzeTest {

    public static Occorrenze occorrenze = new Occorrenze();

    @ParameterizedTest
    @MethodSource("generator")
    @DisplayName("Caso white-box")
    void test(String str, String word, int expectedOutput){
        if (str == null) {
            assertThrows(NullPointerException.class, () -> {
                occorrenze.countOccurrences(str, word);
            });
        } else {
            assertEquals(expectedOutput, occorrenze.countOccurrences(str, word));
        }
    }

    static Stream<Arguments> generator(){
        return Stream.of(

                //test di prova
                of("", "fame", 0),
                of(null, "fame", 0),

                of("La fame nel mond1 ha fame .", "fame", 2),
                of("La fame nel mondo ha fame .", "pane", 0)




                );
    }

}

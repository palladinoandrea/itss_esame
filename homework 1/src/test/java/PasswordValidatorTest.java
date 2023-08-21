import org.example.PasswordValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.of;


@TestMethodOrder(OrderAnnotation.class)
public class PasswordValidatorTest {

    private static PasswordValidator passwordValidator;
    String password;


    @BeforeAll
    static void setUpBeforeClass() {
        passwordValidator = new PasswordValidator();

    }

    @BeforeEach
    public void setUp() {
        passwordValidator= new PasswordValidator(8, true,true);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ciaomichiamonico", "Parabola1", "Mipiaceilcioccolato"})
    @DisplayName("Password Length Requirement Test")
    @Order(1)
    public void testPasswordLengthRequirement(String password) {
        Assertions.assertTrue(password.length() >= 8 && password.length() <= 24);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ciao1", "Parabola1", "123456"})
    @DisplayName("Number Requirement Test")
    @Order(2)
    public void testNumberRequirement(String password) {
        Assertions.assertTrue(password.matches(".*\\d.*"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ciao@", "Parabola1?", "@"})
    @DisplayName("Special Character Requirement Test")
    @Order(3)
    public void testSpecialCharacterRequirement(String password) {
        Assertions.assertTrue(password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"));
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("Empty Password Test")
    @Order(4)
    public void testEmptyPassword(String password) {
        Assertions.assertFalse(passwordValidator.validate(password));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Null Password Test")
    @Order(5)
    public void testNullPassword(String password) {
        Assertions.assertFalse(passwordValidator.validate(password));
    }


    @ParameterizedTest
    @ValueSource(strings = {"Pianura123?", "Parabola1@", "Catastrofe99*","Pasquale001@"})
    @DisplayName("Requisiti minimi password valida")
    @Order(6)
    public void testValidPassword(String password) {

        Assumptions.assumeTrue(password.matches(".*[a-z].*"));

        Assertions.assertAll(
                () -> Assertions.assertTrue(password.length() >= 8 && password.length() <= 24, "Lunghezza minima non rispettata"),
                () -> Assertions.assertTrue(password.matches(".*\\d.*"), "Assenza di almeno 1 numero"),
                () -> Assertions.assertTrue(password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*"), "Assenza di almeno 1 carattere speciale"));
    }




    @ParameterizedTest
    @MethodSource("generator")
    @DisplayName("Caso white-box")
    @Order(7)
    void test(String originalStr, int minLength, boolean requiresNumber, boolean requiresSpecialChar, boolean expectedStr){
        assertThat(new PasswordValidator(minLength,requiresNumber, requiresSpecialChar).validate(originalStr))
                .isEqualTo(expectedStr);

    }


    static Stream<Arguments> generator(){
        return Stream.of(
                of(null, 8, false, false, false),
                of("", 8, false, false, false),

                of("parabola", 6, false, false, true),
                of("palo", 6, false, false, false),
                of("mitico", 6, false, false, true),

                of("parabola", 6, true, false, false),
                of("parabola1", 6, true, false,true),
                of("parabola", 6, false, false, true),
                of("parabola1", 6, false, false, false), //errore progettazione

                of("parabola", 6, false, true, false),
                of("parabola@", 6, false, true,true),
                of("parabola", 6, false, false, true),
                of("parabola@", 6, false, false, false) //errore progettazione


        );
    }


    @AfterAll
    static void tearDownAfterClass() {
        passwordValidator = null;

    }


}

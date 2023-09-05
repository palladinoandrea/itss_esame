import org.example.PasswordValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.of;


@TestMethodOrder(OrderAnnotation.class)
public class PasswordValidatorTest {

    private static PasswordValidator passwordValidator;

    int MIN_LENGTH = 8;
    String password;


    @BeforeAll
    static void setUpBeforeClass() {
        passwordValidator = new PasswordValidator();

    }


    @ParameterizedTest
    @ValueSource(strings = {"ciaomichiamonico", "Parabola", "Mipiaceilcioccolato"})
    @DisplayName("Password Length Requirement Test")
    @Order(1)
    public void testPasswordLengthRequirement(String password) {

        passwordValidator = new PasswordValidator(MIN_LENGTH, false, false);

        assertTrue(passwordValidator.validate(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ciaocia1", "Parabola1", "12345678"})
    @DisplayName("Number Requirement Test")
    @Order(2)
    public void testRequisiteNumerous(String password) {
            passwordValidator = new PasswordValidator(MIN_LENGTH, true, false);
        boolean validationResult = passwordValidator.validate(password);
        assertTrue(validationResult, "La password deve contenere almeno un numero");
        }
    @ParameterizedTest
    @ValueSource(strings = {"ciaocia@", "Parabola?", "@@@@@@@@"})
    @DisplayName("Special Character Requirement Test")
    @Order(3)
    public void testSpecialCharacterRequirement(String password) {
        passwordValidator = new PasswordValidator(MIN_LENGTH, false, true);
        boolean validationResult = passwordValidator.validate(password);
        assertTrue(validationResult, "La password deve contenere almeno un carattere speciale");
    }

    @ParameterizedTest
    @EmptySource
    @DisplayName("Empty Password Test")
    @Order(4)
    public void testEmptyPassword(String password) {
        passwordValidator = new PasswordValidator(MIN_LENGTH, true, true);
        boolean validationResult = passwordValidator.validate(password);
        assertFalse(validationResult, "La password non deve essere vuota");
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Null Password Test")
    @Order(5)
    public void testPasswordNulla(String password) {
        passwordValidator = new PasswordValidator(MIN_LENGTH, true, true);
        boolean validationResult = passwordValidator.validate(password);
        assertFalse(validationResult, "La password non deve essere nulla");
    }


    @ParameterizedTest
    @ValueSource(strings = {"Pianura123?", "Parabola1@", "Catastrofe99*", "Pasquale001@"})
    @DisplayName("Requisiti minimi password valida")
    @Order(6)
    public void testValidPassword(String password) {
        passwordValidator = new PasswordValidator(MIN_LENGTH, true, true);
        boolean validationResult = passwordValidator.validate(password);

        Assertions.assertAll(
                () -> assertTrue(password.length() >= MIN_LENGTH, "Lunghezza minima non rispettata"),
                () -> assertTrue(validationResult, "La password deve contenere almeno un numero"),
                () -> assertTrue(validationResult, "La password deve contenere almeno un carattere speciale")
        );
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

                of("parabola1", 6, false, false, false), //errore progettazione corretto

                of("parabola", 6, false, true, false),
                of("parabola@", 6, false, true,true),
                of("parabola", 6, false, false, true),
                of("parabola@", 6, false, false, false) //errore progettazione corretto


        );
    }




    @AfterAll
    static void tearDownAfterClass() {
        passwordValidator = null;

    }


}

import net.jqwik.api.lifecycle.AfterProperty;
import net.jqwik.api.statistics.Statistics;
import org.example.PasswordValidator;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import static java.lang.Character.isDigit;


public class PasswordValidatorPbtTest {

    int MIN_LENGTH = 6;
    int MAX_LENGTH = 20;
    boolean requiresNumber = true;
    boolean requiresSpecialChar = true;


    @Property
    @Report(Reporting.GENERATED)
    void validPasswordsAlwaysPass(
            @ForAll("validPassword") String password

    ) {


        PasswordValidator validator = new PasswordValidator(MIN_LENGTH, requiresNumber, requiresSpecialChar);
        boolean isValid = validator.validate(password);

        int length = password.length();
        int digit = 0;

        for(int i = 0; i < length; i++){

            if(isDigit(password.charAt(i))){
                digit++;
            }
        }

        String lengt = length >  10 ? "lunghezza < 10 " : "lunghezza > 10";
        String digi = digit > 3 ? "Meno di 3 numeri" : "ALmeno 3 numeri";
        Statistics.collect(lengt, digit);

        Assertions.assertThat(isValid).isTrue();
    }

    @Provide
    Arbitrary<String> validPassword() {

        Arbitrary<String> pass = Arbitraries.strings().withCharRange('a', 'z')
                .ofMinLength(MIN_LENGTH + 1).ofMaxLength(MAX_LENGTH);

        Arbitrary<Integer> number = Arbitraries.integers().between(0, 9);

        Arbitrary<String> special = Arbitraries.strings().withChars("!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?")
                .ofLength(1);

        return Combinators.combine(pass, number, special)
                .as((p, n, s) -> p + "" + n + "" + s);

    }


    @Property
    @Report(Reporting.GENERATED)
    void invalidPasswordsAlwaysFail(
            @ForAll("invalidPasswords") String password
    ) {
        PasswordValidator validator = new PasswordValidator(MIN_LENGTH, requiresNumber, requiresSpecialChar);
        boolean isValid = validator.validate(password);

        int length = password.length();
        int digit = 0;

        for(int i = 0; i < length; i++){

            if(isDigit(password.charAt(i))){
                digit++;
            }
        }

        String lengt = length >  10 ? "lunghezza < 10 " : "lunghezza > 10";
        String digi = digit > 3 ? "Meno di 3 numeri" : "ALmeno 3 numeri";
        Statistics.collect(lengt, digit);

        Assertions.assertThat(isValid).isFalse();
    }


    @Provide
    Arbitrary<String> invalidPasswords() {

        Arbitrary<String> pass = Arbitraries.strings().withCharRange('a', 'z')
                .ofMinLength(1).ofMaxLength(MAX_LENGTH);

        Arbitrary<String> special = Arbitraries.strings().withChars("!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?")
                .ofLength(1);

        Arbitrary<Integer> number = Arbitraries.integers().between(0, 9);

        Arbitrary<String> passSpecialCombo = Combinators.combine(pass, special)
                .as((p, s) -> p + "" + s);

        Arbitrary<String> passNumberCombo = Combinators.combine(pass, number)
                .as((p, n) -> p + "" + n);

        return Arbitraries.oneOf(pass, passNumberCombo, passSpecialCombo);

    }


    @Property
    void nullPasswordAlwaysFails ()
       {
           PasswordValidator validator = new PasswordValidator(8, requiresNumber, requiresSpecialChar);
           boolean isValid = validator.validate(null);
           Assertions.assertThat(isValid).isFalse();
       }



}
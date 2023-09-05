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



    @Property
    @Report(Reporting.GENERATED)
    void validPasswordsAlwaysPass(
            @ForAll("validPassword") Tuple.Tuple3< Boolean, Boolean, String > password

    ) {

        boolean requiresNum = password.get1();
        boolean requiresSpec = password.get2();
        String str = password.get3();


        PasswordValidator validator = new PasswordValidator(MIN_LENGTH, requiresNum, requiresSpec);
        boolean isValid = validator.validate(str);

        int plength = str.length();




        String lengt = plength >  10 ? "lunghezza > 10 " : "lunghezza < 10";

        String pass_number = requiresNum ? "Password con numero" : "Password senza numero";
        String pass_special = requiresSpec ? "Password con carattere speciale" : "Password senza carattere speciale";



        Statistics.collect(lengt);
        Statistics.collect(pass_number);
        Statistics.collect(pass_special);

        //Statistics.collect(lengt, pass_number, pass_special);


        Assertions.assertThat(isValid).isTrue();
    }


    @Provide
    Arbitrary<Tuple.Tuple3<Boolean, Boolean, String>> validPassword() {


        Arbitrary<Boolean> requiresNumArb = Arbitraries.of(true, false);
        Arbitrary<Boolean> requiresSpecArb = Arbitraries.of(true, false);




        return Combinators.combine(requiresNumArb,requiresSpecArb)
                .as(Tuple::of)
                .flatMap(tuple -> {


                    boolean number_temp = tuple.get1();
                    boolean special_temp = tuple.get2();

                    Arbitrary<String> pass = Arbitraries.strings().withCharRange('a', 'z')
                            .ofMinLength(MIN_LENGTH)
                            .ofMaxLength(MAX_LENGTH);

                    Arbitrary<Integer> number = Arbitraries.integers().between(0, 9);

                    Arbitrary<String> special = Arbitraries.strings().withChars("!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?")
                            .ofLength(1);


                    //necessario sia numero che carattere speciale
                    if(number_temp && special_temp){


                        Arbitrary<String> str = Combinators.combine(pass, number, special)
                                .as((p, n, s) -> p + "" + n + "" + s);

                        return str
                                .map(s -> Tuple.of(number_temp, special_temp, s ));



                    }else if(number_temp){
                        //necessario numero e non carattere speciale

                        Arbitrary<String> str = Combinators.combine(pass, number)
                                .as((p, n) -> p + "" + n);

                        return str
                                .map(s -> Tuple.of(number_temp, special_temp, s ));




                    }else if(special_temp){
                        //necessario carattere speciale e non il numero

                        Arbitrary<String> str = Combinators.combine(pass, special)
                                .as((p, s) -> p + "" + s);

                        return str
                                .map(s -> Tuple.of(number_temp, special_temp, s ));



                    }else{
                        //non deve esserci nè il numero e nè il carattere speciale

                        return pass
                                .map(s -> Tuple.of(number_temp, special_temp, s));


                    }

                });

    }





    @Property
    @Report(Reporting.GENERATED)
    void invalidPasswordsAlwaysFail(
            @ForAll("invalidPassword") Tuple.Tuple3< Boolean, Boolean, String > password

    ) {


        boolean requiresNum = password.get1();
        boolean requiresSpec = password.get2();
        String str = password.get3();


        PasswordValidator validator = new PasswordValidator(MIN_LENGTH, requiresNum, requiresSpec);
        boolean isValid = validator.validate(str);

        int plength = str.length();
        int digit = 0;

        for(int i = 0; i < plength; i++){

            if(isDigit(str.charAt(i))){
                digit++;
            }
        }

        String length = plength >  10 ? "lunghezza < 10 " : "lunghezza > 10";
        String digi = digit > 3 ? "Meno di 3 numeri" : "ALmeno 3 numeri";
        Statistics.collect(length, digi);

        Assertions.assertThat(isValid).isFalse();
    }


    @Provide
    Arbitrary<Tuple.Tuple3<Boolean, Boolean, String>> invalidPassword() {


        Arbitrary<Boolean> requiresNumArb = Arbitraries.of(true, false);
        Arbitrary<Boolean> requiresSpecArb = Arbitraries.of(true, false);




        return Combinators.combine(requiresNumArb,requiresSpecArb)
                .as(Tuple::of)
                .flatMap(tuple -> {

                    boolean number_temp = tuple.get1();
                    boolean special_temp = tuple.get2();


                    Arbitrary<Integer> number = Arbitraries.integers().between(0, 9);

                    Arbitrary<String> special = Arbitraries.strings().withChars("!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?")
                            .ofLength(1);


                    //necessario sia numero che carattere speciale
                    if(number_temp && special_temp){

                        Arbitrary<String> pass = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MAX_LENGTH);

                        Arbitrary<String> passFail = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MIN_LENGTH - 2);


                        Arbitrary<String> str = Combinators.combine(pass, special)
                                .as((p, s) -> p + "" + s);

                        Arbitrary<String> str1 = Combinators.combine(pass, number)
                                .as((p, n) -> p + "" + n);


                        return Arbitraries.oneOf(passFail, str, str1, pass)
                                .map(s -> Tuple.of(number_temp, special_temp, s ));


                    }else if(number_temp){
                        //necessario numero e non carattere speciale

                        Arbitrary<String> pass = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MAX_LENGTH);

                        Arbitrary<String> passFail = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MIN_LENGTH - 2);


                        Arbitrary<String> str = Combinators.combine(pass, special)
                                .as((p, s) -> p + "" + s);

                        Arbitrary<String> str1 = Combinators.combine(passFail, number)
                                .as((p, n) -> p + "" + n);

                        return Arbitraries.oneOf(passFail, str, str1, pass)
                                .map(s -> Tuple.of(number_temp, special_temp, s ));




                    }else if(special_temp){
                        //necessario carattere speciale e non il numero

                        Arbitrary<String> pass = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MAX_LENGTH);

                        Arbitrary<String> passFail = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MIN_LENGTH - 2);

                        Arbitrary<String> str = Combinators.combine(passFail, special)
                                .as((p, s) -> p + "" + s);

                        Arbitrary<String> str1 = Combinators.combine(pass, number)
                                .as((p, n) -> p + "" + n);

                        return Arbitraries.oneOf(passFail, str, str1, pass)
                                .map(s -> Tuple.of(number_temp, special_temp, s ));



                    }else{
                        //non deve esserci nè il numero e nè il carattere speciale

                        Arbitrary<String> pass = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MAX_LENGTH);

                        Arbitrary<String> passFail = Arbitraries.strings().withCharRange('a', 'z')
                                .ofMinLength(1)
                                .ofMaxLength(MIN_LENGTH - 2);

                        Arbitrary<String> str = Combinators.combine(pass, special)
                                .as((p, s) -> p + "" + s);

                        Arbitrary<String> str1 = Combinators.combine(pass, number)
                                .as((p, n) -> p + "" + n);

                        Arbitrary<String> str2 = Combinators.combine(pass, number, special)
                                .as((p, n, s) -> p + "" + n + "" + s);

                        return Arbitraries.oneOf(passFail, str, str1, str2)
                                .map(s -> Tuple.of(number_temp, special_temp, s));


                    }

                });

    }


    @Property
    void nullPasswordAlwaysFails (
            @ForAll boolean requiresNumber,
            @ForAll boolean requiresSpecialChar
    )
    {
        PasswordValidator validator = new PasswordValidator(MIN_LENGTH, requiresNumber, requiresSpecialChar);
        boolean isValid = validator.validate(null);
        Assertions.assertThat(isValid).isFalse();
    }






}


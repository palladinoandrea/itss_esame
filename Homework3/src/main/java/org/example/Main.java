package org.example;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Invio with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome!");

        PasswordValidator validator = new PasswordValidator(6, true, true);

        String password = "password1@";

        System.out.println("password : " + password + " esito : " +  validator.validate(password));


    }
}
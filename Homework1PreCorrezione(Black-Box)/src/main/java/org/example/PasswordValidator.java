package org.example;

public class PasswordValidator {
    private int minLength;
    private boolean requiresNumber;
    private boolean requiresSpecialChar;

    public PasswordValidator(int minLength, boolean requiresNumber, boolean requiresSpecialChar) {
        this.minLength = minLength;
        this.requiresNumber = requiresNumber;
        this.requiresSpecialChar = requiresSpecialChar;
    }


    public PasswordValidator() {
    }

    public boolean validate(String password) {
        if (password == null) {
            return false;
        }

        if (password.length() < minLength) {
            return false;
        }

        if (requiresNumber && !password.matches(".*\\d.*")) {
            return false;
        }

        if (requiresSpecialChar && !password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            return false;
        }

        return true;
    }

}

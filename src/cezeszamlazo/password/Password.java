/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.password;

import java.util.Random;

/**
 *
 * @author szekus
 */
public class Password implements IPassword {

    private String passwordString;
    private static PasswordRules passwordRules;

    private Password() {
//        this.passwordRules = new PasswordRules.PasswordRulesBuilder().build();
    }

    private Password(String passwordString) {
        this.passwordString = passwordString;
//        this.passwordRules = new PasswordRules.PasswordRulesBuilder().build();
    }

    public static Password create() {
        return new Password();
    }

    public static Password createPasswordString(String passwordString, PasswordRules passwordRules) throws PasswordRulesException {
        Password.passwordRules = passwordRules;
        if (Password.validate(passwordString)) {
            return new Password(passwordString);
        }

        return new Password();
    }

    public static boolean validate(String passwordString) throws PasswordRulesException {

        if (empty(passwordString)) {
            throw new PasswordRulesException(passwordRules.getEmpty().getMsg());
        }

        if (passwordString.length() < passwordRules.getMinLength().getCount()) {
            throw new PasswordRulesException((passwordRules.getMinLength().getMsg()));
        }
        if (passwordString.length() > passwordRules.getMaxLength().getCount()) {
            throw new PasswordRulesException(passwordRules.getMaxLength().getMsg());
        }
        if (!isEnoughNumber(passwordString, passwordRules.getMinNumberOfNumber().getCount())) {
            throw new PasswordRulesException(passwordRules.getMinNumberOfNumber().getMsg());
        }
        if (!isEnoughUppercase(passwordString, passwordRules.getMinUpperCaseCharacter().getCount())) {
            throw new PasswordRulesException(passwordRules.getMinUpperCaseCharacter().getMsg());
        }

        return true;
    }

    public static String generatePassword(PasswordRules passwordRules) {
        String result = "";
        int randomIndex = 0;
        char character;
        Random generator = new Random();
        int maxLength = passwordRules.getMaxLength().getCount();
        int minLength = passwordRules.getMinLength().getCount();
        int minNumberOfNumber = passwordRules.getMinNumberOfNumber().getCount();
        int minUpperCaseCharacter = passwordRules.getMinUpperCaseCharacter().getCount();

        for (int i = 0; i < minUpperCaseCharacter; i++) {
            randomIndex = generator.nextInt(ALPHA_UPPER_CHARACTERS.length);
            character = ALPHA_UPPER_CHARACTERS[randomIndex];
            result += character;
        }

        for (int i = 0; i < minNumberOfNumber; i++) {
            randomIndex = generator.nextInt(NUMERIC_CHARACTERS.length);
            character = NUMERIC_CHARACTERS[randomIndex];
            result += character;
        }

        for (int i = 0; i < minLength - (minNumberOfNumber + minUpperCaseCharacter); i++) {
            randomIndex = generator.nextInt(ALPHA_LOWER_CHARACTERS.length);
            character = ALPHA_LOWER_CHARACTERS[randomIndex];
            result += character;
        }
        result = Shuffle.create().shuffle(result);
        return result;
    }

    public PasswordRules getPasswordRules() {
        return passwordRules;
    }

    public void setPasswordRules(PasswordRules passwordRules) {
        this.passwordRules = passwordRules;
    }

    private static boolean empty(final String s) {
        // Null-safe, short-circuit evaluation.
        return s == null || s.trim().isEmpty();
    }

    private static boolean isEnoughNumber(final String string, int numberOfNumbers) {
        if (numberOfNumbers == 0) {
            return true;
        }
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                count++;
                if (count >= numberOfNumbers) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isEnoughUppercase(final String string, int numberOfUpperCasaCharacter) {
        if (numberOfUpperCasaCharacter == 0) {
            return true;
        }
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                count++;
                if (count >= numberOfUpperCasaCharacter) {
                    return true;
                }
            }
        }
        return false;
    }

//    public boolean isEquals(Password password) {
//        return password.passwordString.equals(this.passwordString);
//    }
    @Override
    public String toString() {
        return "password: " + passwordString;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Password)) {
            return false;
        }
        Password passwordObject = (Password) obj;
        return passwordObject.passwordString.equals(passwordString);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + passwordString.hashCode();
        return result;
    }

    public void comparePassword(String passwordString_2) throws PasswordRulesException {
        if (!this.passwordString.equals(passwordString_2)) {
            throw new PasswordRulesException(passwordRules.getComparePassword().getMsg());
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.password;

/**
 *
 * @author szekus
 */
public class PasswordRules {

    private Rule comparePassword;
    private Rule empty;
    private Rule minLength;
    private Rule maxLength;
    private Rule minNumberOfNumber;
    private Rule minUpperCaseCharacter;

    private PasswordRules() {
    }

    public static PasswordRules create() {
        return new PasswordRules();
    }

    public static PasswordRules create(PasswordRulesBuilder passwordRulesBuilder) {
        return new PasswordRules(passwordRulesBuilder);
    }

    private PasswordRules(PasswordRulesBuilder passwordRulesBuilder) {
        this.comparePassword = passwordRulesBuilder.comparePassword;
        this.empty = passwordRulesBuilder.empty;
        this.minLength = passwordRulesBuilder.minLength;
        this.maxLength = passwordRulesBuilder.maxLength;
        this.minNumberOfNumber = passwordRulesBuilder.minNumberOfNumber;
        this.minUpperCaseCharacter = passwordRulesBuilder.minUpperCaseCharacter;
    }

    public static class PasswordRulesBuilder {

        public Rule comparePassword;
        public Rule empty;
        public Rule minLength;
        public Rule maxLength;
        public Rule minNumberOfNumber;
        public Rule minUpperCaseCharacter;

        public PasswordRulesBuilder minLength(int count, String msg) {
            this.minLength = new Rule(count, msg);
            return this;
        }

        public PasswordRulesBuilder minLength(int count) {
            this.minLength = new Rule(count);
            this.minLength.setMsg("TOO SHORT");
            return this;
        }

        public PasswordRulesBuilder maxLength(int count, String msg) {
            this.maxLength = new Rule(count, msg);

            return this;
        }

        public PasswordRulesBuilder maxLength(int count) {
            this.maxLength = new Rule(count);
            this.minLength.setMsg("TOO LONG");
            return this;
        }

        public PasswordRulesBuilder minNumberOfNumber(int count, String msg) {
            this.minNumberOfNumber = new Rule(count, msg);
            return this;
        }

        public PasswordRulesBuilder minNumberOfNumber(int count) {
            this.minNumberOfNumber = new Rule(count);
            this.minLength.setMsg("NEED MORE NUMBER");
            return this;
        }

        public PasswordRulesBuilder minUpperCaseCharacter(int count, String msg) {
            this.minUpperCaseCharacter = new Rule(count, msg);
            return this;
        }

        public PasswordRulesBuilder minUpperCaseCharacter(int count) {
            this.minUpperCaseCharacter = new Rule(count);
            this.minLength.setMsg("NEED MORE UpperCaseCharacter");
            return this;
        }

        public PasswordRulesBuilder empty() {
            this.empty = new Rule(0, "NOT BE EMPTY");
            return this;
        }

        public PasswordRulesBuilder empty(String msg) {
            this.empty = new Rule(0, msg);
            return this;
        }
        
         public PasswordRulesBuilder compare() {
            this.comparePassword = new Rule(0, "NOT Equal");
            return this;
        }

        public PasswordRulesBuilder compare(String msg) {
            this.comparePassword = new Rule(0, msg);
            return this;
        }

        public PasswordRules build() {
            return new PasswordRules(this);
        }

    }

    @Override
    public String toString() {
        return "empty: " + empty + "\n"
                + "minLength: " + minLength + "\n"
                + "maxLength: " + maxLength + "\n"
                + "minNumberOfNumber: " + minNumberOfNumber + "\n"
                + "minUpperCaseCharacter: " + minUpperCaseCharacter + "\n";

    }

    public Rule getMaxLength() {
        return maxLength;
    }

    public Rule getMinLength() {
        return minLength;
    }

    public Rule getMinNumberOfNumber() {
        return minNumberOfNumber;
    }

    public Rule getMinUpperCaseCharacter() {
        return minUpperCaseCharacter;
    }

    public Rule getEmpty() {
        return empty;
    }

    public Rule getComparePassword() {
        return comparePassword;
    }
    
    

}

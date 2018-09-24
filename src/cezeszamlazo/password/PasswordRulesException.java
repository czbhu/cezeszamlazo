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
public class PasswordRulesException extends Exception {

    String message;

    PasswordRulesException() {
        
    }

    PasswordRulesException(String message) {
        this.message = message;
    }

    public static PasswordRulesException create(String message) {
//        System.out.println(message);
        if (message != null) {
            return new PasswordRulesException();
        } else {
            return new PasswordRulesException(message);
        }

    }

    @Override
    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

}

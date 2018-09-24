/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.kintlevoseg;

/**
 *
 * @author szekus
 */
public class KintlevosegLevelException extends Exception {

    String msg;

    public KintlevosegLevelException() {
    }

    public KintlevosegLevelException(String msg) {
        this.msg = msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    
    public String getMsg() {
        return msg;
    }

}

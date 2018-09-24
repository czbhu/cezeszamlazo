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
class Rule {

    protected int count;
    protected String msg;

    public Rule() {
    }

    public Rule(int count, String msg) {
        this.count = count;
        this.msg = msg;
    }
    
    public Rule(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public String getMsg() {
        return msg;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "count: " + count + ", msg: " + msg;
    }

}

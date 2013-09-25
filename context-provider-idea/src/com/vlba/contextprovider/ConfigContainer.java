package com.vlba.contextprovider;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 25.09.13
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
public class ConfigContainer {

    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String SERVER = "server";


    public static final String LOGIN_DEFAULT = "context";
    public static final String PASSWORD_DEFAULT = "contextpassword";
    public static final String SERVER_DEFAULT = "http://10.0.2.2:5000/context/repository/api/v1.0/push-context";

    public String login;
    public String password;
    public String server;

    public String toString(){

        return this.login + " , " + this.password + " , " + this.server;
    }
}

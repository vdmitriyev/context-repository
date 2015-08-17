package com.vlba.contextprovider;

/**
 * Created with IntelliJ IDEA.
 * Author: Viktor Dmitriyev
 * Date: 25.09.13
 * Time: 10:23
 * To change this template use File | Settings | File Templates.
 */
public class ConfigContainer {

    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String SERVER = "server";
    public static final String newUser="edit-profile";
    public static final String LOADSERVICES="profile-services";


    public static final String LOGIN_DEFAULT = "";
    public static final String PASSWORD_DEFAULT = "";
    public static final String SERVER_DEFAULT = "http://134.106.153.217:5000/context/repository/api/v1.0/";

    public String login;
    public String password;
    public String server;

    public String toString(){

        return this.login + " , " + this.password + " , " + this.server;
    }
}

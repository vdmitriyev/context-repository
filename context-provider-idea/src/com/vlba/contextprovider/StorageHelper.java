package com.vlba.contextprovider;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 25.09.13
 * Time: 10:14
 * To change this template use File | Settings | File Templates.
 */
public class StorageHelper {

    private static final String StorageConfigurations = "User-Configurations";

    public static void saveConfigurations(Context context, ConfigContainer cc){

        SharedPreferences storage = context.getSharedPreferences(StorageConfigurations, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = storage.edit();
        e.putString(ConfigContainer.LOGIN, cc.login);
        e.putString(ConfigContainer.PASSWORD, cc.password);
        e.putString(ConfigContainer.SERVER, cc.server);
        e.commit();
    }

    public static ConfigContainer readConfigurations(Context context){

        SharedPreferences storage = context.getSharedPreferences(StorageConfigurations, Context.MODE_PRIVATE);

        ConfigContainer result = new ConfigContainer();

        result.login = storage.getString(ConfigContainer.LOGIN, ConfigContainer.LOGIN_DEFAULT);
        result.password = storage.getString(ConfigContainer.PASSWORD, ConfigContainer.PASSWORD_DEFAULT);
        result.server = storage.getString(ConfigContainer.SERVER, ConfigContainer.SERVER_DEFAULT);

        return  result;
    }
}

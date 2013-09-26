package com.vlba.contextprovider;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.text.format.Time;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * Author: Viktor Dmitriyev
 * Date: 26.09.13
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class ContextProvider extends Activity {

    public static final String TAG = ContextProvider.class.getName();

    public static final String JSON_UID = "uid";
    public static final String CTX_TIME = "Time";
    public static final String CTX_LOCATION = "Location";
    public static final String CTX_CONTENT  = "Content";
    public static final String CTX_BATTERY_LIFE = "BatteryLife";

    JSONObject json = new JSONObject();


    public ContextProvider(){

        try {
            json.put(ContextProvider.JSON_UID, "3");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "[e] JSONException Exception", e);
        }

        this.updateContext();
    }

    private void updateContext(){
        try {
            json.remove(CTX_TIME);
            json.remove(CTX_LOCATION);
            json.remove(CTX_CONTENT);
            json.remove(CTX_BATTERY_LIFE);
            json.put(ContextProvider.CTX_TIME, this.getTimeValue());
            json.put(ContextProvider.CTX_LOCATION, this.getLocationValue());
            json.put(ContextProvider.CTX_CONTENT, this.getContentValue());
            json.put(ContextProvider.CTX_BATTERY_LIFE, this.getBatteryLifeValue());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "[e] JSONException Exception", e);
        }
    }

    public String getTimeValue(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
        String resultTime = sdf.format(new Date());

        return resultTime;
    }

    public String getBatteryLifeValue(){
        try {
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus =  this.registerReceiver(null, iFilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level / (float)scale;

            return String.valueOf(batteryPct);
        } catch (Exception ex){
            ex.printStackTrace();
            Log.d(TAG, "[e] getBatteryLifeValue Exception", ex);
        }

        return "100";
    }

    public String getContentValue(){

        return "Chrome Browser";
    }

    public String getLocationValue(){

        return "0.0 0.0 0.0 0.0";
    }

    public JSONObject getContext(){

        this.updateContext();
        return this.json;
    }
}

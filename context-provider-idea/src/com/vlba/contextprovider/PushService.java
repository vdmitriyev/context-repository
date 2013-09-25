package com.vlba.contextprovider;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 25.09.13
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
public class PushService extends Service {

    public static final String TAG = PushService.class.getName();

    private ConfigContainer config = new ConfigContainer();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, "New Service Created", Toast.LENGTH_LONG).show();
    }

    private boolean continuePushing = true;

    Thread thread = new Thread(){

        @Override
        public void run() {
            try {
                while(continuePushing) {
                    sleep(1000);
                    pushContext();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onStart(Intent intent, int startId) {

        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        Bundle extras = intent.getExtras();

        if (extras != null){

            config.login =  extras.getString(ConfigContainer.LOGIN);
            config.password =  extras.getString(ConfigContainer.PASSWORD);
            config.server =  extras.getString(ConfigContainer.SERVER);
            //Toast.makeText(this, "Server : " + config.server, Toast.LENGTH_LONG).show();
        }

        if (HttpHelpers.isInternetAvailable()){
            continuePushing = true;
            thread.start();
        } else {
            Toast.makeText(this, "Internet is not available. Try later.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onDestroy() {

        try{
            continuePushing = false;
            thread.destroy();
        } catch (Exception ex) {
            Log.d(TAG, "Exception :" + ex.toString());
        }

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


    private void pushContext(){

        boolean result = false;

        JSONObject json = new JSONObject();

        try {

            json.put("uid", "2");
            json.put("Time", "23:00");
            json.put("Location", "39 23.516 122 08.625");
            json.put("Content", "Chrome Browser");
            json.put("BatteryLife", "90");
        } catch (Exception ex) {
            Log.d(TAG, "Exception :" + ex.toString());
        }

        try {

            DefaultHttpClient http = new DefaultHttpClient();
//            CredentialsProvider credProvider = new BasicCredentialsProvider();
//            credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_SCHEME),
//                    new UsernamePasswordCredentials(config.login, config.password));
//            http.setCredentialsProvider(credProvider);

            HttpPost post = new HttpPost(config.server);

            String credentials = config.login + ":" + config.password;
            byte[] data = credentials.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            post.setHeader("Authorization", "Basic " + base64);

            try {
                String postMessage = json.toString();
                Log.d(TAG, "postMessage :" + postMessage);
                StringEntity se = new StringEntity(postMessage, "UTF-8");
                post.setEntity(se);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e(TAG, "UnsupportedEncoding: ", e);
            }

            //post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            post.setHeader("Accept-Encoding", "application/json");
            post.setHeader("Accept-Language", "en-US");

            HttpResponse response = http.execute(post);
            Log.d(TAG, "Server Response:" + response.getStatusLine().toString()+" , " + response.getEntity().toString());
            result = true;
        } catch (ClientProtocolException e) {
            Log.d(TAG, "Client Protocol Exception", e);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Exception:", e);
        }

//        if (result)
//            Toast.makeText(this, "Context Pushed", Toast.LENGTH_LONG).show();
//        else
//            Toast.makeText(this, "Context IS NOT Pushed", Toast.LENGTH_LONG).show();

    }

}


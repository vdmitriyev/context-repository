package com.vlba.contextprovider;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * Created with IntelliJ IDEA.
 * Author: Viktor Dmitriyev
 * Date: 25.09.13
 * Time: 14:02
 * To change this template use File | Settings | File Templates.
 */
public class PushService extends Service {

    public static final String TAG = PushService.class.getName();
    public static final int PUSH_REQUEST_INTERVAL = 1000;

    private ConfigContainer config = new ConfigContainer();
    private boolean continuePushing = true;
    private ContextProvider cp = new ContextProvider();

    Thread pushOverHTTPThread = new Thread(){

        @Override
        public void run() {
            try {
                while(continuePushing) {
                    sleep(PUSH_REQUEST_INTERVAL);
                    pushContext();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d(TAG, "[e] Thread Exception", e);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, "[info] New Service Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "[info] [info] New Service Created");

    }

    @Override
    public void onStart(Intent intent, int startId) {

        if (config == null)
        config = new ConfigContainer();

        config =  StorageHelper.readConfigurations(this);
        Toast.makeText(this, "[info] Server Endpoint " + config.server, Toast.LENGTH_LONG).show();
        Log.d(TAG, "[info] Server Endpoint " + config.server);

        if (HttpHelpers.isInternetAvailable()){
            continuePushing = true;
            pushOverHTTPThread.start();
        } else {
            Toast.makeText(this, "[info] Internet is not available. Try later.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "[info] Internet is not available. Try later.");
        }
    }

    @Override
    public void onDestroy() {

        try{
            continuePushing = false;
            pushOverHTTPThread.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, "Exception:" + ex.toString());
        }

        Toast.makeText(this, "[info] Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private void pushContext(){

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

//            httpClient.getCredentialsProvider().setCredentials(
//                    new AuthScope(config.server, 5000),
//                    new UsernamePasswordCredentials(config.login, config.password)
//            );

//            String credentials = config.login + ":" + config.password;
//            byte[] data = credentials.getBytes("UTF-8");
//            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
//            httpPost.setHeader("Authorization", "Basic " + base64);


            HttpPost httpPost = new HttpPost(config.server);
            httpPost.setHeader("content-type", "application/json");

            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(config.login, config.password);
            Header bs = new BasicScheme().authenticate(creds, httpPost);

            httpPost.addHeader("Authorization", bs.getValue());

            //JSONObject context = new ContextProvider().getContext();

            StringEntity entity = new StringEntity(cp.getContext().toString(), HTTP.UTF_8);
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
            Log.d(TAG, "Client Protocol Exception", ex);
        } catch (Exception ex){
            ex.printStackTrace();
            Log.d(TAG, "Exception-pushContext02(): ", ex);
        }
    }

}


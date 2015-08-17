package com.vlba.contextprovider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * Author: Viktor Dmitiriyev
 * Date: 24.09.13
 * Time: 22:08
 * To change this template use File | Settings | File Templates.
 */
public class HttpHelpers {


    private static Context _context;

    public static void initialize(Context context){
        _context = context;
    }

    public static boolean isInternetAvailable(){

        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;

        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected())
            return true;

        return false;
    }


    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public static String connect(String url)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            //Log.i(TAG,response.getStatusLine().toString());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return null;
    }


    public  static String SecureConnection(String url,String method)
    {
        try {
            ConfigContainer cc = StorageHelper.readConfigurations(_context);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            //httpget.setHeader("content-type", "application/json");

            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(cc.login, cc.password);
            Header bs ;
            if(method=="GET"){
                HttpGet httpget = new HttpGet(url);
                bs = new BasicScheme().authenticate(creds, httpget);
                httpget.addHeader("Authorization", bs.getValue());
                response = httpclient.execute(httpget);
            }else {

                HttpPost httpPost = new HttpPost(url);
                bs = new BasicScheme().authenticate(creds,httpPost );
                httpPost.addHeader("Authorization", bs.getValue());
                response = httpclient.execute(httpPost);

            }





                //Log.i(TAG,response.getStatusLine().toString());
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result= convertStreamToString(instream);
                    instream.close();
                    return result;
                }
            } catch (Exception e) {

            }

        return null;
    }
    public  static String SecureConnection(String url,String method,JSONArray data)
    {
        try {
            ConfigContainer cc = StorageHelper.readConfigurations(_context);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response= null;
            //httpget.setHeader("content-type", "application/json");

            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(cc.login, cc.password);
            Header bs ;
            if(method=="GET"){
                HttpGet httpget = new HttpGet(url);
                bs = new BasicScheme().authenticate(creds, httpget);
                httpget.addHeader("Authorization", bs.getValue());

                response = httpclient.execute(httpget);
            }else if(method=="POST") {


                HttpPost httpPost = new HttpPost(url);
                bs = new BasicScheme().authenticate(creds,httpPost );
                Log.d("testing post ", "posting " + data.toString());
                httpPost.addHeader("Authorization", bs.getValue());
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                StringEntity entity = new StringEntity(data.toString());
                httpPost.setEntity(entity);
                response = httpclient.execute(httpPost);
                Log.d("response from server",response.toString());

            }





                //Log.i(TAG,response.getStatusLine().toString());
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    String result= convertStreamToString(instream);
                    instream.close();
                    return result;
                }
            } catch (Exception e) {

            Log.d("exception", e.getMessage());

            }

        return url;
    }


}

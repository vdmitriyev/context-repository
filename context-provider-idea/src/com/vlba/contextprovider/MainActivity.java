package com.vlba.contextprovider;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.context_provider.R;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static final String TAG = MainActivity.class.getName();
    private final int CONFIGURATIONS_ACTIVITY_INTENT_RESULT = 101;

    private Button btnConfigurations;
    private Button btnStartPushing;
    private Button btnStopPushing;
    private Button btnUserProfile;

    private View.OnClickListener clConfigurations;
    private View.OnClickListener clStartPushing;
    private View.OnClickListener clStopPushing;
    private View.OnClickListener clUserProfile;
    private ConfigContainer configs = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initializeWindow();
        initializeApp();
    }

    private void initializeWindow(){
        // Setting full screen, no battery and clock
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Attaching UI to the activity
        setContentView(R.layout.main);
    }

    private void initializeApp(){
        HttpHelpers helpers = new HttpHelpers();
        //StorageHelper.saveConfigurations(this,new ConfigContainer());
        configs = StorageHelper.readConfigurations(this);
        if(configs.login.equals("")){

               new createNewProfile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            Log.d("create new profile","creating a new profile in progress");
        }else{
            Log.d("it's ok","known user");
        }
        Log.d(configs.login+"ee","login");
        HttpHelpers.initialize(this);
        bindButtonsListener();

//        this.registerReceiver(ContextProvider.mBatInfoReceiver,
//                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private void bindButtonsListener(){

        btnUserProfile = (Button) findViewById(R.id.btnUserProfile);
        btnConfigurations = (Button)findViewById(R.id.btnConfigurations);
        btnStartPushing = (Button)findViewById(R.id.btnStartPushing);
        btnStopPushing= (Button)findViewById(R.id.btnStopPushing);

        clConfigurations = new View.OnClickListener(){
            public void onClick(View v){
                getToConfigurations();
            }
        };

        clStartPushing = new View.OnClickListener(){
            public void onClick(View v){
                startPushService(v);
            }
        };

        clStopPushing = new View.OnClickListener(){
            public void onClick(View v){
                stopPushService(v);
            }
        };

        clUserProfile = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getToUserPorfile();

            }
        };
        btnStartPushing.setOnClickListener(clStartPushing);
        btnStartPushing.setOnClickListener(clStopPushing);
        btnConfigurations.setOnClickListener(clConfigurations);
        btnUserProfile.setOnClickListener(clUserProfile);




    }

    private void getToUserPorfile() {

        Intent i = new Intent(this,UserProfileActivity.class);
        i.putExtra(ConfigContainer.LOGIN, configs.login);
        i.putExtra(ConfigContainer.PASSWORD, configs.password);
        i.putExtra(ConfigContainer.SERVER, configs.server);
        startActivity(i);

    }

    private void getToConfigurations() {

        Intent i = new Intent(this, ConfigurationsActivity.class);

        i.putExtra(ConfigContainer.LOGIN, configs.login);
        i.putExtra(ConfigContainer.PASSWORD, configs.password);
        i.putExtra(ConfigContainer.SERVER, configs.server);
        startActivityForResult(i, CONFIGURATIONS_ACTIVITY_INTENT_RESULT);
    }

    public void startPushService(View v) {

        Log.i(TAG, "startPushService: 01");
        //Toast.makeText(this, "Start 01",Toast.LENGTH_LONG).show();

        Intent serviceIntent = new Intent(this, PushService.class);

        serviceIntent.putExtra(ConfigContainer.LOGIN, configs.login);
        serviceIntent.putExtra(ConfigContainer.PASSWORD, configs.password);
        serviceIntent.putExtra(ConfigContainer.SERVER, configs.server);

        Log.i(TAG, "Start 02");
        //Toast.makeText(this, "Start 02",Toast.LENGTH_LONG).show();

        this.startService(serviceIntent);

        Log.i(TAG, "startPushService: pushing started");
        //Toast.makeText(this, "Start 03", Toast.LENGTH_LONG).show();
    }

    public void stopPushService(View v){

        stopService(new Intent(this, PushService.class));
    }

    /*public void editUserProfile(View v){

        Toast.makeText(this, "Use Profile is not yet implemented", Toast.LENGTH_LONG).show();
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){

        if (requestCode != CONFIGURATIONS_ACTIVITY_INTENT_RESULT)
            return;

        Bundle extras = intent.getExtras();

        if (extras != null){
            configs.login = extras.getString(ConfigContainer.LOGIN);
            configs.password = extras.getString(ConfigContainer.PASSWORD);
            configs.server = extras.getString(ConfigContainer.SERVER);
            StorageHelper.saveConfigurations(this, configs);
            Toast.makeText(this, "[info] New configurations: " + "\n" + configs,
                           Toast.LENGTH_LONG).show();
        }
    }

    public   class  createNewProfile extends AsyncTask<Void,Void,ConfigContainer>{

        @Override
        protected ConfigContainer doInBackground(Void... params) {

            HttpHelpers helpers = new HttpHelpers();
            ConfigContainer configContainer = new ConfigContainer();
            configContainer = StorageHelper.readConfigurations(getApplicationContext());
            if(helpers.isInternetAvailable()){
                try {

                    Log.d("url",configContainer.server+configContainer.newUser);
                    String JsonResponse =  HttpHelpers.connect(configContainer.server+configContainer.newUser);
                    JSONObject json=new JSONObject(JsonResponse);
                    JSONArray jsonArray = json.getJSONArray("Profile");
                    JSONObject object = jsonArray.getJSONObject(0);
                    Log.d("done",json.toString());
                    Log.d("userName", object.getString("login"));
                    String login = object.getString("login");
                    String pw = object.getString("pass");
                    configContainer.login =login;
                    configContainer.password=pw;
                    StorageHelper.saveConfigurations(getApplicationContext(),configContainer);



            }catch (Exception e){

                    Log.d("error",e.getMessage());
                }
            }

            return  null;
        }


    }
}
/*DefaultHttpClient httpClient = new DefaultHttpClient();
HttpPost httpPost = new HttpPost(configContainer.server);
httpPost.setHeader("content-type", "application/json");

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(configContainer.login, configContainer.password);
        Header bs = new BasicScheme().authenticate(creds, httpPost);

        httpPost.addHeader("Authorization", bs.getValue());

        //JSONObject context = new ContextProvider().getContext();


        HttpResponse response = httpClient.execute(httpPost);*/

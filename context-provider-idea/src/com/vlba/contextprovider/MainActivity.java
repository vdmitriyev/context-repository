package com.vlba.contextprovider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.context_provider.R;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static final String TAG = MainActivity.class.getName();

    private final int CONFIGURATIONS_ACTIVITY_INTENT_RESULT = 101;

    private Button btnConfigurations;
    private Button btnStartPushing;
    private Button btnStopPushing;

    private View.OnClickListener clConfigurations;
    private View.OnClickListener clStartPushing;
    private View.OnClickListener clStopPushing;

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

        configs = StorageHelper.readConfigurations(this);
        HttpHelpers.initialize(this);
        bindButtonsListener();
    }

    private void bindButtonsListener(){

        btnConfigurations = (Button)findViewById(R.id.btnConfigurations);
//        btnStartPushing = (Button)findViewById(R.id.btnStartPushing);
//        btnStopPushing= (Button)findViewById(R.id.btnStopPushing);

        clConfigurations = new View.OnClickListener(){
            public void onClick(View v){
                getToConfigurations();
            }
        };

//        clStartPushing = new View.OnClickListener(){
//            public void onClick(View v){
//                startPushService(v);
//            }
//        };
//
//        clStopPushing = new View.OnClickListener(){
//            public void onClick(View v){
//                stopPushService(v);
//            }
//        };

        btnConfigurations.setOnClickListener(clConfigurations);
//        btnStartPushing.setOnClickListener(clStartPushing);
//        btnStartPushing.setOnClickListener(clStopPushing);
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

    public void editUserProfile(View v){

        Toast.makeText(this, "Use Profile is not yet implemented", Toast.LENGTH_LONG).show();
    }


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
            Toast.makeText(this, "New configurations: " + "\n" + configs,
                           Toast.LENGTH_LONG).show();
        }
    }
}

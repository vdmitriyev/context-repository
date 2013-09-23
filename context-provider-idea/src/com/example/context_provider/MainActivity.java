package com.example.context_provider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private final int CONFIGURATIONS_ACTIVITY_INTENT_RESULT = 101;

    private Button btnConfigurations;
    private View.OnClickListener clConfigurations;

    private String strLogin = "context";
    private String strPassword = "contextpassword";
    private String strServer = "http://127.0.0.1:5000/";

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

    private  void initializeApp(){

        btnConfigurations = (Button)findViewById(R.id.btnConfigurations);

        clConfigurations = new View.OnClickListener(){
            public void onClick(View v){
             getToConfigurations();
            }
        };

        btnConfigurations.setOnClickListener(clConfigurations);
    }

    private void getToConfigurations() {

        Intent i = new Intent(this, ConfigurationsActivity.class);

        i.putExtra("login", strLogin);
        i.putExtra("password", strPassword);
        i.putExtra("server", strServer);

        //startActivity(i);
        startActivityForResult(i, CONFIGURATIONS_ACTIVITY_INTENT_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent intent){

        if (requestCode != CONFIGURATIONS_ACTIVITY_INTENT_RESULT)
            return;

        Bundle extras = intent.getExtras();

        if (extras != null){
            strLogin = extras.getString("login");
            strPassword = extras.getString("password");
            strServer = extras.getString("server");

            Toast.makeText(this, "New configurations: " + "\b" +
                                strLogin + "\n" +
                                strPassword + "\n" +
                                strServer + "\n",
                            Toast.LENGTH_LONG).show();
        }
    }
}

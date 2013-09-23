package com.example.context_provider;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 23.09.13
 * Time: 23:34
 * To change this template use File | Settings | File Templates.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.content.Intent;
import android.widget.EditText;

public class ConfigurationsActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private Intent currentIntent;
    private EditText teLogin;
    private EditText tePassword;
    private EditText teServer;

    private Button btnConfigurationsSave;
    private Button btnConfigurationsCancel;

    private View.OnClickListener clConfigurationsSave;
    private View.OnClickListener clConfigurationsCancel;

    private String initialLogin = "";
    private String initialPassword = "";
    private String initialServer = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        initializeWindow();
        initializeApp();
        processIntent();
    }


    private void initializeWindow(){
        // Setting full screen, no battery and clock
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Attaching UI to the activity
        setContentView(R.layout.configurations);
    }

    private void initializeApp() {

        btnConfigurationsSave = (Button)findViewById(R.id.btnConfigurationsSave);
        btnConfigurationsCancel = (Button)findViewById(R.id.btnConfigurationsCancel);

        clConfigurationsSave = new View.OnClickListener(){
            public void onClick(View v){
                getToConfigurationsSave();
            }
        };

        clConfigurationsCancel = new View.OnClickListener(){
            public void onClick(View v){
                getToConfigurationsCancel();
            }
        };

        btnConfigurationsSave.setOnClickListener(clConfigurationsSave);
        btnConfigurationsCancel.setOnClickListener(clConfigurationsCancel);
    }

    private void getToConfigurationsCancel() {

        currentIntent.putExtra("login", initialLogin);
        currentIntent.putExtra("password", initialPassword);
        currentIntent.putExtra("server", initialServer);

        setResult(Activity.RESULT_OK, currentIntent);
        finish();
    }

    private void getToConfigurationsSave() {

        String changedLogin = teLogin.getText().toString();
        String changedPassword = tePassword.getText().toString();
        String changedServer = teServer.getText().toString();

        if (changedLogin != null)
            currentIntent.putExtra("login", changedLogin);
        else
            currentIntent.putExtra("login", initialLogin);

        if (changedPassword != null)
            currentIntent.putExtra("password", changedPassword);
        else
            currentIntent.putExtra("password", initialPassword);

        if (changedServer != null)
            currentIntent.putExtra("server", changedServer);
        else
            currentIntent.putExtra("server", initialServer);

        setResult(Activity.RESULT_OK, currentIntent);
        finish();
    }


    private void processIntent(){
        currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();

        if (extras != null){
            teLogin = (EditText) findViewById(R.id.editTextLogin);
            tePassword = (EditText) findViewById(R.id.editTextPassword);
            teServer = (EditText) findViewById(R.id.editTextServer);
            initialLogin =  extras.getString("login");
            initialPassword =  extras.getString("password");
            initialServer =  extras.getString("server");

            teLogin.setText(initialLogin);
            tePassword.setText(initialPassword);
            teServer.setText(initialServer);
        }
    }
}

package com.vlba.contextprovider;

/**
 * Created with IntelliJ IDEA.
 * Author: Viktor Dmitriyev
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
import com.example.context_provider.R;

public class ConfigurationsActivity extends Activity {

    private Intent currentIntent;
    private EditText teLogin;
    private EditText tePassword;
    private EditText teServer;

    private Button btnConfigurationsSave;
    private Button btnConfigurationsCancel;

    private View.OnClickListener clConfigurationsSave;
    private View.OnClickListener clConfigurationsCancel;

    ConfigContainer initial = new ConfigContainer();

    /**
     * Called when the activity is first created.
     */
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

        currentIntent.putExtra(ConfigContainer.LOGIN, initial.login);
        currentIntent.putExtra(ConfigContainer.PASSWORD, initial.password);
        currentIntent.putExtra(ConfigContainer.SERVER, initial.server);

        setResult(Activity.RESULT_OK, currentIntent);
        finish();
    }

    private void getToConfigurationsSave() {

        ConfigContainer changed = new ConfigContainer();

        changed.login = teLogin.getText().toString();
        changed.password = tePassword.getText().toString();
        changed.server = teServer.getText().toString();

        if (changed.login != null)
            currentIntent.putExtra(ConfigContainer.LOGIN, changed.login);
        else
            currentIntent.putExtra(ConfigContainer.LOGIN, initial.login);

        if (changed.password != null)
            currentIntent.putExtra(ConfigContainer.PASSWORD, changed.password);
        else
            currentIntent.putExtra(ConfigContainer.PASSWORD, initial.password);

        if (changed.server != null)
            currentIntent.putExtra(ConfigContainer.SERVER, changed.server);
        else
            currentIntent.putExtra(ConfigContainer.SERVER, initial.login);

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

            initial.login =  extras.getString(ConfigContainer.LOGIN);
            initial.password =  extras.getString(ConfigContainer.PASSWORD);
            initial.server =  extras.getString(ConfigContainer.SERVER);

            teLogin.setText(initial.login );
            tePassword.setText(initial.password);
            teServer.setText(initial.server);
        }
    }
}

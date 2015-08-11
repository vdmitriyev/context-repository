package com.vlba.contextprovider;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.context_provider.R;

/**
 * Created by hamdi on 11/08/15.
 */
public class UserProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initializeWindow();
        initializeApp();


    }

    private void initializeApp() {

        bindButtonsListener();
    }

    private void bindButtonsListener() {


    }

    private void initializeWindow() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Attaching UI to the activity
        setContentView(R.layout.userprofile);
    }




}

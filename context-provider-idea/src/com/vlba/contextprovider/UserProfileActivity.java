package com.vlba.contextprovider;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.context_provider.R;

/**
 * Created by hamdi on 11/08/15.
 */
public class UserProfileActivity extends Activity {

    LinearLayout menuOptions;
    LinearLayout option1;
    LinearLayout option2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initializeWindow();
        initializeApp();


    }

    private void initializeApp() {
        bindLinearLayouts();

    }

    private void bindLinearLayouts() {

        menuOptions = (LinearLayout) findViewById(R.id.profileMenu);
        option1 = (LinearLayout) findViewById(R.id.whitelist);
        option2 = (LinearLayout) findViewById(R.id.whatToDo);


    }



    private void initializeWindow() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Attaching UI to the activity
        setContentView(R.layout.userprofile);
    }


    @Override
    public void onBackPressed()
    {
        if(menuOptions.getVisibility()==View.VISIBLE){
            super.onBackPressed();
        }else{
            menuOptions.setVisibility(View.VISIBLE);
            option1.setVisibility(View.GONE);
            option2.setVisibility(View.GONE);
        }

    }
    public  void  onClick(View v){

        switch (v.getId()){
            case R.id.btnUPoption1 : menuOptions.setVisibility(View.GONE);
                option2.setVisibility(View.GONE);
                option1.setVisibility(View.VISIBLE);
                break;

            case R.id.btnUPOption2 : menuOptions.setVisibility(View.GONE);
                option2.setVisibility(View.VISIBLE);
                option1.setVisibility(View.GONE);
                break;

        }
    }




}

package com.vlba.contextprovider;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.example.context_provider.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hamdi on 11/08/15.
 */
public class UserProfileActivity extends Activity {

    LinearLayout menuOptions;
    LinearLayout option1;
    LinearLayout option2;
    ListView pendingList;
    ListView servicesList;
    ArrayList<ServiceContainer> newWl;
    ArrayList<ServiceContainer> whiteList;
    ArrayList<ServiceContainer> intial;
    Button newServices;
    Button updateServices;
    ConfigContainer cc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initializeWindow();
        bindLists();
        initializeApp();


    }

    public void reset(View v){
        finish();
        startActivity(getIntent());
    }

    private void bindLists() {
        cc = StorageHelper.readConfigurations(this);


        whiteList = new ArrayList<ServiceContainer>();
        newWl = new ArrayList<ServiceContainer>();
        intial = new ArrayList<ServiceContainer>();
        new loadServices().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        final ServicesAdapter newList = new ServicesAdapter(this,newWl);
        final ServicesAdapter oldList = new ServicesAdapter(this,whiteList);
        pendingList = (ListView) findViewById(R.id.wlUpdates);
        servicesList = (ListView) findViewById(R.id.wlList);
        pendingList.setAdapter(newList);
        servicesList.setAdapter(oldList);
        newServices = (Button) findViewById(R.id.saveNewServices);
        updateServices  =(Button) findViewById(R.id.updateServices);

        newServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{


                ArrayList<ServiceContainer> list1 = newList.getTobeUpdated();
                JSONArray jsonArray = new JSONArray();
                for (ServiceContainer a : list1) {
                    JSONObject object = new JSONObject();
                    object.put("id",a.getId());
                    object.put("perm",a.getPerm());
                    jsonArray.put(object);
                    Log.d("to send", a.toString());
                }

                Log.d("data",list1.toString());

                String result = HttpHelpers.SecureConnection(cc.server + cc.LOADSERVICES, "POST", jsonArray);
                Log.d("data to be send",jsonArray.toString());
                Log.d("result from the server",result.toString());
            }catch (Exception e) {

                }}
        });

        updateServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ServiceContainer> list1 = oldList.getTobeUpdated();

                JSONArray jsonArray = new JSONArray(list1);

                String result = HttpHelpers.SecureConnection(cc.server+cc.LOADSERVICES,"POST",jsonArray);
                Log.d("json to be send",jsonArray.toString());
            }
        });

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



    public class loadServices extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpHelpers helpers = new HttpHelpers();

            ConfigContainer configContainer ;
            configContainer = StorageHelper.readConfigurations(getApplicationContext());
            if(helpers.isInternetAvailable()){
                try {

                    //Log.d("url", configContainer.server + configContainer.LOADSERVICES);
                    String JsonResponse =  HttpHelpers.SecureConnection(configContainer.server+configContainer.LOADSERVICES,"GET");
                    JSONObject json=new JSONObject(JsonResponse);
                    JSONArray jsonArray = json.getJSONArray("services");

                    for(int i = 0 ; i< jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        if(object.getInt("perm")==-1){
                            newWl.add(new ServiceContainer(object.getInt("id"),object.getString("name"),object.getString("desc"),object.getInt("perm")));
                        }else{
                            whiteList.add(new ServiceContainer(object.getInt("id"),object.getString("name"),object.getString("desc"),object.getInt("perm")));
                        }



                    }





                    for (ServiceContainer s:newWl
                         ) {
                        Log.d("new service :",s.toString());

                    }
                    for(ServiceContainer s:whiteList){
                        Log.d("old",s.toString());
                    }
                    /*JSONObject object = jsonArray.getJSONObject(0);

                    
                    Log.d("done",json.toString());

                    Log.d("userName", object.getString("login"));
                    String login = object.getString("login");
                    String pw = object.getString("pass");

                    configContainer.login =login;
                    configContainer.password=pw;
                    StorageHelper.saveConfigurations(getApplicationContext(),configContainer);
                    */

                    Log.d("data",jsonArray.toString());


                }catch (Exception e){

                    Log.d("error",e.getMessage());
                }
            }

            return  null;
        }
        }
}

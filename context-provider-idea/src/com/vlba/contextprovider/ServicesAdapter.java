package com.vlba.contextprovider;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;

import com.example.context_provider.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamdi on 17/08/15.
 */
public class ServicesAdapter extends BaseAdapter{

    private List<ServiceContainer> serviceContainers;
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<tempServiceContainer> tempServiceContainers = new ArrayList<tempServiceContainer>();
    private ArrayList<ServiceContainer> tobeUpdated = new ArrayList<ServiceContainer>();

    public ServicesAdapter(Context context , ArrayList<ServiceContainer> serviceContainers){
        this.context= context;
        this.serviceContainers = serviceContainers;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return serviceContainers.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceContainers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout;
        if(convertView ==null){
            linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.custom_listview_layout,parent,false);
        }else{
            linearLayout = (LinearLayout) convertView;

        }

        TextView name = (TextView) linearLayout.findViewById(R.id.name);
        TextView desc = (TextView) linearLayout.findViewById(R.id.desc);








        RadioGroup group = (RadioGroup) linearLayout.findViewById(R.id.state);
        final RadioButton pending = (RadioButton) linearLayout.findViewById(R.id.pending);
        final RadioButton accepted = (RadioButton) linearLayout.findViewById(R.id.accepted);
        final RadioButton rejected  = (RadioButton) linearLayout.findViewById(R.id.rejected);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int state = serviceContainers.get(position).getPerm();
                Log.d("intial",state+"");

                if(pending.isChecked()&& state !=-1){
                    serviceContainers.get(position).setPerm(-1);
                }
                if(accepted.isChecked()&& state!=0){
                    serviceContainers.get(position).setPerm(0);
                }
                if(rejected.isChecked()&&state!=1){
                    serviceContainers.get(position).setPerm(1);
                }
                Log.d("new state",serviceContainers.get(position).getPerm()+"");
                int newState = serviceContainers.get(position).getPerm();

                if(newState != state){


                if(tobeUpdated.size()==0 && state!=serviceContainers.get(position).getPerm()){
                    tobeUpdated.add(serviceContainers.get(position));
                }else{
                    int index =getIndex(tobeUpdated,serviceContainers.get(position).getId());

                    if(index == -1){
                        tobeUpdated.add(serviceContainers.get(position));
                    }else{
                        tobeUpdated.remove(index);
                        tobeUpdated.add(serviceContainers.get(position));
                    }
                }
            }
            }
        });


        switch (serviceContainers.get(position).getPerm()){
            case -1 : pending.setChecked(true);break;
            case 0 : accepted.setChecked(true);break;
            case 1 : rejected.setChecked(true);break;
        }


        name.setText(serviceContainers.get(position).getName());
        desc.setText(serviceContainers.get(position).getDesc());


        return linearLayout;
    }

    private int getIndex(ArrayList<ServiceContainer> tobeUpdated, int id) {
        for(ServiceContainer ser : tobeUpdated){
            if(ser.getId()==id){
                return tobeUpdated.indexOf(ser);
            }
        }
        return -1;
    }


    public ArrayList<ServiceContainer> getTobeUpdated(){
        return tobeUpdated;
    }

    public ArrayList<tempServiceContainer> getTemp() {
        return tempServiceContainers;
    }
}
package com.crescoop.modmob.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crescoop.modmob.MyApplication;
import com.crescoop.modmob.R;


public class ConnectionConfigurationFragment extends Fragment {

    private Button saveConfButton;

    public static ConnectionConfigurationFragment newInstance() {
        return new ConnectionConfigurationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        saveConfButton = (Button) getView().findViewById(R.id.save_conf_button);
        saveConfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication singleton = MyApplication.getInstance();

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_configuration, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}

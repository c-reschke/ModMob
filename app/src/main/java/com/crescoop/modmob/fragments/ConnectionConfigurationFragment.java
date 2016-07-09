package com.crescoop.modmob.fragments;

import android.content.Context;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crescoop.modmob.MyApplication;
import com.crescoop.modmob.R;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class ConnectionConfigurationFragment extends Fragment {

    private Button saveConfButton;
    private EditText hostEditText;
    private EditText portEditText;
    private EditText slaveIdEditText;
    private EditText timeoutEditText;

    public static ConnectionConfigurationFragment newInstance() {
        return new ConnectionConfigurationFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_configuration, container, false);


        hostEditText = (EditText) view.findViewById(R.id.frag_conf_edit_text_host);
        portEditText = (EditText) view.findViewById(R.id.frag_conf_edit_text_port);
        slaveIdEditText = (EditText) view.findViewById(R.id.frag_conf_edit_text_slave_id);
        timeoutEditText = (EditText) view.findViewById(R.id.frag_conf_edit_text_timeout);

        saveConfButton = (Button) view.findViewById(R.id.save_conf_button);
        saveConfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication singleton = MyApplication.getInstance();

                InetAddress addr = null;
                try {
                    addr = InetAddress.getByName(hostEditText.getText().toString());
                    singleton.setAddr(addr);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                singleton.setPort(Integer.valueOf(portEditText.getText().toString()));

                singleton.setSlave_id(Integer.valueOf(slaveIdEditText.getText().toString()));

                singleton.setTimeout(Integer.valueOf(timeoutEditText.getText().toString()));

                Toast.makeText(getContext(),singleton.getConfiguration(),Toast.LENGTH_LONG).show();


            }
        });
        return view;
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

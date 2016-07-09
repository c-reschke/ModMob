package com.crescoop.modmob.fragments;


import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crescoop.modmob.MyApplication;
import com.crescoop.modmob.R;
import com.crescoop.modmob.interfaces.HoldingRegisterListner;
import com.crescoop.modmob.interfaces.InputRegisterListner;
import com.crescoop.modmob.servico.HoldingRegisterService;
import com.crescoop.modmob.servico.InputRegisterService;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;

import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;

public class StatusFragment extends Fragment {


    private static final String TAG = "StatusFragment";
    private TextInputEditText holdingRegister;
    private TextInputEditText inputRegister;
    private TextInputEditText holdingRegisterValue;
    private TextInputEditText inputRegisterValue;
    private TextView terminalTextView;

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_status, container, false);

        terminalTextView =(TextView) view.findViewById(R.id.termial_text);
        holdingRegister = (TextInputEditText) view.findViewById(R.id.holding_register_register);
        holdingRegisterValue = (TextInputEditText) view.findViewById(R.id.holding_register_value);

        inputRegister = (TextInputEditText) view.findViewById(R.id.input_register_register);
        inputRegisterValue = (TextInputEditText) view.findViewById(R.id.input_register_value);
        inputRegisterValue.setClickable(false);

        ImageButton btnReadHoldingRegister = (ImageButton) view.findViewById(R.id.btn_read_holding_register);

        btnReadHoldingRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminalTextView.setText("");
                readHolding();
            }
        });

        ImageButton btnWriteHoldingRegister = (ImageButton) view.findViewById(R.id.btn_write_holding_register);
        btnWriteHoldingRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminalTextView.setText("");
                writeHolding();
            }
        });

        ImageButton btnReadInputRegister = (ImageButton) view.findViewById(R.id.btn_read_input_register);

        btnReadInputRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminalTextView.setText("");
                readInput();
            }
        });


        return view;
    }

    private void readHolding() {


        int register = Integer.valueOf(holdingRegister.getText().toString());

        HoldingRegisterService readHoldingRegister = new HoldingRegisterService(new HoldingRegisterListner() {
            @Override
            public void serviceSuccess(int result) {
                holdingRegisterValue.setText(String.valueOf(result));
            }

            @Override
            public void serviceFailure(Exception exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void serviceUpdate(String msgHex) {
                terminalTextView.append(msgHex);
            }
        });
        readHoldingRegister.execute(register);
    }

    private void writeHolding() {
       /* Runnable runnable = new Runnable() {
            public void run() {



                TCPMasterConnection con = null; //the connection
                ModbusTCPTransaction trans = null; //the transaction
                WriteSingleRegisterRequest req = null; //the request
                WriteSingleRegisterResponse res = null; //the response

                Register mRegister = new SimpleRegister();
                mRegister.setValue(Integer.valueOf(holdingRegisterValue.getText().toString()));


                InetAddress addr = singleton.getAddr();
                int port = singleton.getPort();
                int ref = Integer.valueOf(holdingRegister.getText().toString());
                int count = 10; //the number of DI's to read
                int repeat = 0; //a loop for repeating the transaction

                //2. Open the connection
                con = new TCPMasterConnection(addr);
                con.setPort(port);

                try {
                    con.connect();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                //3. Prepare the request
                req = new WriteSingleRegisterRequest(ref,mRegister);
                req.setUnitID(singleton.getSlave_id());

                //4. Prepare the transaction
                trans = new ModbusTCPTransaction(con);
                trans.setRequest(req);

                try {
                    trans.execute();
                } catch (ModbusException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                inputRegisterValue.setText(res.getRegisterValue());

                con.close();
            }
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }
            }
        }*/
    }

    private void readInput() {

        int register = Integer.valueOf(inputRegister.getText().toString());

        InputRegisterService readInput = new InputRegisterService(new InputRegisterListner() {
            @Override
            public void serviceSuccess(int result) {
                inputRegisterValue.setText(String.valueOf(result));
            }

            @Override
            public void serviceFailure(Exception exception) {
                Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void serviceUpdate(String msgHex) {

                terminalTextView.append(msgHex);
            }
        });
        readInput.execute(register);

    }

}



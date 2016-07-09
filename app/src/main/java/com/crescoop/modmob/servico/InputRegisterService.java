
package com.crescoop.modmob.servico;

import android.os.AsyncTask;
import android.util.Log;

import com.crescoop.modmob.MyApplication;
import com.crescoop.modmob.interfaces.InputRegisterListner;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;

public class InputRegisterService extends AsyncTask<Integer, String, Integer> {

    private static final String TAG = "InputRegisterService";
    private InputRegisterListner listener;
    private Exception error;

    private  TCPMasterConnection con;
    private ModbusTCPTransaction trans;
    private ReadMultipleRegistersRequest req;
    private ReadMultipleRegistersResponse res;

    private InetAddress addr;
    private int port;
    private int slaveID;
    private int timeout;

    public InputRegisterService(InputRegisterListner listener) {
        super();
        this.listener = listener;

        MyApplication singleton = MyApplication.getInstance();
        this.addr = singleton.getAddr();
        this.port = singleton.getPort();
        this.slaveID = singleton.getSlave_id();
        this.timeout = singleton.getTimeout();

    }

    @Override
    protected Integer doInBackground(Integer... register) {

        con = new TCPMasterConnection(addr);
        con.setPort(port);
        con.setTimeout(timeout);

        try {
            con.connect();
        } catch (Exception e) {
            e.printStackTrace();
            error = new InputRegisterServiceException(e.getLocalizedMessage());
        }

        req = new ReadMultipleRegistersRequest(register[0], 1);
        req.setUnitID(this.slaveID);


        trans = new ModbusTCPTransaction(con);
        trans.setRequest(req);

        publishProgress("TX: "+req.getHexMessage());

        try {
            trans.execute();
            res = (ReadMultipleRegistersResponse) trans.getResponse();
            publishProgress("\nRX: "+res.getHexMessage());
        } catch (ModbusException e) {
            e.printStackTrace();
            error = new InputRegisterServiceException(e.getLocalizedMessage());
            con.close();
            return null;
        }
        try {
            int result = res.getRegister(0).getValue();
            con.close();
            return result;

        }catch (Exception e){
            e.printStackTrace();
            error = new InputRegisterServiceException(e.getMessage());
        }

        con.close();

        return null;
    }
    @Override
    protected void onProgressUpdate(String... progress){
        listener.serviceUpdate(progress[0]);
        Log.d(TAG,progress[0]);
    }

    @Override
    protected void onPostExecute(Integer result) {

        if (result == null && error != null) {
            listener.serviceFailure(error);
        }
        if (result != null && error == null){
            listener.serviceSuccess(result);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    public class InputRegisterServiceException extends Exception {
        public InputRegisterServiceException(String detailMessage) {
            super(detailMessage);
        }
    }
}


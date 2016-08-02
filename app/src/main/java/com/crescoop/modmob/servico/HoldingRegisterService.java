
package com.crescoop.modmob.servico;

import android.os.AsyncTask;
import android.util.Log;

import com.crescoop.modmob.MyApplication;
import com.crescoop.modmob.interfaces.HoldingRegisterListner;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

import java.net.InetAddress;

public class HoldingRegisterService extends AsyncTask<Integer, String, Integer> {

    private static final String TAG = "HoldingRegisterService";
    private HoldingRegisterListner listener;
    private Exception error;

    private TCPMasterConnection con;
    private ModbusTCPTransaction trans;
    private ReadMultipleRegistersRequest req;
    private ReadMultipleRegistersResponse res;

    private InetAddress addr;
    private int port;
    private int slaveID;
    private int timeout;

    public HoldingRegisterService(HoldingRegisterListner listener) {
        super();
        this.listener = listener;

        MyApplication singleton = MyApplication.getInstance();
        this.addr = singleton.getAddr();
        this.port = singleton.getPort();
        this.slaveID = singleton.getSlave_id();
        this.timeout = singleton.getTimeout();
    }

    @Override
    protected Integer doInBackground(Integer... inputRegister) {

        con = new TCPMasterConnection(addr);
        con.setPort(port);
        con.setTimeout(timeout);

        try {
            con.connect();
        } catch (Exception e) {
            e.printStackTrace();
            error = new HoldingRegisterServiceException(e.getLocalizedMessage());
        }
        //3. Prepare the request
        req = new ReadMultipleRegistersRequest(inputRegister[0], 1);
        req.setUnitID(this.slaveID);

        //4. Prepare the transaction
        trans = new ModbusTCPTransaction(con);
        trans.setRequest(req);
        publishProgress("TX: "+req.getHexMessage());
        try {

            trans.execute();
            res = (ReadMultipleRegistersResponse) trans.getResponse();
            publishProgress("\nRX: "+res.getHexMessage());
        } catch (ModbusException e) {
            e.printStackTrace();
            error = new HoldingRegisterServiceException(e.getLocalizedMessage());
            con.close();
            return null;
        }
        try {
            int result = res.getRegister(0).getValue();
            con.close();
            return result;

        }catch (Exception e){
            e.printStackTrace();
            error = new HoldingRegisterServiceException(e.getMessage());
        }

        con.close();

        return null;
    }
    @Override
    protected void onProgressUpdate(String... progress){
        listener.serviceUpdate(progress[0]);
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

    public class HoldingRegisterServiceException extends Exception {
        public HoldingRegisterServiceException(String detailMessage) {
            super(detailMessage);
        }
    }
}


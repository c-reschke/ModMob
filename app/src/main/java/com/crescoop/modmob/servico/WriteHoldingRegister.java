package com.crescoop.modmob.servico;


import android.os.AsyncTask;

import com.crescoop.modmob.MyApplication;
import com.crescoop.modmob.interfaces.HoldingRegisterListner;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.ModbusIOException;
import net.wimpi.modbus.ModbusSlaveException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ExceptionResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.IllegalAddressException;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;

import java.net.InetAddress;

public class WriteHoldingRegister extends AsyncTask<Integer, String, Integer>{


    private static final String TAG = "WriteHoldingRegister";
    private HoldingRegisterListner listener;
    private Exception error;

    private TCPMasterConnection con;
    private ModbusTCPTransaction trans;
    private WriteSingleRegisterRequest req;
    private WriteSingleRegisterResponse res;

    private InetAddress addr;
    private int port;
    private int slaveID;
    private int timeout;

    public WriteHoldingRegister(HoldingRegisterListner listener) {
        super();
        this.listener = listener;

        MyApplication singleton = MyApplication.getInstance();
        this.addr = singleton.getAddr();
        this.port = singleton.getPort();
        this.slaveID = singleton.getSlave_id();
        this.timeout = singleton.getTimeout();
    }

    @Override
    protected Integer doInBackground(Integer... holdingRegister) {

        con = new TCPMasterConnection(addr);
        con.setPort(port);
        con.setTimeout(timeout);

        try {
            con.connect();
        } catch (Exception e) {
            e.printStackTrace();
            error = new WriteHoldingRegisterException(e.getLocalizedMessage());
        }
        //3. Prepare the request
        Register mRegister = new SimpleRegister();
        mRegister.setValue(holdingRegister[0]);
        req = new WriteSingleRegisterRequest(holdingRegister[1],mRegister);
        req.setUnitID(this.slaveID);

        //4. Prepare the transaction
        trans = new ModbusTCPTransaction(con);
        trans.setRequest(req);
        publishProgress("TX: "+req.getHexMessage());
        try {

            trans.execute();
            res = (WriteSingleRegisterResponse) trans.getResponse();
            publishProgress("\nRX: "+res.getHexMessage());
        } catch (ModbusSlaveException ex){
            if ( ex.isType(2)){
                error = new WriteHoldingRegisterException("Illegal Data Address");

            }else {
                error = new WriteHoldingRegisterException(ex.getMessage());
            }
            con.close();
            return null;
        } catch (ModbusException e) {
            e.printStackTrace();
            error = new WriteHoldingRegisterException(e.getLocalizedMessage());
            con.close();
            return null;
        }
        try {
            // int result = res.getRegister(0).getValue();
            con.close();
            // return result;

        }catch (Exception e){
            e.printStackTrace();
            error = new WriteHoldingRegisterException(e.getMessage());
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

    public class WriteHoldingRegisterException extends Exception {
        public WriteHoldingRegisterException(String detailMessage) {
            super(detailMessage);
        }
    }
}


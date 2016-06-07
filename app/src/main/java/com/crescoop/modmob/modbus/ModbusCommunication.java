package com.crescoop.modmob.modbus;

import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.TCPMasterConnection;

public class ModbusCommunication  {

    TCPMasterConnection con = null; //the connection
    ModbusTCPTransaction trans = null; //the transaction
    WriteSingleRegisterRequest wReg = null;
    WriteSingleRegisterResponse wResp = null;

    private boolean runnig;

    public boolean isRunning() {

        return runnig;
    }

    public void init(ModbusConfiguration modConf) throws Exception {

        con = new TCPMasterConnection(modConf.getAddr());
        con.setPort(modConf.getPort());
        con.connect();

        wReg = new WriteSingleRegisterRequest();

        trans = new ModbusTCPTransaction(con);
        trans.setRequest(wReg);

    }

}

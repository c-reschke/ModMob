package com.crescoop.modmob.servico;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.crescoop.modmob.interfaces.InterfaceSwap;
import com.crescoop.modmob.modbus.ModbusCommunication;
import com.crescoop.modmob.modbus.ModbusConfiguration;

public class SwapService extends Service implements InterfaceSwap {

    private ModbusConfiguration modConf;

    private ModbusCommunication modComm = new ModbusCommunication();


    public class SwapServiceBinder extends Binder{

        public InterfaceSwap getInterface(){
            return SwapService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new SwapServiceBinder();
    }

    @Override
    public void onDestroy(){
        stop();
    }

    @Override
    public void start(ModbusConfiguration modbusConfiguration){

        modConf = modbusConfiguration;

        try {
            modComm.init(modConf);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop(){

    }

    public boolean isRunning(){
        return  modComm.isRunning();
    }


}

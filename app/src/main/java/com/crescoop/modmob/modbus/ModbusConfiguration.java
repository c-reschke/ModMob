package com.crescoop.modmob.modbus;
/*
InetAddress addr = null; //the slave's address
int port = Modbus.DEFAULT_PORT;
int ref = 0; //the reference; offset where to start reading from
int count = 0; //the number of DI's to read
int repeat = 1; //a loop for repeating the transaction
*/

import net.wimpi.modbus.Modbus;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ModbusConfiguration {

    private InetAddress addr;
    private int port;
    private int ref;
    private int repeat;

    public ModbusConfiguration() {

        try {
            this.addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = Modbus.DEFAULT_PORT;
        this.ref = 0;
        this.repeat = 1;
    }

    public ModbusConfiguration(String addr, int port, int offset, int repeat) {

        try {
            this.addr = InetAddress.getByName(addr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.port = port;
        this.ref = offset;
        this.repeat = repeat;

    }

    public InetAddress getAddr() {
        return addr;
    }

    public void setAddr(InetAddress addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}
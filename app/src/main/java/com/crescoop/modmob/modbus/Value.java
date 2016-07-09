package com.crescoop.modmob.modbus;

import android.util.Log;

public class Value {

    private float valueIO;
    private float valueUE;

    private float minIO;
    private float maxIO;
    private float minUE;
    private float maxUE;

    public Value(){}
    // Já que possui todos parâmetros a escala já foi setada true
    public Value(float minIO, float maxIO, float minUE, float maxUE) {
        this.minIO = minIO;
        this.maxIO = maxIO;
        this.minUE = minUE;
        this.maxUE = maxUE;
        scaleEnable = true;
    }

    public float getValue(){

        float tempValueUE = 0;

        if (scaleEnable){
            try {
                return  (((this.valueIO - this.minIO) * (this.maxUE - this.minUE)) / (this.maxIO - minIO)) + this.minUE;
            }catch (ArithmeticException e){
                e.printStackTrace();
                return 0;
            }
        }else {
            return this.valueIO;
        }
    }

    public boolean isScaleEnable() {
        return scaleEnable;
    }

    public void setScaleEnable(boolean scaleEnable) {
        this.scaleEnable = scaleEnable;
    }

    public float getValueIO() {
        return valueIO;
    }

    public void setValueIO(float valueIO) {
        this.valueIO = valueIO;
    }

    public float getMinIO() {
        return minIO;
    }

    public void setMinIO(float minIO) {
        this.minIO = minIO;
    }

    public float getMaxIO() {
        return maxIO;
    }

    public void setMaxIO(float maxIO) {
        this.maxIO = maxIO;
    }

    public float getMinUE() {
        return minUE;
    }

    public void setMinUE(float minUE) {
        this.minUE = minUE;
    }

    public float getMaxUE() {
        return maxUE;
    }

    public void setMaxUE(float maxUE) {
        this.maxUE = maxUE;
    }

    private boolean scaleEnable;

    public void seValueIO(float valueIO){

    }
    public void printValue(){
        Log.d("Value", String.valueOf(this.valueIO+" "+getValue()));
    }

}


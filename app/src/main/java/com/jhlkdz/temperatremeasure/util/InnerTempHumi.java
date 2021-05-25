package com.jhlkdz.temperatremeasure.util;

public class InnerTempHumi {

    private float temp;
    private float humi;

    public InnerTempHumi() {
    }

    public InnerTempHumi(float temp, float humi) {
        this.temp = temp;
        this.humi = humi;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getHumi() {
        return humi;
    }

    public void setHumi(float humi) {
        this.humi = humi;
    }
}

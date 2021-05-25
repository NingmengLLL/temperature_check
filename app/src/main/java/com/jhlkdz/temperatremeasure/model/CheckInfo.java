package com.jhlkdz.temperatremeasure.model;

import android.content.Context;

import com.jhlkdz.temperatremeasure.service.BarnInfoService;
import com.jhlkdz.temperatremeasure.service.impl.BarnInfoServiceImpl;

import java.io.Serializable;
import java.util.List;

public class CheckInfo implements Serializable {

    private int cid;
    private int id;
    private String time;
    private float out_temperature=-1;
    private float out_humidity=-1;
    private float in_temperature=-1;
    private float in_humidity=-1;
    private List<Float> temperature;

    public CheckInfo() {
    }

    public CheckInfo(int cid, int id, String time, float out_temperature, float out_humidity, float in_temperature, float in_humidity, List<Float> temperature) {
        this.cid = cid;
        this.id = id;
        this.time = time;
        this.out_temperature = out_temperature;
        this.out_humidity = out_humidity;
        this.in_temperature = in_temperature;
        this.in_humidity = in_humidity;
        this.temperature = temperature;
    }

    public boolean isValid(BarnInfo barnInfo){
        return out_humidity!=-1&&out_temperature!=-1&&in_humidity!=-1&&in_temperature!=-1
                &&temperature.size()==(barnInfo.getRow()*barnInfo.getColumn()*barnInfo.getLevel());
    }

    @Override
    public String toString() {
        return "CheckInfo{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", out_temperature=" + out_temperature +
                ", out_humidity=" + out_humidity +
                ", in_temperature=" + in_temperature +
                ", in_humidity=" + in_humidity +
                ", temperature=" + temperature +
                '}';
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getOut_temperature() {
        return out_temperature;
    }

    public void setOut_temperature(float out_temperature) {
        this.out_temperature = out_temperature;
    }

    public float getOut_humidity() {
        return out_humidity;
    }

    public void setOut_humidity(float out_humidity) {
        this.out_humidity = out_humidity;
    }

    public float getIn_temperature() {
        return in_temperature;
    }

    public void setIn_temperature(float in_temperature) {
        this.in_temperature = in_temperature;
    }

    public float getIn_humidity() {
        return in_humidity;
    }

    public void setIn_humidity(float in_humidity) {
        this.in_humidity = in_humidity;
    }

    public List<Float> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<Float> temperature) {
        this.temperature = temperature;
    }
}

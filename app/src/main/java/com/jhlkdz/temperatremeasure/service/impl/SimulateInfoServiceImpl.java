package com.jhlkdz.temperatremeasure.service.impl;

import com.jhlkdz.temperatremeasure.model.CheckInfo;
import com.jhlkdz.temperatremeasure.service.SimulateInfoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimulateInfoServiceImpl implements SimulateInfoService {

    @Override
    public CheckInfo getCheckInfoById(int barnId){

        CheckInfo checkInfo =  new CheckInfo();
        checkInfo.setCid(0);
        checkInfo.setId(barnId);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        checkInfo.setTime(time);
        checkInfo.setOut_temperature(random(50));
        checkInfo.setOut_humidity(random(50));
        checkInfo.setIn_humidity(random(50));
        checkInfo.setIn_humidity(random(50));
        List<Float> temp = new ArrayList<>();
        for(int i=0;i<getColumn(barnId)*getLevel(barnId)*getRow(barnId);i++){
            temp.add(random(25)+5);
        }
        checkInfo.setTemperature(temp);
        return checkInfo;
    }

    @Override
    public int getRow(int barnId){
        if(barnId<=4)
            return barnId+1;
        else
            return barnId-2;
    }

    @Override
    public int getColumn(int barnId){
        if(barnId<=4)
            return barnId+2;
        else
            return barnId-3;
    }

    @Override
    public int getLevel(int barnId){
        if(barnId<=4)
            return barnId+1;
        else
            return barnId-2;
    }

    private float random(int i){
        return (float)(Math.random()*i);
    }
}

package com.jhlkdz.temperatremeasure.dataService.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jhlkdz.temperatremeasure.dataService.CheckInfoDataService;
import com.jhlkdz.temperatremeasure.db.CheckInfoContract;
import com.jhlkdz.temperatremeasure.model.CheckInfo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckInfoDataServiceImpl implements CheckInfoDataService {

    private static SQLiteDatabase db;

    public static void setDb(SQLiteDatabase db) {
        CheckInfoDataServiceImpl.db = db;
    }

    public static void closeSQLiteDatabase() {
        db.close();
    }

    @Override
    public void insertCheckInfo(CheckInfo checkInfo) {
        ContentValues values = new ContentValues();
        values.put(CheckInfoContract.CheckInfoEntry.cID,checkInfo.getCid());
        values.put(CheckInfoContract.CheckInfoEntry.ID, checkInfo.getId());
        values.put(CheckInfoContract.CheckInfoEntry.TIME,checkInfo.getTime());
        values.put(CheckInfoContract.CheckInfoEntry.OUT_TEMPERATURE, checkInfo.getOut_temperature());
        values.put(CheckInfoContract.CheckInfoEntry.OUT_HUMIDITY,checkInfo.getOut_humidity());
        values.put(CheckInfoContract.CheckInfoEntry.IN_TEMPERATURE,checkInfo.getIn_temperature());
        values.put(CheckInfoContract.CheckInfoEntry.IN_HUMIDITY,checkInfo.getIn_humidity());
        Gson gson = new Gson();
        String string= gson.toJson(checkInfo.getTemperature());
        values.put(CheckInfoContract.CheckInfoEntry.TEMPERATURE,string);
        long result = db.insert(CheckInfoContract.CheckInfoEntry.TABLE_NAME,null,values);
        Log.i(" aaa",""+result);
    }

    @Override
    public List<CheckInfo> getCheckInfo(int barn) {
        Cursor cursor = db.query(CheckInfoContract.CheckInfoEntry.TABLE_NAME,null, CheckInfoContract.CheckInfoEntry.ID+" = ?",
                new String[]{barn+""},null,null,null,null);
        return CheckInfoCursorToList(cursor);
    }

    @Override
    public List<CheckInfo> getCheckInfoByTime(String time){
        Cursor cursor = db.query(CheckInfoContract.CheckInfoEntry.TABLE_NAME,null, CheckInfoContract.CheckInfoEntry.TIME+" = ?",
                new String[]{time},null,null,null,null);
        return CheckInfoCursorToList(cursor);
    }

    private static List<CheckInfo> CheckInfoCursorToList(Cursor cursor) {
        List<CheckInfo> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int cId = cursor.getInt(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.cID));
            int id = cursor.getInt(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.ID));
            String time = cursor.getString(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.TIME));
            float outTemp = cursor.getFloat(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.OUT_TEMPERATURE));
            float outHumid = cursor.getFloat(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.OUT_HUMIDITY));
            float inTemp = cursor.getFloat(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.IN_TEMPERATURE));
            float inHumid = cursor.getFloat(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.IN_HUMIDITY));
            String string  = cursor.getString(cursor.getColumnIndex(CheckInfoContract.CheckInfoEntry.TEMPERATURE));
            Type type = new TypeToken<ArrayList<Float>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Float> temps = gson.fromJson(string, type);
            CheckInfo checkInfo = new CheckInfo(cId,id,time,outTemp,outHumid,inTemp,inHumid,temps);
            list.add(checkInfo);
        }
        return list;
    }
}

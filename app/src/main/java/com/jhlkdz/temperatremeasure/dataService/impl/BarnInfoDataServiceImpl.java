package com.jhlkdz.temperatremeasure.dataService.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jhlkdz.temperatremeasure.dataService.BarnInfoDataService;
import com.jhlkdz.temperatremeasure.db.BarnInfoContract;
import com.jhlkdz.temperatremeasure.model.BarnInfo;

import java.util.ArrayList;
import java.util.List;

public class BarnInfoDataServiceImpl implements BarnInfoDataService {

    private static SQLiteDatabase db;

    public static void setDb(SQLiteDatabase db) {
        BarnInfoDataServiceImpl.db = db;
    }

    public static void closeSQLiteDatabase() {
        db.close();
    }

    @Override
    public int insertBarnInfo(BarnInfo barnInfo) {

        ContentValues values = new ContentValues();
        values.put(BarnInfoContract.BarnInfoEntry.ID,barnInfo.getId());
        values.put(BarnInfoContract.BarnInfoEntry.SYSTEM_TYPE,barnInfo.getSystemType());
        values.put(BarnInfoContract.BarnInfoEntry.EXTENSION,barnInfo.getExtension());
        values.put(BarnInfoContract.BarnInfoEntry.ROW,barnInfo.getRow());
        values.put(BarnInfoContract.BarnInfoEntry.COLUMN,barnInfo.getColumn());
        values.put(BarnInfoContract.BarnInfoEntry.LEVEL,barnInfo.getLevel());
        values.put(BarnInfoContract.BarnInfoEntry.START_COLUMN,barnInfo.getStartColumn());
        values.put(BarnInfoContract.BarnInfoEntry.B8,barnInfo.getB8());
        values.put(BarnInfoContract.BarnInfoEntry.B9,barnInfo.getB9());
        values.put(BarnInfoContract.BarnInfoEntry.IN_ADDRESS,barnInfo.getIn_address());
        values.put(BarnInfoContract.BarnInfoEntry.IN_POINT,barnInfo.getIn_point());
        values.put(BarnInfoContract.BarnInfoEntry.B12,barnInfo.getB12());
        values.put(BarnInfoContract.BarnInfoEntry.MAIN_ADDRESS,barnInfo.getMainAddress());
        values.put(BarnInfoContract.BarnInfoEntry.COLLECTOR_NUM,barnInfo.getCollectorNum());
        values.put(BarnInfoContract.BarnInfoEntry.B15,barnInfo.getB15());
        long result = db.insert(BarnInfoContract.BarnInfoEntry.TABLE_NAME,null,values);
        return (int)result;
    }

    @Override
    public void updateBarnInfo(BarnInfo barnInfo) {
        ContentValues values = new ContentValues();
        values.put(BarnInfoContract.BarnInfoEntry.ID,barnInfo.getId());
        values.put(BarnInfoContract.BarnInfoEntry.SYSTEM_TYPE,barnInfo.getSystemType());
        values.put(BarnInfoContract.BarnInfoEntry.EXTENSION,barnInfo.getExtension());
        values.put(BarnInfoContract.BarnInfoEntry.ROW,barnInfo.getRow());
        values.put(BarnInfoContract.BarnInfoEntry.COLUMN,barnInfo.getColumn());
        values.put(BarnInfoContract.BarnInfoEntry.LEVEL,barnInfo.getLevel());
        values.put(BarnInfoContract.BarnInfoEntry.START_COLUMN,barnInfo.getStartColumn());
        values.put(BarnInfoContract.BarnInfoEntry.B8,barnInfo.getB8());
        values.put(BarnInfoContract.BarnInfoEntry.B9,barnInfo.getB9());
        values.put(BarnInfoContract.BarnInfoEntry.IN_ADDRESS,barnInfo.getIn_address());
        values.put(BarnInfoContract.BarnInfoEntry.IN_POINT,barnInfo.getIn_point());
        values.put(BarnInfoContract.BarnInfoEntry.B12,barnInfo.getB12());
        values.put(BarnInfoContract.BarnInfoEntry.MAIN_ADDRESS,barnInfo.getMainAddress());
        values.put(BarnInfoContract.BarnInfoEntry.COLLECTOR_NUM,barnInfo.getCollectorNum());
        values.put(BarnInfoContract.BarnInfoEntry.B15,barnInfo.getB15());
        long result = db.update(BarnInfoContract.BarnInfoEntry.TABLE_NAME,values,BarnInfoContract.BarnInfoEntry.ID+" =?",new String[]{barnInfo.getId()+""});
    }

    @Override
    public List<BarnInfo> getBarnInfo(int bId) {
        Cursor cursor = db.query(BarnInfoContract.BarnInfoEntry.TABLE_NAME,null, BarnInfoContract.BarnInfoEntry.bID+" = ?",
                new String[]{bId+""},null,null,null,null);
        return BarnInfoCursorToList(cursor);
    }

    private List<BarnInfo> BarnInfoCursorToList(Cursor cursor){
        List<BarnInfo> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            BarnInfo barnInfo = new BarnInfo();
            barnInfo.setbId(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.bID)));
            barnInfo.setId(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.ID)));
            barnInfo.setSystemType(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.SYSTEM_TYPE)));
            barnInfo.setExtension(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.EXTENSION)));
            barnInfo.setRow(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.ROW)));
            barnInfo.setColumn(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.COLUMN)));
            barnInfo.setLevel(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.LEVEL)));
            barnInfo.setStartColumn(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.START_COLUMN)));
            barnInfo.setB8(cursor.getBlob(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.B8)));
            barnInfo.setB9(cursor.getBlob(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.B9)));
            barnInfo.setIn_address(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.IN_ADDRESS)));
            barnInfo.setIn_point(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.IN_POINT)));
            barnInfo.setB12(cursor.getBlob(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.B12)));
            barnInfo.setMainAddress(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.MAIN_ADDRESS)));
            barnInfo.setCollectorNum(cursor.getInt(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.COLLECTOR_NUM)));
            barnInfo.setB15(cursor.getBlob(cursor.getColumnIndex(BarnInfoContract.BarnInfoEntry.B15)));
            list.add(barnInfo);
        }
        return list;
    }
}

package com.jhlkdz.temperatremeasure.dataService.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jhlkdz.temperatremeasure.dataService.BaseInfoDataService;
import com.jhlkdz.temperatremeasure.db.BaseInfoContract;
import com.jhlkdz.temperatremeasure.model.BaseInfo;

import java.util.ArrayList;
import java.util.List;

public class BaseInfoDataServiceImpl implements BaseInfoDataService {

    private static SQLiteDatabase db;

    public static void setDb(SQLiteDatabase db) {
        BaseInfoDataServiceImpl.db = db;
    }

    public static void closeSQLiteDatabase() {
        db.close();
    }

    @Override
    public void insertBaseInfo(BaseInfo baseInfo) {
        ContentValues values = new ContentValues();
        values.put(BaseInfoContract.BaseInfoEntry.ID,baseInfo.getId());
        values.put(BaseInfoContract.BaseInfoEntry.BARN_NUM,baseInfo.getBarn_num());
        values.put(BaseInfoContract.BaseInfoEntry.OUT_EXTENTION,baseInfo.getOut_extension());
        values.put(BaseInfoContract.BaseInfoEntry.OUT_ADDRESS,baseInfo.getOut_address());
        values.put(BaseInfoContract.BaseInfoEntry.OUT_POINT,baseInfo.getOut_point());
        values.put(BaseInfoContract.BaseInfoEntry.MAX_BYTES,baseInfo.getMax_bytes());
        long result = db.insert(BaseInfoContract.BaseInfoEntry.TABLE_NAME,null,values);
    }

    @Override
    public List<BaseInfo> getBaseInfo(int id) {
        Cursor cursor = db.query(BaseInfoContract.BaseInfoEntry.TABLE_NAME,null, BaseInfoContract.BaseInfoEntry.ID+" = ?",
                new String[]{id+""},null,null,null,null);

        return BaseInfoCursorToList(cursor);
    }

    private static List<BaseInfo> BaseInfoCursorToList(Cursor cursor) {
        List<BaseInfo> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(BaseInfoContract.BaseInfoEntry.ID));
            int out_extension = cursor.getInt(cursor.getColumnIndex(BaseInfoContract.BaseInfoEntry.OUT_EXTENTION));
            int out_address = cursor.getInt(cursor.getColumnIndex(BaseInfoContract.BaseInfoEntry.OUT_ADDRESS));
            int out_point = cursor.getInt(cursor.getColumnIndex(BaseInfoContract.BaseInfoEntry.OUT_POINT));
            int barn_num = cursor.getInt(cursor.getColumnIndex(BaseInfoContract.BaseInfoEntry.BARN_NUM));
            int max_bytes = cursor.getInt(cursor.getColumnIndex(BaseInfoContract.BaseInfoEntry.MAX_BYTES));

            BaseInfo baseInfo = new BaseInfo(id,barn_num,out_extension,out_address,out_point,max_bytes);
            list.add(baseInfo);
        }
        return list;
    }
}

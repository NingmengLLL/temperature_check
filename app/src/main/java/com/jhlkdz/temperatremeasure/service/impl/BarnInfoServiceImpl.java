package com.jhlkdz.temperatremeasure.service.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jhlkdz.temperatremeasure.dataService.BarnInfoDataService;
import com.jhlkdz.temperatremeasure.dataService.impl.BarnInfoDataServiceImpl;
import com.jhlkdz.temperatremeasure.db.DbHelper;
import com.jhlkdz.temperatremeasure.model.BarnInfo;
import com.jhlkdz.temperatremeasure.service.BarnInfoService;
import com.jhlkdz.temperatremeasure.socket.api.Response;

import java.util.List;

public class BarnInfoServiceImpl implements BarnInfoService {

    private Context context;
    private BarnInfoDataService barnInfoDataService;

    public BarnInfoServiceImpl(Context context) {
        this.context = context;
        barnInfoDataService = new BarnInfoDataServiceImpl();
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        BarnInfoDataServiceImpl.setDb(db);
    }

    @Override
    public int addBarnInfo(Response response) {

        List<Byte> list = response.getParams();
        BarnInfo barnInfo = new BarnInfo();
        barnInfo.setId(list.get(0)*256+list.get(1));
        barnInfo.setSystemType(list.get(2));
        barnInfo.setExtension(list.get(3));
        barnInfo.setRow(list.get(4));
        barnInfo.setColumn(list.get(5));
        barnInfo.setLevel(list.get(6));
        barnInfo.setStartColumn(list.get(7));
        barnInfo.setB8(new byte[]{list.get(8)});
        barnInfo.setB9(new byte[]{list.get(9)});
        barnInfo.setIn_address(list.get(10));
        barnInfo.setIn_point(list.get(11));
        barnInfo.setB12(new byte[]{list.get(12)});
        barnInfo.setMainAddress(list.get(13));
        barnInfo.setCollectorNum(list.get(14));
        barnInfo.setB15(new byte[]{list.get(15)});

        return barnInfoDataService.insertBarnInfo(barnInfo);
    }

    @Override
    public List<BarnInfo> getBarnInfo(int bId) {
        return barnInfoDataService.getBarnInfo(bId);
    }
}

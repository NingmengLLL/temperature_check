package com.jhlkdz.temperatremeasure.service.impl;

import android.content.Context;

import com.jhlkdz.temperatremeasure.dataService.BaseInfoDataService;
import com.jhlkdz.temperatremeasure.dataService.impl.BaseInfoDataServiceImpl;
import com.jhlkdz.temperatremeasure.model.BaseInfo;
import com.jhlkdz.temperatremeasure.service.BaseInfoService;

import java.util.List;

public class BaseInfoServiceImpl implements BaseInfoService {

    private Context context;
    private BaseInfoDataService baseInfoDataService;


    public BaseInfoServiceImpl(Context context) {
        this.context = context;
        baseInfoDataService = new BaseInfoDataServiceImpl();

    }

    @Override
    public void addBaseInfo(BaseInfo baseInfo) {

    }

    @Override
    public List<BaseInfo> getBaseInfo(int id) {
        return null;
    }
}

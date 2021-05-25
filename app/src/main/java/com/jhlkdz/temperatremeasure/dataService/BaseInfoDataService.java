package com.jhlkdz.temperatremeasure.dataService;

import com.jhlkdz.temperatremeasure.model.BaseInfo;

import java.util.List;

public interface BaseInfoDataService {

    //baseinfo
    public void insertBaseInfo(BaseInfo baseInfo);

    public List<BaseInfo> getBaseInfo(int id);
}

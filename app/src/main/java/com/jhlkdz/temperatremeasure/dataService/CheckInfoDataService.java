package com.jhlkdz.temperatremeasure.dataService;

import com.jhlkdz.temperatremeasure.model.CheckInfo;

import java.util.List;

public interface CheckInfoDataService {

    //checkinfo
    public void insertCheckInfo(CheckInfo checkInfo);

    public List<CheckInfo> getCheckInfo(int barn);

    public List<CheckInfo> getCheckInfoByTime(String time);
}

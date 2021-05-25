package com.jhlkdz.temperatremeasure.service;

import com.jhlkdz.temperatremeasure.model.BaseInfo;

import java.util.List;

public interface BaseInfoService {

    public void addBaseInfo(BaseInfo baseInfo);

    public List<BaseInfo> getBaseInfo(int id);
}

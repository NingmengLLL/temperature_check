package com.jhlkdz.temperatremeasure.dataService;

import com.jhlkdz.temperatremeasure.model.BarnInfo;

import java.util.List;

public interface BarnInfoDataService {

    //barninfo
    public int insertBarnInfo(BarnInfo barnInfo);

    public void updateBarnInfo(BarnInfo barnInfo);

    public List<BarnInfo> getBarnInfo(int bId);
}

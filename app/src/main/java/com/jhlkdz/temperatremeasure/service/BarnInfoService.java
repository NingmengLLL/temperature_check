package com.jhlkdz.temperatremeasure.service;

import com.jhlkdz.temperatremeasure.model.BarnInfo;
import com.jhlkdz.temperatremeasure.socket.api.Response;

import java.util.List;

public interface BarnInfoService {

    public int addBarnInfo(Response response);

    public List<BarnInfo> getBarnInfo(int bId);
}

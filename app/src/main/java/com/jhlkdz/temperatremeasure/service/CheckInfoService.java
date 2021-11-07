package com.jhlkdz.temperatremeasure.service;

import android.app.ProgressDialog;

import com.jhlkdz.temperatremeasure.model.CheckInfo;
import com.jhlkdz.temperatremeasure.ui.Task.TimeoutThread;
import com.jhlkdz.temperatremeasure.ui.activity.api.LoginInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public interface CheckInfoService {

    public List<Integer> addCheckInfo(List<Integer> barns,List<String> nameList, LoginInfo loginInfo, TimeoutThread tTime) throws UnknownHostException, IOException;

    public ArrayList<Integer> syncCheckInfo(LoginInfo loginInfo)throws UnknownHostException, IOException;

    public List<CheckInfo> getCheckInfo(int barnIndex)throws UnknownHostException, IOException;
}

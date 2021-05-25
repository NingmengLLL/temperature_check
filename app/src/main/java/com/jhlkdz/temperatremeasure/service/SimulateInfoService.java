package com.jhlkdz.temperatremeasure.service;

import com.jhlkdz.temperatremeasure.model.CheckInfo;

public interface SimulateInfoService {

    public CheckInfo getCheckInfoById(int barnId);

    public int getRow(int barnId);

    public int getColumn(int barnId);

    public int getLevel(int barnId);
}

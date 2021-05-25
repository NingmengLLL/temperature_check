package com.jhlkdz.temperatremeasure.model;

public class BarnInfo {

    private int bId;
    private int id;
    private int systemType;
    private int extension; //分机号
    private int row;
    private int column;
    private int level;
    private int startColumn;
    private byte[] B8;
    private byte[] B9;
    private int in_address;
    private int in_point;
    private byte[] B12;
    private int mainAddress;
    private int collectorNum;
    private byte[] B15;

    public BarnInfo() {
    }

    public BarnInfo(int id, int systemType, int extension, int row, int column, int level, int startColumn, byte[] b8, byte[] b9, int in_address, int in_point, byte[] b12, int mainAddress, int collectorNum, byte[] b15) {
        this.id = id;
        this.systemType = systemType;
        this.extension = extension;
        this.row = row;
        this.column = column;
        this.level = level;
        this.startColumn = startColumn;
        B8 = b8;
        B9 = b9;
        this.in_address = in_address;
        this.in_point = in_point;
        B12 = b12;
        this.mainAddress = mainAddress;
        this.collectorNum = collectorNum;
        B15 = b15;
    }

    public int getbId() {
        return bId;
    }

    public void setbId(int bId) {
        this.bId = bId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSystemType() {
        return systemType;
    }

    public void setSystemType(int systemType) {
        this.systemType = systemType;
    }

    public int getExtension() {
        return extension;
    }

    public void setExtension(int extension) {
        this.extension = extension;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }


    public byte[] getB8() {
        return B8;
    }

    public void setB8(byte[] b8) {
        B8 = b8;
    }

    public byte[] getB9() {
        return B9;
    }

    public void setB9(byte[] b9) {
        B9 = b9;
    }

    public byte[] getB12() {
        return B12;
    }

    public void setB12(byte[] b12) {
        B12 = b12;
    }

    public byte[] getB15() {
        return B15;
    }

    public void setB15(byte[] b15) {
        B15 = b15;
    }

    public int getIn_address() {
        return in_address;
    }

    public void setIn_address(int in_address) {
        this.in_address = in_address;
    }

    public int getIn_point() {
        return in_point;
    }

    public void setIn_point(int in_point) {
        this.in_point = in_point;
    }



    public int getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(int mainAddress) {
        this.mainAddress = mainAddress;
    }

    public int getCollectorNum() {
        return collectorNum;
    }

    public void setCollectorNum(int collectorNum) {
        this.collectorNum = collectorNum;
    }


}

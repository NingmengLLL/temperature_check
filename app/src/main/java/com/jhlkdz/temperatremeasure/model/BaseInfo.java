package com.jhlkdz.temperatremeasure.model;

public class BaseInfo {

    private int id;
    private int barn_num;
    private int out_extension;
    private int out_address;
    private int out_point;
    private int max_bytes;

    public BaseInfo() {
    }

    public BaseInfo(int id, int barn_num, int out_extension, int out_address, int out_point, int max_bytes) {
        this.id = id;
        this.barn_num = barn_num;
        this.out_extension = out_extension;
        this.out_address = out_address;
        this.out_point = out_point;
        this.max_bytes = max_bytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBarn_num() {
        return barn_num;
    }

    public void setBarn_num(int barn_num) {
        this.barn_num = barn_num;
    }

    public int getOut_extension() {
        return out_extension;
    }

    public void setOut_extension(int out_extension) {
        this.out_extension = out_extension;
    }

    public int getOut_address() {
        return out_address;
    }

    public void setOut_address(int out_address) {
        this.out_address = out_address;
    }

    public int getOut_point() {
        return out_point;
    }

    public void setOut_point(int out_point) {
        this.out_point = out_point;
    }

    public int getMax_bytes() {
        return max_bytes;
    }

    public void setMax_bytes(int max_bytes) {
        this.max_bytes = max_bytes;
    }
}

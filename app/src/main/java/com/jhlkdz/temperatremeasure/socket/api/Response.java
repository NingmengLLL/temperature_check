package com.jhlkdz.temperatremeasure.socket.api;

import java.util.ArrayList;
import java.util.List;

import static com.jhlkdz.temperatremeasure.util.ConverseUtil.byteToHex;

public class Response {

    private byte beginFlag;

    private byte endFlag;

    private byte responseFlag;

    private byte operationType;

    private byte address;

    private byte paramLength;

    private List<Byte> params;

    private byte CRC;

    public Response() {
    }

    public Response(byte beginFlag, byte endFlag, byte responseFlag, byte operationType, byte address, byte paramLength, List<Byte> params, byte CRC) {
        this.beginFlag = beginFlag;
        this.endFlag = endFlag;
        this.responseFlag = responseFlag;
        this.operationType = operationType;
        this.address = address;
        this.paramLength = paramLength;
        this.params = params;
        this.CRC = CRC;
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        for (Byte b : params) {
            list.add(byteToHex(b));
        }
        String result = "beginFlag:      " + byteToHex(beginFlag) + "\n"
                + "responseFlag:   " + byteToHex(responseFlag) + "\n"
                + "operationType:  " + byteToHex(operationType) + "\n"
                + "address:        " + byteToHex(address) + "\n"
                + "paramLength:    " + byteToHex(paramLength) + "\n"
                + "params:         " + list + "\n"
                + "CRC:            " + byteToHex(CRC) + "\n"
                + "endFlag:        " + byteToHex(endFlag) + "\n";
        return result;
    }

    public boolean isValid(){

        if (beginFlag!=(byte)0x55&&responseFlag!=(byte) 0xAA){
            return false;
        }
        Command command = new Command(responseFlag,operationType,address,paramLength,params);
        byte validCRC = command.getCRC();
        if(validCRC!=CRC){
            return false;
        }
        else
            return true;
    }

    public boolean isValidHands(){
        return isValid()&&operationType==(byte)0x00;
    }

    public boolean isValidOutdata(){
        return isValid()&&operationType==(byte)0x00&&paramLength==(byte)0x20;
    }

    public boolean isValidInnerData(){
        return isValid()&&operationType==(byte)0x00&&paramLength==(byte)0x20;
    }


    public byte getBeginFlag() {
        return beginFlag;
    }

    public void setBeginFlag(byte beginFlag) {
        this.beginFlag = beginFlag;
    }

    public byte getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(byte endFlag) {
        this.endFlag = endFlag;
    }

    public byte getResponseFlag() {
        return responseFlag;
    }

    public void setResponseFlag(byte responseFlag) {
        this.responseFlag = responseFlag;
    }

    public byte getOperationType() {
        return operationType;
    }

    public void setOperationType(byte operationType) {
        this.operationType = operationType;
    }

    public byte getAddress() {
        return address;
    }

    public void setAddress(byte address) {
        this.address = address;
    }

    public byte getParamLength() {
        return paramLength;
    }

    public void setParamLength(byte paramLength) {
        this.paramLength = paramLength;
    }

    public List<Byte> getParams() {
        return params;
    }

    public void setParams(List<Byte> params) {
        this.params = params;
    }

    public byte getCRC() {
        return CRC;
    }

    public void setCRC(byte CRC) {
        this.CRC = CRC;
    }
}

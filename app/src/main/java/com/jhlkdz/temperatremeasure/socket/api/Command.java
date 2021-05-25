package com.jhlkdz.temperatremeasure.socket.api;

import java.util.ArrayList;
import java.util.List;

import static com.jhlkdz.temperatremeasure.util.ConverseUtil.byteToHex;

public class Command {

    private byte beginFlag = (byte)0x55;

    private byte endFlag = (byte)0xA5;

    private byte responseFlag;

    private byte operationType;

    private byte address;

    private byte paramLength;

    private List<Byte> params;

    private byte CRC;

    public Command(byte responseFlag, byte operationType, byte address, byte paramLength, List<Byte> params) {
        this.responseFlag = responseFlag;
        this.operationType = operationType;
        this.address = address;
        this.paramLength = paramLength;
        this.params = params;
        //this.CRC = CRC;
        initCRC();
    }

    @Override
    public String toString() {
        List<String> list = new ArrayList<>();
        if(params!=null){
            for (Byte b : params) {
                list.add(byteToHex(b));
            }
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

    public byte[] toBytes(){
        byte[] res = new byte[7+paramLength];
        res[0] = beginFlag;
        res[1] = responseFlag;
        res[2] = operationType;
        res[3] = address;
        res[4] = paramLength;
        int index=5;
        if(paramLength>1){
            for(int i=0;i<paramLength;i++){
                res[index] = params.get(i);
                index++;
            }
        }
        else if(paramLength==1){
            res[index] = params.get(0);
            index++;
        }
        res[index] = CRC;
        res[index+1] = endFlag;

        return res;
    }

    private void initCRC(){
        int sum = beginFlag+responseFlag+operationType+address+paramLength;
        if(params!=null){
            for(Byte b:params){
                sum += b;
            }
        }
        while(sum>256){
            sum-=256;
        }
        byte b=(byte)sum;
        b = (byte)(b-256);

        CRC = b;
    }

    public byte getCRC() {
        return CRC;
    }

    public void setCRC(byte CRC) {
        this.CRC = CRC;
    }
}

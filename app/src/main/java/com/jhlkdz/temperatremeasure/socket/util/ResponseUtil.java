package com.jhlkdz.temperatremeasure.socket.util;

import com.jhlkdz.temperatremeasure.socket.api.Response;

import java.util.ArrayList;
import java.util.List;

public class ResponseUtil {

    public static Response bytesToResponse(byte[] bytes){
        Response response = new Response();
        response.setBeginFlag(bytes[0]);
        response.setResponseFlag(bytes[1]);
        response.setOperationType(bytes[2]);
        response.setAddress(bytes[3]);
        response.setParamLength(bytes[4]);
        int len = bytes[4];
        int index = 5;
        List<Byte> list = new ArrayList<>();
        for(int i=0;i<len;i++){
            list.add(bytes[index]);
            index++;
        }
        response.setParams(list);
        response.setCRC(bytes[index]);
        response.setEndFlag(bytes[index+1]);
        return response;
    }

}

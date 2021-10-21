package com.jhlkdz.temperatremeasure;

import com.jhlkdz.temperatremeasure.socket.Client;
import com.jhlkdz.temperatremeasure.socket.api.Response;
import com.jhlkdz.temperatremeasure.util.ConverseUtil;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SocketTest {

    private List<Integer> list = new ArrayList<>();

    @Test
    public void test00()throws IOException{
        Client client = new Client("linzhengjun009-js.vicp.io", 34919);
        Response response =  client.executeHands(1,1,new int[]{1,1,1,1,1});
        //Response response1 = client.executeSystemParam(1,0);
//        List<Integer> temp = new ArrayList<>();
//        temp.add(3);
//        client.executeStartCheck(1,temp,null,null);
//        Response innerDataResponse = client.executeInnerData(1,3);
//        System.out.println(innerDataResponse);

       // Response response = client.executeInit(1,3);
       System.out.println(response);

        client.destroy();
    }

    @Test
    public void test01()throws IOException{
        Client client = new Client("192.168.1.200", 1000);
        //18f7n17189.iask.in
        //linzhengjun-js.vicp.io
        //linzhengjun009-js.vicp.io
//        InetAddress inetAddress = InetAddress.getByName("linzhengjun-js.vicp.io");
//        String ip = inetAddress.getHostAddress();
//        System.out.println(ip);
//        Client client = new Client(ip, 1000);
//        Response response = client.executeHands(1);
        Response response1 = client.executeSystemParam(1,3);
//        System.out.println(response);
        System.out.println(response1);
        client.destroy();
    }

    @Test
    public void test02()throws IOException{
        Client client = new Client("linzhengjun009-js.vicp.io", 34919);
        client.executeHands(1,1,new int[]{1,1,1,1,1});
        Response response3 = client.executeOuterData(1);
        System.out.println(11);
        System.out.println(response3);
//        for(Response r:response3)
//            System.out.println(r);
        System.out.println(22);
        response3 = client.executeRepeat(1);
        System.out.println(response3);

        client.destroy();
    }

    @Test
    public void test03()throws IOException{
        Client client = new Client("192.168.1.200", 1000);
        Response res = client.executeHands(1,1,new int[]{1,1,1,1,1});
        System.out.println(res);
        List<Integer> temp = new ArrayList<>();
        temp.add(1);
        //client.executeStartCheck(1,temp,null,null);
        Response res1 = client.executeOuterData(1);
        System.out.println(res1);
        Response res2 =client.executeSystemParam(1,0);
        System.out.println(res2);
        Response response = client.executeInnerData(1,1);
        System.out.println(response);
        client.destroy();
    }

    @Test
    public void test(){
        System.out.println(Integer.parseInt("0w"));
    }

    @Test
    public void test04()throws IOException{
        Client client = new Client("192.168.1.200", 1000);
        //Response response = client.executeHands(1);
        //Response response1 = client.executeSystemParam(1,4);
        //System.out.println(response);
        client.destroy();
    }

    @Test
    public void test05()throws IOException{
        Client client = new Client("192.168.1.200", 1000);
        Response response = client.executeColumnTemperature(1,0);
        System.out.println(response);
        client.destroy();
    }

    @Test
    public void testNum(){
        System.out.println(ConverseUtil.IntToTime((byte)0x16));
    }

}

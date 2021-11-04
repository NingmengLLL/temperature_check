package com.jhlkdz.temperatremeasure.service.impl;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.jhlkdz.temperatremeasure.dataService.CheckInfoDataService;
import com.jhlkdz.temperatremeasure.dataService.impl.CheckInfoDataServiceImpl;
import com.jhlkdz.temperatremeasure.db.DbHelper;
import com.jhlkdz.temperatremeasure.model.BarnInfo;
import com.jhlkdz.temperatremeasure.model.CheckInfo;
import com.jhlkdz.temperatremeasure.service.BarnInfoService;
import com.jhlkdz.temperatremeasure.service.CheckInfoService;
import com.jhlkdz.temperatremeasure.socket.Client;
import com.jhlkdz.temperatremeasure.socket.api.Command;
import com.jhlkdz.temperatremeasure.socket.api.Response;
import com.jhlkdz.temperatremeasure.ui.Task.TimeoutThread;
import com.jhlkdz.temperatremeasure.ui.activity.api.LoginInfo;
import com.jhlkdz.temperatremeasure.util.ConverseUtil;
import com.jhlkdz.temperatremeasure.util.InnerTempHumi;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CheckInfoServiceImpl implements CheckInfoService {

    private Context context;
    private CheckInfoDataService checkInfoDataService;
    private BarnInfoService barnInfoService;
    private ProgressDialog pd;

    public CheckInfoServiceImpl(Context context){
        this.context = context;
        checkInfoDataService = new CheckInfoDataServiceImpl();
        barnInfoService = new BarnInfoServiceImpl(context);
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        CheckInfoDataServiceImpl.setDb(db);
    }

    public CheckInfoServiceImpl(Context context, ProgressDialog pd){
        this.context = context;
        this.pd = pd;
        checkInfoDataService = new CheckInfoDataServiceImpl();
        barnInfoService = new BarnInfoServiceImpl(context);
        DbHelper helper = new DbHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        CheckInfoDataServiceImpl.setDb(db);
    }

    @Override
    public List<Integer> addCheckInfo(List<Integer> barns, List<String> nameList, LoginInfo loginInfo, TimeoutThread tTime) throws UnknownHostException, IOException {

        // 返回值
        //正常集合
        List<Integer> res = new ArrayList<>();

        CheckInfo checkInfo = new CheckInfo();
        int address = loginInfo.getAddress();

        Client client = new Client(loginInfo.getIp(),loginInfo.getPort());
        Response handsResponse = client.executeHands(address,loginInfo.getAccount(),loginInfo.getPassword());
        if (!handsResponse.isValidHands())
            return res;

        //start check
        List<Response> startCheckResponses = client.executeStartCheck(address,barns,nameList,context,pd);
        for(Response r:startCheckResponses){
            if(r.getOperationType()==0x15&&r.getParams().get(0)==0x01){
                res.add((int)r.getParams().get(1)+1);
            }
            System.out.println(r);
        }
        //out data
        Response outDataResponse = client.executeOuterData(address);
        System.out.println("outDataResponse");
        System.out.println(outDataResponse);

        if (!outDataResponse.isValidOutdata()){
            return null;
        }
        List<Byte> outParams = outDataResponse.getParams();

        checkInfo.setOut_temperature((256*ConverseUtil.ByteToInt(outParams.get(9))+ConverseUtil.ByteToInt(outParams.get(10)))/10.0f);
        checkInfo.setOut_humidity((256*ConverseUtil.ByteToInt(outParams.get(11))+ConverseUtil.ByteToInt(outParams.get(12)))/10.0f);
        //checkInfo.setOut_temperature(ConverseUtil.byteToTemp(outParams.get(10),outParams.get(9)));
        //checkInfo.setOut_humidity(ConverseUtil.byteToHumid(outParams.get(11),outParams.get(12)));
        int position = (outParams.get(3)&0x80)>0?1:0;
        int fj = outParams.get(4);
        //checkInfo.setOut_humidity(ConverseUtil.getOutHumi(position,0,outParams.get(11),outParams.get(12),fj));

        int year = 2000+ConverseUtil.IntToTime(outParams.get(18));
        int month = ConverseUtil.IntToTime(outParams.get(17));
        int day = ConverseUtil.IntToTime(outParams.get(16));
        int hour = ConverseUtil.hourIntToTime(outParams.get(15));
        int minute = ConverseUtil.IntToTime(outParams.get(14));
        int second = ConverseUtil.IntToTime(outParams.get(13));
        String time = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time = sdf.format(date);
        checkInfo.setTime(time);
        int flag = 0;
        for(int i=0;i<barns.size();i++){
            int index = barns.get(i);
            checkInfo.setId(index);
            List<Float> temperatureList = new ArrayList<>();

            //取参数
            Response systemParamResponse = client.executeSystemParam(address,2,index);
            int temp= barnInfoService.addBarnInfo(systemParamResponse);
            checkInfo.setCid(temp);

            //inner data
            Response innerDataResponse = client.executeInnerData(address,index);
            System.out.println("innerDataResponse");
            System.out.println(innerDataResponse);
            if(!innerDataResponse.isValidInnerData()){
                return null;
            }
            List<Byte> params = innerDataResponse.getParams();

            InnerTempHumi innerTempHumi = ConverseUtil.getInTempHumi(params.get(11),params.get(12),
                    params.get(13),params.get(14),params.get(15),params.get(16),params.get(17));
            checkInfo.setIn_temperature(innerTempHumi.getTemp());
            checkInfo.setIn_humidity(innerTempHumi.getHumi());
            int rows = params.get(4);
            int columns = params.get(5);
            int levels = params.get(6);
            int begin = params.get(7);
            Log.i("","rows: "+rows+"columns: "+columns+"levels: "+levels+"begin: "+begin);

            if (params.get(11)==12){
                HashMap<Integer,List<Float>> columnTemps = new HashMap<>();
                for(int j=0;j<columns;j++){
                    Response response = client.executeColumnTemperature(address,j);
                    System.out.println(response);
                    List<Byte> columnParams = response.getParams();
                    HashMap<Integer, List<Float>> spotTemps = new HashMap<>();

                    if (!ConverseUtil.byteToHex(columnParams.get(0)).equals("FF")){
                        int columnIndex = columnParams.get(0);
                        int spot = columnParams.get(3)%rows;
                        List<Float> temps = new ArrayList<>();
                        for(int k=3;k<columnParams.size();k++){
                            if(k==3||(k-3)%(2*levels+1)==0){
                                spot = columnParams.get(k)%rows;
                                if (spot==0)
                                    spot = rows;
                            }
                            else {
                                temps.add(ConverseUtil.byteToTemp(columnParams.get(k+1),columnParams.get(k)));
                                k++;
                            }
                            if((k-2)%(2*levels+1)==0){
                                spotTemps.put(spot,temps);
                                temps = new ArrayList<>();
                            }
                        }

                        List<Float> columnTemp = new ArrayList<>();
                        for(int k=1;k<=rows;k++){
                            List<Float> spotTemp = spotTemps.get(k);
                            for(float f:spotTemp)
                                columnTemp.add(f);
                        }
                        columnTemps.put(columnIndex,columnTemp);
                    }

                }
                for(int j=1;j<=columns;j++){
                    List<Float> columnTemp = columnTemps.get(j);
                    if(columnTemp!=null){
                        for(float f:columnTemp)
                            temperatureList.add(f);
                    }
                    else {
                        for(int k=0;k<rows*levels;k++)
                            temperatureList.add(888.0f);
                    }
                }
                checkInfo.setTemperature(temperatureList);
                checkInfoDataService.insertCheckInfo(checkInfo);
            }
            else {
                for(int j=0;j<columns;j++){
                    Response response = client.executeColumnTemperature(address,j);
                    List<Byte> temps = response.getParams();
                    for (int k=0;k<temps.size();k+=2){
                        temperatureList.add(ConverseUtil.byteToTemp(temps.get(k+1),temps.get(k)));
                    }
                }
                checkInfo.setTemperature(temperatureList);
                Log.i("",checkInfo.toString());
                BarnInfo barnInfo = barnInfoService.getBarnInfo(checkInfo.getCid()).get(0);
                if (checkInfo.isValid(barnInfo)) {
                    flag=0;
                    checkInfoDataService.insertCheckInfo(checkInfo);
                }
                else{
                    i--;
                    flag++;
                }
                if (flag==3){
                    i++;
                    for(int k=0;k<res.size();k++){
                        if (res.get(k)==index)
                            res.remove(k);
                    }
                }
            }
        }
        return res;
    }

    public ArrayList<Integer> syncCheckInfo(LoginInfo loginInfo)throws UnknownHostException, IOException{

        setTitle(pd,"同步中");
        pd.setProgress(30);
        sleep(150);

        ArrayList<Integer> res = new ArrayList<>();

        int address = loginInfo.getAddress();
        Client client = new Client(loginInfo.getIp(),loginInfo.getPort());
        Response handsResponse = client.executeHands(address,loginInfo.getAccount(),loginInfo.getPassword());
        if (!handsResponse.isValidHands()){
            pd.cancel();
            return res;
        }

        CheckInfo checkInfo = new CheckInfo();
        //out data
        Response outDataResponse = client.executeOuterData(address);
        List<Byte> outParams = outDataResponse.getParams();
        //外温外湿
        //checkInfo.setOut_temperature(ConverseUtil.byteToTemp(outParams.get(10),outParams.get(9)));
        int position = (outParams.get(3)&0x80)>0?1:0;
        int fj = outParams.get(4);
        //checkInfo.setOut_humidity(ConverseUtil.getOutHumi(position,0,outParams.get(11),outParams.get(12),fj));
        checkInfo.setOut_temperature((256*ConverseUtil.ByteToInt(outParams.get(9))+ConverseUtil.ByteToInt(outParams.get(10)))/10.0f);
        checkInfo.setOut_humidity((256*ConverseUtil.ByteToInt(outParams.get(11))+ConverseUtil.ByteToInt(outParams.get(12)))/10.0f);

        int year = 2000+ConverseUtil.IntToTime(outParams.get(18));
        int month = ConverseUtil.IntToTime(outParams.get(17));
        int day = ConverseUtil.IntToTime(outParams.get(16));
        int hour = ConverseUtil.hourIntToTime(outParams.get(15));
        int minute = ConverseUtil.IntToTime(outParams.get(14));
        int second = ConverseUtil.IntToTime(outParams.get(13));
        String time = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time = sdf.format(date);
        if(checkInfoDataService.getCheckInfoByTime(time).size()!=0){
            pd.cancel();
            return res;
        }
        checkInfo.setTime(time);

        pd.setProgress(50);
        sleep(150);

        int numOfBarn = outParams.get(0)*256+outParams.get(1);
        for(int i=1;i<=numOfBarn;i++){
            List<Float> temperatureList = new ArrayList<>();

            Response tempResponse = client.executeOrderInnerData(address,i);
            List<Byte> params = tempResponse.getParams();
            int barnId = params.get(0)*256+params.get(1);
            checkInfo.setId(barnId);
            res.add(barnId);

            //取参数
            Response systemParamResponse = client.executeSystemParam(address,2,barnId);
            int temp= barnInfoService.addBarnInfo(systemParamResponse);
            checkInfo.setCid(temp);

            InnerTempHumi innerTempHumi = ConverseUtil.getInTempHumi(params.get(11),params.get(12),
                    params.get(13),params.get(14),params.get(15),params.get(16),params.get(17));
            checkInfo.setIn_temperature(innerTempHumi.getTemp());
            checkInfo.setIn_humidity(innerTempHumi.getHumi());

//            checkInfo.setIn_temperature(ConverseUtil.byteToTemp(params.get(15),params.get(14)));
//            checkInfo.setIn_humidity(ConverseUtil.byteToHumid(params.get(16),params.get(17)));
            int rows = params.get(4);
            int columns = params.get(5);
            int levels = params.get(6);
            int begin = params.get(7);

            pd.setProgress(80);
            sleep(150);

            if (params.get(11)==12){
                HashMap<Integer,List<Float>> columnTemps = new HashMap<>();
                for(int j=0;j<columns;j++){
                    Response response = client.executeColumnTemperature(address,j);
                    System.out.println(response);
                    List<Byte> columnParams = response.getParams();
                    HashMap<Integer, List<Float>> spotTemps = new HashMap<>();

                    if (!ConverseUtil.byteToHex(columnParams.get(0)).equals("FF")){
                        int columnIndex = columnParams.get(0);
                        int spot = columnParams.get(3)%rows;
                        List<Float> temps = new ArrayList<>();
                        for(int k=3;k<columnParams.size();k++){
                            if(k==3||(k-3)%(2*levels+1)==0){
                                spot = columnParams.get(k)%rows;
                                if (spot==0)
                                    spot = rows;
                            }
                            else {
                                temps.add(ConverseUtil.byteToTemp(columnParams.get(k+1),columnParams.get(k)));
                                k++;
                            }
                            if((k-2)%(2*levels+1)==0){
                                spotTemps.put(spot,temps);
                                temps = new ArrayList<>();
                            }
                        }

                        List<Float> columnTemp = new ArrayList<>();
                        for(int k=1;k<=rows;k++){
                            List<Float> spotTemp = spotTemps.get(k);
                            for(float f:spotTemp)
                                columnTemp.add(f);
                        }
                        columnTemps.put(columnIndex,columnTemp);
                    }

                }
                for(int j=1;j<=columns;j++){
                    List<Float> columnTemp = columnTemps.get(j);
                    if(columnTemp!=null){
                        for(float f:columnTemp)
                            temperatureList.add(f);
                    }
                    else {
                        for(int k=0;k<rows*levels;k++)
                            temperatureList.add(888.0f);
                    }
                }
                checkInfo.setTemperature(temperatureList);
                checkInfoDataService.insertCheckInfo(checkInfo);
            }
            else {
                for(int j=0;j<columns;j++){
                    Response response = client.executeColumnTemperature(address,j);
                    List<Byte> temps = response.getParams();
                    for (int k=0;k<temps.size();k+=2){
                        temperatureList.add(ConverseUtil.byteToTemp(temps.get(k+1),temps.get(k)));
                    }
                }
                checkInfo.setTemperature(temperatureList);
                System.out.println(checkInfo);
                Log.i("",checkInfo.toString());
                checkInfoDataService.insertCheckInfo(checkInfo);
            }
        }
        pd.setProgress(100);
        sleep(150);
        pd.cancel();
        return res;
    }

    @Override
    public List<CheckInfo> getCheckInfo(int barnIndex) throws UnknownHostException, IOException {
        return checkInfoDataService.getCheckInfo(barnIndex);
    }

    private void setTitle(final ProgressDialog pd, final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                pd.setTitle(text);
            }
        });
    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setPd(ProgressDialog pd) {
        this.pd = pd;
    }
}

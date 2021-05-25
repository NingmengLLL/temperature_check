package com.jhlkdz.temperatremeasure.socket;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.jhlkdz.temperatremeasure.socket.api.Response;
import com.jhlkdz.temperatremeasure.socket.util.CommandsUtil;
import com.jhlkdz.temperatremeasure.socket.util.ResponseUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {

    private Socket server;

    private DataInputStream in;

    private DataOutputStream out;

    public Client(String host, int port)throws UnknownHostException, IOException{
        server = new Socket(host, port);
        in = new DataInputStream(server.getInputStream());
        out = new DataOutputStream(server.getOutputStream());
    }

    public void destroy()throws IOException{
        if(out!=null)
            out.close();
        if(in !=null)
            in.close();
        if(server!=null)
            server.close();
    }

    public Response executeHands(int address, int account, int[] password)throws IOException {
        byte[] bytes = CommandsUtil.getHandsCommamnd(address,account,password);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    public Response executeSystemParam(int address, int barnIndex)throws IOException{
        byte[] bytes = CommandsUtil.getSystemParamCommand(address,barnIndex);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    public Response executeOuterData(int address)throws IOException{
        byte[] bytes = CommandsUtil.getOuterData(address);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    public Response executeInnerData(int address, int barnIndex)throws IOException{
        byte[] bytes = CommandsUtil.getInnerData(address,barnIndex);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    public Response executeOrderInnerData(int address, int index)throws IOException{
        byte[] bytes = CommandsUtil.getOrderInnerData(address,index);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    private ProgressDialog pd;
    private Context context;

    public List<Response> executeStartCheck(int address, List<Integer> list, Context context, ProgressDialog pd)throws IOException{
        this.pd = pd;
        this.context = context;

        setTitle(pd,"正在检测"+list.get(0)+"仓"+"(1/"+list.size()+")");
        pd.setProgress(20);
        sleep(300);
        pd.setProgress(40);
        sleep(300);
        byte[] bytes = CommandsUtil.getStartCheck(address,list);
        out.write(bytes);
        out.flush();
        List<Response> res = new ArrayList<>();
        for(int i=0;i<list.size()+2;i++){
            Response response = ResponseUtil.bytesToResponse(getResponse());
            if (response.getOperationType()==0x15&&response.getParams().get(0)==0x01&&i!=1) {
                pd.setProgress(80);
                sleep(300);
                pd.setProgress(100);
                sleep(300);
                setTitle(pd, "正在检测" + (response.getParams().get(1) + 1) + "仓" + "(" + i + "/" + list.size() + ")");
                pd.setProgress(20);
                sleep(300);
                pd.setProgress(40);
                sleep(300);
            }
            res.add(response);
        }
        return res;
    }

    private void setTitle(final ProgressDialog pd, final String text){
        new Handler(context.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                pd.setTitle(text);
            }
        });
    }

    public Response executeColumnTemperature(int address, int column)throws IOException{
        byte[] bytes = CommandsUtil.getColumnTemperature(address,column);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    public Response executeRepeat(int address)throws IOException{
        byte[] bytes = CommandsUtil.repeatInfo(address);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    public Response executeInit(int address, int barnIndex)throws IOException{
        byte[] bytes = CommandsUtil.init(address,barnIndex);
        out.write(bytes);
        out.flush();
        return ResponseUtil.bytesToResponse(getResponse());
    }

    private byte[] getResponse()throws IOException{
        byte[] res = new byte[300];
        int index = 0;
        byte[] bytes = new byte[1];
        while (in.read(bytes)!=-1){
            res[index] = bytes[0];
            index++;
            if("ffffffa5".equals(Integer.toHexString(bytes[0]))){
                break;
            }
        }
        return res;
    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
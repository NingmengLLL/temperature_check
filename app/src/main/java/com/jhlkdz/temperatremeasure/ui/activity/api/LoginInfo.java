package com.jhlkdz.temperatremeasure.ui.activity.api;

import com.jhlkdz.temperatremeasure.util.CheckUtil;

public class LoginInfo {

    private String ipAndPort;
    private String address;
    private String account;
    private String password;

    public LoginInfo(){}

    public LoginInfo(String ipAndPort, String address, String account, String password) {
        this.ipAndPort = ipAndPort;
        this.address = address;
        this.account = account;
        this.password = password;
    }

    public String check(){
        if(CheckUtil.isEmpty(ipAndPort)){
            return "ip不能为空";
        }
        else if(CheckUtil.isEmpty(address)){
            return "中控机地址不能为空";
        }
        else if(CheckUtil.isEmpty(account)){
            return "账户不能为空";
        }
        else if(getAccount()>255||getAccount()<0){
            return "账户必须在0-255之间";
        }
        else if(CheckUtil.isEmpty(password)){
            return "密码不能为空";
        }
        else if(ipAndPort.indexOf(':')==-1||ipAndPort.indexOf(':')!=ipAndPort.lastIndexOf(':')){
            return "ip格式错误";
        }
        else if(!CheckUtil.isNum(ipAndPort.substring(ipAndPort.indexOf(':')+1))){
            return "端口必须为数字";
        }
        else if(password.length()!=5){
            return "密码错误";
        }
        else {
            return "OK";
        }
    }

    public String getIp(){
        return ipAndPort.substring(0, ipAndPort.indexOf(':'));
    }

    public int getPort(){
        return Integer.parseInt(ipAndPort.substring(ipAndPort.indexOf(':')+1));
    }

    public int getAddress() {
        return Integer.parseInt(address);
    }

    public int getAccount() {
        return Integer.parseInt(account);
    }

    public int[] getPassword() {
        int[] res = new int[5];
        for(int i=0;i<5;i++){
            res[i] = Integer.parseInt(password.substring(i,i+1));
        }
        return res;
    }

}

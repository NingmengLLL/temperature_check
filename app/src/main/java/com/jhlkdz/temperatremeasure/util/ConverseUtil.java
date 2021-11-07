package com.jhlkdz.temperatremeasure.util;

import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

public class ConverseUtil {

    //todo
    public static int bytesToInt(byte[] bytes) {
        //如果不与0xff进行按位与操作，转换结果将出错
        int result=0;
        for (int i=0;i<bytes.length;i++){
            result|=(bytes[i]&0xff);
        }
        return result;
    }

    public static String byteToHex(byte b) {
        String strHex;
        StringBuilder sb = new StringBuilder();
        strHex = Integer.toHexString(b & 0xFF);
        sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        return sb.toString().toUpperCase().trim();
    }

    // 仓名
    public static String byteToSingleChar(byte b){
        String str = byteToHex(b);
        if (str.equals("00")||str.equals("FF"))
            return "";
        int res = HexToInt(str);
        return String.valueOf((char)res);
    }

    public static float byteToTemp(byte h,byte l){// 00 F0  0 240
        if(h==127&&l==-1){
            return 888f;
        }
        if(h<0){
            l^=0xff;
            h^=0xff;
            if(l==-1){
                l=0;
                h=(byte)(h+1);
            }
            else {
                l= (byte)(l+1);
            }
            if(l<0){
                return -((l+256)/16.0f+h*16.0f);
            }
            else {
                return -(l/16.0f+h*16.0f);
            }

        }
        else {
            if(l<0){
                return (l+256)/16.0f+h*16.0f;
            }
            else {
                return l/16.0f+h*16.0f;
            }
        }
    }

    private static float[] humiditys = new float[]{265,255,240,225,210,195,182,167,152,135,124,106,88,70,60};

    public static float byteToHumid(byte h,byte l){
        int m = (h & 0x0F)*256+l;
        float s = (m*4.9f)/1024.0f;
        Log.i("","!!!!"+s);

        return getHumiByTable(s*100);
    }

    private static float getHumiByTable(float s){
        int i;
        for(i=0;i<humiditys.length;i++){
            if(s>humiditys[i]){
                if(i==0)
                    return 90;
                else
                    return 90-i*5+(s-humiditys[i])*5/(humiditys[i-1]-humiditys[i]);
            }
        }
        return 20;
    }

    public static float[][][] ListToArray(List<Float> list,int row, int column,int level){
        float[][][] result = new float[level][column][row];
        int index = 0;
        for(int i=column-1;i>=0;i--){
            for(int j=row-1;j>=0;j--){
                for(int k=level-1;k>=0;k--){
                    result[k][i][j] = getNormalFloat(list.get(index),1);
                    index++;
                }
            }
        }
        return result;
    }

    public static HashMap<Integer,LevelData> getLevelData(List<Float> list,int row,int column,int level){
        float[][][] data = ListToArray(list,row,column,level);
        HashMap<Integer,LevelData> result = new HashMap<>();
        for(int i=0;i<level;i++){
            float max = Float.MIN_VALUE;
            float min = Float.MAX_VALUE;
            float sum = 0;
            int num= column*row;
            for(int j=0;j<column;j++){
                for(int k=0;k<row;k++){
                    float temp = data[i][j][k];
                    if(temp>50||temp<-10){
                        num--;
                    }
                    else {
                        if(temp>max)
                            max = temp;
                        if(temp<min)
                            min = temp;
                        sum+=temp;
                    }
                }
            }
            result.put(i+1,new LevelData(max,min,getNormalFloat(sum/num,1)));
        }
        return result;
    }

    public static HashMap<Integer,LevelData> getRowData(List<Float> list,int row,int column,int level){
        float[][][] data = ListToArray(list,row,column,level);
        HashMap<Integer,LevelData> result = new HashMap<>();
        for(int i=0;i<row;i++){
            float max = Float.MIN_VALUE;
            float min = Float.MAX_VALUE;
            float sum = 0;
            int num= column*level;
            for(int j=0;j<column;j++){
                for(int k=0;k<level;k++){
                    float temp = data[k][j][i];
                    if(temp>50||temp<-10){
                        num--;
                    }
                    else {
                        System.out.print(temp+",");
                        if(temp>max)
                            max = temp;
                        if(temp<min)
                            min = temp;
                        sum+=temp;
                    }
                }
            }
            System.out.println(max+" "+min);
            result.put(row-i,new LevelData(max,min,getNormalFloat(sum/num,1)));
        }
        return result;
    }



    public static float getTotalAverage(List<Float> list){
        float sum = 0;
        int num = list.size();
        for(int i=0;i<list.size();i++){
            float temp = list.get(i);
            if(temp>50||temp<-10){
                num--;
            }
            else {
                sum+=temp;
            }
        }
        return getNormalFloat(sum/num,1);
    }

    public static float getNormalFloat(float f,int i){
        int c = (int)Math.pow(10,i);
        return (float) ((Math.round(f*c))*0.1);
    }

    public static int[] pwdIntToArray(int q){
        System.out.println("helo"+q);
        String temp = String.valueOf(q);
        int[] res = new int[5];
        for(int i=0;i<5;i++){
            res[i] = Integer.parseInt(temp.substring(i,i+1));
        }
        return res;
    }

    public static int IntToTime(byte b){
        int temp = b;
        if(temp<0)
            temp+=256;
        String str = Integer.toBinaryString(temp);
        while(str.length()<8){
            str = "0"+str;
        }
        int first = Integer.parseInt(str.substring(0,4),2);
        int second = Integer.parseInt(str.substring(4,8),2);
        return first*10+second;
    }

    public static int hourIntToTime(byte b){
        int temp = b;
        String str = Integer.toBinaryString(temp);
        if (temp<0){
            str=str.substring(24);
        }
        while(str.length()<8){
            str = "0"+str;
        }
        System.out.println(str);
        if(str.charAt(0)=='1'){
            str = "0"+str.substring(1,8);
        }
        int first = Integer.parseInt(str.substring(0,4),2);
        int second = Integer.parseInt(str.substring(4,8),2);
        return first*10+second;
    }

    // unsigned
    public static int HexToInt(String hex){
        int res = 0;
        for (char c:hex.toCharArray()){
            if (Character.isDigit(c)){
                res = res*16+(c-'0');
            }
            else {
                res = res*16+(c-'A'+10);
            }
        }
        return res;
    }

    public static int ByteToInt(byte b){
        return HexToInt(byteToHex(b));
    }

    //内温内湿
    public static InnerTempHumi getInTempHumi(byte systemType, byte humiMode, byte fj, byte tmpl,
                                              byte temph, byte humh, byte huml){
        InnerTempHumi res = new InnerTempHumi();
        res.setTemp((ByteToInt(tmpl)*256+ByteToInt(temph))/10.0f);
        res.setHumi((ByteToInt(humh)*256+ByteToInt(huml))/10.0f);
//        res.setTemp(getTemp(tmpl,temph,humiMode));
//        if(systemType==9||systemType==8){
//            res.setHumi(getHumi(humh,huml,humiMode,0));
//        }else {
//            int n=-1;
//            if((humiMode&0x80)>0)
//                n=2;
//            else
//                n=1;
//            if((fj&0x7)==1)
//                n=2;
//            if(systemType==4&&(fj&0x7)==2)
//                n=0;
//            res.setHumi(getHumi(humh,huml,humiMode,n));
//        }

        return res;
    }

    //内温
    private static float getTemp(byte l, byte h, byte imode){

        if((imode&0x7F)==4){
            System.out.println("q");
            float temp = (l&0x3F)*256+h;
            return -39.6f+temp/100;
        }
        else if((imode&0x7F)==2){
            System.out.println("w");
            System.out.println(h+""+l);
            return byteToTemp(h,l);
        }
        else {
            return 0.0f;
        }
    }

    //内湿
    private static float getHumi(byte h, byte l, byte imode, int ihum){

        int high = h<0?h+256:h;
        int low = l<0?l+256:l;

        float res = 0f;
        int temp1 = imode&0x7F;
        if((imode&0x7F)==4){
            float temp = (high&0xF)*256+low;
            res = temp*0.0367f-2.0468f;
        }
        else if((imode&0x7F)==2){
            if (ihum==0){
                float temp = (high&0xF)*256+low;
                res = (temp*1.25f)/2048;
                res = res*10.2f/2.7f;
            }
            else if(ihum==1){
                float temp = (high&0xF)*256+low;
                res = (temp*3.3f)/1024;
                res = res*162/100;
            }
            else if(ihum==2){
                float temp = (high&0xF)*256+low;
                res = (temp*4.9f)/1024;
            }
            res = getHumiByTable(res*100);
            if (res>95)
                res = 95f;
        }
        return res;
    }

    //外湿
    public static float getOutHumi(int position, int config, byte h, byte l,int fj){

        int high = h<0?h+256:h;
        int low = l<0?l+256:l;

        float temp = 0;
        if(position==0){
            if((fj&0x7)<2){
                temp = ((high * 256 + low) * 4.9f) / 1024;
            }
            else if((fj&0x7)==2){
                temp = (high & 0xF) * 256 + low;
                temp = (temp * 1.25f) / 2048;
                temp = temp * 10.2f / 2.7f;
            }
            else if((fj&0x7)==3){
                temp = high * 256 + low;
                temp = (temp * 3.3f) / 1024;
                temp = temp * 162 / 100;
            }
        }
        else if(position==1){
            temp = ((high * 256 + low) * 5.2f) / 1024;
        }

        temp = getHumiByTable(temp*100);
        return temp;
    }

    public static String getRSSI(byte b){
        int temp;
        if (b<0){
            temp = (~b)+1;
            return "-"+temp;
        }
        else
            return ""+b;
    }
}

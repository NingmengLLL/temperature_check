package com.jhlkdz.temperatremeasure.socket.util;

import com.jhlkdz.temperatremeasure.socket.api.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandsUtil {

    private static final byte RESPONSE_FLAG = (byte) 0x01;

    private static final byte NO_RESPONSE_FLAG = (byte) 0xAA;

    private static final byte HANDS_COMMAND = (byte) 0x49;

    private static final byte SYSTEM_PARAM_COMMAND = (byte) 0x43;

    private static final byte START_CHECK_COMMAND = (byte) 0x4a;

    private static final byte OUTER_DATA_COMMAND = (byte)0x4c;

    private static final byte INNER_DATA_COMMAND = (byte)0x4f;

    private static final byte ORDER_INNER_DATA_COMMAND = (byte)0x50;

    private static final byte COLUMN_TEMP_COMMAND = (byte)0x4e;

    private static final byte REPEAT_INFO = (byte)0x04;

    private static final byte INIT_COMMAND = (byte)0x4d;

    // 握手命令
    public static byte[] getHandsCommamnd(int address,int account,int[] password) {
        List<Byte> params = new ArrayList<>();
        byte b = (byte)(account-256);
        params.add(b);
        for(int i=0;i<5;i++){
            params.add((byte)(password[i]+48));
        }
        params.add((byte)0);
        params.add((byte)0);
        Command command = new Command(NO_RESPONSE_FLAG, HANDS_COMMAND, (byte)address, (byte) 8, params);
        return command.toBytes();
    }

    // 取系统参数
    public static byte[] getSystemParamCommand(int address, int mod, int barnIndex) {
        Command command = new Command(NO_RESPONSE_FLAG, SYSTEM_PARAM_COMMAND, (byte)address, (byte) 0x02, Arrays.asList((byte)mod,(byte)barnIndex));
        return command.toBytes();
    }

    // 启动检测
    public static byte[] getStartCheck(int address, List<Integer> barnIndexs) {
        List<Byte> params = new ArrayList<>();
        for (int i : barnIndexs) {
            params.add((byte)(i-1)); //仓号需要减一
        }
        int length = params.size();
        Command command = new Command(RESPONSE_FLAG, START_CHECK_COMMAND, (byte)address, (byte)length, params);
        return command.toBytes();
    }

    // 取外温外湿
    public static byte[] getOuterData(int address){
        Command command = new Command(NO_RESPONSE_FLAG, OUTER_DATA_COMMAND, (byte) address,(byte)0x0, null);
        return command.toBytes();
    }

    // 取内温内湿
    public static byte[] getInnerData(int address, int barnIndex){
        Command command = new Command(NO_RESPONSE_FLAG, INNER_DATA_COMMAND, (byte)address, (byte)0x01,Arrays.asList((byte)barnIndex));
        System.out.println("inner_command");
        System.out.println(command);
        return command.toBytes();
    }

    //取顺序内温内湿
    public static byte[] getOrderInnerData(int address, int index){
        Command command = new Command(NO_RESPONSE_FLAG, ORDER_INNER_DATA_COMMAND,(byte)address,(byte)0x01,Arrays.asList((byte)index));
        return command.toBytes();
    }

    // 取某列的温度
    public static byte[] getColumnTemperature(int address, int column){
        Command command = new Command(NO_RESPONSE_FLAG,COLUMN_TEMP_COMMAND,(byte)address,(byte)0x01,Arrays.asList((byte)column));
        return command.toBytes();
    }

    // 重发信息
    public static byte[] repeatInfo(int address){
        Command command = new Command(NO_RESPONSE_FLAG, REPEAT_INFO, (byte)address, (byte)0x0,null);
        return command.toBytes();
    }

    // 初始化
    public static byte[] init(int address, int barnIndex){
        Command command = new Command(NO_RESPONSE_FLAG, INIT_COMMAND, (byte)address, (byte)0x01, Arrays.asList((byte)barnIndex));
        return command.toBytes();
    }
}

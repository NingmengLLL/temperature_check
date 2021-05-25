package com.jhlkdz.temperatremeasure.db;

public final class BaseInfoContract {

    private BaseInfoContract(){}

    public static final class BaseInfoEntry{
        public final static String TABLE_NAME = "baseinfo";

        public final static String ID = "id";
        public final static String BARN_NUM = "barn_num";
        public final static String OUT_EXTENTION = "out_extention";
        public final static String OUT_ADDRESS = "out_address";
        public final static String OUT_POINT = "out_point";
        public final static String MAX_BYTES = "max_bytes";
    }
}

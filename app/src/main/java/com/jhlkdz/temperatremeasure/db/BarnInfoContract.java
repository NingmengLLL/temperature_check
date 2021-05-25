package com.jhlkdz.temperatremeasure.db;

public final class BarnInfoContract {

    private BarnInfoContract(){}

    public static final class BarnInfoEntry{
        public final static String TABLE_NAME = "barninfo";

        public final static String bID = "bid";
        public final static String ID = "id";
        public final static String SYSTEM_TYPE = "system_type";
        public final static String EXTENSION = "extension";
        public final static String ROW = "num_of_row";
        public final static String COLUMN = "num_of_column";
        public final static String LEVEL = "num_of_level";
        public final static String START_COLUMN = "start_column";
        public final static String B8 = "b8";
        public final static String B9 = "b9";
        public final static String IN_ADDRESS = "in_address";
        public final static String IN_POINT = "in_point";
        public final static String B12=  "b12";
        public final static String MAIN_ADDRESS = "main_address";
        public final static String COLLECTOR_NUM = "collector_num";
        public final static String B15 = "b15";
    }
}

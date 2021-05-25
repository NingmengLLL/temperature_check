package com.jhlkdz.temperatremeasure.db;

public final class CheckInfoContract {

    private CheckInfoContract(){}

    public static final class CheckInfoEntry {
        public final static String TABLE_NAME = "checkinfo";

        public final static String cID = "cid";
        public final static String ID = "id";
        public final static String TIME = "time";
        public final static String OUT_TEMPERATURE = "out_temperature";
        public final static String OUT_HUMIDITY = "out_humidity";
        public final static String IN_TEMPERATURE = "in_temperature";
        public final static String IN_HUMIDITY = "in_humidity";
        public final static String TEMPERATURE = "temperature";

    }

}

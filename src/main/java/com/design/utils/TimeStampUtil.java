package com.design.utils;

import java.sql.Timestamp;
import java.util.Date;

public class TimeStampUtil {

    public static Timestamp to(){
        Date currentDate = new Date();
        return new Timestamp(currentDate.getTime());
    }

}

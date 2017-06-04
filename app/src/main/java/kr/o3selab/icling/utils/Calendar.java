package kr.o3selab.icling.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar {

    static SimpleDateFormat sdf = new SimpleDateFormat("w");

    public static String getWeek(long date) {
        return sdf.format(new Date(date));
    }

}

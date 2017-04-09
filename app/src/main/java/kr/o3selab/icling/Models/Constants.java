package kr.o3selab.icling.Models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by O3SE Lab on 2017-04-03.
 */

public class Constants {

    // 어플리케이션 정보
    private static String APP_NAME = "ICling";
    private static double APP_VERSION = 1.0;



    // 설정정보
    private static String sData = "ICling.db";
    public static String APP_FIRST_OPEN = "first_open";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(sData, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

}

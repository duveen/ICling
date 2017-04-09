package kr.o3selab.icling.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by O3SE Lab on 2017-04-03.
 */

public class Constants {

    // 어플리케이션 정보
    private static String APP_NAME = "ICling";
    private static double APP_VERSION = 1.0;

    // 사용자 정보
    public static User user;


    // Firebase Reference
    public static String KAKAO_USER = "User/Kakao/";
    public static String GOOGLE_USER = "User/Google/";

    // 설정정보
    private static String sData = "ICling.db";
    public static String APP_FIRST_OPEN = "first_open";
    public static String APP_UUID = "app_uuid";

    public static String USER = "user";

    public static String USER_HEIGHT = "user_height";
    public static String USER_WEIGHT = "user_weight";
    public static String USER_AGE = "user_age";
    public static String USER_SEX = "user_sex";

    public static String BIKE_RADIUS = "bike_radius";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(sData, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    // 에러 리포팅
    private static StringBuilder logs = new StringBuilder();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

    public static void printLog(String message, boolean isSend) {

        StringBuilder log = new StringBuilder();
        log.append("ICling").append("(").append(sdf.format(new Date(System.currentTimeMillis()))).append(") : ").append(message);
        String result = log.toString();

        Log.d("ICling", result);
        logs.append(result).append("\n");
        if (isSend) sendReport();
    }

    public static void printLog(String message) {
        printLog(message, false);
    }

    public static void printLog(Exception e) {
        printLog(e.getMessage(), true);
    }

    private static void sendReport() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("eLogs").push();

        ErrorReport report = new ErrorReport(logs.toString());
        myRef.setValue(report);
    }

}

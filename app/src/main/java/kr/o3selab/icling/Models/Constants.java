package kr.o3selab.icling.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import kr.o3selab.icling.utils.Debug;

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

    public static String uuid = "uuid";

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


    // 장비제어
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // 파일제어
    public static boolean setConfigFile(LoginStatus status, Context context) throws IOException {
        if (isExternalStorageWritable()) {
            File root = new File(Environment.getExternalStorageDirectory(), "users");
            if (!root.exists()) root.mkdirs();
            File config = new File(root, "Config.db");
            Debug.d(config.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(status);
            byte[] data = baos.toByteArray();

            FileOutputStream fos = new FileOutputStream(config);
            fos.write(data);
            fos.close();

            Uri uri = FileProvider.getUriForFile(context, "kr.o3selab.icling.provider", config);
            context.grantUriPermission("kr.o3selab.icling.provider", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Debug.d("Completed Save Login Status File");

            return true;
        } else {
            Debug.d("Failed Save Login Status File");
            return false;
        }
    }

}

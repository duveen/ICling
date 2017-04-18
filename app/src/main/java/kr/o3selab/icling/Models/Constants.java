package kr.o3selab.icling.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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


    // 로그인 제어
    public static User setLogin(User user) {
        user.mLoginStatus = true;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User/" + user.mLoginType + "/" + user.mUUID);
        databaseReference.setValue(user);

        return user;
    }

    public static void setLogout(User user) {
        user.mLoginStatus = false;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User/" + user.mLoginType + "/" + user.mUUID);
        databaseReference.setValue(user);
    }


    // 픽셀 DP 제어
    public static int PixelToDp(Context context, int pixel) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = pixel / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    public static int DpToPixel(Context context, int DP) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DP, context.getResources().getDisplayMetrics());
        return (int) px;
    }


}

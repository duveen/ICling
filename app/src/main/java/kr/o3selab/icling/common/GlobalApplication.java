package kr.o3selab.icling.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.provider.Settings;

import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.utils.Debug;

public class GlobalApplication extends Application {

    private static volatile GlobalApplication instance = null;
    private static volatile String uuid = null;

    public static GlobalApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    public static String getUUID() {
        SharedPreferences sharedPreferences = Constants.getSharedPreferences(getGlobalApplicationContext());
        uuid = sharedPreferences.getString(Constants.uuid, null);

        if (uuid == null) {
            uuid = Settings.Secure.getString(instance.getContentResolver(), Settings.Secure.ANDROID_ID);
            SharedPreferences.Editor editor = Constants.getEditor(instance);
            editor.putString(Constants.uuid, uuid);
            editor.commit();
        }

        return uuid;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Debug.d("UUID:" + getUUID());
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}

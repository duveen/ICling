package kr.o3selab.icling.models;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.MainActivity;
import kr.o3selab.icling.activities.loaddata.LoadDataActivity;
import kr.o3selab.icling.common.GlobalApplication;

public class User implements Serializable {

    public static String KAKAO = "Kakao";
    public static String GOOGLE = "Google";

    public String mLoginType = null;
    public String mUserName = null;
    public String mUserID = null;
    public String mUserEmail = null;
    public Integer mHeight = null;
    public Integer mWeight = null;
    public Integer mAge = null;
    public Integer mSex = null;
    public Integer mRadius = null;
    public String mUserProfileImage = null;
    public Long mRegdate = null;
    public String mUUID = null;
    public Boolean mLoginStatus = null;

    public User() {
        mLoginStatus = false;
    }

    public User(String type, String name, String uid, String email) {
        super();
        mLoginType = type;
        mUserName = name;
        mUserID = uid;
        mUserEmail = email;
    }

    public boolean isData() {
        if (mHeight == null) return false;
        else return true;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        return this.mLoginType.equals(user.mLoginType) && this.mUserEmail.equals(user.mUserEmail);
    }

    @Override
    public String toString() {
        return mLoginType + ":" + mUserEmail;
    }

    public void setUserProfileImage(String url) {
        this.mUserProfileImage = url;
    }

    public static void checkUserDevice(final Activity activity, final User user, final DatabaseReference reference) {
        if (user != null && user.mUUID.equals(GlobalApplication.getUUID())) {
            Constants.user = Constants.setLogin(user);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } else if (user == null) {
            activity.startActivity(new Intent(activity, LoadDataActivity.class));
            activity.finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("알림");
            builder.setMessage("기존 가입한 기기와 다릅니다. 현재 기기로 기기를 변경하시겠습니까?");
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.default_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    user.mUUID = GlobalApplication.getUUID();
                    reference.setValue(user);

                    Constants.user = Constants.setLogin(user);
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }
            });
            builder.setNegativeButton(R.string.default_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(activity, "아이클링은 한 개의 기기에서만 실행이 가능합니다. 프로그램을 종료합니다.", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            });
            builder.show();
        }
    }

}

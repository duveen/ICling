package kr.o3selab.icling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.LoadData.LoadDataActivity;
import kr.o3selab.icling.models.Constants;

public class LoadingActivity extends AppCompatActivity {

    SharedPreferences mPreferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Constants.printLog("Loading Activity");

        mPreferences = Constants.getSharedPreferences(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Constants.printLog("Login: " + user.getEmail() + " / Provider: " + user.getProviders());

            if (user.getProviders().contains("google.com")) {

            }

            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            startActivity(new Intent(this, LoadDataActivity.class));
            this.finish();
        }

    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Constants.printLog("HashKey: " + something);
            }
        } catch (Exception e) {
            Constants.printLog(e);
        }
    }
}

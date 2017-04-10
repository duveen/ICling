package kr.o3selab.icling.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.loaddata.LoadDataActivity;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.User;

public class LoadingActivity extends AppCompatActivity {

    SharedPreferences mPreferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Constants.printLog("LoadingActivity onCreate");

        mPreferences = Constants.getSharedPreferences(this);
        mAuth = FirebaseAuth.getInstance();
        getAppKeyHash();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(authStateListener);
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Constants.printLog("LoginStatus:login");

                if (user.getProviders().contains("google.com")) {
                    for (UserInfo info : user.getProviderData()) {
                        if (!info.getProviderId().contains("google.com")) continue;

                        String uID = info.getUid();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER + uID);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Constants.user = dataSnapshot.getValue(User.class);

                                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                                LoadingActivity.this.finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                startActivity(new Intent(LoadingActivity.this, LoadDataActivity.class));
                                LoadingActivity.this.finish();
                            }
                        });
                    }
                }
            } else {
                Constants.printLog("LoginStatus:logout");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(LoadingActivity.this, LoadDataActivity.class));
                        LoadingActivity.this.finish();
                    }
                }).start();

            }
        }
    };

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

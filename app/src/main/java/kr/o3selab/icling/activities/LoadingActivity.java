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
import kr.o3selab.icling.activities.LoadData.LoadDataActivity;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.User;

public class LoadingActivity extends AppCompatActivity {

    SharedPreferences mPreferences;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Constants.printLog("Login: " + user.getEmail() + " / Provider: " + user.getProviders());

                for (UserInfo userInfo : user.getProviderData()) {
                    if (userInfo.getProviderId().equals("google.com")) {
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER + userInfo.getUid());
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    Constants.user = user;
                                    startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                                    LoadingActivity.this.finish();
                                } else {
                                    startActivity(new Intent(LoadingActivity.this, LoadDataActivity.class));
                                    LoadingActivity.this.finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            } else {
                startActivity(new Intent(LoadingActivity.this, LoadDataActivity.class));
                LoadingActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Constants.printLog("Start Loading Activity");

        mPreferences = Constants.getSharedPreferences(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuth.addAuthStateListener(mAuthStateListener);

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

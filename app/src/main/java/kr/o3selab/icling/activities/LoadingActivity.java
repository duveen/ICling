package kr.o3selab.icling.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.security.MessageDigest;
import java.util.ArrayList;

import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.loaddata.LoadDataActivity;
import kr.o3selab.icling.common.GlobalApplication;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.User;
import kr.o3selab.icling.utils.Debug;

public class LoadingActivity extends AppCompatActivity {

    SharedPreferences mPreferences;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getAppKeyHash();

        Debug.d("LoadingActivity onCreate");

        if (GlobalApplication.getUUID() == null) {
            Toast.makeText(this, "프로그램을 실행할 수 없습니다. 프로그램을 종료합니다.", Toast.LENGTH_SHORT).show();
            LoadingActivity.this.finish();
        }

        mPreferences = Constants.getSharedPreferences(this);
        mAuth = FirebaseAuth.getInstance();

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("설정 정보를 저장하기 위한 권한 입니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(authStateListener);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            mAuth.addAuthStateListener(authStateListener);
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(LoadingActivity.this, "권한이 없어 프로그램을 종료합니다.", Toast.LENGTH_SHORT).show();
            LoadingActivity.this.finish();
        }
    };

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Debug.d("LoginStatus:login");

                if (user.getProviders().contains("google.com")) {
                    for (UserInfo info : user.getProviderData()) {
                        if (!info.getProviderId().contains("google.com")) continue;

                        String uuid = GlobalApplication.getUUID();
                        if (uuid != null) {
                            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER + uuid);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    User.checkUserDevice(LoadingActivity.this, user, reference);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(LoadingActivity.this, "서버 연결 오류 입니다. 프로그램을 종료합니다.", Toast.LENGTH_LONG).show();
                                    LoadingActivity.this.finish();
                                }
                            });
                        }
                    }
                }
            } else {
                Debug.d("LoginStatus:logout");

                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER + GlobalApplication.getUUID());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            user.mLoginStatus = false;
                            databaseReference.setValue(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            LoadingActivity.this.finish();
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
                Debug.d("Hash:" + something);
            }
        } catch (Exception e) {
            Debug.e(e.getMessage());
        }
    }
}

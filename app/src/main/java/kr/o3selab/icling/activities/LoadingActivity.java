package kr.o3selab.icling.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import kr.o3selab.icling.models.LoginStatus;
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

                        String uID = info.getUid();
                        final LoginStatus loginStatus = new LoginStatus(LoginStatus.LOGIN, uID);

                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference(Constants.GOOGLE_USER + uID);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final User user = dataSnapshot.getValue(User.class);
                                if (user != null && user.mUUID.equals(GlobalApplication.getUUID())) {

                                    boolean returnValue = false;

                                    try {
                                        returnValue = Constants.setConfigFile(loginStatus, LoadingActivity.this);
                                    } catch (Exception e) {
                                        Toast.makeText(LoadingActivity.this, e.getMessage() + "데이터 저장에 실패했습니다. 프로그램을 다시 실행시켜주세요.", Toast.LENGTH_SHORT).show();
                                        LoadingActivity.this.finish();
                                    }

                                    if (returnValue) {
                                        Constants.user = user;
                                        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                                        LoadingActivity.this.finish();
                                    } else {
                                        Toast.makeText(LoadingActivity.this, "데이터 저장에 실패했습니다. 프로그램을 다시 실행시켜주세요.", Toast.LENGTH_SHORT).show();
                                        LoadingActivity.this.finish();
                                    }

                                } else if (user == null) {
                                    startActivity(new Intent(LoadingActivity.this, LoadDataActivity.class));
                                    LoadingActivity.this.finish();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoadingActivity.this);
                                    builder.setTitle("알림");
                                    builder.setMessage("기존 가입한 기기와 다릅니다. 현재 기기로 기기를 변경하시겠습니까?");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton(R.string.default_ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            user.mUUID = GlobalApplication.getUUID();
                                            reference.setValue(user);

                                            boolean returnValue = false;

                                            try {
                                                returnValue = Constants.setConfigFile(loginStatus, LoadingActivity.this);
                                            } catch (Exception e) {
                                                Toast.makeText(LoadingActivity.this, e.getMessage() + "\n데이터 저장에 실패했습니다. 프로그램을 다시 실행시켜주세요.", Toast.LENGTH_SHORT).show();
                                                LoadingActivity.this.finish();
                                            }

                                            if (returnValue) {
                                                Constants.user = user;
                                                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                                                LoadingActivity.this.finish();
                                            } else {
                                                Toast.makeText(LoadingActivity.this, "데이터 저장에 실패했습니다. 프로그램을 다시 실행시켜주세요.", Toast.LENGTH_SHORT).show();
                                                LoadingActivity.this.finish();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton(R.string.default_cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(LoadingActivity.this, "아이클링은 한 개의 기기에서만 실행이 가능합니다. 프로그램을 종료합니다.", Toast.LENGTH_SHORT).show();
                                            LoadingActivity.this.finish();
                                        }
                                    });
                                    builder.show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            } else {
                Debug.d("LoginStatus:logout");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            Constants.setConfigFile(new LoginStatus(LoginStatus.LOGOUT, null), LoadingActivity.this);
                        } catch (Exception ignored) {

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

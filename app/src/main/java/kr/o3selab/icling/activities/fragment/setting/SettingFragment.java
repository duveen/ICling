package kr.o3selab.icling.activities.fragment.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.LoadingActivity;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.common.GlobalApplication;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.models.User;

public class SettingFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);

        ButterKnife.bind(this, view);

        // addRidingData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setTitle("설정");
    }

    @OnClick(R.id.setting_profile)
    void profile() {
        ProfileFragment fragment = new ProfileFragment();
        fragment.setActivity(activity);

        activity.addFragment(fragment, true);
    }

    /*void addRidingData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RidingData ridingData = new RidingData();
                ridingData.mStartTime = System.currentTimeMillis();

                ridingData.mTotalDistance = new Random().nextFloat() * 40;
                ridingData.mRestTime = 2000L;
                ridingData.mAverageSpeed = new Random().nextFloat() * 40;
                ridingData.mMaxSpeed = new Random().nextFloat() * 40;

                ridingData.mDetailSpeed = new HashMap<>();
                for (int i = 0; i < 20; i++) {
                    ridingData.mDetailSpeed.put(String.valueOf(System.currentTimeMillis()), new Random().nextFloat());
                    Debug.d(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ridingData.mAverageHeartRate = new Random().nextFloat() * 120;
                ridingData.mMaxHeartRate = new Random().nextFloat() * 120;

                ridingData.mKcal = 123;
                ridingData.mFinishTime = System.currentTimeMillis();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserRidingData/101964580841481179586");
                reference.child(String.valueOf(ridingData.mStartTime)).setValue(ridingData);
            }
        }).start();

    }*/

    @OnClick(R.id.setting_logout)
    void logout() {
        new AlertDialog.Builder(activity)
                .setTitle("로그아웃 하시겠습니까?")
                .setPositiveButton(R.string.default_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();

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

                        startActivity(new Intent(activity, LoadingActivity.class));
                        activity.finish();
                    }
                })
                .setNegativeButton(R.string.default_cancel, null)
                .show();
    }
}

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
import com.google.firebase.auth.FirebaseUser;
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

    @OnClick(R.id.setting_logout)
    void logout() {
        new AlertDialog.Builder(activity)
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton(R.string.default_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Init Firebase DB User Info
                        User user = Constants.user;
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/" + user.mUserID);
                        user.mUUID = "";
                        reference.setValue(Constants.user);

                        // Logout Auth
                        FirebaseAuth.getInstance().signOut();

                        // Clear Static Info
                        Constants.user = null;

                        // Clear sharedPreference
                        Constants.getEditor(getContext()).clear().apply();

                        // Move Activity
                        startActivity(new Intent(activity, LoadingActivity.class));
                        activity.finish();
                    }
                })
                .setNegativeButton(R.string.default_cancel, null)
                .show();
    }
}

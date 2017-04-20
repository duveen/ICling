package kr.o3selab.icling.activities.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.LoadingActivity;
import kr.o3selab.icling.activities.fragment.setting.ProfileFragment;

public class SettingFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);

        ButterKnife.bind(this, view);

        return view;
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
                .setTitle("로그아웃 하시겠습니까?")
                .setPositiveButton(R.string.default_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(activity, LoadingActivity.class));
                        activity.finish();
                    }
                })
                .setNegativeButton(R.string.default_cancel, null)
                .show();
    }
}

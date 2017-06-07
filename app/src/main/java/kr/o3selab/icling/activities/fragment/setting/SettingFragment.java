package kr.o3selab.icling.activities.fragment.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.LoadingActivity;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.models.User;

public class SettingFragment extends BaseFragment {

    private AlertDialog bikeRadiusSettingDialog;
    private AlertDialog kcalSettingDialog;

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

    @OnClick(R.id.setting_bike)
    void bike() {
        View view = View.inflate(getContext(), R.layout.dialog_setting_bike_radius, null);

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/digital-7.ttf");

        final TextView radiusView = (TextView) view.findViewById(R.id.setting_bike_radius);
        radiusView.setText(Constants.user.mRadius.toString());
        radiusView.setTextColor(getResources().getColor(R.color.colorPrimary));
        radiusView.setTypeface(font);

        final DiscreteSeekBar seekBar = (DiscreteSeekBar) view.findViewById(R.id.setting_bike_seekbar);
        seekBar.setProgress(Constants.user.mRadius);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                radiusView.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        View okButton = view.findViewById(R.id.setting_bike_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.user.mRadius = seekBar.getProgress();
                FirebaseDatabase.getInstance().getReference("User/" + Constants.user.mUserID).setValue(Constants.user);
                bikeRadiusSettingDialog.dismiss();
            }
        });

        View cancelButton = view.findViewById(R.id.setting_bike_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bikeRadiusSettingDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        bikeRadiusSettingDialog = builder.create();
        bikeRadiusSettingDialog.show();
    }

    @OnClick(R.id.setting_kcal)
    void kcal() {
        View view = View.inflate(getContext(), R.layout.dialog_setting_kcal, null);

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/digital-7.ttf");

        final TextView goalView = (TextView) view.findViewById(R.id.setting_kcal_goal);
        goalView.setText(Constants.user.mWeekKcal.toString());
        goalView.setTextColor(getResources().getColor(R.color.colorPrimary));
        goalView.setTypeface(font);

        final DiscreteSeekBar seekBar = (DiscreteSeekBar) view.findViewById(R.id.setting_kcal_seekbar);
        seekBar.setProgress(Constants.user.mWeekKcal / 500);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                goalView.setText(String.valueOf(value * 500));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        View okButton = view.findViewById(R.id.setting_kcal_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.user.mWeekKcal = seekBar.getProgress() * 500;
                FirebaseDatabase.getInstance().getReference("User/" + Constants.user.mUserID).setValue(Constants.user);
                kcalSettingDialog.dismiss();
            }
        });

        View cancelButton = view.findViewById(R.id.setting_kcal_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kcalSettingDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        kcalSettingDialog = builder.create();
        kcalSettingDialog.show();
    }

    @OnClick(R.id.setting_distance)
    void distance() {
        View view = View.inflate(getContext(), R.layout.dialog_setting_distance, null);

        Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/digital-7.ttf");

        final TextView goalView = (TextView) view.findViewById(R.id.setting_distance_goal);
        goalView.setText(Constants.user.mWeekDistance.toString());
        goalView.setTextColor(getResources().getColor(R.color.colorPrimary));
        goalView.setTypeface(font);

        final DiscreteSeekBar seekBar = (DiscreteSeekBar) view.findViewById(R.id.setting_distance_seekbar);
        seekBar.setProgress(Constants.user.mWeekDistance / 5);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                goalView.setText(String.valueOf(value * 5));
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        View okButton = view.findViewById(R.id.setting_distance_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.user.mWeekDistance = seekBar.getProgress() * 5;
                FirebaseDatabase.getInstance().getReference("User/" + Constants.user.mUserID).setValue(Constants.user);
                kcalSettingDialog.dismiss();
            }
        });

        View cancelButton = view.findViewById(R.id.setting_distance_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kcalSettingDialog.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        kcalSettingDialog = builder.create();
        kcalSettingDialog.show();
    }
}

package kr.o3selab.icling.activities.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.BaseFragment;
import kr.o3selab.icling.common.Constants;
import kr.o3selab.icling.models.User;

public class ProfileFragment extends BaseFragment {

    View mBackButton;

    @BindView(R.id.setting_profile_imageView)
    CircleImageView profileImageView;
    @BindView(R.id.setting_profile_login)
    TextView loginView;
    @BindView(R.id.setting_profile_name)
    TextView nameView;
    @BindView(R.id.setting_profile_email)
    TextView emailView;
    @BindView(R.id.setting_profile_height)
    TextView heightView;
    @BindView(R.id.setting_profile_weight)
    TextView weightView;
    @BindView(R.id.setting_profile_age)
    TextView ageView;
    @BindView(R.id.setting_profile_sex)
    TextView sexView;
    @BindView(R.id.setting_profile_radius)
    TextView radiusView;
    @BindView(R.id.setting_profile_regdate)
    TextView regdateView;

    @BindView(R.id.setting_profile_unregister)
    View unregisterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_profile, null);
        ButterKnife.bind(this, view);
        TAG = "ProfileFragment";

        User user = Constants.user;
        if (user.mUserProfileImage != null)
            Picasso.with(activity).load(user.mUserProfileImage).into(profileImageView);
        loginView.setText(user.mLoginType);
        nameView.setText(user.mUserName);
        emailView.setText(user.mUserEmail);
        heightView.setText(String.valueOf(user.mHeight));
        weightView.setText(String.valueOf(user.mWeight));
        ageView.setText(String.valueOf(user.mAge));
        if (user.mSex == 0) sexView.setText("남자");
        else sexView.setText("여자");
        radiusView.setText(String.valueOf(user.mRadius));
        regdateView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(user.mRegdate)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        enableBackButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBackButton.setVisibility(View.GONE);
        mBackButton.setOnClickListener(null);
    }

    void enableBackButton() {
        mBackButton = activity.getBackButton();
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeFragment();
            }
        });
        activity.setTitle("프로필");
    }
}


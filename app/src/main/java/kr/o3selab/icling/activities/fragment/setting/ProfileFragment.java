package kr.o3selab.icling.activities.fragment.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_profile, null);
        ButterKnife.bind(this, view);
        TAG = "ProfileFragment";

        User user = Constants.user;
        Picasso.with(activity).load(user.mUserProfileImage).into(profileImageView);

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


package kr.o3selab.icling.activities.fragment;

import android.support.v4.app.Fragment;

import kr.o3selab.icling.activities.MainActivity;

public abstract class BaseFragment extends Fragment {

    public MainActivity activity = null;

    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }

}

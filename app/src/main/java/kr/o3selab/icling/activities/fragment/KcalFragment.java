package kr.o3selab.icling.activities.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.o3selab.icling.R;

/**
 * Created by O3SE Lab on 2017-04-18.
 */

public class KcalFragment extends Fragment {

    public KcalFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kcal, null);

        return view;
    }
}

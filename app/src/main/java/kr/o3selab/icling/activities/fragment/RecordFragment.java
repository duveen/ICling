package kr.o3selab.icling.activities.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.record.RecordItemFragment;
import kr.o3selab.icling.models.Constants;
import kr.o3selab.icling.models.RidingData;

public class RecordFragment extends BaseFragment {

    @BindView(R.id.record_list)
    LinearLayout mItemListLayout;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("hh:mm a");

    private LayoutInflater mInflater;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record, null);
        ButterKnife.bind(this, view);

        mInflater = inflater;

        if (Constants.mTotalData == null)
            FirebaseDatabase.getInstance().getReference("UserRidingData/" + Constants.user.mUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Constants.mTotalData = dataSnapshot;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        getData();

        return view;
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (Constants.mTotalData != null) break;

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {

                    }
                }

                int flag = 0;

                List<DataSnapshot> lists = new LinkedList<>();
                for (DataSnapshot snapshot : Constants.mTotalData.getChildren())
                    lists.add(snapshot);

                Collections.reverse(lists);

                for (DataSnapshot snapshot : lists) {
                    final int iFlag = flag++;
                    final RidingData data = snapshot.getValue(RidingData.class);

                    final View item = mInflater.inflate(R.layout.fragment_record_list_item, null);

                    TextView dateView = (TextView) item.findViewById(R.id.record_list_item_date);
                    dateView.setText(mDateFormat.format(new Date(data.mStartRegdate)));

                    TextView timeView = (TextView) item.findViewById(R.id.record_list_item_time);
                    timeView.setText(mTimeFormat.format(new Date(data.mStartRegdate)));

                    TextView distanceView = (TextView) item.findViewById(R.id.record_list_item_d_distance);
                    distanceView.setText(data.mTotalDistance.toString());

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RecordItemFragment fragment = new RecordItemFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("item", data);
                            fragment.setArguments(bundle);
                            fragment.setActivity(activity);
                            activity.addFragment(fragment, true);
                        }
                    });

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mItemListLayout.addView(item, iFlag);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setTitle("주행기록");
    }


}

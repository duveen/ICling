package kr.o3selab.icling.activities.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.o3selab.icling.R;
import kr.o3selab.icling.activities.fragment.record.RecordItemFragment;
import kr.o3selab.icling.models.RidingData;

public class RecordFragment extends BaseFragment {

    @BindView(R.id.record_list)
    private LinearLayout mItemListLayout;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("hh:mm a");

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_record, null);
        ButterKnife.bind(this, view);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserRidingData/" + user.getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int childrenCount = (int) dataSnapshot.getChildrenCount();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RidingData data = snapshot.getValue(RidingData.class);
                    View item = inflater.inflate(R.layout.fragment_record_list_item, null);

                    TextView dateView = (TextView) item.findViewById(R.id.record_list_item_date);
                    dateView.setText(mDateFormat.format(new Date(data.mStartRegdate)));

                    TextView timeView = (TextView) item.findViewById(R.id.record_list_item_time);
                    timeView.setText(mTimeFormat.format(new Date(data.mStartRegdate)));

                    TextView distanceView = (TextView) item.findViewById(R.id.record_list_item_d_distance);
                    distanceView.setText(data.mTotalDistance.toString());

                    mItemListLayout.addView(item, childrenCount--);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        View item = view.findViewById(R.id.record_item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordItemFragment fragment = new RecordItemFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", new RidingData());
                fragment.setArguments(bundle);
                fragment.setActivity(activity);
                activity.addFragment(fragment, true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        activity.setTitle("주행기록");
    }
}

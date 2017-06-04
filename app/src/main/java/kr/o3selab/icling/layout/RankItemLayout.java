package kr.o3selab.icling.layout;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.o3selab.icling.R;
import kr.o3selab.icling.models.RaceRecord;
import kr.o3selab.icling.models.RidingData;
import kr.o3selab.icling.models.User;

public class RankItemLayout extends LinearLayout {

    @BindView(R.id.ranking_item_image)
    ImageView rankImageView;
    @BindView(R.id.ranking_item_text)
    TextView rankTextView;

    @BindView(R.id.ranking_item_profile)
    CircleImageView profileImageView;
    @BindView(R.id.ranking_item_name)
    FontFitTextView nameTextView;
    @BindView(R.id.ranking_item_time)
    FontFitTextView timeTextView;

    Context mContext;

    public RankItemLayout(Context context) {
        super(context);
        mContext = context;
    }

    public RankItemLayout(Context context, int rank, RaceRecord raceRecord) {
        super(context);
        View view = inflate(context, R.layout.layout_rank_item, this);

        ButterKnife.bind(this, view);

        // 랭킹순위 표시
        switch (rank) {
            default:
                rankTextView.setVisibility(VISIBLE);
                rankTextView.setText(String.valueOf(rank));
                break;

            case 1:
                rankImageView.setVisibility(VISIBLE);
                rankImageView.setImageResource(R.drawable.rank_1);
                nameTextView.setTextColor(getResources().getColor(R.color.rank1));
                timeTextView.setTextColor(getResources().getColor(R.color.rank1));
                break;

            case 2:
                rankImageView.setVisibility(VISIBLE);
                rankImageView.setImageResource(R.drawable.rank_2);
                nameTextView.setTextColor(getResources().getColor(R.color.rank2));
                timeTextView.setTextColor(getResources().getColor(R.color.rank2));
                break;

            case 3:
                rankImageView.setVisibility(VISIBLE);
                rankImageView.setImageResource(R.drawable.rank_3);
                nameTextView.setTextColor(getResources().getColor(R.color.rank3));
                timeTextView.setTextColor(getResources().getColor(R.color.rank3));
                break;

        }

        long recordTime = (long) (raceRecord.getmTime() * 1000);
        RidingData.RTime time = RidingData.RTime.getTime(recordTime);
        timeTextView.setText(time.toStringWithMillisecond());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/" + raceRecord.getmUserId());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user.mUserProfileImage != null)
                    Picasso.with(getContext()).load(user.mUserProfileImage).into(profileImageView);
                nameTextView.setText(user.mUserName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}

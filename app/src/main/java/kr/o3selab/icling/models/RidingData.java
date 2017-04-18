package kr.o3selab.icling.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class RidingData implements Serializable {

    public Long mRegdate = System.currentTimeMillis();
    public Float mTotalDistance;
    public Float mAverageSpeed;
    public Float mMaxSpeed;
    public ArrayList<Float> mDetailSpeed;

    public Float mAverageHeartbeat;

    public RidingData() {
        mTotalDistance = 0.0f;
        mAverageSpeed = 0.0f;
        mMaxSpeed = 0.0f;
        mDetailSpeed = new ArrayList<>();

        Random r = new Random();
        Float totalSpeed = 0.0f;
        for (int i = 0; i < 10; i++) {
            Float speed = (float) (10 * i + r.nextInt(10));
            totalSpeed += speed;
            if (speed > mMaxSpeed) mMaxSpeed = speed;
            mDetailSpeed.add(speed);
        }

        mAverageSpeed = totalSpeed / 100;
    }
}

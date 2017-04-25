package kr.o3selab.icling.models;

import java.io.Serializable;
import java.util.ArrayList;

public class RidingData implements Serializable {

    public Long mStartRegdate;
    public Long mFinishRegdate;

    public Float mTotalDistance;
    public Float mAverageSpeed;
    public Float mMaxSpeed;

    public ArrayList<Float> mDetailSpeed;
    public Float mAverageHeartRate;
    public Float mTotalHeartRate;
    public int mKcal;

    public RidingData() {
        mStartRegdate = System.currentTimeMillis();
        mTotalDistance = 0.0f;
        mAverageSpeed = 0.0f;
        mMaxSpeed = 0.0f;
        mDetailSpeed = new ArrayList<>();

        mAverageHeartRate = 0.0f;
        mTotalHeartRate = 0.0f;
    }

    public void addData(String speed, String distance) {
        mDetailSpeed.add(Float.valueOf(speed));
        mTotalDistance += Float.valueOf(distance);
    }

    public void addData(String speed, String distance, String heartrate) {
        addData(speed, distance);
        mTotalHeartRate += Float.valueOf(heartrate);
    }

    public void finishRiding(String weight) {
        mFinishRegdate = System.currentTimeMillis();

        int dataSize = mDetailSpeed.size();

        // 최대 속도 / 평균속도 계산
        Float totalSpeed = 0.0f;
        for (Float val : mDetailSpeed) {
            if (mMaxSpeed < val) mMaxSpeed = val;
            totalSpeed += val;
        }

        mAverageSpeed = totalSpeed / dataSize;
        mAverageHeartRate = mTotalHeartRate / dataSize;


    }

    public int getKcalCoefficient(Float aSpeed) {

        return 0;


    }
}

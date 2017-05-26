package kr.o3selab.icling.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class RidingData implements Serializable {

    public Long mStartTime;
    public Long mRestTime;
    public Long mFinishTime;

    public Double mTotalDistance;

    public Double mAverageSpeed;
    public Double mMaxSpeed;
    public HashMap<String, Double> mDetailSpeed;

    public Integer mAverageHeartRate;
    public Integer mMaxHeartRate;

    public int mKcal;

    public RidingData() {

    }

    public RTime getTotalTime() {
        return RTime.getTime(mFinishTime - mStartTime);
    }

    public RTime getRidingTime() {
        return RTime.getTime(mFinishTime - mRestTime - mStartTime);
    }

    public RTime getRestTime() {
        return RTime.getTime(mRestTime);
    }

    public String getAveragePace() {
        RTime pace = RTime.getTime((long) (3600000L / mAverageSpeed));
        return RTime.format(pace.m) + "'" + RTime.format(pace.s) + "\"";
    }

    public String getMaxPace() {
        RTime pace = RTime.getTime((long) (3600000L / mMaxSpeed));
        return RTime.format(pace.m) + "'" + RTime.format(pace.s) + "\"";
    }

    public static byte[] getBytes(RidingData ridingData) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(ridingData);

        return baos.toByteArray();
    }

    public static RidingData getRidingData(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);

        return (RidingData) ois.readObject();
    }

    @Override
    public boolean equals(Object obj) {
        RidingData data = (RidingData) obj;
        return this.mStartTime.equals(data.mStartTime);
    }


    public static class RTime {
        public int h;
        public int m;
        public int s;

        public static RTime getTime(long distanceTime) {
            RTime r = new RTime();
            distanceTime /= 1000;
            r.h = (int) (distanceTime / 3600);
            distanceTime %= 3600;
            r.m = (int) (distanceTime / 60);
            distanceTime %= 60;
            r.s = (int) distanceTime;
            return r;
        }

        public static String format(int value) {
            return String.format("%02d", value);
        }

        @Override
        public String toString() {
            return String.format("%02d", h) + ":" + String.format("%02d", m) + ":" + String.format("%02d", s);
        }
    }

    public Long getmStartTime() {
        return mStartTime;
    }

    public void setmStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public Long getmRestTime() {
        return mRestTime;
    }

    public void setmRestTime(long mRestTime) {
        this.mRestTime = mRestTime;
    }

    public Long getmFinishTime() {
        return mFinishTime;
    }

    public void setmFinishTime(long mFinishTime) {
        this.mFinishTime = mFinishTime;
    }

    public Double getmTotalDistance() {
        return mTotalDistance;
    }

    public void setmTotalDistance(double mTotalDistance) {
        this.mTotalDistance = mTotalDistance;
    }

    public Double getmAverageSpeed() {
        return mAverageSpeed;
    }

    public void setmAverageSpeed(double mAverageSpeed) {
        this.mAverageSpeed = mAverageSpeed;
    }

    public Double getmMaxSpeed() {
        return mMaxSpeed;
    }

    public void setmMaxSpeed(double mMaxSpeed) {
        this.mMaxSpeed = mMaxSpeed;
    }

    public HashMap<String, Double> getmDetailSpeed() {
        return mDetailSpeed;
    }

    public void setmDetailSpeed(HashMap<String, Double> mDetailSpeed) {
        this.mDetailSpeed = mDetailSpeed;
    }

    public Integer getmAverageHeartRate() {
        return mAverageHeartRate;
    }

    public void setmAverageHeartRate(int mAverageHeartRate) {
        this.mAverageHeartRate = mAverageHeartRate;
    }

    public Integer getmMaxHeartRate() {
        return mMaxHeartRate;
    }

    public void setmMaxHeartRate(int mMaxHeartRate) {
        this.mMaxHeartRate = mMaxHeartRate;
    }

    public Integer getmKcal() {
        return mKcal;
    }

    public void setmKcal(int mKcal) {
        this.mKcal = mKcal;
    }
}

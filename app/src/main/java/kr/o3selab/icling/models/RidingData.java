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
    public HashMap<String, Integer> mDetailHeartRate;

    public int mKcal;
    public HashMap<String, Integer> mDetailKcal;

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
        if (mAverageSpeed == 0) return "00'00\"";
        return RTime.format(pace.m) + "'" + RTime.format(pace.s) + "\"";
    }

    public String getMaxPace() {
        RTime pace = RTime.getTime((long) (3600000L / mMaxSpeed));
        if (mMaxSpeed == 0) return "00'00\"";
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

        public long time;
        public int h;
        public int m;
        public int s;
        public int ms;
        // m:s.ms

        public static RTime getTime(long distanceTime) {
            RTime r = new RTime();
            r.time = distanceTime;
            r.h = (int) (distanceTime / 3600000);
            distanceTime %= 3600000;
            r.m = (int) (distanceTime / 60000);
            distanceTime %= 60000;
            r.s = (int) (distanceTime / 1000);
            distanceTime %= 1000;
            r.ms = (int) distanceTime;
            return r;
        }

        public static String format(int value) {
            return String.format("%02d", value);
        }

        @Override
        public String toString() {
            return format(h) + ":" + format(m) + ":" + format(s);
        }

        public String toStringWithMillisecond() {
            return format(m) + ":" + format(s) + "." + String.format("%03d", ms);
        }

    }
}

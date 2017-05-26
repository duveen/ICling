package kr.o3selab.icling.utils;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.o3selab.icling.models.ErrorReport;

/**
 * Created by O3SE Lab on 2017-04-12.
 */

public class Debug {

    private static final String TAG = "ICling";
    private static boolean DEBUG = true;

    // 에러 리포팅
    private static final StringBuilder logs = new StringBuilder();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");

    // Error
    public static void e(String message) {
        String msg = buildMsg(message);
        if (DEBUG) Log.e(TAG, msg);
        sendErrorReportToFirebaseServer();
    }

    // Warning
    public static void w(String message) {
        String msg = buildMsg(message);
        if (DEBUG) Log.w(TAG, msg);
    }

    // Information
    public static void i(String message) {
        String msg = buildMsg(message);
        if (DEBUG) Log.i(TAG, msg);
    }

    // Debug
    public static void d(String message) {
        String msg = buildMsg(message);
        if (DEBUG) Log.d(TAG, msg);
    }

    public static void d(int value) {
        String msg = buildMsg(String.valueOf(value));
        if (DEBUG) Log.d(TAG, msg);
    }

    // Verbose
    public static void v(String message) {
        String msg = buildMsg(message);
        if (DEBUG) Log.v(TAG, msg);
    }

    private static String buildMsg(String message) {

        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];

        StringBuilder log = new StringBuilder();
        log.append("[");
        log.append(TAG);
        log.append("$");
        log.append(stackTraceElement.getFileName().replace(".java", ""));
        log.append("$");
        log.append(stackTraceElement.getMethodName());
        log.append("$");
        log.append(sdf.format(new Date(System.currentTimeMillis())));
        log.append("] ");
        log.append(message);

        logs.append(log.toString()).append("\n");

        return log.toString();
    }

    private static void sendErrorReportToFirebaseServer() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("eLogs").push();

        ErrorReport report = new ErrorReport(logs.toString());
        myRef.setValue(report);
    }

}

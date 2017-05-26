package kr.o3selab.icling.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.Vector;

import kr.o3selab.icling.models.RidingData;

public class DBHelper extends SQLiteOpenHelper {

    public static volatile DBHelper instance;

    public static DBHelper getInstance(Context context) {
        if (instance == null) instance = new DBHelper(context, "RidingData.db", null, 1);
        return instance;
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE riding (_id INTEGER PRIMARY KEY AUTOINCREMENT, regdate TEXT, item BLOB);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(RidingData item) {
        byte[] itemBytes = null;

        try {
            itemBytes = RidingData.getBytes(item);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (itemBytes == null) return false;

        SQLiteDatabase db = getWritableDatabase();

        String sql = "INSERT INTO riding VALUES(?,?,?)";
        SQLiteStatement insertStmt = db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindNull(1);
        insertStmt.bindString(2, String.valueOf(item.mStartTime));
        insertStmt.bindBlob(3, itemBytes);
        insertStmt.execute();

        db.close();
        return true;
    }

    public void remove() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM riding;");
    }

    public boolean findRidingData(String time) {
        boolean flag = false;

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM riding WHERE regdate = '" + time + "';";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) flag = time.equals(cursor.getString(1));

        cursor.close();
        return flag;
    }

    public Vector<RidingData> getRidingData() {

        Vector<RidingData> datas = new Vector<>();

        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM riding order by regdate desc";
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            try {
                datas.add(RidingData.getRidingData(cursor.getBlob(2)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cursor.close();

        return datas;
    }
}

package biz.dealnote.powercodetestapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int V = 1;
    private static volatile DbHelper instance;

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DbHelper.class) {
                if (instance == null) {
                    instance = new DbHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private DbHelper(Context context) {
        super(context, "recent.sqlite", null, V);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS [" + RecentColumns.TABLENAME + "] (\n" +
                "  [" + RecentColumns._ID + "] INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "  [" + RecentColumns.PATH + "] INTEGER, " +
                "  [" + RecentColumns.CREATE_DATETIME + "] INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
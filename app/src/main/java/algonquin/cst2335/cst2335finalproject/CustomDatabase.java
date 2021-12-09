package algonquin.cst2335.finalprojectassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class CustomDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "finalProjectAssignment.db";
    private static final String TABLE_NAME = "Save";
    private static final String KEY_ID = "id";
    private static final String KEY_DEFINITION = "definition";
    String TAG = "theS";

    public CustomDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + "(" +
                        KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        KEY_DEFINITION + " TEXT);";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public long save(String definition) {
        if (definition.contains("'")) {
            definition = definition.replace("'", "");
        }
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEFINITION, definition);
        long result = db.insert(TABLE_NAME, null, values);
        Log.d(TAG, "save: " + definition + " " + result);
        return result;
    }

    public HashMap<String, Long> readAll() {
        HashMap<String, Long> list = new HashMap<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.put(cursor.getString(1), cursor.getLong(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public long read(String def) {
        if (def.contains("'")) {
            def = def.replace("'", "");
        }
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY_DEFINITION + "= '" + def + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        long id = -1;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getLong(0);
            } while (cursor.moveToNext());
        }
        Log.d(TAG, "read: " + def + " " + id);
        return id;
    }

    public boolean remove(long id) {
        Log.d(TAG, "remove: " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
    }
}


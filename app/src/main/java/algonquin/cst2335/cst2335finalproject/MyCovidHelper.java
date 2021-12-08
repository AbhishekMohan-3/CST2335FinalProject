package algonquin.cst2335.cst2335finalproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyCovidHelper extends SQLiteOpenHelper {

    public static final String name = "TheCovidDatabase";
    public static final int version = 3;
    public static final String TABLE_NAME = "SavedDates";

    public MyCovidHelper(Context context) {
        super(context, name, null, version);
    }
    public static final String col_date = "Date";
    public static final String col_cases = "Cases";
    public static final String col_fatalities = "Fatalities";
    public static final String col_hospitalizations = "Hospitalizations";
    public static final String col_vaccinations = "Vaccinations";
    public static final String col_recoveries = "Recoveries";

    @Override
    public void onCreate(SQLiteDatabase db) {
       /* db.execSQL("Create table "+ TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_date + "TEXT,"
                + col_cases + "INTEGER,"
                + col_fatalities + "INTEGER,"
                + col_hospitalizations +"INTEGER,"
                + col_vaccinations + "INTEGER,"
                + col_recoveries+ "INTEGER);");*/
        db.execSQL("Create table "+ TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + col_date + " TEXT,"
                + col_cases +" INTEGER," + col_fatalities +" INTEGER," + col_hospitalizations +" INTEGER," + col_vaccinations +" INTEGER," + col_recoveries+ " INTEGER);") ;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}

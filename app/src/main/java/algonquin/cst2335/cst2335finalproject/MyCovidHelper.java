package algonquin.cst2335.cst2335finalproject;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 *The class used to create the database and the tables to store the records saved by users
 * @author Abhishek Mohan
 * @version 1.0
 *
 */


public class MyCovidHelper extends SQLiteOpenHelper {

    /**
     * declaring variables conatining name of database, version, and table name
     */

    public static final String name = "TheCovidDatabase";
    public static final int version = 3;
    public static final String TABLE_NAME = "SavedDates";
    /**
     * Class MyCovidHelper
     * @param context
     */
    public MyCovidHelper(Context context) {
        super(context, name, null, version);
    }
    /**Varaible storing names of the columns of database */
    public static final String col_date = "Date";
    public static final String col_cases = "Cases";
    public static final String col_fatalities = "Fatalities";
    public static final String col_hospitalizations = "Hospitalizations";
    public static final String col_vaccinations = "Vaccinations";
    public static final String col_recoveries = "Recoveries";

    @Override
    /**
     *Creating database using SQl database creation query
     * @param db: the database object which we are using*/
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
    /**Class to help in case upgrading the database is required
     * @param db: the database object
     * @param oldVersion: the old version of database
     * @param newVersion: int representing new version of the database
     * */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}

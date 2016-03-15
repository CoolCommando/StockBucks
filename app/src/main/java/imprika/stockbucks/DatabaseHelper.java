package imprika.stockbucks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME= "SBDatabase";
    public static final String TABLE_NAME= "Notifications";
    public static final String FIRST_COLUMN= "Date";
    public static final String SECOND_COLUMN= "Message";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (Date String, Message String )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Method to insert data into the table.

    public boolean insertData(String date, String message){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table if not exists " + TABLE_NAME + " (Date String, Message String )");
        ContentValues contentValues = new ContentValues();
        contentValues.put(FIRST_COLUMN, date);
        contentValues.put(SECOND_COLUMN, message);
        long result= db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }
        else{
        return true; }
    }

    public Cursor getData(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table if not exists " + TABLE_NAME + " (Date String, Message String )");
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME, null);

        return res;
    }

    public boolean deleteTable(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        return true;
    }
}

package com.example.liuqimin.lifary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by User on 2015/8/1.
 */
public class DiaryDBHandler extends SQLiteOpenHelper {

    private static final String DEBUG = "Lifary";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "diaryDB.db";
    private static final String TABLE_NAME = "diary";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_LATITUDE = "latitude";

    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_SHARE = "share";
    public static final String COLUMN_IMAGE = "img";

    public static final String COLUMN_SOUND = "sound";
    public static final String COLUMN_DATE = "date";

    /*
    * diary:
    * 0:COLUMN_ID, 1:COLUMN_TEXT, 2:COLUMN_LATITUDE, 3:COLUMN_LONGITUDE,
    *           4:COLOMN_SHARE, 5:COLOMN_DATE, 6:COLUMN_IMAGE, 7:COLUMN_SOUND
    *
    * */


    public DiaryDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DiaryDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TEXT
                + " TEXT," + COLUMN_LATITUDE + " REAL, " + COLUMN_LONGITUDE
                +" REAL,"+ COLUMN_SHARE + " INTEGER," + COLUMN_DATE + " Text, "+ COLUMN_IMAGE+" BLOB,"+ COLUMN_SOUND + " BLOB " +")";
        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

   public void addDiary(Diary diary){
       ContentValues values = new ContentValues();
       values.put(COLUMN_DATE, diary.getDate());
       values.put(COLUMN_TEXT, diary.getContent());
       values.put(COLUMN_IMAGE, diary.getImg());
       values.put(COLUMN_SOUND, diary.getAudio());
       values.put(COLUMN_LATITUDE, diary.getLatitude());
       values.put(COLUMN_LONGITUDE, diary.getLongitude());
       values.put(COLUMN_SHARE, diary.getShare());

       SQLiteDatabase db = this.getWritableDatabase();
       db.insert(TABLE_NAME, null, values);
       db.close();
       Log.d("Lifary", "Diary Database added");
   }

    public Diary findDiaryByID(int id){
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Diary diary = new Diary(0);

        if (cursor.moveToFirst()) {
            Log.d("Lifary","cursor success: ");
            cursor.moveToFirst();
            Log.d("Lifary", "moves to first: ");
            diary.setId(Integer.parseInt(cursor.getString(0)));

            Log.d("Lifary", "set id ");
            diary.addContents(cursor.getString(1));
            Log.d("Lifary", "add contents ");
            diary.addLocation(cursor.getFloat(2), cursor.getFloat(3));
            Log.d("Lifary", "add location ");
            diary.setShare(cursor.getInt(4));
            Log.d("Lifary", "set share ");
            diary.setDate(cursor.getString(5));
            Log.d("Lifary", "set date ");
            diary.setImageByByte(cursor.getBlob(6));

            Log.d("Lifary", "add image: ");
            diary.setAudioByte(cursor.getBlob(7));
            Log.d("Lifary", "add audio");
            cursor.close();
            Log.d("Lifary", "close");
        } else {
            diary = null;
        }
        db.close();
        return diary;
    }

    public Diary findDiaryByTime(String da){
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + " = \"" + da + "\"";

        Log.d("Lifary", "SQL db");
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("Lifary", "SQL cursor");
        Cursor cursor = db.rawQuery(query, null);

        Log.d("Lifary", "SQL cursor done");
        Diary diary = new Diary(0);
        if ( cursor.moveToFirst()) {

            Log.d("Lifary","cursor success: ");
            cursor.moveToFirst();
            Log.d("Lifary", "moves to first: ");
            diary.setId(Integer.parseInt(cursor.getString(0)));

            Log.d("Lifary", "set id , id = " + diary.getId());
            diary.addContents(cursor.getString(1));
            Log.d("Lifary", "add contents, contents =   " + diary.getContent());
            diary.addLocation(cursor.getFloat(2), cursor.getFloat(3));
            Log.d("Lifary", "add location ");
            diary.setShare(cursor.getInt(4));
            Log.d("Lifary", "set share ");
            diary.setDate(cursor.getString(5));
            Log.d("Lifary", "set date , date == " + diary.getDate());
            diary.setImageByByte(cursor.getBlob(6));
            diary.setAudioByte(cursor.getBlob(7));
            cursor.close();
            Log.d("Lifary", "cursor close");
            } else {
                diary = null;
                Log.d(DEBUG, "cannot find diary by time");
            }
        db.close();
        return diary;
    }

    public  boolean deleteDiary(int id){
       boolean result = false;
        String query = "Select * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Diary d= new Diary(0);

        if (cursor.moveToFirst()) {
            do {
                d.setId(Integer.parseInt(cursor.getString(0)));
                db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(d.getId())});
                cursor.close();
                result = true;
            }while (cursor.moveToNext());
        }
        db.close();
        return result;
    }
}

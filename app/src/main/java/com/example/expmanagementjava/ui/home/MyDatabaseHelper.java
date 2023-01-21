package com.example.expmanagementjava.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Filter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "ExpManager.db";
    private static final int DATABASE_VERSION = 1;

    private  static final String TABLE_NAME = "history";
    private  static final String COLUMN_ID = "_id";
    private  static final String COLUMN_DATE = "_today_date";
    private  static final String COLUMN_CATEGORY = "_category";
    private  static final String COLUMN_NAME = "_name";
    private  static final String COLUMN_AMT = "_amount";
    private  static final String COLUMN_AMT_REMAINING = "_amt_remaining";
    private  static final String COLUMN_ADDORSUB = "_amt_add_or_sub";

    private static final String TABLE_2_NAME = "savings";
    private static final String COL_2_ID = "sav_id";
    private static final String COL_2_NAME = "sav_month";
    private static final String COL_2_YEAR = "sav_year";
    private static final String COL_2_AMT = "sav_amt";
    private static final String COL_2_AMT_REM = "sav_amt_remaining";
    private static final String COL_2_ADDORSUB = "sav_amt_add_or_sub";

    // Singleton Support
    private static MyDatabaseHelper mInstance = null ;
    public MyDatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static MyDatabaseHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new MyDatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    // Table Creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " REAL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_AMT + " INTEGER, " +
                COLUMN_AMT_REMAINING + " INTEGER, " +
                COLUMN_ADDORSUB + " INTEGER);";

        String query_2_sav = "CREATE TABLE " + TABLE_2_NAME +
                " (" + COL_2_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2_NAME + " TEXT, " +
                COL_2_YEAR + " TEXT, " +
                COL_2_AMT + " INTEGER, " +
                COL_2_AMT_REM + " INTEGER, " +
                COL_2_ADDORSUB + " INTEGER);";


        db.execSQL(query);
        db.execSQL(query_2_sav);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_2_NAME);
        onCreate(db);

    }

    // Function to Insert records to the Transactions table
    void addTransaction(String currentDate, String transCategory, String transName,
                        Integer transAmt, Integer amtRemaining, Integer addOrSub){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE, currentDate);
        cv.put(COLUMN_CATEGORY, transCategory);
        cv.put(COLUMN_NAME, transName);
        cv.put(COLUMN_AMT, transAmt);
        cv.put(COLUMN_AMT_REMAINING, amtRemaining);
        cv.put(COLUMN_ADDORSUB,addOrSub);
        long result = db.insert(TABLE_NAME,null,cv);

        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to Insert records to the Savings table
    void addSavings(String savMonth, String savYear, Integer savAmt, Integer savAmtRemaining, Integer savAddOrSub){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_2_NAME, savMonth);
        cv.put(COL_2_YEAR, savYear);
        cv.put(COL_2_AMT, savAmt);
        cv.put(COL_2_AMT_REM, savAmtRemaining);
        cv.put(COL_2_ADDORSUB, savAddOrSub);
        long result = db.insert(TABLE_2_NAME,null,cv);

        if(result == -1){
            Toast.makeText(context, "Failed to update Savings", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Savings Updated", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to Delete all Records (from both the Transactions table and Savings table)
    public void  deleteAllRecords(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME + ";");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + TABLE_NAME + "' ; ");

        db.execSQL("DELETE FROM " + TABLE_2_NAME + ";");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + TABLE_2_NAME + "' ; ");

        db.close();
    }

    // Function to delete single record
    public void delSingleRecord(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + "= '" + title + "'");
        //db.delete(TABLE_NAME, COLUMN_NAME + "=?", new String[])
    }

    // Function to retrive the Amount Remaining from the Last Record of the Savings Table
    public int savGetLastRecDataofAmtRemaining(){
        int savAmtRem = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + COL_2_AMT_REM + " from " + TABLE_2_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getInt(res.getColumnIndex(COL_2_AMT_REM)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            savAmtRem = arrayList.get(arrayList.size() - 1);
        res.close();
        return savAmtRem;
    }

    // 7 Functions to retrieve the Last Record's various attributes each from the Transactions table

    public int getLastRecDataofAmtRemaining(){
        int amtRem = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + COLUMN_AMT_REMAINING + " from " + TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getInt(res.getColumnIndex(COLUMN_AMT_REMAINING)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            amtRem = arrayList.get(arrayList.size() - 1);
        res.close();
        return amtRem;
    }

    public int getLastRecDataofAmount(){
        int amt = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + COLUMN_AMT + " from " + TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getInt(res.getColumnIndex(COLUMN_AMT)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            amt = arrayList.get(arrayList.size() - 1);
        res.close();
        return amt;
    }

    public int getLastRecID(){
        int ID = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + COLUMN_ID + " from " + TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getInt(res.getColumnIndex(COLUMN_ID)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            ID = arrayList.get(arrayList.size() - 1);
        res.close();
        return ID;
    }

    public String getLastRecName(){
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor res = db.rawQuery("select " + COLUMN_NAME + " from " + TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            name = arrayList.get(arrayList.size() - 1);
        res.close();
        return name;
    }

    public String getLastRecDate(){
        String date = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor res = db.rawQuery("select " + COLUMN_DATE + " from " + TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getString(res.getColumnIndex(COLUMN_DATE)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            date = arrayList.get(arrayList.size() - 1);
        res.close();
        return date;
    }

    public String getLastRecCategory(){
        String category = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor res = db.rawQuery("select " + COLUMN_CATEGORY + " from " + TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getString(res.getColumnIndex(COLUMN_CATEGORY)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            category = arrayList.get(arrayList.size() - 1);
        res.close();
        return category;
    }

    public int getLastRecAddOrSub(){
        int addorsub = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + COLUMN_ADDORSUB + " from " + TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            arrayList.add(res.getInt(res.getColumnIndex(COLUMN_ADDORSUB)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            addorsub = arrayList.get(arrayList.size() - 1);
        res.close();
        return addorsub;
    }

    // Function to real All Data from the Savings table
    public Cursor savReadAllData(){
        String query = "SELECT * FROM " + TABLE_2_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    // Function to read All Data from the Transactions table
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
}

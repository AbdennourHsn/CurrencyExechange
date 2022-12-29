package com.example.mcd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import static android.content.ContentValues.TAG;

class DatabeseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Exechange.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "currencies";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CurrencyName= "currencyName";
    private static final String COLUMN_Price = "price";
    private static final String COLUMN_Time = "updateDate";

    DatabeseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CurrencyName + " TEXT, " +
                COLUMN_Price + " TEXT, " +
                COLUMN_Time + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addCurrency(String CurrencyName, Double Price, String Time){
        if(currencyExist(CurrencyName)) ;
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_CurrencyName, CurrencyName);
            cv.put(COLUMN_Price, Price);
            cv.put(COLUMN_Time, Time);
            long result = db.insert(TABLE_NAME, null, cv);
            if (result == -1) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "addCurrency: Added");
                Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean currencyExist(String name){
        Cursor cursor = readRow(name);
        cursor.moveToFirst();

        if(cursor.getCount() == 0) return  false;
        return  true;
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    Cursor readRow(String name){
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE currencyName  ="+"'"+name+"' " ;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
            //cursor.moveToNext();

        }
        return cursor;
    }

    void updateData(int row_id, String CurrencyName, double Price, String Time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CurrencyName, CurrencyName);
        cv.put(COLUMN_Price, Price);
        cv.put(COLUMN_Time, Time);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{""+row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
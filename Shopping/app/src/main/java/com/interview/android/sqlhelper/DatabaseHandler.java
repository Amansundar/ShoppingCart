package com.interview.android.sqlhelper;
import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.interview.android.dto.ProductObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class DatabaseHandler extends SQLiteOpenHelper {


    private Context mContext;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "shoppingManager";
 
    // Contacts table name
    private static final String TABLE_PURCHASE = "purchase_table";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String PURCHASE_DETAIL = "purchase_detail";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext=context;
        //3rd argument to be passed is CursorFactory instance
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_PURCHASE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + PURCHASE_DETAIL + " BLOB" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE);
 
        // Create tables again
        onCreate(db);
    }

    public void addPurchase( ArrayList<ProductObject> mProductObject) {
        Log.d("databaseTest", "databaseTest ");
        Gson gson = new Gson();
        ContentValues values = new ContentValues();
        values.put(PURCHASE_DETAIL, gson.toJson(mProductObject).getBytes()); // Contact Phone
        Log.d("databaseTest", "databaseTest values.put(PURCHASE_DETAIL");
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("databaseTest", "databaseTest getWritableDatabase()");
        // Inserting Row
        db.insert(TABLE_PURCHASE, null, values);
        Toast.makeText(mContext, "Your payment updated successfully in local database", Toast.LENGTH_SHORT).show();
        Log.d("databaseTest", "databaseTest db.insert");
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        Log.d("databaseTest", "databaseTest db.close");
    }

    // Getting getAllPurchase
    public ArrayList<ArrayList<ProductObject>>  getAllPurchase() {
        ArrayList<ArrayList<ProductObject>>  ProductObjectArray = new ArrayList<ArrayList<ProductObject>>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PURCHASE;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                byte[] getbyte =cursor.getBlob(1);
                try {
                    Type productList = new TypeToken<ArrayList<ProductObject>>(){}.getType();
                    ArrayList<ProductObject> products = new Gson().fromJson(new String(getbyte),productList);
                    ProductObjectArray.add(products);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        return ProductObjectArray;
    }
}
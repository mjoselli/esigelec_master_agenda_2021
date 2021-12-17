package com.esigelec.agendaapp.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ContactDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "contacts.sqlite";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "Contact";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_ADDRESS = "address";
    private static final String COL_PHONE = "phone";

    private Context context;

    public ContactDatabase(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "Create table if not exists " + DB_TABLE + "(" +
               COL_ID + " integer primary key autoincrement, "+
                COL_NAME + " TEXT, "+
                COL_ADDRESS + " TEXT, "+
                COL_PHONE + " TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public long createContactInDB(ContactDetail c){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME,c.getName());
        values.put(COL_ADDRESS,c.getAddress());
        values.put(COL_PHONE,c.getPhone());
        long id = database.insert(DB_TABLE,null,values);
        database.close();
        return id;
    }
    public long insertContactInDB(ContactDetail c){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ID,c.getId());
        values.put(COL_NAME,c.getName());
        values.put(COL_ADDRESS,c.getAddress());
        values.put(COL_PHONE,c.getPhone());
        long id = database.insert(DB_TABLE,null,values);
        database.close();
        return id;
    }
    @SuppressLint("Range")
    public ArrayList<ContactDetail> retrieveContactsFromDB(){
        SQLiteDatabase database = getReadableDatabase();
        //query == select
        Cursor cursor = database.query(DB_TABLE,null,null,
                null,null,null,null);
        ArrayList<ContactDetail> contacts = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String address = cursor.getString(cursor.getColumnIndex(COL_ADDRESS));
                String phone = cursor.getString(cursor.getColumnIndex(COL_PHONE));
                contacts.add(
                        new ContactDetail(id,name,address,phone)
                );

            }while (cursor.moveToNext());
        }
        database.close();
        return contacts;
    }
    public int updateContactInDB(ContactDetail c){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME,c.getName());
        values.put(COL_ADDRESS,c.getAddress());
        values.put(COL_PHONE,c.getPhone());
        String id = String.valueOf(c.getId());
        int count = database.update(DB_TABLE,values,
                COL_ID + "=?",new String[]{id});
        database.close();
        return count;
    }
    public int removeContactFromDB(ContactDetail c){
        SQLiteDatabase database = getWritableDatabase();
        String id = String.valueOf(c.getId());
        int count = database.delete(DB_TABLE,
                COL_ID + "=?",
                new String[]{id});
        database.close();
        return count;
    }
}

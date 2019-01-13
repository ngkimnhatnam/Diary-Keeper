package com.meomoc.toolbar_optionmenu;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAdapter {

    private SQLiteDatabase db;
    private Context context;
    private BaseDBOpenHelper dbHelper;

    static final String BUSINESS = "business";
    static final String ID = "_id";
    static final String TITLE = "title";
    static final String DATE = "date";
    static final String CONTENT = "content";

    public DatabaseAdapter(Context _context){

        context=_context;
        dbHelper = new BaseDBOpenHelper(context);
    }

    public void close(){
        db.close();
    }

    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        }catch (SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public long insertBusiness(String tittle, String date, String content) {

        ContentValues business = new ContentValues();
             business.put(TITLE, tittle);
             business.put(DATE, date);
             business.put(CONTENT, content);

             return db.insert(BUSINESS, null, business);
    }

    public void updateBusiness(long id, String tittle, String date, String content) {

        ContentValues business = new ContentValues();
             business.put(TITLE, tittle);
             business.put(DATE, date);
             business.put(CONTENT, content);
             db.update(BUSINESS, business, "_id="+id, null);
    }


    public void delete_business(long id) {

               db.delete(BUSINESS, "_id="+id, null);
    }

               public void delete_all_from_table(String table) {
             db.delete(table, null, null); }

    public void setupBusiness(String[] tittle, String[]date, String[] content){
        for(int i=0; i < tittle.length; i++){
            insertBusiness(tittle[i], date[i], content[i]);
            }
    }

    public int countRows(String table) {
            Cursor cursor = db.query(table,  null, null, null, null, null, null);
            return cursor.getCount();
    }

    public int getId(String table, String signature)     {
        int id=0;
        Cursor cursor = db.query(table,  null, "signature=?", new String[]{signature}, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex("_id"));
        }
            return id;
    }

    public int getDataId(String table) {
        int id=-1;
        Cursor cursor = db.query(table,  null, null, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        return id;
    }

    public Cursor generalWhereStatemet(String table, String column, int value){

        Cursor cursor = db.query(table,  null, column+"=?", new String[]{Integer.toString(value)}, null, null, null);
        return cursor;
    }

    public ArrayList<String[]> orderByDate(String table){
        ArrayList<String[]> rows = new ArrayList<String[]>();
        Cursor cursor = db.query(table,null,null,null,null,null, DATE);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String [] content = new String[5];
                    content[0] = Integer.toString(cursor.getInt(cursor.getColumnIndex(ID)));
                    content[1] = cursor.getString(cursor.getColumnIndex(TITLE));
                    content[2] = cursor.getString(cursor.getColumnIndex(DATE));
                    content[3] = cursor.getString(cursor.getColumnIndex(CONTENT));
                    rows.add(content);
                }
                while (cursor.moveToNext());
            }
        }
        return rows;

    }

    public ArrayList<String[]> orderByName(String table){
        ArrayList<String[]> rows = new ArrayList<String[]>();
        Cursor cursor = db.query(table,null,null,null,null,null, TITLE);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String [] content = new String[5];
                    content[0] = Integer.toString(cursor.getInt(cursor.getColumnIndex(ID)));
                    content[1] = cursor.getString(cursor.getColumnIndex(TITLE));
                    content[2] = cursor.getString(cursor.getColumnIndex(DATE));
                    content[3] = cursor.getString(cursor.getColumnIndex(CONTENT));
                    rows.add(content);
                }
                while (cursor.moveToNext());
            }
        }
        return rows;

    }

    public ArrayList<String[]> get_all_from_table(String table) {
        ArrayList<String[]>rows = new ArrayList<String[]>();
        Cursor cursor = db.query(table,  null, null, null, null, null, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String [] content = new String[5];
                    content[0] = Integer.toString(cursor.getInt(cursor.getColumnIndex(ID)));
                    content[1] = cursor.getString(cursor.getColumnIndex(TITLE));
                    content[2] = cursor.getString(cursor.getColumnIndex(DATE));
                    content[3] = cursor.getString(cursor.getColumnIndex(CONTENT));
                    rows.add(content);
                }
                while (cursor.moveToNext());
            }
        }
        return rows;
    }

    public Cursor getSingleRecordFromTable(String table, String id, int p) {
        Cursor cursor = db.query(table,  null, null, null, null, null, null);
        cursor.moveToPosition(p);

        return cursor;
    }

    static class BaseDBOpenHelper extends SQLiteOpenHelper {

        public BaseDBOpenHelper(Context context) {
            super(context, "business.db", null, 1);
        }

        private static final String CREATE_BUSINESS = "create table " +BUSINESS+
                     " (" +ID+" integer primary key autoincrement, "+TITLE+
                     " text null, "+DATE+" text null, "+ CONTENT+" text null); ";

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(CREATE_BUSINESS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            _db.execSQL("DROP TABLE IF EXISTS " +BUSINESS);
            onCreate(_db);
        }
    }






}

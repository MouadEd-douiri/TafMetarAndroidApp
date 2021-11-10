package com.example.projet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
        // All Static variables
// Database Version
        private static final int DATABASE_VERSION = 1;
        // Database Name
        private static final String DATABASE_NAME = "Aéoroport.db";
        // Client table name
        public static final String TABLE_AEOROPORT = "Aéoroport";
        // Clients Table Columns names
        public static final String COLUMN_OACI = "OACI";


        public MySQLiteHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_AEOROPORT_TABLE = "CREATE TABLE " + TABLE_AEOROPORT + "("
                    + COLUMN_OACI + " String PRIMARY KEY )";
            db.execSQL(CREATE_AEOROPORT_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AEOROPORT);
            onCreate(db);
        }

    public List<Aéoroport> getAllAEORO() {
        List<Aéoroport> AéoroportList = new ArrayList<Aéoroport>();
        String selectQuery = "SELECT * FROM " +MySQLiteHelper.TABLE_AEOROPORT;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Aéoroport Aéoroport = new Aéoroport();
                Aéoroport.setOACI(cursor.getString(0));
                AéoroportList.add(Aéoroport);
            } while (cursor.moveToNext());
        }
        return AéoroportList;}

public List<String>  getAllOACI(List<Aéoroport> lst){
    SQLiteDatabase db = this.getWritableDatabase();
    List<String> temp=new ArrayList<String >();
    for (Aéoroport a: lst
    ) {
        temp.add(a.OACI);
    }
    return temp;
}
    Aéoroport getaero(String oaci) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.query(MySQLiteHelper.TABLE_AEOROPORT, new String[] { MySQLiteHelper.COLUMN_OACI,
                        }, COLUMN_OACI + "=?",
                new String[] {oaci.toUpperCase()}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Aéoroport aero = new Aéoroport();
        aero.setOACI(cursor.getString(0));
// return client
        return aero;
    }

    public int updateAERO(String oldoaci,String OACI) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Aéoroport aero=getaero(oldoaci);
        values.put(MySQLiteHelper.COLUMN_OACI, OACI.toUpperCase());
// updating row
        return db.update(MySQLiteHelper.TABLE_AEOROPORT, values, MySQLiteHelper.COLUMN_OACI + " = ?",
                new String[] {oldoaci.toUpperCase()});
    }
    public boolean IsIn(String oaci){
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> temp;
        temp=getAllOACI(getAllAEORO());
        for(String s: temp){
            if(s.equals(oaci.toUpperCase())){
                return true;
            }
        }
        return false;
    }
    void addAéoroport(Aéoroport aéoroport) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_OACI, aéoroport.OACI.toUpperCase());

        db.insert(MySQLiteHelper.TABLE_AEOROPORT, null, values);
        db.close();
    }

    public List<Aéoroport> searchAéoroport(String S) {
        List<Aéoroport> contactList = new ArrayList<Aéoroport>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(MySQLiteHelper.TABLE_AEOROPORT, new String[] { MySQLiteHelper.COLUMN_OACI,
                      }, MySQLiteHelper.COLUMN_OACI + " LIKE ?",
                new String[] {"%"+S+"%"}, null, null, MySQLiteHelper.COLUMN_OACI);
        if (cursor.moveToFirst()) {
            do {
                Aéoroport aero = new Aéoroport();
                aero.setOACI(cursor.getString(0));
                contactList.add(aero);
            } while (cursor.moveToNext());
        }

        return contactList;}

    public void deleteAero(String S) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MySQLiteHelper.TABLE_AEOROPORT, MySQLiteHelper.COLUMN_OACI + " = ?",
                new String[] {S.toUpperCase()});
        db.close();
    }

}


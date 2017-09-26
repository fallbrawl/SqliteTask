package com.attracttest.attractgroup.sqlitetask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by nexus on 20.09.2017.
 */
public class DBHelper extends SQLiteOpenHelper {

    // connectin' to bd
    private SQLiteDatabase db = getWritableDatabase();

    public DBHelper(Context context) {
        super(context, "myDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBhelper", "--- onCreate database ---");
        // Table fields creation
        db.execSQL("create table mytable ("
                + "id integer,"
                + "name text,"
                + "surname text,"
                + "date text,"
                + "desc text,"
                + "class text,"
                + "misc text" + ");");
        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addOrUpdate(CustomClass cc) {

        // Creating an object for data
        ContentValues cv = new ContentValues();
        JSONArray jsArray = new JSONArray(cc.getCustomClassInners());

        //Log.d(LOG_TAG, "--- Insert in mytable: ---");
        // preparing data: column - data
        cv.put("id", cc.getId());
        cv.put("name", cc.getName());
        cv.put("surname", cc.getSurname());
        cv.put("date", cc.getDate());
        cv.put("desc", cc.getDesc());
        cv.put("misc", cc.getMisc());
        cv.put("class", String.valueOf(jsArray));

        // gettin id ID
        // Which row to update, based on the ID
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(cc.getId())};
        if (db.update("mytable", cv, selection, selectionArgs) == 0) {
            db.insert("mytable", null, cv);
        }


    }

    public ArrayList<CustomClass> get(int offset, int limit, String sort) {
        ArrayList<CustomClass> result = new ArrayList<>();

        // querin' all database; get Cursor object
        Log.e("staty", String.valueOf(offset));
        ArrayList<CustomClassInner> ccI = new ArrayList<>();
        Cursor c = db.query("mytable", null, null, null, null, null, sort, offset + "," + limit);

        // set Cursors position to first
        // if no values - returnin' false
        if (c.moveToFirst()) {

            // gettin numbers of columns
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int surnameColIndex = c.getColumnIndex("surname");
            int dateColIndex = c.getColumnIndex("date");
            int descColIndex = c.getColumnIndex("desc");
            int miscColIndex = c.getColumnIndex("misc");
            int innerClassColIndex = c.getColumnIndex("innerClass");

            do {
                // gettin' values via columns
//                Log.d("staty",
//                        "ID = " + c.getInt(idColIndex) +
//                                ", name = " + c.getString(nameColIndex) +
//                                ", email = " + c.getString(surnameColIndex) +
//                                ", date = " + c.getString(dateColIndex) +
//                                ", desc = " + c.getString(descColIndex) +
//                                ", misc = " + c.getString(miscColIndex));

                if (c.getString(innerClassColIndex) != null) {
                    for (int i = 0; i < c.getString(innerClassColIndex).length(); i++) {
                        ccI.add(c.getString(innerClassColIndex).toString());
                    }
                }
                result.add(new CustomClass(c.getInt(idColIndex), c.getString(nameColIndex), c.getString(surnameColIndex),
                        c.getString(dateColIndex), c.getString(descColIndex),
                        c.getString(miscColIndex), new ArrayList<CustomClassInner>()CustomClassInner(c.getString(innerFIeld1ColIndex), c.getString(innerFIeld2ColIndex), c.getString(innerFIeld3ColIndex))));

                //movin to the next row
                // if no next - get out of cycle
            } while (c.moveToNext());
        } else
            Log.d("wow", "0 rows");

        c.close();

        return result;

    }

    @Override
    public void close() {
        db.close();
        super.close();
    }

    public int getLastId() {
        Cursor c = db.query("mytable", null, null, null, null, null, "id", null);
        c.moveToLast();
        int idColIndex = c.getColumnIndex("id");
        Log.e("staty", "last id " + c.getInt(idColIndex));

        return c.getInt(idColIndex);
    }

}


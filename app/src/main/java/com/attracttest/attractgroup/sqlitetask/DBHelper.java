package com.attracttest.attractgroup.sqlitetask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nexus on 20.09.2017.
 */
public class DBHelper extends SQLiteOpenHelper {

    // connectin' to bd
    private SQLiteDatabase db = getWritableDatabase();
    private final String TABLENAME = "mytable";
    private final String INNERTABLENAME = "mytableInner";

    public DBHelper(Context context) {
        super(context, "myDB3", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBhelper", "--- onCreate database ---");

        // Main table fields creation
        db.execSQL("create table " + TABLENAME + "("
                + "mainID integer,"
                + "name text,"
                + "surname text,"
                + "date text,"
                + "desc text,"
                + "class text,"
                + "misc text" + ");");

        // Inner table fields creation
        db.execSQL("create table " + INNERTABLENAME + " ("
                + "innerID integer,"
                + "field1 text,"
                + "field2 text,"
                + "field3 text" + ");");
        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (i1 == 3)  {

        }

    }

    public void addOrUpdate(CustomClass cc) {

        // Creating an object for data
        ContentValues maincv = new ContentValues();
        ContentValues innercv = new ContentValues();

        // Log.d(LOG_TAG, "--- Insert in mytable: ---");
        // preparing data: column - data
        // main table
        maincv.put("mainID", cc.getId());
        maincv.put("name", cc.getName());
        maincv.put("surname", cc.getSurname());
        maincv.put("date", cc.getDate());
        maincv.put("desc", cc.getDesc());
        maincv.put("misc", cc.getMisc());

        //extractin inner table from Arraylist
        innercv.put("innerID", cc.getCustomClassInners().getId());
        innercv.put("field1", cc.getCustomClassInners().getField1());
        innercv.put("field2", cc.getCustomClassInners().getField2());
        innercv.put("field3", cc.getCustomClassInners().getField3());


        // gettin id ID
        // Which row to update, based on the ID
        String selection = "mainID=?";
        String[] selectionArgs = {String.valueOf(cc.getId())};
        if (db.update(TABLENAME, maincv, selection, selectionArgs) == 0) {
            db.insert(TABLENAME, null, maincv);
            db.insert("mytableInner", null, innercv);
        }
    }

    public ArrayList<CustomClass> get(int offset, int limit, String sort) {
        ArrayList<CustomClass> result = new ArrayList<>();

        // querin' all database; get Cursor object

        Cursor c = db.query("mytable LEFT OUTER JOIN mytableInner ON mytable.mainID=mytableInner.innerID" ,
                null, null, null, null, null, sort, offset + "," + limit);
        //Cursor c = db.query("mytable", null, null, null, null, null, sort, offset + "," + limit);


        // set Cursors position to first
        // if no values - returnin' false
        if (c.moveToFirst()) {

            // gettin numbers of columns in Main table
            int idMainColIndex = c.getColumnIndex("mainID");
            int nameMainColIndex = c.getColumnIndex("name");
            int surnameMainColIndex = c.getColumnIndex("surname");
            int dateMainColIndex = c.getColumnIndex("date");
            int descMainColIndex = c.getColumnIndex("desc");
            int miscMainColIndex = c.getColumnIndex("misc");

            // gettin numbers of columns in Inner table
            int idInnerColIndex = c.getColumnIndex("innerID");
            int innerField1 = c.getColumnIndex("field1");
            int innerField2 = c.getColumnIndex("field2");
            int innerField3 = c.getColumnIndex("field3");


            do {
                // gettin' values via columns
//                Log.d("staty",
//                        "ID = " + c.getInt(idColIndex) +
//                                ", name = " + c.getString(nameColIndex) +
//                                ", email = " + c.getString(surnameColIndex) +
//                                ", date = " + c.getString(dateColIndex) +
//                                ", desc = " + c.getString(descColIndex) +
//                                ", misc = " + c.getString(miscColIndex));

                result.add(new CustomClass(c.getInt(idMainColIndex), c.getString(nameMainColIndex),
                        c.getString(surnameMainColIndex),
                        c.getString(dateMainColIndex), c.getString(descMainColIndex),
                        c.getString(miscMainColIndex),
                        new CustomClassInner(c.getInt(idInnerColIndex),
                                c.getString(innerField1), c.getString(innerField2), c.getString(innerField3))));

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


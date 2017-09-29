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
        super(context, "myDB3", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBhelper", "--- onCreate database ---");

        // Main table fields creation
        db.execSQL("create table " + TABLENAME + "("
                + "mainID text,"
                + "name text,"
                + "surname text,"
                + "date text,"
                + "desc text,"
                + "class text,"
                + "misc text" + ");");

        // Inner table fields creation
        db.execSQL("create table " + INNERTABLENAME + " ("
                + "innerID text,"
                + "field1 text,"
                + "field2 text,"
                + "field3 text" + ");");
        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (i1 == 3) {


        }

    }

    public void addOrUpdateMain(CustomClass customClass) {

        // Creating an object for data
        ContentValues maincv = new ContentValues();


        // Log.d(LOG_TAG, "--- Insert in mytable: ---");
        // preparing data: column - data
        // main table
        maincv.put("mainID", customClass.getId());
        maincv.put("name", customClass.getName());
        maincv.put("surname", customClass.getSurname());
        maincv.put("date", customClass.getDate());
        maincv.put("desc", customClass.getDesc());
        maincv.put("misc", customClass.getMisc());

        // gettin id ID
        // Which row to update, based on the ID
        String selection = "mainID=?";
        String[] selectionArgs = {String.valueOf(customClass.getId())};
        if (db.update(TABLENAME, maincv, selection, selectionArgs) == 0) {
            db.insert(TABLENAME, null, maincv);
        }
        addOrUpdateInner(customClass.getCustomClassInners());
    }

    public void addOrUpdateInner(ArrayList<CustomClassInner> customClassInners) {

        ContentValues innercv = new ContentValues();
        //extractin inner table from Arraylist

        for (int i = 0; i < 3; i++) {
            innercv.put("innerID", customClassInners.get(i).getId());
            innercv.put("field1", customClassInners.get(i).getField1());
            innercv.put("field2", customClassInners.get(i).getField2());
            innercv.put("field3", customClassInners.get(i).getField3());

            // gettin id ID
            // Which row to update, based on the ID
            String selection = "innerID=?";
            String[] selectionArgs = {String.valueOf(customClassInners.get(i).getId())};
            if (db.update(INNERTABLENAME, innercv, selection, selectionArgs) == 0) {
                db.insert("mytableInner", null, innercv);
            }
        }
    }

    public ArrayList<CustomClass> get(int offset, int limit, String sort) {
        ArrayList<CustomClass> result = new ArrayList<>();

        // querin' all database; get Cursor object

        Cursor c = db.query("mytable INNER JOIN mytableInner ON mytable.mainID=mytableInner.innerID",
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

            ArrayList<CustomClassInner> customClassInnersResult = new ArrayList<CustomClassInner>();


            do {
                // gettin' values via columns
                Log.d("Maintable",
                        "ID = " + c.getInt(idMainColIndex) +
                                ", name = " + c.getString(nameMainColIndex) +
                                ", email = " + c.getString(surnameMainColIndex) +
                                ", date = " + c.getString(dateMainColIndex) +
                                ", desc = " + c.getString(descMainColIndex) +
                                ", misc = " + c.getString(miscMainColIndex) +
                "innerfield1 = "+ c.getString(innerField1));
               Log.e("staty", "cursor " + String.valueOf(c.getCount()));
//                for (int i = 0; i < 2; i++) {
//                    if (c.moveToNext()) {
//                        customClassInnersResult.add(new CustomClassInner(c.getString(idInnerColIndex), c.getString(innerField1), c.getString(innerField2),
//                                c.getString(innerField3)));
//                    }
//
//                }
//                customClassInnersResult.add(new CustomClassInner(c.getString(idInnerColIndex), c.getString(innerField1), c.getString(innerField2),
//                        c.getString(innerField3)));
//               result.add(new CustomClass(c.getString(idMainColIndex), c.getString(nameMainColIndex),
//                        c.getString(surnameMainColIndex),
//                        c.getString(dateMainColIndex), c.getString(descMainColIndex),
//                        c.getString(miscMainColIndex), customClassInnersResult));
//                customClassInnersResult.clear();

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


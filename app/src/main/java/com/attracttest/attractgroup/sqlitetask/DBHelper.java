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
        super(context, "myDB3", null, 4);
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
                + "mainID text,"
                + "field1 text,"
                + "field2 text,"
                + "field3 text" + ");");
        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        if (i1 == 4) {
            sqLiteDatabase.execSQL("ALTER TABLE " + INNERTABLENAME + " ADD COLUMN mainID TEXT;");

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
        for (CustomClassInner cci : customClass.getCustomClassInners()) {
            addOrUpdateInner(cci, customClass.getId());
        }

    }

    public void addOrUpdateInner(CustomClassInner customClassInners, String ID) {

        ContentValues innercv = new ContentValues();
        //extractin inner table from Arraylist


        innercv.put("innerID", customClassInners.getId());
        innercv.put("mainID", ID);
        innercv.put("field1", customClassInners.getField1());
        innercv.put("field2", customClassInners.getField2());
        innercv.put("field3", customClassInners.getField3());

        // gettin id ID
        // Which row to update, based on the ID
        String selection = "innerID=?";
        String[] selectionArgs = {String.valueOf(customClassInners.getId())};
        if (db.update(INNERTABLENAME, innercv, selection, selectionArgs) == 0) {
            db.insert("mytableInner", null, innercv);

        }
    }

    public ArrayList<CustomClass> get(int offset, int limit, String sort) {
        ArrayList<CustomClass> result = new ArrayList<>();

        // querin' all database; get Cursor object

        Cursor c = db.query("mytable INNER JOIN mytableInner ON mytable.mainID=mytableInner.mainID",
                null, null, null, null, null, sort, offset + "," + limit);


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
//                Log.d("Maintable",
//                        "ID = " + c.getInt(idMainColIndex) +
//                                ", name = " + c.getString(nameMainColIndex) +
//                                ", email = " + c.getString(surnameMainColIndex) +
//                                ", date = " + c.getString(dateMainColIndex) +
//                                ", desc = " + c.getString(descMainColIndex) +
//                                ", misc = " + c.getString(miscMainColIndex) +
//                                "innerfield1 = " + c.getString(innerField1));
//                Log.e("staty", "cursor " + String.valueOf(c.getCount()));

                CustomClass cc = new CustomClass(c.getString(idMainColIndex), c.getString(nameMainColIndex),
                        c.getString(surnameMainColIndex),
                        c.getString(dateMainColIndex), c.getString(descMainColIndex),
                        c.getString(miscMainColIndex), new ArrayList<CustomClassInner>());

                if(result.size()==0)
                    result.add(cc);
                int index=result.size()-1;

               if (!result.get(index).getId().equals(cc.getId())) {
                        result.add(cc);
                }

                result.get(index).getCustomClassInners().add(new CustomClassInner(c.getString(idInnerColIndex), c.getString(innerField1), c.getString(innerField2),
                        c.getString(innerField3)));


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


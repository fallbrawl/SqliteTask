package com.attracttest.attractgroup.sqlitetask;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Intent intent;
    private final String LOG_TAG = "myLogs";
    private int MAXRECORDS = 73;
    private CustomClassAdapter customClassAdapter;
    private int i = 0;
    private int left = 0;
    private DBHelper dbHelper;
    ArrayList<CustomClass> classesFromDb;
    CustomClass whatToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<CustomClass> classesForDb = CustomClass.init();
        classesFromDb = new ArrayList<>();
        final ArrayList<CustomClass> current = new ArrayList<>();

        // Creating an object for data
        ContentValues cv = new ContentValues();

        // Object for controllin' the DB
        dbHelper = new DBHelper(this);

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(LOG_TAG, "--- Insert in mytable: ---");
        // подготовим данные для вставки в виде пар: наименование столбца - значение

        for (CustomClass cc :
                classesForDb) {
            cv.put("name", cc.getName());
            cv.put("surname", cc.getSurname());
            cv.put("date", cc.getDate());
            cv.put("desc", cc.getDesc());
            cv.put("misc", cc.getMisc());

            // вставляем запись и получаем ее ID
            long rowID = db.insert("mytable", null, cv);
            Log.d(LOG_TAG, "row inserted, ID = " + rowID);
        }

        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int surnameColIndex = c.getColumnIndex("surname");
            int dateColIndex = c.getColumnIndex("date");
            int descColIndex = c.getColumnIndex("desc");
            int miscColIndex = c.getColumnIndex("misc");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", email = " + c.getString(surnameColIndex) +
                                ", date = " + c.getString(dateColIndex) +
                                ", desc = " + c.getString(descColIndex) +
                                ", misc = " + c.getString(miscColIndex));

                classesFromDb.add(new CustomClass(c.getString(nameColIndex), c.getString(surnameColIndex),
                        c.getString(dateColIndex),c.getString(descColIndex),
                        c.getString(miscColIndex)));

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        // закрываем подключение к БД
        dbHelper.close();

        customClassAdapter = new CustomClassAdapter(this, current);

        ListView listView = (ListView) findViewById(R.id.lvClasses);
        listView.setAdapter(customClassAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalItemCount < MAXRECORDS) {
                    i++;
                    if (totalItemCount % 20 == 0) {

                        current.addAll(classesFromDb.subList(totalItemCount, totalItemCount + 20));

                        left = MAXRECORDS - 20 * i;

                    }

                    if (left != 0) {
                        current.addAll(classesFromDb.subList(MAXRECORDS - left, MAXRECORDS));
                    }
                    customClassAdapter.notifyDataSetChanged();
                }
            }
        });

        //Set up the fab
        intent = new Intent(this, Main2Activity.class);
        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent.putExtra("extra", CustomClass.init());
                startActivityForResult(intent, 607);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        whatToAdd = new CustomClass(data.getStringExtra("name"), data.getStringExtra("surname"),
                data.getStringExtra("date"), data.getStringExtra("misc"), data.getStringExtra("desc"));

        customClassAdapter.add(whatToAdd);

    }
}


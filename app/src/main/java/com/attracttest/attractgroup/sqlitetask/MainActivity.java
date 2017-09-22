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

        classesFromDb = new ArrayList<>();
        final ArrayList<CustomClass> current = new ArrayList<>();

        // Object for controllin' the DB
        dbHelper = new DBHelper(this);

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

                        //current.addAll(dbHelper.get().subList(totalItemCount, totalItemCount + 20));
                        current.addAll(dbHelper.get(totalItemCount, totalItemCount, null));

                        left = MAXRECORDS - 20 * i;

                    }

                    if (left != 0) {
                        current.addAll(dbHelper.get(MAXRECORDS - left, MAXRECORDS - left, null));
                    }
//                    int pos=totalItemCount + 20;
//                    current.addAll(classesFromDb.subList(totalItemCount, pos<=classesFromDb.size()?pos:classesFromDb.size()));
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

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();

    }
}


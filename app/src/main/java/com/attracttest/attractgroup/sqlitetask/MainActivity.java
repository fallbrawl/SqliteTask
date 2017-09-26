package com.attracttest.attractgroup.sqlitetask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Intent intent;
    private final String LOG_TAG = "myLogs";
    private int MAXRECORDS = 73;
    private CustomClassAdapter customClassAdapter;
    private DBHelper dbHelper;
    private ArrayList<CustomClass> classesForDb;
    private CustomClass whatToAdd;
    private Toolbar toolbar;
    private String[] category;
    private String orderby;
    ListView listView;
    final ArrayList<CustomClass> current = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SQLite");

        category = getResources().getStringArray(R.array.category);

        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.category, R.layout.spinner_dropdown_item);
        Spinner navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(spinnerAdapter);
        toolbar.addView(navigationSpinner, 0);
        orderby = "date ASC";

        classesForDb = new ArrayList<>();

        // Object for controllin' the DB
        dbHelper = new DBHelper(this);

        //init the DB
        classesForDb = CustomClass.init();

        for (CustomClass cc :
                classesForDb) {
            dbHelper.addOrUpdate(cc);

        }
        //Log.e("staty", "db: " + String.valueOf(dbHelper.lol()));
        //MAXRECORDS = dbHelper.lol();

        customClassAdapter = new CustomClassAdapter(this, current);

        listView = (ListView) findViewById(R.id.lvClasses);
        listView.setAdapter(customClassAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalItemCount < MAXRECORDS) {
//                    i++;

//                    if (totalItemCount % 20 == 0) {
//
//                        //current.addAll(dbHelper.get().subList(totalItemCount, totalItemCount + 20));
//                        current.addAll(dbHelper.get(totalItemCount, null));
//
//                        left = MAXRECORDS - 20 * i;
//                    }
//
//                    if (left != 0) {
//                        current.addAll(dbHelper.get(MAXRECORDS - left, null));
//                    }

                    int pos = totalItemCount + 20;
                    //current.addAll(classesForDb.subList(totalItemCount, pos<=classesForDb.size()?pos:classesForDb.size()));
                    current.addAll(dbHelper.get(totalItemCount, pos <= classesForDb.size() ? pos : classesForDb.size() - totalItemCount, orderby));
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



        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int check = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                switch (category[position]){
                    case "Asc":
                        orderby = "date ASC";
                        Toast.makeText(MainActivity.this,
                                "asc",
                                Toast.LENGTH_SHORT).show();
                        listView.smoothScrollToPosition(0);

                        refreshInit();
                        break;
                    case "Desc":
                        orderby = "date DESC";
                        Toast.makeText(MainActivity.this,
                                "desc",
                                Toast.LENGTH_SHORT).show();
                        listView.smoothScrollToPosition(0);

                        refreshInit();
                        break;
                }}

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //MAXRECORDS = dbHelper.lol();
        refreshInit();
        Log.e("staty", "RESTARTED!");
        //Log.e("staty", "db: " + String.valueOf(dbHelper.lol()));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        whatToAdd = new CustomClass(data.getIntExtra("id", 0), data.getStringExtra("name"), data.getStringExtra("surname"),
                data.getStringExtra("date"), data.getStringExtra("misc"), data.getStringExtra("desc"), (CustomClassInner) data.getSerializableExtra("class"));
        Log.e("staty" ," id is: " + whatToAdd.getId());
        dbHelper.addOrUpdate(whatToAdd);

    }

    private void refreshInit() {
        //customClassAdapter.clear();
        current.clear();
        current.addAll(dbHelper.get(0, 20, orderby));
        customClassAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        deleteDatabase(dbHelper.getDatabaseName());
        dbHelper.close();

        super.onDestroy();

    }
}


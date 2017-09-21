package com.attracttest.attractgroup.sqlitetask;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Intent intent;
    private final String LOG_TAG = "myLogs";
    private final int MAXRECORDS = 60;
    private CustomClassAdapter customClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<CustomClass> classes = CustomClass.init();
        customClassAdapter = new CustomClassAdapter(this, classes);

        Log.e("staty", String.valueOf(CustomClass.init().size()));
        Log.e("staty", String.valueOf(CustomClass.init().get(1).getDate()));

        ListView listView = (ListView) findViewById(R.id.lvClasses);
        listView.setAdapter(customClassAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalItemCount < MAXRECORDS) {
                    customClassAdapter.add(CustomClass.init());
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
                startActivity(intent);
            }
        });

    }
}

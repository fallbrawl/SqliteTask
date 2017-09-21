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
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private Intent intent;
    private final String LOG_TAG = "myLogs";
    private final int MAXRECORDS = 73;
    private CustomClassAdapter customClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ArrayList<CustomClass> classes = CustomClass.init();
        final ArrayList<CustomClass> current = new ArrayList<>();
        //current.addAll(classes.subList(0,20));

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

                    current.addAll(classes.subList(totalItemCount, totalItemCount+20));
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

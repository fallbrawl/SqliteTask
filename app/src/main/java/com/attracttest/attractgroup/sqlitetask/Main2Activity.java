package com.attracttest.attractgroup.sqlitetask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";

    private Button btnAdd;
    private EditText etName, etSurname, etDate, etDesc, etMisc;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etDate = (EditText) findViewById(R.id.etDate);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etMisc = (EditText) findViewById(R.id.etMisc);

        // Object for creating and maintaining DB
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View view) {

    }
}

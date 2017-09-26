package com.attracttest.attractgroup.sqlitetask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    private static int lastId;
    DBHelper dbHelper;

    private CustomClassInner ccI;
    private Button btnAdd;
    private EditText etName, etSurname, etDate, etDesc, etMisc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        dbHelper = new DBHelper(this);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etDate = (EditText) findViewById(R.id.etDate);
        etDesc = (EditText) findViewById(R.id.etDesc);
        etMisc = (EditText) findViewById(R.id.etMisc);
        lastId = dbHelper.getLastId();

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        ++lastId;
        intent.putExtra("id", lastId);
        intent.putExtra("name", etName.getText().toString());
        intent.putExtra("surname", etSurname.getText().toString());
        intent.putExtra("date", etDate.getText().toString());
        intent.putExtra("misc", etMisc.getText().toString());
        intent.putExtra("desc", etDesc.getText().toString());
        intent.putExtra("class", new CustomClassInner("name" + lastId, "name" + lastId, "name" + lastId));

        setResult(RESULT_OK, intent);
        finish();
    }
}

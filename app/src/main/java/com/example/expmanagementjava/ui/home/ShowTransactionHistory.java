package com.example.expmanagementjava.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.example.expmanagementjava.R;

import java.util.ArrayList;

public class ShowTransactionHistory extends AppCompatActivity {

    RecyclerView recyclerView;
    public MyDatabaseHelper myDB;
    public ArrayList<String> trans_id, trans_date, trans_category, trans_name, trans_amount, trans_amt_rem, trans_add_or_sub;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction_history);

        recyclerView = findViewById(R.id.recyclerView);
        myDB = MyDatabaseHelper.getInstance(ShowTransactionHistory.this);
        trans_id = new ArrayList<>();
        trans_date = new ArrayList<>();
        trans_category = new ArrayList<>();
        trans_name = new ArrayList<>();
        trans_amount = new ArrayList<>();
        trans_amt_rem = new ArrayList<>();
        trans_add_or_sub = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(ShowTransactionHistory.this,
                trans_id, trans_date, trans_category, trans_name, trans_amount, trans_amt_rem, trans_add_or_sub);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowTransactionHistory.this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }


    // Function to take all data from the database and store it into respective arrays
    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                trans_id.add(cursor.getString(0));
                trans_date.add(cursor.getString(1));
                trans_category.add(cursor.getString(2));
                trans_name.add(cursor.getString(3));
                trans_amount.add(cursor.getString(4));
                trans_amt_rem.add(cursor.getString(5));
                trans_add_or_sub.add(cursor.getString(6));

            }
        }
    }
}
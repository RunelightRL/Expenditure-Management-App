package com.example.expmanagementjava;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import com.example.expmanagementjava.ui.home.MyDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Dialog dialog;
    Button ok_button , cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dialog = new Dialog(MainActivity.this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,  R.id.nav_analytics , R.id.nav_analytics_earned)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    //Creates the options menu i.e The 3 Dots on the top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    //Creates the action "Delete all Records" on the options menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_table:
                openDelDialog();
                return true;
            default: return super.onOptionsItemSelected(item);
        }


    }

    //Function for confirmation of deletion of all records
    private void openDelDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.delete_all_records_confirmation_dialog,null);
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();

        ok_button = mView.findViewById(R.id.button_ok);
        cancel_button = mView.findViewById(R.id.button_cancel);

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(MainActivity.this);
                myDB.deleteAllRecords();
                Toast.makeText(MainActivity.this, "Deleted All Records in the Table", Toast.LENGTH_SHORT).show();

                //Refreshes Activity
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);

                dialog.dismiss();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
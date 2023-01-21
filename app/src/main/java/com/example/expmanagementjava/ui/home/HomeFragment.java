package com.example.expmanagementjava.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.expmanagementjava.MainActivity;
import com.example.expmanagementjava.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    //Declaration
    Dialog dialog;
    EditText transName, transAmt;
    String dbDialogDate, transCategory, dbAddOrSub;
    Integer dbAmtRemaining = 0;
    Button accept_button;
    private PieChart pieChart; //PieChart on the Home Fragment

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        dialog = new Dialog(getActivity());
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Button addMoneyButton = v.findViewById(R.id.addMoneyButton);
        Button subMoneyButton = v.findViewById(R.id.subMoneyButton);
        Button transactionHistoryButton = v.findViewById(R.id.showTransactionHistoryButton);

        MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());

        // Home Screen Pie Chart
        pieChart = v.findViewById(R.id.homePieChart);
        setupPieChartHome();
        loadPieChartDataHome();

        // Display Current Date
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        TextView textViewDate = v.findViewById(R.id.textView_date);
        textViewDate.setText(currentDate);


        // Display the Remaining Balance in Account
        TextView textViewWalletHome = v.findViewById(R.id.textView_WalletHome);
        String remAmt = String.valueOf(myDB.getLastRecDataofAmtRemaining()); // The function returns int but requires to be converted to String
        textViewWalletHome.setText("Rs." + remAmt);

        // Displaying Last Record
        TextView homeTransID , homeTransName , homeTransCategory , homeTransDate , homeTransAmt ;
        String lastIDText = "", lastNameText = "", lastCatText = "", lastDateText = "", lastAmtText = "";
        Integer lastAddOrSub = 0;

        homeTransID = v.findViewById(R.id.home_trans_id);
        homeTransName = v.findViewById(R.id.home_trans_name);
        homeTransCategory = v.findViewById(R.id.home_trans_category);
        homeTransDate = v.findViewById(R.id.home_trans_date);
        homeTransAmt = v.findViewById(R.id.home_trans_amt);

        lastIDText = String.valueOf(myDB.getLastRecID());
        lastDateText = myDB.getLastRecDate();
        lastCatText = myDB.getLastRecCategory();
        lastNameText = myDB.getLastRecName();
        lastAmtText = String.valueOf(myDB.getLastRecDataofAmount());
        lastAddOrSub = myDB.getLastRecAddOrSub();

        homeTransID.setText(lastIDText);
        homeTransDate.setText(lastDateText);
        homeTransCategory.setText(lastCatText);
        homeTransName.setText(lastNameText);
        homeTransAmt.setText("Rs. " + lastAmtText);

        if (lastAddOrSub.equals(1)){
            homeTransID.setTextColor(Color.parseColor("#138031"));
            homeTransName.setTextColor(Color.parseColor("#138031"));
            homeTransCategory.setTextColor(Color.parseColor("#138031"));
            homeTransDate.setTextColor(Color.parseColor("#138031"));
            homeTransAmt.setTextColor(Color.parseColor("#138031"));
        }
        if (lastAddOrSub.equals(-1)){
            homeTransID.setTextColor(Color.parseColor("#FF4141"));
            homeTransName.setTextColor(Color.parseColor("#FF4141"));
            homeTransCategory.setTextColor(Color.parseColor("#FF4141"));
            homeTransDate.setTextColor(Color.parseColor("#FF4141"));
            homeTransAmt.setTextColor(Color.parseColor("#FF4141"));
        }

        // Reset-Salary

        DateFormat fullDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String fullDate = fullDateFormat.format(calendar.getTime());
        Toast.makeText(getActivity(), fullDate, Toast.LENGTH_SHORT).show();
        String todayDate = fullDate.substring(fullDate.length()-2);
        Integer totalAmt = myDB.getLastRecDataofAmtRemaining();

        String[]monthName={"January","February","March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};

        String savMonth=monthName[calendar.get(Calendar.MONTH)];
        int savYear = calendar.get(Calendar.YEAR);
        Integer savAmtRem = myDB.savGetLastRecDataofAmtRemaining();

        if ((!lastDateText.equals(fullDate)) && ((todayDate.equals("01"))||(todayDate.equals("1")))){

            myDB.addTransaction(fullDate.trim(),
                    "Transferred to Savings",
                    "Transferred to savings",
                    totalAmt,
                    0,
                    0
                    );

            savAmtRem = savAmtRem + totalAmt;
            myDB.addSavings(savMonth.trim(),
                    String.valueOf(savYear).trim(),
                    totalAmt,
                    savAmtRem,
                    1);

            dbAmtRemaining = myDB.getLastRecDataofAmtRemaining();
            dbAmtRemaining = dbAmtRemaining + 15000;

            myDB.addTransaction(fullDate.trim(),
                    "Salary",
                    "Salary",
                    15000,
                    dbAmtRemaining,
                    1);

            //Refreshes Activity
            Intent i = new Intent(getActivity() , MainActivity.class);
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(i);
            getActivity().overridePendingTransition(0, 0);
        }
        else {
            Toast.makeText(getActivity(), todayDate + " today not Salary Day" , Toast.LENGTH_SHORT).show();
        }

        // Displaying Total Savings
        TextView textViewSavings = v.findViewById(R.id.textView_savings);
        String totalSavings = String.valueOf(myDB.savGetLastRecDataofAmtRemaining()); // The function returns int but requires to be converted to String
        textViewSavings.setText("Rs." + totalSavings);

        // Showing Transaction History
        transactionHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShowTransactionHistory.class);
                startActivity(intent);
            }
        });

        // Showing the Add Money Dialog Box
        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog();
            }

        });

        // Showing the Subtract Money Dialog Box
        subMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSubDialog();
            }
        });

        return v;
    }

    // Function to open the Subtract Money Dialog Box and performing operation
    private void openSubDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.sub_money_dialog,null);
        Spinner mSpinner = mView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.CategoryList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dialogDate = formatter.format(calendar.getTime());
        TextView textViewDate = mView.findViewById(R.id.dialog_Date);
        textViewDate.setText(dialogDate);
        TextView textWarning = mView.findViewById(R.id.textWarningSub);

        mSpinner.setAdapter(adapter);
        mBuilder.setView(mView);

        dbDialogDate = dialogDate;
        transName = mView.findViewById(R.id.trans_name);
        transAmt = mView.findViewById(R.id.trans_amt);
        dbAddOrSub = "-1";

        AlertDialog dialog = mBuilder.create();

        accept_button = mView.findViewById(R.id.button_accept);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = mSpinner.getSelectedItemPosition();
                transCategory = adapter.getItem(pos);

                if(transName.getText().toString().trim().equals("") || transAmt.getText().toString().trim().equals("") || transCategory.equals("Choose a category.")){
                    textWarning.setText("Fields cannot be left empty!");
                    textWarning.setTextColor(Color.parseColor("#fa4d43"));
                }
                else {
                    MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());
                    dbAmtRemaining = myDB.getLastRecDataofAmtRemaining();
                    dbAmtRemaining = dbAmtRemaining + (Integer.valueOf(dbAddOrSub.trim()) * Integer.valueOf(transAmt.getText().toString().trim()));
                    myDB.addTransaction(dbDialogDate.trim(),
                            transCategory.trim(),
                            transName.getText().toString().trim(),
                            Integer.valueOf(transAmt.getText().toString().trim()),
                            dbAmtRemaining,
                            Integer.valueOf(dbAddOrSub.trim()));

                    // Refreshes Activity
                    Intent i = new Intent(getActivity() , MainActivity.class);
//                    getActivity().finish();
//                    getActivity().overridePendingTransition(0, 0);
                    startActivity(i);
                    getActivity().overridePendingTransition(0, 0);

                    dialog.dismiss();
                }
            }
        });



        dialog.show();
    }

    // Function to open the Add Money Dialog Box and performing operation
    private void openAddDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.add_money_dialog,null);
        Spinner mSpinner = mView.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.CategoryList));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Calendar calendar = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dialogDate = formatter.format(calendar.getTime());
        TextView textViewDate = mView.findViewById(R.id.dialog_Date);
        textViewDate.setText(dialogDate);
        TextView textWarning = mView.findViewById(R.id.textWarningAdd);

        mSpinner.setAdapter(adapter);
        mBuilder.setView(mView);


        dbDialogDate = dialogDate;
        transName = mView.findViewById(R.id.trans_name);
        transAmt = mView.findViewById(R.id.trans_amt);
        dbAddOrSub = "1";

        AlertDialog dialog = mBuilder.create();

        accept_button = mView.findViewById(R.id.button_accept);
        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = mSpinner.getSelectedItemPosition();
                transCategory = adapter.getItem(pos);

                if(transName.getText().toString().trim().equals("") || transAmt.getText().toString().trim().equals("") || transCategory.equals("Choose a category.")){
                    textWarning.setText("Fields cannot be left empty!");
                    textWarning.setTextColor(Color.parseColor("#fa4d43"));
                }
                else {
                    MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());
                    dbAmtRemaining = myDB.getLastRecDataofAmtRemaining();
                    dbAmtRemaining = dbAmtRemaining + (Integer.valueOf(dbAddOrSub.trim()) * Integer.valueOf(transAmt.getText().toString().trim()));

                    myDB.addTransaction(dbDialogDate.trim(),
                            transCategory.trim(),
                            transName.getText().toString().trim(),
                            Integer.valueOf(transAmt.getText().toString().trim()),
                            dbAmtRemaining,
                            Integer.valueOf(dbAddOrSub.trim()));

                    //Refreshes Activity
                    Intent i = new Intent(getActivity() , MainActivity.class);
//                    getActivity().finish();
//                    getActivity().overridePendingTransition(0, 0);
                    startActivity(i);
                    getActivity().overridePendingTransition(0, 0);

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    // Function to setup the home PieChart
    private void setupPieChartHome(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setWordWrapEnabled(true);
        l.setTextSize(12f);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    // Function to load the home PieChart
    private void loadPieChartDataHome() {
        ArrayList<PieEntry> entriesHome = new ArrayList<>();
        MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());
        Cursor cursor = myDB.readAllData();
        ArrayList<Integer> trans_id, trans_amount, trans_add_or_sub;

        final int[] my_colors = {Color.rgb(104, 180, 125) , Color.rgb(255, 65, 65) , Color.rgb(65, 147, 255)};

        Integer addAmount = 0 , subAmount = 0 , savAmount = 0 ;

        trans_id = new ArrayList<>();
        trans_amount = new ArrayList<>();
        trans_add_or_sub = new ArrayList<>();

        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()){
                trans_id.add(cursor.getInt(0));
                trans_amount.add(cursor.getInt(4));
                trans_add_or_sub.add(cursor.getInt(6));
            }
        }

        int i = 0;

        while (i < trans_id.size()){
            int i1 = trans_amount.get(i);
            if(trans_add_or_sub.get(i).equals(-1)){
                subAmount = subAmount + i1 ;
            }
            if (trans_add_or_sub.get(i).equals(1)){
                addAmount = addAmount + i1 ;
            }
            if (trans_add_or_sub.get(i).equals(0)){
                savAmount = savAmount + i1 ;
            }
            i++;
        }


        entriesHome.add(new PieEntry(addAmount, "Money Earned"));
        entriesHome.add(new PieEntry(subAmount, "Money Spent"));
        entriesHome.add(new PieEntry(savAmount, "Money Saved"));

//        if (addAmount != 0) {
//            entriesHome.add(new PieEntry(addAmount, "Money Earned"));
//        }
//        if (subAmount != 0) {
//            entriesHome.add(new PieEntry(subAmount, "Money Spent"));
//        }
//        if (savAmount != 0) {
//            entriesHome.add(new PieEntry(savAmount, "Money Saved"));
//        }

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: my_colors){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entriesHome, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(20f);


        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);


        pieChart.setData(data);
        pieChart.invalidate();
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }

}
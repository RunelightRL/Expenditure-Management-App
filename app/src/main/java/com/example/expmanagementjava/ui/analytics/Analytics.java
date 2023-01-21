package com.example.expmanagementjava.ui.analytics;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.expmanagementjava.R;
import com.example.expmanagementjava.ui.home.MyDatabaseHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Analytics extends Fragment {

    private AnalyticsViewModel analyticsViewModel;
    private PieChart pieChart;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
        View v = inflater.inflate(R.layout.analytics_fragment,container,false);
        pieChart = v.findViewById(R.id.pie_chart_spend);
        setupPieChart();
        loadPieChartData();

        return v;
    }

    // Function to setup the Expense PieChart
    private void setupPieChart(){
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(false);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("MONEY SPENT");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
    }

    // Function to load the Expense PieChart
    private void loadPieChartData(){
        MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(getContext());
        Cursor cursor = myDB.readAllData();
        ArrayList<Integer> trans_id, trans_amount, trans_add_or_sub;
        ArrayList<String> trans_category;
        Integer catFood=0 , catSubscription=0 , catShared=0 , catEntertainment=0 , catGrocery=0 , catClothing=0 , catSalary=0 , catOthers=0  ;

        trans_id = new ArrayList<>();
        trans_category = new ArrayList<>();
        trans_amount = new ArrayList<>();
        trans_add_or_sub = new ArrayList<>();

        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()){
                trans_id.add(cursor.getInt(0));
                trans_category.add(cursor.getString(2));
                trans_amount.add(cursor.getInt(4));
                trans_add_or_sub.add(cursor.getInt(6));

            }
        }

        int i = 0;

        while (i < trans_id.size()){
            int i1 = trans_amount.get(i);
            if(trans_add_or_sub.get(i).equals(-1)){
                if (trans_category.get(i).equals("Food")) {
                    catFood = catFood + i1;
                }
                if (trans_category.get(i).equals("Subscription")) {
                    catSubscription = catSubscription + i1;
                }
                if (trans_category.get(i).equals("Shared")) {
                    catShared = catShared + i1;
                }
                if (trans_category.get(i).equals("Entertainment")) {
                    catEntertainment = catEntertainment + i1;
                }
                if (trans_category.get(i).equals("Grocery")) {
                    catGrocery = catGrocery + i1;
                }
                if (trans_category.get(i).equals("Clothing")) {
                    catClothing = catClothing + i1;
                }
                if (trans_category.get(i).equals("Salary")) {
                    catSalary = catSalary + i1;
                }
                if (trans_category.get(i).equals("Others")) {
                    catOthers = catOthers + i1;
                }
            }

            i++;
        }


        ArrayList<PieEntry> entries = new ArrayList<>();


        if (catFood != 0) {
            entries.add(new PieEntry(catFood, "Food"));
        }
        if (catSubscription != 0) {
            entries.add(new PieEntry(catSubscription, "Subscription"));
        }
        if (catShared != 0) {
            entries.add(new PieEntry(catShared, "Shared"));
        }
        if (catEntertainment != 0) {
            entries.add(new PieEntry(catEntertainment, "Entertainment"));
        }
        if (catGrocery != 0) {
            entries.add(new PieEntry(catGrocery, "Grocery"));
        }
        if (catClothing != 0) {
            entries.add(new PieEntry(catClothing, "Clothing"));
        }
        if (catSalary != 0) {
            entries.add(new PieEntry(catSalary, "Salary"));
        }
        if (catOthers != 0) {
            entries.add(new PieEntry(catOthers, "Others"));
        }

        if(entries.isEmpty()) Toast.makeText(getContext(), "No data.", Toast.LENGTH_SHORT).show();


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
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
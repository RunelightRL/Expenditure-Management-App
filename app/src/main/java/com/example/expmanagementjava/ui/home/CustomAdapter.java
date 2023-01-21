package com.example.expmanagementjava.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.expmanagementjava.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList trans_id, trans_date, trans_category, trans_name, trans_amt, trans_amt_rem, trans_add_or_sub;
    //Update variables
    Dialog dialog;
    EditText transName, transAmt;
    String dbDialogDate, transCategory;
    Button accept_button;

    MyDatabaseHelper myDB = MyDatabaseHelper.getInstance(context);

    CustomAdapter(Context context,
                  ArrayList trans_id, ArrayList trans_date, ArrayList trans_category, ArrayList trans_name,
                  ArrayList trans_amount, ArrayList trans_amt_rem, ArrayList trans_add_or_sub){
        this.context = context;
        this.trans_id = trans_id;
        this.trans_date = trans_date;
        this.trans_category = trans_category;
        this.trans_name = trans_name;
        this.trans_amt = trans_amount;
        this.trans_amt_rem = trans_amt_rem;
        this.trans_add_or_sub = trans_add_or_sub;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        // Update function
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Click working" , Toast.LENGTH_SHORT).show();

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                LayoutInflater li = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                //View mView = context.getLayoutInflater().inflate(R.layout.update_dialog,null);
                View mView = li.inflate(R.layout.update_dialog,null);
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                accept_button = mView.findViewById(R.id.button_accept);

                accept_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(context, "Delete Click Working", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }); //Updating record
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(trans_add_or_sub.get(position).toString().trim().equals("1")) {
            holder.trans_id_txt.setText(String.valueOf(trans_id.get(position)));
            holder.trans_id_txt.setTextColor(Color.parseColor("#68B47D"));
            holder.trans_name_txt.setText(String.valueOf(trans_name.get(position)));
            holder.trans_name_txt.setTextColor(Color.parseColor("#68B47D"));
            holder.trans_category_txt.setText(String.valueOf(trans_category.get(position)));
            holder.trans_category_txt.setTextColor(Color.parseColor("#68B47D"));
            holder.trans_date_txt.setText(String.valueOf(trans_date.get(position)));
            holder.trans_date_txt.setTextColor(Color.parseColor("#68B47D"));
            holder.trans_amt_txt.setText(String.valueOf("Rs." + trans_amt.get(position)));
            holder.trans_amt_txt.setTextColor(Color.parseColor("#68B47D"));
        }

        if(trans_add_or_sub.get(position).toString().trim().equals("-1")) {
            holder.trans_id_txt.setText(String.valueOf(trans_id.get(position)));
            holder.trans_id_txt.setTextColor(Color.parseColor("#FF4141"));
            holder.trans_name_txt.setText(String.valueOf(trans_name.get(position)));
            holder.trans_name_txt.setTextColor(Color.parseColor("#FF4141"));
            holder.trans_category_txt.setText(String.valueOf(trans_category.get(position)));
            holder.trans_category_txt.setTextColor(Color.parseColor("#FF4141"));
            holder.trans_date_txt.setText(String.valueOf(trans_date.get(position)));
            holder.trans_date_txt.setTextColor(Color.parseColor("#FF4141"));
            holder.trans_amt_txt.setText(String.valueOf("Rs." + trans_amt.get(position)));
            holder.trans_amt_txt.setTextColor(Color.parseColor("#FF4141"));
        }

        if(trans_add_or_sub.get(position).toString().trim().equals("0")) {
            holder.trans_id_txt.setText(String.valueOf(trans_id.get(position)));
            holder.trans_id_txt.setTextColor(Color.parseColor("#4193ff"));
            holder.trans_name_txt.setText(String.valueOf(trans_name.get(position)));
            holder.trans_name_txt.setTextColor(Color.parseColor("#4193ff"));
            holder.trans_category_txt.setText(String.valueOf(trans_category.get(position)));
            holder.trans_category_txt.setTextColor(Color.parseColor("#4193ff"));
            holder.trans_date_txt.setText(String.valueOf(trans_date.get(position)));
            holder.trans_date_txt.setTextColor(Color.parseColor("#4193ff"));
            holder.trans_amt_txt.setText(String.valueOf("Rs." + trans_amt.get(position)));
            holder.trans_amt_txt.setTextColor(Color.parseColor("#4193ff"));
        }


    }

    @Override
    public int getItemCount() {
        return trans_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView trans_id_txt, trans_date_txt, trans_category_txt, trans_name_txt,
                trans_amt_txt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            trans_id_txt = itemView.findViewById(R.id.sav_id_txt);
            trans_name_txt = itemView.findViewById(R.id.sav_name_txt);
            trans_category_txt = itemView.findViewById(R.id.sav_year_txt);
            trans_date_txt = itemView.findViewById(R.id.trans_date_txt);
            trans_amt_txt = itemView.findViewById(R.id.sav_amt_txt);
        }
    }
}

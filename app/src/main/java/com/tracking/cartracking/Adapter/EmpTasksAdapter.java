package com.tracking.cartracking.Adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.NotificationMSG;
import com.tracking.cartracking.Model.TaskModel;
import com.tracking.cartracking.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class EmpTasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    SharedPreferences prefs;
    private List<TaskModel> list ;
    private Context context;
    FirebaseDatabase database;
    DatabaseReference ref;

    String email_employee = "";
    public EmpTasksAdapter(Context context, List<TaskModel> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        prefs =  context.getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email_employee = prefs.getString(Keys.KEY_EMPLOYEE,"");
        Holder holder1 = (Holder) holder;
        String HelveticaNeueLTArabic = "fonts/bold.ttf";
        Typeface HelveticaNeueLTArabicB = Typeface.createFromAsset(context.getAssets(),
                HelveticaNeueLTArabic);

        final TaskModel news = list.get(position);

        holder1.task.setText(news.getTask());
        holder1.car_no.setText(news.getCarNumber());
        holder1.task.setTypeface(HelveticaNeueLTArabicB);
        holder1.start_date.setText(news.getStart_date());
        holder1.end_date.setText(news.getEnd_date());
        holder1.edit.setText(context.getResources().getString(R.string.send_notification));
    //    holder1.edit.setVisibility(View.GONE);
        holder1.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View views = inflater.inflate(R.layout.dialoge_add_noti_msg2, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();
                final TextView name = (TextView) views.findViewById(R.id.name);
                name.setText(news.getAdmin());
                final EditText msg = (EditText) views.findViewById(R.id.msg);


                String myFormat = "dd/MM/yyyy HH:mm";
                final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


                Button send = (Button) views.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotificationMSG note = new NotificationMSG();
                       note.setEmailEmp(news.getAdmin());
                        note.setMsg(msg.getText().toString());
                        note.setDate(sdf.format(Calendar.getInstance().getTime()) + "");
                        ref.child("notification").push().setValue(note);
                        dialog.dismiss();
                    }
                });

            }});}

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView task,start_date,end_date ,car_no;
        TextView edit;
        public Holder(View itemView) {
            super(itemView);
            task = (TextView) itemView.findViewById(R.id.task);
            car_no = (TextView) itemView.findViewById(R.id.car_Number);
            start_date = (TextView) itemView.findViewById(R.id.start_date);
            end_date= (TextView) itemView.findViewById(R.id.end_date);
            edit=(TextView) itemView.findViewById(R.id.edit);


    }}


}







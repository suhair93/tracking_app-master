package com.tracking.cartracking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tracking.cartracking.Model.EmpModelReg;
import com.tracking.cartracking.Model.NotificationMSG;
import com.tracking.cartracking.R;
import com.tracking.cartracking.Admin.notifictionActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class RegAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FirebaseDatabase database;
    DatabaseReference ref;

    private List<EmpModelReg> list ;
    private Context context;
    public RegAdapter(Context context, List<EmpModelReg> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_emp_reg, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Holder newsHolder = (Holder) holder;
        String HelveticaNeueLTArabic = "fonts/bold.ttf";
        Typeface HelveticaNeueLTArabicB = Typeface.createFromAsset(context.getAssets(),
                HelveticaNeueLTArabic);

        final EmpModelReg news = list.get(position);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        newsHolder.Name.setText(news.getName());
        newsHolder.Name.setTypeface(HelveticaNeueLTArabicB);
        newsHolder.arrive_time.setText(news.getArrivel());
       newsHolder.dep_time.setText(news.getDeparture());
        newsHolder.send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.dialoge_add_noti_msg, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();
                final TextView name=(TextView)views.findViewById(R.id.name);
                name.setText(news.getName());
                final EditText msg=(EditText)views.findViewById(R.id.msg);


                String myFormat = "dd/MM/yyyy HH:mm";
                final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


                Button send=(Button)views.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NotificationMSG note = new NotificationMSG();
                        note.setEmailEmp(news.getEmail());
                        note.setMsg(msg.getText().toString());
                        note.setDate(sdf.format(Calendar.getInstance().getTime()) + "");
                        ref.child("notification").push().setValue(note);
                        dialog.dismiss();
                    }
                });


            }
        });
        newsHolder.show_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, notifictionActivity.class);
                i.putExtra("email",news.getEmail());
                context.startActivity(i);
            }});

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView Name,dep_time,arrive_time;
        Button send_msg,show_msg;
        public Holder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.Name);
            send_msg = (Button) itemView.findViewById(R.id.send_msg);
            show_msg= (Button) itemView.findViewById(R.id.show_msg);
            arrive_time= (TextView) itemView.findViewById(R.id.arrive_time);
            dep_time= (TextView) itemView.findViewById(R.id.dep_time);


        }

    }
}







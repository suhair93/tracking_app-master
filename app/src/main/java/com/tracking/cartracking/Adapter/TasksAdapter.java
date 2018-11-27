package com.tracking.cartracking.Adapter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tracking.cartracking.Model.TaskModel;
import com.tracking.cartracking.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class TasksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FirebaseDatabase database;
    DatabaseReference ref;

    private List<TaskModel> list ;
    private Context context;
    public TasksAdapter(Context context, List<TaskModel> List1) {
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

        final Holder holder1 = (Holder) holder;
        String HelveticaNeueLTArabic = "fonts/bold.ttf";
        Typeface HelveticaNeueLTArabicB = Typeface.createFromAsset(context.getAssets(),
                HelveticaNeueLTArabic);

        final TaskModel news = list.get(position);

        holder1.task.setText(news.getTask());
        holder1.car_no.setText(news.getCarNumber());
        holder1.task.setTypeface(HelveticaNeueLTArabicB);
        holder1.start_date.setText(news.getStart_date());
        holder1.end_date.setText(news.getEnd_date());
        holder1.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.dialoge_edit_task, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();

                final TextView car=(TextView)views.findViewById(R.id.sp);
                car.setText(news.getCarNumber());

                final EditText task=(EditText)views.findViewById(R.id.task);
                task.setText(news.getTask());

                final EditText start_date=(EditText)views.findViewById(R.id.start_date);
                holder1.getTime(start_date);
                start_date.setText(news.getStart_date());

                final EditText end_date=(EditText)views.findViewById(R.id.end_date);
                holder1.getTime(end_date);
                end_date.setText(news.getEnd_date());



                Button send=(Button)views.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Query query1 = ref.child("task").orderByChild("id").equalTo(news.getId());
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    TaskModel taskModel = new TaskModel();
                                    taskModel.setTask(task.getText().toString());
                                    taskModel.setEnd_date(end_date.getText().toString());
                                    taskModel.setStart_date(start_date.getText().toString());
                                    taskModel.setAdmin(news.getAdmin());
                                    taskModel.setCarNumber(news.getCarNumber());
                                    snapshot.getRef().setValue(taskModel);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });


            }
        });
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
        TextView task,start_date,end_date ,car_no,edit;
        public Holder(View itemView) {
            super(itemView);
            task = (TextView) itemView.findViewById(R.id.task);
            car_no = (TextView) itemView.findViewById(R.id.car_Number);
            start_date = (TextView) itemView.findViewById(R.id.start_date);
            end_date= (TextView) itemView.findViewById(R.id.end_date);
            edit= (TextView) itemView.findViewById(R.id.edit);
        }
        public void getTime(final EditText editTextDate){

            final Calendar currentDate = Calendar.getInstance();
            final Calendar date = Calendar.getInstance();
            final DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                    date.set(year, monthOfYear, dayOfMonth);
                    new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            date.set(Calendar.MINUTE, minute);
                            String myFormat = "dd/MM/yyyy HH:mm";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            editTextDate.setText(sdf.format(date.getTime()));
                            // editTextDate.setText(new SimpleDateFormat("dd-MMM-yyyy h:mm").format(date.getTime()));
                        }
                    },currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

                }
            }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
            //   datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
            editTextDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    datePickerDialog.show();
                }});
        }
    }
}







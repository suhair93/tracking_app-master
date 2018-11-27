package com.tracking.cartracking.Admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tracking.cartracking.Adapter.TasksAdapter;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.TaskModel;
import com.tracking.cartracking.Model.employee;
import com.tracking.cartracking.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

public class TasksFragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences prefs;
    TextView noMember;
    RecyclerView recyclerView;
    String Admin_email ="",employee_email ="";
    List<TaskModel> List = new ArrayList<TaskModel>();
    List<String> lList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
         view= inflater.inflate(R.layout.fragment_tasks, container, false);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        Admin_email = prefs.getString(Keys.KEY_ADMIN,"");
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.dialoge_add_task, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();
                final AppCompatSpinner spinner = views.findViewById(R.id.sp);
                Getspinner(spinner);
                final EditText task_msg=(EditText)views.findViewById(R.id.task);
                final EditText start_date=(EditText)views.findViewById(R.id.start_date);
                getTime(start_date);
                final EditText end_date=(EditText)views.findViewById(R.id.end_date);
                getTime(end_date);
                Button send=(Button)views.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TaskModel task = new TaskModel();
                        task.setId(generateUniqueId());
                        task.setTask(task_msg.getText().toString());
                        task.setAdmin(Admin_email);
                        task.setCarNumber(spinner.getSelectedItem().toString());
                        task.setStart_date(start_date.getText().toString());
                        task.setEnd_date(end_date.getText().toString());

                        ref.child("task").push().setValue(task);
                        dialog.dismiss();


                      }
                });



            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);



        ref.child("task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TaskModel taskModel = snapshot.getValue(TaskModel.class);
                    if (taskModel.getAdmin().equals(Admin_email)) {
                        List.add(taskModel);

                        if(List.size()==0)
                            noMember.setVisibility(View.VISIBLE);
                        else {
                            TasksAdapter nAdapter = new TasksAdapter(getContext(), List);
                            recyclerView.setAdapter(nAdapter);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());


                        }

                    }
                }

                Collections.reverse(List);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ref.child("employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    employee employee = snapshot.getValue(employee.class);
                    if (employee.getAdmin().equals(Admin_email)) {
                        lList.add(employee.getCar_no());

                    }
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }
    public void Getspinner(AppCompatSpinner spinner) {


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,lList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                ((TextView) view).setTextColor(Color.BLACK);
                if (item != null) {



                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






    }

    public void getTime(final EditText editTextDate){

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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


    public static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
}

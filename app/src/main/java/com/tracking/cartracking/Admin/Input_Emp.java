package com.tracking.cartracking.Admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tracking.cartracking.Model.EmpModelReg;
import com.tracking.cartracking.Model.employee;
import com.tracking.cartracking.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import info.androidhive.barcode.BarcodeReader;

public class Input_Emp extends AppCompatActivity   {

    BarcodeReader barcodeReader;
    String emailEmployee ="",emailAdmin= "", nameEmployee ="";
    Calendar myCalendar;
    TextView Time ;
    FirebaseDatabase database;
    DatabaseReference ref;
    String car_no = "";
    DatePickerDialog.OnDateSetListener date1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialoge_scan_emp);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        Bundle extre = getIntent().getExtras();
        if(extre != null){
            car_no = extre.getString("code");
        }

        final AppCompatSpinner spinner = (AppCompatSpinner) findViewById(R.id.sp);
        Getspinner(spinner);
        Time =  findViewById(R.id.time);

        String myFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Time.setText(sdf.format(Calendar.getInstance().getTime()) + "");


        final EditText carNo =  findViewById(R.id.car_id);
        carNo.setText(car_no);

        myCalendar = Calendar.getInstance();
        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        Button register = (Button) findViewById(R.id.register);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(carNo.getText().toString())) {
                            // الرسالة التى تظهر للمستخدم
                            Toast.makeText(getApplicationContext(), " Enter car number ", Toast.LENGTH_SHORT).show();
                            return;

                        } else if (spinner.getSelectedItem().toString().equals("Select Status ...")) {
                            Toast.makeText(getApplicationContext(), "Select  status ", Toast.LENGTH_SHORT).show();
                        }else{


                        Query fireQuery = ref.child("employee").orderByChild("car_no").equalTo(carNo.getText().toString());
                        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // ازا غير موجود قم بتخزينه
                                if (dataSnapshot.getValue() == null) {
                                    Toast.makeText(getApplicationContext(), "this employee no member ", Toast.LENGTH_LONG).show();
                                } else {

                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        employee employee = snapshot.getValue(employee.class);
                                        nameEmployee = employee.getName();
                                        emailEmployee = employee.getEmail();
                                        emailAdmin = employee.getAdmin();

                                    }
                                    Query fireQuery = ref.child("record").orderByChild("car").equalTo(carNo.getText().toString());
                                    fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // ازا غير موجود قم بتخزينه
                                            if (dataSnapshot.getValue() == null) {

                                                // اوبجكت من نوع يوزر لتخزين بيانات الادمن الجديد
                                                EmpModelReg user = new EmpModelReg();
                                                user.setEmail(emailEmployee);
                                                user.setCar(carNo.getText().toString());
                                                user.setAdmin(emailAdmin);
                                                user.setName(nameEmployee);
                                                if (spinner.getSelectedItem().toString().equals("Arrival")) {

                                                user.setArrivel(Time.getText().toString());
                                                user.setDeparture("");

                                                } else if (spinner.getSelectedItem().toString().equals("Departure")) {
                                                    user.setDeparture(Time.getText().toString());
                                                    user.setArrivel("");
                                                }

                                                ref.child("record").push().setValue(user);

                                                // حفظه ك اوبجكت في جدول اليوزر بالفيربيس

                                                // رسالة عند الانتهاء
                                                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(Input_Emp.this,MainActivity.class);
                                                finish();
                                                startActivity(i);

                                            }else {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    EmpModelReg user = snapshot.getValue(EmpModelReg.class);
                                                   if(user.getArrivel().equals("") && spinner.getSelectedItem().toString().equals("Arrival")){


                                                           user.setEmail(emailEmployee);
                                                           user.setCar(carNo.getText().toString());
                                                           user.setAdmin(emailAdmin);
                                                           user.setName(nameEmployee);
                                                           user.setArrivel(Time.getText().toString());

                                                       snapshot.getRef().setValue(user);
                                                   }else if (user.getDeparture().equals("") && spinner.getSelectedItem().toString().equals("Departure")){
                                                       user.setEmail(emailEmployee);
                                                       user.setCar(carNo.getText().toString());
                                                       user.setAdmin(emailAdmin);
                                                       user.setName(nameEmployee);

                                                       user.setDeparture(Time.getText().toString());
                                                       snapshot.getRef().setValue(user);
                                                   }

                                                }
                                                // رسالة عند الانتهاء
                                                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(Input_Emp.this,MainActivity.class);
                                                finish();
                                                startActivity(i);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                            // رساله خطأ
                                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                    });


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                                // رساله خطأ
                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });


                    }}
                }
        );




    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date currentTime = Calendar.getInstance().getTime();

        Time.setText(sdf.format(Calendar.getInstance().getTime()) + "");

    }
    public void getdate(final EditText editTextDate){

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(Input_Emp.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(Input_Emp.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        String myFormat = "dd-mm-yyyy HH:mm";
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
    public void Getspinner(AppCompatSpinner spinner) {


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.arrival_departure));

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
}

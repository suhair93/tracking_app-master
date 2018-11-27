package com.tracking.cartracking.Employees;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tracking.cartracking.Admin.MainActivity;
import com.tracking.cartracking.LoginActivity;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.employee;
import com.tracking.cartracking.Model.user;
import com.tracking.cartracking.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;
import static com.tracking.cartracking.Employees.MainActivityEmployee.title;

public class Emp_homeFragment extends Fragment {
    View view;
    String Select;
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences prefs;
    String email = "" ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
         ///////////////////////////////

         view= inflater.inflate(R.layout.emp_fragment_home, container, false);
        title.setText(getResources().getString(R.string.home));
        ////////////////////////////

        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email = prefs.getString(Keys.KEY_EMPLOYEE, "");

        ///////////////////////////////
        final ImageView barcode_btn=(ImageView)view.findViewById(R.id.barcode_btn);
        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //////////////////////////////

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        Query fireQuery = ref.child("employee").orderByChild("email").equalTo(email);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        employee e = snapshot.getValue(employee.class);
                        String  car_number = e.getCar_no();
                        String name = e.getName();

                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(car_number, BarcodeFormat.CODABAR,300,300);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            barcode_btn.setImageBitmap(bitmap);
                        } catch (WriterException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
                else {

                        Toast.makeText(getActivity(), "no result", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "no connected internet", Toast.LENGTH_SHORT).show();}


        });








        return view;
    }


}

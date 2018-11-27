package com.tracking.cartracking.Employees;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.tracking.cartracking.Adapter.EmpTasksAdapter;
import com.tracking.cartracking.Adapter.NotificationsAdapter;
import com.tracking.cartracking.Adapter.TasksAdapter;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.NotificationMSG;
import com.tracking.cartracking.Model.TaskModel;
import com.tracking.cartracking.Model.employee;
import com.tracking.cartracking.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

public class Emp_TasksFragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference ref;
    TextView noMember;
    RecyclerView recyclerView;
    SharedPreferences prefs;
    String email_employee = "";
    List<TaskModel> lList = new ArrayList<TaskModel>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
         view= inflater.inflate(R.layout.emp_fragment_tasks, container, false);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref = database.getReference();
        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email_employee = prefs.getString(Keys.KEY_EMPLOYEE,"");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);
        // GetNotification(Token);








        Query fireQuery = ref.child("employee").orderByChild("email").equalTo(email_employee);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        employee e = snapshot.getValue(employee.class);
                        final String  car_number = e.getCar_no();

                        ref.child("task").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                lList.clear();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    TaskModel employee = snapshot.getValue(TaskModel.class);
                                    if (employee.getCarNumber().equals(car_number)) {
                                        lList.add(employee);
                                        if(lList.size()==0)
                                            noMember.setVisibility(View.VISIBLE);
                                        else {
                                            EmpTasksAdapter nAdapter = new EmpTasksAdapter(getContext(), lList);
                                            recyclerView.setAdapter(nAdapter);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                                        }

                                    }
                                }

                                Collections.reverse(lList);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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

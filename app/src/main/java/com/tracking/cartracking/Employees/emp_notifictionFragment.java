package com.tracking.cartracking.Employees;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.tracking.cartracking.Adapter.NotificationsAdapter;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.NotificationMSG;
import com.tracking.cartracking.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

public class emp_notifictionFragment extends Fragment {
    TextView noMember;
    RecyclerView recyclerView;
    List<NotificationMSG> lList = new ArrayList<NotificationMSG>();
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences prefs;
    String email_employee = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        View view= inflater.inflate(R.layout.emp_fragment_notifiction, container, false);

        ImageView Back_btn=(ImageView)view.findViewById(R.id.Back_btn);
        noMember=(TextView)view.findViewById(R.id.no_member) ;
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email_employee = prefs.getString(Keys.KEY_EMPLOYEE,"");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);
       // GetNotification(Token);




        ref.child("notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationMSG employee = snapshot.getValue(NotificationMSG.class);
                    if (employee.getEmailEmp().equals(email_employee)) {
                        lList.add(employee);
                        if(lList.size()==0)
                            noMember.setVisibility(View.VISIBLE);
                        else {
                            NotificationsAdapter nAdapter = new NotificationsAdapter(getContext(), lList);
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


        return view;
    }

}

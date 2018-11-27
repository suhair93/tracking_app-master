package com.tracking.cartracking.Admin;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tracking.cartracking.Adapter.EmpAdapter;
import com.tracking.cartracking.Adapter.RegAdapter;
import com.tracking.cartracking.Model.EmpModel;
import com.tracking.cartracking.Model.EmpModelReg;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.employee;
import com.tracking.cartracking.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

public class RegistryFragment extends Fragment {
    View view;
    FirebaseDatabase database;
    DatabaseReference ref;
    TextView noMember;
    String email_admin = "";
    RecyclerView recyclerView;
    SharedPreferences prefs;
    List<EmpModelReg> lList = new ArrayList<EmpModelReg>();
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

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);
        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email_admin = prefs.getString(Keys.KEY_ADMIN, "");
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref.child("record").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EmpModelReg employee = snapshot.getValue(EmpModelReg.class);
                    if (employee.getAdmin().equals(email_admin)) {
                        lList.add(employee);
                        if(lList.size()==0) {
                            noMember.setText(getResources().getString(R.string.no_any_emp));
                            noMember.setVisibility(View.VISIBLE);
                        }else {
                            RegAdapter nAdapter = new RegAdapter(getContext(), lList);
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

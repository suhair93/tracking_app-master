package com.tracking.cartracking.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.tracking.cartracking.Adapter.EmpAdapter;
import com.tracking.cartracking.Adapter.TasksAdapter;
import com.tracking.cartracking.AddAccountActivity;
import com.tracking.cartracking.LoginActivity;
import com.tracking.cartracking.Model.EmpModel;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.TaskModel;
import com.tracking.cartracking.Model.employee;
import com.tracking.cartracking.Model.user;
import com.tracking.cartracking.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.content.Context.MODE_PRIVATE;

public class EmployeesFragment extends Fragment {
    View view;
    ProgressDialog dialog1;
    FirebaseDatabase database;
    DatabaseReference ref;
    String type_user ="employee",city ="", name_org="", email_admin="";
    TextView noMember;
    RecyclerView recyclerView;
    SharedPreferences prefs;
    EmpAdapter nAdapter;
    List<employee> lList = new ArrayList<employee>();
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
        prefs =  getActivity().getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        city = prefs.getString(Keys.KEY_CITY, "");
        name_org = prefs.getString(Keys.KEY_ORGNIZATION_NAME, "");
        email_admin = prefs.getString(Keys.KEY_ADMIN, "");
        noMember =  view.findViewById(R.id.no_member);
        dialog1 = new ProgressDialog(getActivity());
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage(getResources().getString(R.string.created));
        dialog1.setIndeterminate(true);
        dialog1.setCanceledOnTouchOutside(false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.dialoge_add_emp, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();
                TextView edit_employee=(TextView) views.findViewById(R.id.title);
                edit_employee.setText(getResources().getString(R.string.add_employee));
                final EditText name=(EditText)views.findViewById(R.id.name);
                final EditText emp_email=(EditText)views.findViewById(R.id.emp_email);
                final EditText phone_number=(EditText)views.findViewById(R.id.phone_number);
                final EditText car_number=(EditText)views.findViewById(R.id.car_number);
                final EditText car_type=(EditText)views.findViewById(R.id.car_type);
                final EditText pass=(EditText)views.findViewById(R.id.emp_password);





                Button send=(Button)views.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (TextUtils.isEmpty(emp_email.getText().toString())) {
                            // الرسالة التى تظهر للمستخدم
                            Toast.makeText(getActivity(), "Enter email address!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(pass.getText().toString())) {
                            Toast.makeText(getActivity(), "Enter password!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(name.getText().toString())) {
                            Toast.makeText(getActivity(), "Enter name!", Toast.LENGTH_SHORT).show();


                        } else if (!isEmailValid(emp_email.getText().toString())) {
                            Toast.makeText(getActivity(), "Enter correct email address!", Toast.LENGTH_SHORT).show();


                        } else{

                            dialog1.show();
                        Query fireQuery = ref.child("user").orderByChild("email").equalTo(emp_email.getText().toString());
                        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getValue() == null) {



                                    user user = new user();
                                    user.setEmail(emp_email.getText().toString());
                                    user.setPassword(pass.getText().toString());
                                    user.setTypeUser(type_user);
                                    user.setCity(city);
                                    user.setName_org(name.getText().toString());

                                    ref.child("user").push().setValue(user);

                                    employee e= new employee();
                                    e.setAdmin(email_admin);
                                    e.setEmail(emp_email.getText().toString());
                                    e.setName(name.getText().toString());
                                    e.setPassword(pass.getText().toString());
                                    e.setCar_no(car_number.getText().toString());
                                    e.setCar_type(car_type.getText().toString());
                                    e.setPhone(phone_number.getText().toString());
                                    e.setName_org(name_org);
                                    ref.child("employee").push().setValue(e);


                                    Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    dialog1.dismiss();
                                } else {

                                    dialog1.dismiss();
                                    Toast.makeText(getActivity(), "This account already exists", Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dialog1.dismiss();

                                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });

                    }}



                });



            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);

            nAdapter = new EmpAdapter(getActivity(), lList);
            recyclerView.setAdapter(nAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            ref.child("employee").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        employee employee = snapshot.getValue(employee.class);
                        if (employee.getAdmin().equals(email_admin)) {
                            lList.add(employee);
                            nAdapter.notifyDataSetChanged();
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

    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

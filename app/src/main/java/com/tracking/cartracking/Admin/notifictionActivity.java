package com.tracking.cartracking.Admin;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class notifictionActivity extends AppCompatActivity {
    TextView noMember;
    RecyclerView recyclerView;
    List<NotificationMSG> lList = new ArrayList<NotificationMSG>();
  String email_admin = "";
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiction);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        ImageView Back_btn=(ImageView)findViewById(R.id.Back_btn);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noMember=(TextView)findViewById(R.id.no_member) ;
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        prefs =  getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE);
        email_admin = prefs.getString(Keys.KEY_ADMIN,"");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);

        ref.child("notification").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationMSG employee = snapshot.getValue(NotificationMSG.class);
                    if (employee.getEmailEmp().equals(email_admin)) {
                        lList.add(employee);
                        if(lList.size()==0)
                            noMember.setVisibility(View.VISIBLE);
                        else {
                            NotificationsAdapter nAdapter = new NotificationsAdapter(notifictionActivity.this, lList);
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
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

package com.tracking.cartracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tracking.cartracking.Model.user;

import java.sql.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddAccountActivity extends AppCompatActivity {
    ProgressDialog dialog1;
    FirebaseDatabase database;
    DatabaseReference ref;
    String type_user ="admin",selectedName ="";
    String[] cityS ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        TextView lable=(TextView)findViewById(R.id.lable);
        lable.setText(getResources().getString(R.string.create_a_company_account));
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        final EditText name=(EditText)findViewById(R.id.name);
        final EditText password=(EditText)findViewById(R.id.password);
        final EditText email=(EditText)findViewById(R.id.email);
        final Spinner city= findViewById(R.id.city);
        cityS = new String[12];
        cityS[0] = 	"select ";
        cityS[1] = 	"Jeddah";
        cityS[2] = 	"Mecca";
        cityS[3] = 	"Medina";
        cityS[4] = 	"Al-Ahsa";
        cityS[5] = 	"Ta'if";
        cityS[6] = 	"Dammam";
        cityS[7] = 	"Buraidah";
        cityS[8] = 	"Khobar";
        cityS[9] = 	"Tabuk";
        cityS[10] = 	"Riyadh";
        cityS[11] = "other";

        ArrayAdapter<String>  arrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.tv, cityS);
        city.setAdapter(arrayAdapter);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                ((TextView) view).setTextColor(Color.BLACK);
                if (item != null) {

                    selectedName =  cityS[position];

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialog1 = new ProgressDialog(this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage(getResources().getString(R.string.created));
        dialog1.setIndeterminate(true);
        dialog1.setCanceledOnTouchOutside(false);


        ImageView Back_btn=(ImageView)findViewById(R.id.Back_btn);
        Back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAccountActivity.this.finish();
            }
        });

        Button register=(Button)findViewById(R.id.register);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(email.getText().toString())) {
                            // الرسالة التى تظهر للمستخدم
                            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                            return;
                        } else
                            if (!isEmailValid(email.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Enter correct email address!", Toast.LENGTH_SHORT).show();
                        return;}
                        else
                            if(password.getText().toString().length()<6 &&!isValidPassword(password.getText().toString())){
                            System.out.println("Not Valid");
                            Toast.makeText(getApplicationContext(), "Enter strong password minimam 6 char]!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        if(TextUtils.isEmpty(name.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();

                        }else
                        if (TextUtils.isEmpty(password.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                            return;
                        }else



                        if(selectedName.equals("select")){
                            Toast.makeText(getApplicationContext(), "Select city!", Toast.LENGTH_SHORT).show();





                        }else{
                            // ظهور علامة التحميل
                            dialog1.show();
// هذه الدالة خاصه بالبحث في الفيربيس للتأكد من ان الايميل الذي تم ادخاله غير موجود بالداتا بيس
                        //user اسم الجدول الذي يتم تخزين فيه كل حسابات المسخدمين
                        //id هو العمود الي يتم تخزين فيه الايميل
                        // email وهو متغير الستلانج اللي خزنت فيه ليتم مقارنته بالاوبجكت الموجود
                        Query fireQuery = ref.child("user").orderByChild("email").equalTo(email.getText().toString());
                        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // ازا غير موجود قم بتخزينه
                                if (dataSnapshot.getValue() == null) {


                                    // اوبجكت من نوع يوزر لتخزين بيانات الادمن الجديد
                                    user user = new user();
                                    user.setEmail(email.getText().toString());
                                    user.setPassword(password.getText().toString());
                                    user.setTypeUser(type_user);
                                    user.setCity(selectedName);

                                    user.setName_org(name.getText().toString());


                                    // حفظه ك اوبجكت في جدول اليوزر بالفيربيس
                                    ref.child("user").push().setValue(user);
                                    ref.child("admin").push().setValue(user);

                                    // رسالة عند الانتهاء
                                    Toast.makeText(getApplicationContext(), "Thank you , success regester", Toast.LENGTH_LONG).show();
                                    // الانتقال اللي تسجيل الدخول
                                    startActivity(new Intent(AddAccountActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة
                                    dialog1.dismiss();
                                    Toast.makeText(getApplicationContext(), "This account already exists", Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dialog1.dismiss();
                                // رساله خطأ
                                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }}
                }
        );
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}

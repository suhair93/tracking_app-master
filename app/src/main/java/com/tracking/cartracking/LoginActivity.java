package com.tracking.cartracking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tracking.cartracking.Admin.MainActivity;
import com.tracking.cartracking.Employees.MainActivityEmployee;
import com.tracking.cartracking.Model.Keys;
import com.tracking.cartracking.Model.user;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {
    EditText username,pass ;
    Button login,new_account;
    ProgressDialog dialog ;
    FirebaseDatabase database;
    DatabaseReference ref;
    List<user> userlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_login);
        username=(EditText)findViewById(R.id.username);
        pass =(EditText)findViewById(R.id.password);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        userlist= new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getResources().getString(R.string.sign_in));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        login=(Button)findViewById(R.id.login);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final String email = username.getText().toString();
                        final String password = pass.getText().toString();

                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                            return;
                        }else if (!isEmailValid(email)) {
                        Toast.makeText(getApplicationContext(), "Enter correct email address!", Toast.LENGTH_SHORT).show();
                        return;



                    }

                        if (TextUtils.isEmpty(password)) {
                            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                            return;
                        }else {
                            dialog.show();
                            //authenticate user
                            // نفس الداله المستخدمه بتسجيل حساب جديد

                            Query fireQuery = ref.child("user").orderByChild("email").equalTo(email);
                            fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // ازا الحساب غير موجوديظهر مسج
                                    if (dataSnapshot.getValue() == null) {
                                        Toast.makeText(LoginActivity.this, "Not Exist", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                                    } else {
                                        List<user> searchList = new ArrayList<user>();
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            user user = snapshot.getValue(user.class);
                                            searchList.add(user);

                                        }
                                        // لوب ليقوم بالبحث عن الحساب
                                        for (int i = 0; i < searchList.size(); i++) {
                                            // ازا الايميل والباسورد صحيحة
                                            if (searchList.get(i).getEmail().equals(email) && searchList.get(i).getPassword().equals(password)) {

                                                //الاوبجكت هذا خاص بنقل البيانات من كلاس لكلاس اخر
                                                SharedPreferences.Editor editor = getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE).edit();
                                                // شرط ازا كان نوع المستخدم ادمن
                                                if (searchList.get(i).getTypeUser().equals(Keys.KEY_ADMIN)) {
                                                    // Keys.KEY_TOKEN خزن الايميل بال
                                                    //لاعتماده كid لحساب
                                                    editor.putString(Keys.KEY_ADMIN, email);
                                                    editor.putString(Keys.KEY_ORGNIZATION_NAME, searchList.get(i).getName_org());
                                                    editor.putString(Keys.KEY_CITY, searchList.get(i).getCity());
                                                    editor.apply();
                                                    dialog.dismiss();
                                                    // الانتقال لواجهة الرئيسية اللادمن

                                                    Toast.makeText(getApplicationContext(), " welcome", Toast.LENGTH_LONG).show();

                                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                                    startActivity(intent);
                                                    finish();
                                                }
                                                // ازا نوع المتسخدم طالب
                                                if (searchList.get(i).getTypeUser().equals(Keys.KEY_EMPLOYEE)) {


                                                    // خزن الايميل هنا
                                                    editor.putString(Keys.KEY_EMPLOYEE, email);
                                                    editor.apply();
                                                    dialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), " welcome", Toast.LENGTH_LONG).show();

                                                    Intent intent = new Intent(LoginActivity.this, MainActivityEmployee.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();

                                                }

                                                // غير ذلك ازا كان خطأ بكلمة المرور او اسم المستخدم
                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(LoginActivity.this, "invalid user name or password", Toast.LENGTH_SHORT).show();
                                            }

                                        }


                                        dialog.dismiss();

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    dialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "no connected internet", Toast.LENGTH_SHORT).show();
                                }


                            });

                        }




                    }
                }
        );
        new_account=(Button)findViewById(R.id.new_account);
        new_account.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // dialog1.show();
                        //  Login(username.getText().toString(), password.getText().toString());
                        Intent i = new Intent(LoginActivity.this, AddAccountActivity.class);
                        startActivity(i);
                    }
                }
        );

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}

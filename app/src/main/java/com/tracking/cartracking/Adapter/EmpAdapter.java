package com.tracking.cartracking.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.tracking.cartracking.Model.EmpModel;
import com.tracking.cartracking.Model.TaskModel;
import com.tracking.cartracking.Model.employee;
import com.tracking.cartracking.R;

import java.util.List;


public class EmpAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<employee> list ;
    private Context context;
    public EmpAdapter(Context context, List<employee> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_emp, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        Holder Holder = (Holder) holder;
        String HelveticaNeueLTArabic = "fonts/bold.ttf";
        Typeface HelveticaNeueLTArabicB = Typeface.createFromAsset(context.getAssets(),
                HelveticaNeueLTArabic);

        final employee model = list.get(position);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        Holder.Name.setText(model.getName());
        Holder.Name.setTypeface(HelveticaNeueLTArabicB);
        Holder.barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.dialoge_emp, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();
                ImageView imageView=(ImageView)views.findViewById(R.id.imageView);

                alertDialog.setNegativeButton(
                        context.getResources().getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

                String text=""+model.getCar_no() ;// Whatever you need to encode in the QR code
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.CODABAR,250,250);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


            }
        });

        Holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.dialoge_add_emp, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();
                dialog.show();
                final employee model = list.get(position);

                TextView title=(TextView)views.findViewById(R.id.title);
                title.setText(context.getResources().getString(R.string.edit_employee));
                final EditText name=(EditText)views.findViewById(R.id.name);
                name.setText(model.getName());
                final EditText emp_employee=(EditText)views.findViewById(R.id.emp_email);
                emp_employee.setText(model.getEmail());
                final EditText phone_number=(EditText)views.findViewById(R.id.phone_number);
                phone_number.setText(model.getPhone());
                final EditText car_number=(EditText)views.findViewById(R.id.car_number);
                car_number.setText(model.getCar_no());
                final EditText car_type=(EditText)views.findViewById(R.id.car_type);
                car_type.setText(model.getCar_no());
                final EditText password=(EditText)views.findViewById(R.id.emp_password);
                password.setText(model.getPassword());



                Button send=(Button)views.findViewById(R.id.send);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.setName(name.getText().toString());
                        model.setEmail(emp_employee.getText().toString());
                        model.setPassword(password.getText().toString());
                        model.setPhone(phone_number.getText().toString());
                        model.setCar_type(car_type.getText().toString());
                        model.setCar_no(car_number.getText().toString());

                        final Query query1 = ref.child("employee").orderByChild("email").equalTo(model.getEmail().toString());
                        final Query query2 = ref.child("user").orderByChild("email").equalTo(model.getEmail().toString());
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    snapshot.getRef().setValue(model);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                    snapshot.getRef().setValue(model);
                                    dialog.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dialog.dismiss();
                            }
                        });
                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView barcode;
        public Holder(View itemView) {
            super(itemView);
            Name = (TextView) itemView.findViewById(R.id.Name);
            barcode = (ImageView) itemView.findViewById(R.id.barcode);

        }

    }
}







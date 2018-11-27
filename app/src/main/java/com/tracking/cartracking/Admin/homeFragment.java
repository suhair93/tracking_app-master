package com.tracking.cartracking.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.tracking.cartracking.R;
import com.tracking.cartracking.barcode.ScanActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import static com.tracking.cartracking.Admin.MainActivity.title;

public class homeFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
         view= inflater.inflate(R.layout.fragment_home, container, false);
        title.setText(getResources().getString(R.string.home));


        ImageView barcode_btn=(ImageView)view.findViewById(R.id.barcode_btn);
        barcode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),ScanActivity.class);
                startActivity(i);


            }
        });




        return view;
    }




}

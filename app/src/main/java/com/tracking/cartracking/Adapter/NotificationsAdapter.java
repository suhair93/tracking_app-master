package com.tracking.cartracking.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tracking.cartracking.Model.NotificationMSG;
import com.tracking.cartracking.R;

import java.util.List;



public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NotificationMSG> list ;
    private Context context;
    public NotificationsAdapter(Context context, List<NotificationMSG> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notifiction, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Holder newsHolder = (Holder) holder;
        String HelveticaNeueLTArabic = "fonts/bold.ttf";
        Typeface HelveticaNeueLTArabicB = Typeface.createFromAsset(context.getAssets(),
                HelveticaNeueLTArabic);

        NotificationMSG news = list.get(position);

        newsHolder.title.setText(news.getMsg());
        newsHolder.title.setTypeface(HelveticaNeueLTArabicB);
        newsHolder.data_time.setText(news.getDate());

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
        TextView title,data_time ;
        public Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            data_time = (TextView) itemView.findViewById(R.id.date_time);

        }

    }
}







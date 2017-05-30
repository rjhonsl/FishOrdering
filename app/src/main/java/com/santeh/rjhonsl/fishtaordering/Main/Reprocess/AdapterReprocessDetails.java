package com.santeh.rjhonsl.fishtaordering.Main.Reprocess;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.Main.OrderItems.MainActivity;
import com.santeh.rjhonsl.fishtaordering.Pojo.BLBO_recordPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBConstants;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;

import java.util.List;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class AdapterReprocessDetails extends RecyclerView.Adapter<AdapterReprocessDetails.MyViewHolder> {

    private List<BLBO_recordPojo> reprocessList;
    Context context1;
    Activity activity1;
    DBaseQuery db;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtType, txtTime, txtItems;
        public ImageView imgDelete;
        LinearLayout ll_currentpipeline_items;

        public MyViewHolder(View view) {
            super(view);
            txtType = (TextView) view.findViewById(R.id.txtStoreName);
            txtTime = (TextView) view.findViewById(R.id.txtcurrentTime);
            txtItems = (TextView) view.findViewById(R.id.txtItem);
            imgDelete = (ImageView) view.findViewById(R.id.btnDelete);
        }

    }


    public AdapterReprocessDetails(List<BLBO_recordPojo> itemlist, Context context, Activity activity) {
        this.reprocessList = itemlist;
        context1 = context;
        activity1 = activity;

        db = new DBaseQuery(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orconfirmation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        db.open();

        holder.txtTime.setText(Helper.convert.LongToDateTime_Gregorian(Long.parseLong(reprocessList.get(position).getTimeSent())));
        holder.txtItems.setText(db.rearrangeReprocessBatchItems(reprocessList.get(position).getAllitems()));

        holder.txtType.setText(db.getStoreName(reprocessList.get(position).getContent().split(";")[0].split("-")[1]));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = Helper.dialogBox.yesNo(activity1, "Delete this REPROCESS?\n\nThis is permanent and not reversible.", "DELETE", "YES", "NO");
                Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        reprocessList.remove(position);
                        notifyItemRemoved(position);
                        db.deleteOne(DBConstants.Reprocess.tableName, DBConstants.Reprocess.id + " =  '" + reprocessList.get(position).getId() + "'");
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return reprocessList.size();
    }

    public void removeAt(int position) {
        reprocessList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, reprocessList.size());
        if (reprocessList.size() < 1){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.toggleNoItemImage();
                }
            }, 300);
            MainActivity.toggleSendButtonVisibility();
        }

    }



}
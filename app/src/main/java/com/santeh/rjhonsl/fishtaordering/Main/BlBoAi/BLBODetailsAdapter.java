package com.santeh.rjhonsl.fishtaordering.Main.BlBoAi;

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
public class BLBODetailsAdapter extends RecyclerView.Adapter<BLBODetailsAdapter.MyViewHolder> {

    private List<BLBO_recordPojo> blboList;
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


    public BLBODetailsAdapter(List<BLBO_recordPojo> itemlist, Context context, Activity activity) {
        this.blboList = itemlist;
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

        holder.txtTime.setText(Helper.convert.LongToDateTime_Gregorian(Long.parseLong(blboList.get(position).getTimeSent())));
        holder.txtItems.setText(db.rearrangeBatchItems(blboList.get(position).getAllitems()));
        String[] allProcessType = new String[]{"BO", "BL", "AI"};
        String theProcess = "";
        if (blboList.get(position).getContent().split(";")[0].split("-")[0].equalsIgnoreCase(allProcessType[0])){
            theProcess = "BAD ORDER";
        }else if (blboList.get(position).getContent().split(";")[0].split("-")[0].equalsIgnoreCase(allProcessType[1])){
            theProcess = "BACK LOAD";
        }else if (blboList.get(position).getContent().split(";")[0].split("-")[0].equalsIgnoreCase(allProcessType[2])){
            theProcess = "ACTUAL INVENTORY";
        }
        holder.txtType.setText(db.getStoreName(blboList.get(position).getContent().split(";")[0].split("-")[1]) + " - " + theProcess);


        final String finalTheProcess = theProcess;
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = Helper.dialogBox.yesNo(activity1, "Delete this " + finalTheProcess + "?\n\nThis is permanent and not reversible.", "DELETE", "YES", "NO");
                Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        blboList.remove(position);
                        notifyItemRemoved(position);
                        db.deleteOne(DBConstants.BLBO.tableName, DBConstants.BLBO.id + " =  '" + blboList.get(position).getId() + "'");
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
        return blboList.size();
    }

    public void removeAt(int position) {
        blboList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, blboList.size());
        if (blboList.size() < 1){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.toggleNoItemImage();
                }
            }, 300);
            MainActivity.toggleSendButtonVisibility();
        }


//        notifyItemRangeChanged(blboList.size(), blboList.size());


    }



}
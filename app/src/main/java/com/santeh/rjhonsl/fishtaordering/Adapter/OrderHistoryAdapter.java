package com.santeh.rjhonsl.fishtaordering.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.Main.Activity_OrderHistory;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.SendSMS;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.List;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private List<VarFishtaOrdering> sentHistoryList;
    Context context1;
    Activity activity1;
    public static ProgressDialog pd;
    DBaseQuery db;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTimeSent, txtItems, txtIsSent, txtStoreName;
        public ImageView btnResend;
        public FrameLayout llItems;

        public MyViewHolder(View view) {
            super(view);
            txtTimeSent = (TextView) view.findViewById(R.id.txtcurrentTime);
            txtIsSent = (TextView) view.findViewById(R.id.txtIsFailed);
            txtItems = (TextView) view.findViewById(R.id.txtItem);
            txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
            btnResend = (ImageView) view.findViewById(R.id.btnResend);

            llItems = (FrameLayout) view.findViewById(R.id.ll_items);
        }
    }


    public OrderHistoryAdapter(List<VarFishtaOrdering> historyList, Context context, Activity activity) {
        this.sentHistoryList = historyList;
        context1 = context;
        activity1 = activity;
        db = new DBaseQuery(context);
        db.open();

        Log.d("BINDVIEW", "Static Main" + sentHistoryList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_senthistory, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final VarFishtaOrdering orderingOB = sentHistoryList.get(position);
        String timeSent = Helper.convert.LongToDateTime_Gregorian(Long.parseLong(sentHistoryList.get(position).getHst_timesent()));
        if (sentHistoryList.get(position).getHst_isSent().equalsIgnoreCase("0")){
            holder.txtIsSent.setVisibility(View.VISIBLE);
        }else{
            holder.txtIsSent.setVisibility(View.GONE);
        }


        pd = new ProgressDialog(context1);
        pd.setMessage("Resending order please wait...");
        String mainInfo = sentHistoryList.get(position).getHst_message().split(";")[0];
        String storeid = mainInfo.split("-")[1];
        holder.txtStoreName.setText(db.getStoreName(storeid));
        holder.txtItems.setText(db.rearrangeItems(sentHistoryList.get(position).getHst_message()));
        holder.txtTimeSent.setText(timeSent);

        holder.btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog d = Helper.dialogBox.yesNo(activity1, "Resend this order?", "Resend", "RESEND", "CANCEL");
                Button ok = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                Button cancel = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendSMS.resendOrderHistory(activity1, context1,
                                sentHistoryList.get(position).getHst_sendTo(),
                                sentHistoryList.get(position).getHst_message(),
                                sentHistoryList.get(position).getHst_id(),
                                position+"", sentHistoryList.size()+""
                        );
                        pd.show();
                        d.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });

            }
        });

        holder.llItems.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                Helper.toast.indefinite(activity1, sentHistoryList.size()+" "+position);

                PopupMenu popup = new PopupMenu(context1, holder.btnResend);
                popup.getMenuInflater().inflate(R.menu.contextmenu_orderhistory, popup.getMenu());
                popup.setGravity(Gravity.RIGHT|Gravity.CENTER);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.itmDelete) {
                            db.deleteHistory(sentHistoryList.get(position).getHst_id());
                            removeAt(position, orderingOB);
                        }else if(item.getItemId() == R.id.itmResendOrder){
                            SendSMS.resendOrderHistory(activity1, context1,
                                    sentHistoryList.get(position).getHst_sendTo(),
                                    sentHistoryList.get(position).getHst_message(),
                                    sentHistoryList.get(position).getHst_id(),
                                    position+"",sentHistoryList.size()+""
                                    );
                            pd.show();
                        }
                        return true;
                    }
                });
                popup.show();

//                Helper.toast.indefinite(activity1, sentHistoryList.get(position).getHst_message()+"");
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return sentHistoryList.size();
    }

    public void removeAt(int position, VarFishtaOrdering orderingOBJ) {
        sentHistoryList.remove(orderingOBJ);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, sentHistoryList.size());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity_OrderHistory.toggleHistoryVisibility(sentHistoryList.size());
            }
        }, 300);

    }



}
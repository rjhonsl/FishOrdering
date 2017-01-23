package com.santeh.rjhonsl.fishtaordering.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.Main.Activity_ConfirmedOrders;
import com.santeh.rjhonsl.fishtaordering.Main.Activity_OrderHistory;
import com.santeh.rjhonsl.fishtaordering.Pojo.OrderConfirmationPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBConstants;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.List;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class ORConfirmationAdapter extends RecyclerView.Adapter<ORConfirmationAdapter.MyViewHolder> {

    private List<OrderConfirmationPojo> confirmationPojos;
    Context context1;
    Activity activity1;
    DBaseQuery db;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeSent, txtItems, txtStoreName;
        ImageView btnDelete;
        FrameLayout llItems;

        MyViewHolder(View view) {
            super(view);
            txtTimeSent = (TextView) view.findViewById(R.id.txtcurrentTime);
            txtItems = (TextView) view.findViewById(R.id.txtItem);
            txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
            btnDelete = (ImageView) view.findViewById(R.id.btnDelete);

            llItems = (FrameLayout) view.findViewById(R.id.ll_items);
        }
    }


    public ORConfirmationAdapter(List<OrderConfirmationPojo> historyList, Context context, Activity activity) {
        this.confirmationPojos = historyList;
        context1 = context;
        activity1 = activity;
        db = new DBaseQuery(context);
        db.open();

        Log.d("BINDVIEW", "Static Main" + confirmationPojos.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orconfirmation, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        String timeSent = Helper.convert.LongToDateTime_Gregorian(Long.parseLong(confirmationPojos.get(position).getTimeReceived()));



        try {
            holder.txtStoreName.setText(db.getStoreName(confirmationPojos.get(position).getCustID()) + " - OR#" +confirmationPojos.get(position).getORnumber());
//        holder.txtItems.setText(db.rearrangeItems(confirmationPojos.get(position).getContent()));
            holder.txtItems.setText(db.rearrangeBatchItems(confirmationPojos.get(position).getAllitems()));
            holder.txtTimeSent.setText(timeSent);
        }catch (Exception e){
            Log.d("Split error", e.toString());
        }


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog d = Helper.dialogBox.yesNo(activity1, "Delete order confirmation?","Warning", "YES", "NO");

                Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                        db.deleteOne(DBConstants.OrderConfirmation.tableName, DBConstants.OrderConfirmation.ORnumber + " = '"+confirmationPojos.get(position).getORnumber()+"'");
                        removeAt(position,confirmationPojos.get(position) );
                    }
                });

                Button no = (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

            }
        });

        holder.llItems.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

//                Helper.toast.indefinite(activity1, confirmationPojos.size()+" "+position);

//                PopupMenu popup = new PopupMenu(context1, holder.btnDelete);
//                popup.getMenuInflater().inflate(R.menu.contextmenu_orderhistory, popup.getMenu());
//                popup.setGravity(Gravity.RIGHT|Gravity.CENTER);
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if (item.getItemId() == R.id.itmDelete) {
//                            db.deleteHistory(confirmationPojos.get(position).getHst_id());
//                            removeAt(position, orderingOB);
//                        }else if(item.getItemId() == R.id.itmResendOrder){
//                            SendSMS.resendOrderHistory(activity1, context1,
//                                    confirmationPojos.get(position).getHst_sendTo(),
//                                    confirmationPojos.get(position).getHst_message(),
//                                    confirmationPojos.get(position).getHst_id(),
//                                    position+"", confirmationPojos.size()+""
//                                    );
//                            pd.show();
//                        }
//                        return true;
//                    }
//                });
//                popup.show();

//                Helper.toast.indefinite(activity1, confirmationPojos.get(position).getHst_message()+"");
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return confirmationPojos.size();
    }

    public void removeAt(int position, OrderConfirmationPojo orderingOBJ) {
        confirmationPojos.remove(orderingOBJ);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, confirmationPojos.size());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity_ConfirmedOrders.toggleHistoryVisibility(confirmationPojos.size());
            }
        }, 300);

    }



}
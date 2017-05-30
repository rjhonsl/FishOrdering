package com.santeh.rjhonsl.fishtaordering.Main.DeliveryConfirmation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.santeh.rjhonsl.fishtaordering.Pojo.DeliveryConfirmationPojo;
import com.santeh.rjhonsl.fishtaordering.Pojo.DeliveryItemsPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBConstants;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.SendSMS;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class DeliveryConfirmationAdapter extends RecyclerView.Adapter<DeliveryConfirmationAdapter.MyViewHolder> {

    private List<DeliveryConfirmationPojo> drlist;
    private Context context1;
    private Activity activity1;
    private DBaseQuery db;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeSent, txtItems, txtStoreName;
        ImageView btnDelete, confirmed;
        FrameLayout llItems;

        MyViewHolder(View view) {
            super(view);
            txtTimeSent = (TextView) view.findViewById(R.id.txtcurrentTime);
            txtItems = (TextView) view.findViewById(R.id.txtItem);
            txtStoreName = (TextView) view.findViewById(R.id.txtStoreName);
            btnDelete = (ImageView) view.findViewById(R.id.btnDelete);
            confirmed = (ImageView) view.findViewById(R.id.img_confirmed);

            llItems = (FrameLayout) view.findViewById(R.id.ll_items);
        }
    }


    public DeliveryConfirmationAdapter(List<DeliveryConfirmationPojo> historyList, Context context, Activity activity) {
        this.drlist = historyList;
        context1 = context;
        activity1 = activity;
        db = new DBaseQuery(context);
        db.open();

        Log.d("BINDVIEW", "Static Main" + drlist.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_delivery_confirmation, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtStoreName.setText(db.getStoreName(drlist.get(position).getCustID()) + " - DR#" +drlist.get(position).getDrNumber());
        holder.txtItems.setText(db.rearrangeBatchItems(drlist.get(position).getAllitems()));
        holder.txtTimeSent.setText(Helper.convert.LongToDateTime_Gregorian(Long.parseLong(drlist.get(position).getTimeReceived())));

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drlist.get(position).getItems_sent().length() <1){
                    Toast.makeText(context1, "You cant remove an unconfirmed delivery.", Toast.LENGTH_SHORT).show();
                }else{
                    final Dialog d = Helper.dialogBox.yesNo(activity1, "Are you sure you want to remove this delivery confirmation?", "DELETE", "YES", "NO");
                    Button yes = (Button) d.findViewById(R.id.btn_dialog_yesno_opt1);
                    Button no= (Button) d.findViewById(R.id.btn_dialog_yesno_opt2);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context1, "DR#"+ drlist.get(position).getDrNumber()+ " has been removed." , Toast.LENGTH_SHORT).show();
                            db.deleteOne(DBConstants.DeliveryConfirmation.tableName, DBConstants.DeliveryConfirmation.drNumber + " = '" + drlist.get(position).getDrNumber() + "'");
                            d.dismiss();
                            notifyDataSetChanged();
                            notifyItemRemoved(position);
                            removeAt(position, drlist.get(position));
                        }
                    });
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            d.dismiss();
                        }
                    });
                }
            }
        });

        int x = (drlist.get(position).getItems_sent()+"").length();
        if (x > 0){
            holder.confirmed.setVisibility(View.VISIBLE);
        }else {
            holder.confirmed.setVisibility(View.GONE);
        }

        holder.llItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(activity1, R.style.PauseDialog);//
                d.requestWindowFeature(Window.FEATURE_NO_TITLE); //notitle
                d.setContentView(R.layout.dialog_dr_confirmation_detail);//Set the xml view of the dialog
                d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = d.getWindow();
                lp.copyFrom(window.getAttributes());

                // This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(lp);

                ImageButton btnClose = (ImageButton) d.findViewById(R.id.btnClose);
                Button btnSendDR = (Button) d.findViewById(R.id.btnSendDRConfirmation);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        d.dismiss();
                    }
                });

                RecyclerView rvDrContents = (RecyclerView) d.findViewById(R.id.rv_drContents);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context1);
                rvDrContents.setLayoutManager(mLayoutManager);
                rvDrContents.setItemAnimator(new FadeInRightAnimator(new OvershootInterpolator(2f)));
                activity1.registerForContextMenu(rvDrContents);

                String[] splitted_receivedItems = drlist.get(position).getAllitems().split(";");
                String[] splitted_sentItems = drlist.get(position).getItems_sent().split(";");
                Log.d("RECEIVED_ALLITEM",  drlist.get(position).getAllitems() + " itemcount: \n" + splitted_receivedItems[0] + "\n"  + splitted_receivedItems[1] + "\n" + splitted_receivedItems[2]);
                final List<DeliveryItemsPojo> itemsLIst = new ArrayList<DeliveryItemsPojo>();

                for (int i = 0; i < splitted_receivedItems.length; i++) {

                    DeliveryItemsPojo item = new DeliveryItemsPojo();

                    item.setDrNumber(drlist.get(position).getDrNumber());
                    item.setStoreID(drlist.get(position).getCustID());
                    Log.d("RECEIVED_PERITEM", splitted_receivedItems[i] +"");

                    item.setReceidItemid(splitted_receivedItems[i].split(",")[0]);
                    item.setReceivedQty(splitted_receivedItems[i].split(",")[1]);
                    item.setReceivedUnits(splitted_receivedItems[i].split(",")[2]);

                    try {
                        if (drlist.get(position).getItems_sent().length() < 1){
                            item.setActualItemID(splitted_receivedItems[i].split(",")[0]);
                            item.setActualQty(splitted_receivedItems[i].split(",")[1]);
                            item.setActualunits(splitted_receivedItems[i].split(",")[2]);
                        }else{
                            item.setActualItemID(splitted_sentItems[i].split(",")[0]);
                            item.setActualQty(splitted_sentItems[i].split(",")[1]);
                            item.setActualunits(splitted_sentItems[i].split(",")[2]);
                        }
                    }catch (Exception e){
                        Log.d("ERROR in splitting", e.toString());
                        item.setActualItemID(splitted_receivedItems[i].split(",")[0]);
                        item.setActualQty(splitted_receivedItems[i].split(",")[1]);
                        item.setActualunits(splitted_receivedItems[i].split(",")[2]);
                    }
                    itemsLIst.add(item);
                }//end of loop

                // populating dr items dialog
                DeliveryDetailsAdapter drAdapter = new DeliveryDetailsAdapter(itemsLIst, context1, activity1);
                rvDrContents.setAdapter(drAdapter);
                drAdapter.notifyDataSetChanged();

                btnSendDR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String allitemstobeSent = "";
                        for (int i = 0; i < itemsLIst.size(); i++) {
                            if (i==0){
                                allitemstobeSent = itemsLIst.get(i).getActualItemID() + "," + itemsLIst.get(i).getActualQty() + "," + itemsLIst.get(i).getActualunits();
                            }else{
                                allitemstobeSent = allitemstobeSent + ";" + itemsLIst.get(i).getActualItemID() + "," + itemsLIst.get(i).getActualQty() + "," + itemsLIst.get(i).getActualunits();
                            }
                        }

                        if (itemsLIst.size() > 0){
                            final Dialog d = new Dialog(activity1);//
                            d.requestWindowFeature(Window.FEATURE_NO_TITLE); //notitle
                            d.setContentView(R.layout.dialog_sendingconf);//Set the xml view of the dialog
                            d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            Window window = d.getWindow();
                            lp.copyFrom(window.getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            window.setAttributes(lp);
                            d.show();

                            String formattedOrder = "#DR-"+drlist.get(position).getDrNumber()+"-"+drlist.get(position).getCustID()+";" +allitemstobeSent;

                            TextView txtstore = (TextView) d.findViewById(R.id.txtStoreName);
                            TextView txtCurrent = (TextView) d.findViewById(R.id.txtcurrentTime);
                            TextView txtItems = (TextView) d.findViewById(R.id.txtItem);
                            TextView txtClose = (TextView) d.findViewById(R.id.txtCloseDialog);
                            ImageView imgButton = (ImageView) d.findViewById(R.id.btnFinalSend);
                            txtItems.setText(db.rearrangeItems(formattedOrder));
                            txtstore.setText(db.getStoreName(drlist.get(position).getCustID()));

                            txtCurrent.setText(Helper.convert.LongToDateTime_Gregorian(System.currentTimeMillis()));

                            final String finalFormattedOrder = formattedOrder;
                            final String finalAllitemstobeSent = allitemstobeSent;
                            imgButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final Dialog ddd = Helper.dialogBox.yesNo(activity1, "Are you sure this is the actual items you received?", "Confirm Delivery", "YES", "NO");
                                    Button yes = (Button) ddd.findViewById(R.id.btn_dialog_yesno_opt1);
                                    Button no = (Button) ddd.findViewById(R.id.btn_dialog_yesno_opt2);
                                    yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ddd.dismiss();
                                            d.dismiss();
                                            SendSMS.sendDeliveryConfirmation(activity1, context1, db.getServerNum(), finalFormattedOrder, drlist.get(position).getDrNumber(), finalAllitemstobeSent);
                                            notifyDataSetChanged();
                                            notifyItemChanged(position);
                                        }
                                    });

                                    no.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ddd.dismiss();
                                        }
                                    });
                                }
                            });

                            txtClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    d.dismiss();
                                }
                            });
                        }else{
                            Helper.toast.short_(activity1, "No items to send.");
                        }
                    }
                });


                d.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drlist.size();
    }

    public void removeAt(int position, DeliveryConfirmationPojo orderingOBJ) {
        drlist.remove(orderingOBJ);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, drlist.size());


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity_DeliveryConfirmation.toggleHistoryVisibility(drlist.size());
            }
        }, 300);
    }

}
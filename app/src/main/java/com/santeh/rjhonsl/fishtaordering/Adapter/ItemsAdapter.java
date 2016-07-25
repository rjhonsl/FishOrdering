package com.santeh.rjhonsl.fishtaordering.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.Main.MainActivity;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.Helper;
import com.santeh.rjhonsl.fishtaordering.Util.VarFishtaOrdering;

import java.util.List;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private List<VarFishtaOrdering> itemList;
    Context context1;
    Activity activity1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItem, txtQuantity;
        public ImageButton btnremove;
        public LinearLayout llitems;

        public MyViewHolder(View view) {
            super(view);
            txtItem = (TextView) view.findViewById(R.id.txtItem);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
            btnremove = (ImageButton) view.findViewById(R.id.btnRemove);
            llitems = (LinearLayout) view.findViewById(R.id.ll_items);

        }
    }


    public ItemsAdapter(List<VarFishtaOrdering> newsFeedsList, Context context, Activity activity) {
        this.itemList = newsFeedsList;
        context1 = context;
        activity1 = activity;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orders, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.txtItem.setText(itemList.get(position).getOrder_description());
        holder.txtQuantity.setText(itemList.get(position).getOrder_qty()+" "+itemList.get(position).getOrder_unit());

        holder.btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
            }
        });
        holder.llitems.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                PopupMenu popup = new PopupMenu(context1, holder.txtQuantity);
//                if (newsFeedsObj.getCurrentUserID().equalsIgnoreCase())
                popup.getMenuInflater().inflate(R.menu.contextmenu_items, popup.getMenu());

                popup.setGravity(Gravity.RIGHT);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.itmDelete) {
                            removeAt(position);
                        }else if(item.getItemId() == R.id.itmEdit){
                            String units = itemList.get(position).getItem_units();
                            final String[] pax = units.split(",");

                            final Dialog d = Helper.dialogBox.numberAndPAXpicker(activity1, itemList.get(position).getOrder_description(), 1, 1000,pax );
                            Button btnOk = (Button) d.findViewById(R.id.btn_numberAndPaxPicekr_set);
                            final NumberPicker value = (NumberPicker) d.findViewById(R.id.dialog_numandpax_value);
                            final NumberPicker pax1 = (NumberPicker) d.findViewById(R.id.dialog_numandpax_pax);
                            value.setValue(Integer.valueOf(itemList.get(position).getOrder_qty()));
                            pax1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                            value.clearFocus();
                            pax1.clearFocus();



                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    value.clearFocus();
                                    pax1.clearFocus();
                                    itemList.get(position).setOrder_qty(value.getValue()+"");
                                    itemList.get(position).setOrder_unit(pax[pax1.getValue()]+"");
                                    notifyItemRangeChanged(position, itemList.size());
                                    d.dismiss();
                                }
                            });
                        }
                        return true;
                    }
                });
                popup.show();

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void removeAt(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, itemList.size());
        if (itemList.size() < 1){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.showNoItemImage();
                }
            }, 300);
            MainActivity.toggleSendButtonVisibility();
        }


//        notifyItemRangeChanged(itemList.size(), itemList.size());


    }

}
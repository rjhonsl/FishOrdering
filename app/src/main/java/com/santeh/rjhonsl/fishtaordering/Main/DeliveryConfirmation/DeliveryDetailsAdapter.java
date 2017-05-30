package com.santeh.rjhonsl.fishtaordering.Main.DeliveryConfirmation;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.Main.OrderItems.MainActivity;
import com.santeh.rjhonsl.fishtaordering.Pojo.DeliveryItemsPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;

import java.util.List;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class DeliveryDetailsAdapter extends RecyclerView.Adapter<DeliveryDetailsAdapter.MyViewHolder> {

    private List<DeliveryItemsPojo> itemlistt;
    Context context1;
    Activity activity1;
    DBaseQuery db;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItem1, txtItem2, txtITem4;
        public EditText edtText3;
        LinearLayout ll_currentpipeline_items;

        public MyViewHolder(View view) {
            super(view);
            txtItem1 = (TextView) view.findViewById(R.id.txt_item_1);
            txtItem2 = (TextView) view.findViewById(R.id.txt_item_2);
            edtText3 = (EditText) view.findViewById(R.id.txt_item_3);
            txtITem4 = (TextView) view.findViewById(R.id.txt_item_4);
            ll_currentpipeline_items = (LinearLayout) view.findViewById(R.id.ll_currentpipeline_items);
        }

    }


    public DeliveryDetailsAdapter(List<DeliveryItemsPojo> itemlist, Context context, Activity activity) {
        this.itemlistt = itemlist;
        context1 = context;
        activity1 = activity;

        db = new DBaseQuery(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dr_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        db.open();
        if (position%2 == 1){
            holder.ll_currentpipeline_items.setBackground(activity1.getResources().getDrawable(R.drawable.bg_orange_50_darkpressed));
        }else{
            holder.ll_currentpipeline_items.setBackground(activity1.getResources().getDrawable(R.drawable.bg_gray_darkpressed));
        }


        holder.txtItem1.setText(db.getitemDescription(itemlistt.get(position).getActualItemID()));
        holder.txtItem2.setText(itemlistt.get(position).getReceivedQty());
        holder.edtText3.setText(itemlistt.get(position).getActualQty());
        holder.txtITem4.setText(itemlistt.get(position).getActualunits());

        holder.edtText3.setSelectAllOnFocus(true);

        holder.edtText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (holder.edtText3.getText().toString().length() <1){

                    holder.edtText3.setText("0");
                    holder.edtText3.setSelection(0,holder.edtText3.getText().length());
                    itemlistt.get(position).setActualQty("0");
                }else {
                    itemlistt.get(position).setActualQty(holder.edtText3.getText().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemlistt.size();
    }

    public void removeAt(int position) {
        itemlistt.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, itemlistt.size());
        if (itemlistt.size() < 1){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.toggleNoItemImage();
                }
            }, 300);
            MainActivity.toggleSendButtonVisibility();
        }


//        notifyItemRangeChanged(itemlistt.size(), itemlistt.size());


    }



}
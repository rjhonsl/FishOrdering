package com.santeh.rjhonsl.fishtaordering.Main.BlBoAi;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.Pojo.BLBO_itemPojo;
import com.santeh.rjhonsl.fishtaordering.R;
import com.santeh.rjhonsl.fishtaordering.Util.DBaseQuery;

import java.util.List;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class AdapterBLBODetails extends RecyclerView.Adapter<AdapterBLBODetails.MyViewHolder> {

    private List<BLBO_itemPojo> itemlistt;
    Context context1;
    Activity activity1;
    DBaseQuery db;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtitemName, txtUnit;
        public EditText edtExpectedQty, edtActualQty;
        public ImageButton btnRemove;
        LinearLayout llBlBo_item;

        public MyViewHolder(View view) {
            super(view);
            txtitemName = (TextView) view.findViewById(R.id.txt_item_1);
            edtActualQty = (EditText) view.findViewById(R.id.txt_item_2);
            edtExpectedQty = (EditText) view.findViewById(R.id.txt_item_3);
            txtUnit = (TextView) view.findViewById(R.id.txt_item_4);
            llBlBo_item = (LinearLayout) view.findViewById(R.id.ll_currentpipeline_items);
            btnRemove = (ImageButton) view.findViewById(R.id.btnRemoveItem);
        }

    }


    public AdapterBLBODetails(List<BLBO_itemPojo> itemlist, Context context, Activity activity) {
        this.itemlistt = itemlist;
        context1 = context;
        activity1 = activity;

        db = new DBaseQuery(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blbo_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        db.open();
        if (holder.getAdapterPosition()%2 == 1){
            holder.llBlBo_item.setBackground(activity1.getResources().getDrawable(R.drawable.bg_orange_50_darkpressed));
        }else{
            holder.llBlBo_item.setBackground(activity1.getResources().getDrawable(R.drawable.bg_gray_darkpressed));
        }


        holder.txtitemName.setText(itemlistt.get(holder.getAdapterPosition()).getActualDescription());
        holder.btnRemove.setVisibility(View.VISIBLE);
        holder.edtActualQty.setText(itemlistt.get(holder.getAdapterPosition()).getActualQty());
        holder.edtExpectedQty.setText(itemlistt.get(holder.getAdapterPosition()).getActualQty());
        holder.txtUnit.setText(itemlistt.get(holder.getAdapterPosition()).getActualItemUnit());

        holder.edtExpectedQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (holder.edtExpectedQty.getText().toString().length() <1){

                    holder.edtExpectedQty.setText("0");
                    holder.edtExpectedQty.setSelection(0, holder.edtExpectedQty.getText().length());
                    itemlistt.get(holder.getAdapterPosition()).setActualQty("0");
                }else {
                    Log.d("ACTUAL_ITEM", ""+position + " - " + editable.toString());
                    itemlistt.get(holder.getAdapterPosition()).setActualQty(holder.edtExpectedQty.getText().toString());
                }
//                notifyDataSetChanged();

            }
        });


        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAt(holder.getAdapterPosition());
            }
        });


//        holder.txtitemName.setText(db.getitemDescription(itemlistt.get(position).getActualItemID()));
//        holder.edtActualQty.setText(itemlistt.get(position).getReceivedQty());
//        holder.edtExpectedQty.setText(itemlistt.get(position).getActualQty());
//        holder.txtUnit.setText(itemlistt.get(position).getActualunits());
//
//        holder.edtExpectedQty.setSelectAllOnFocus(true);
//
//        holder.edtExpectedQty.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (holder.edtExpectedQty.getText().toString().length() <1){
//                    itemlistt.get(position).setActualQty("0");
//                    holder.edtExpectedQty.setText("0");
//                    holder.edtExpectedQty.setSelection(0,holder.edtExpectedQty.getText().length());
//                }else {
//                    itemlistt.get(position).setActualQty(holder.edtExpectedQty.getText().toString());
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return itemlistt.size();
    }

    private void removeAt(int position) {
        itemlistt.remove(position);
        notifyItemRemoved(position);

//        ProgressDialog pd = new ProgressDialog(context1);
//        pd.setIndeterminate(true);
//        pd.setCancelable(false);
//        pd.show();
//
//        try {
//            Thread.sleep(500);
//            pd.dismiss();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            pd.dismiss();
//        }


//        if (itemlistt.size() < 1){
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    MainActivity.toggleNoItemImage();
//                }
//            }, 300);
//            MainActivity.toggleSendButtonVisibility();
//        }
    }



}
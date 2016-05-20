package com.santeh.rjhonsl.fishtaordering.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.santeh.rjhonsl.fishtaordering.R;

import java.util.ArrayList;

/**
 * Created by rjhonsl on 5/18/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> newsFeedsList;
    private LruCache<Integer, Bitmap> imageCache;

//    private RequestQueue queue;
    Context context1;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtItem, txtQuantity;
        public ImageView imagePreview;

        public MyViewHolder(View view) {
            super(view);
            txtItem = (TextView) view.findViewById(R.id.txtItem);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
//            imagePreview = (ImageView) view.findViewById(R.id.img_content);
        }
    }


    public RecyclerViewAdapter(ArrayList<String> newsFeedsList, Context context) {
        this.newsFeedsList = newsFeedsList;
        context1 = context;

//        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
//        final int cacheSize = maxMemory / 8;
//        imageCache = new LruCache<>(cacheSize);

//        queue = Volley.newRequestQueue(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orders, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        final VarFishbook newsFeedsObj = newsFeedsList.get(position);
        holder.txtItem.setText(newsFeedsList.get(position));


    }

    @Override
    public int getItemCount() {
        return newsFeedsList.size();
    }

}
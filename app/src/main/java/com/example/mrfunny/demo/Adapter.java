package com.example.mrfunny.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    public List<Quote> quotes = new ArrayList<>();

    public void setData(ArrayList<Quote> quotes) {
    }

    public Adapter(Context context) {
        this.context = context;
    }

    public void addAllData(final List<Quote> data) {
        // Add all items.
        for (int i = 0; i < data.size(); ++i) {
            addData(i, data.get(i));
        }
        notifyDataSetChanged();
    }

    public void addAllData(final List<Quote> data, int atIndex) {
        // Add all items.
        for (int i = 0; i < data.size(); ++i) {
            addData(atIndex, data.get(i));
        }
    }

    public void addData(int i, Quote entity) {
        quotes.add(entity);
        notifyDataSetChanged();
    }

    public void initData(final List<Quote> data) {
        // Add all items.
        quotes.clear();
        quotes.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(quotes.get(position));
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDate;
        TextView txtClose;
        TextView txtVolume;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.date);
            txtClose = (TextView) itemView.findViewById(R.id.close);
            txtVolume = (TextView) itemView.findViewById(R.id.volume);
        }

        public void setData(Quote quote) {
            txtDate.setText(quote.getDate());
            txtClose.setText(quote.getClose());
            txtVolume.setText(quote.getVolume());
        }
    }
}

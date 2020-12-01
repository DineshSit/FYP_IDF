package com.navigine.naviginedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HelperAdapter extends RecyclerView.Adapter {
    List<ReviewClass> fetchDataList;
    //List<FetchData> fetchDataList;
    ReviewClass reviewClass;
    static final String TAG = "NAVIGINE.Demo";

    public HelperAdapter(List<ReviewClass> fetchDataList){
        this.fetchDataList=fetchDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        reviewClass = fetchDataList.get(position);
        viewHolderClass.place.setText(reviewClass.getLocotion());
        viewHolderClass.star.setText((int) reviewClass.getRating());
        viewHolderClass.para.setText(reviewClass.getFeedback());
    }

    @Override
    public int getItemCount() {
        return fetchDataList.size();
    }

    public class ViewHolderClass extends RecyclerView.ViewHolder{
        TextView place,star,para;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.place);
            star = itemView.findViewById(R.id.star);
            para = itemView.findViewById(R.id.para);
        }
    }
}

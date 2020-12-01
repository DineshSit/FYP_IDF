package com.navigine.naviginedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import androidx.recyclerview.widget.RecyclerView;

import com.navigine.naviginedemo.R;
import com.navigine.naviginedemo.ReviewClass;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>{

    private Context mCtx;
    private List<ReviewClass> ReviewClassList;

    public ProductAdapter(Context mCtx, List<ReviewClass> reviewClassList) {
        this.mCtx = mCtx;
        ReviewClassList = reviewClassList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_layout,parent,false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ReviewClass reviewClass = ReviewClassList.get(position);
        holder.place.setText(reviewClass.getLocotion());
        holder.star.setText((int) reviewClass.getRating());
        holder.para.setText(reviewClass.getFeedback());
    }

    @Override
    public int getItemCount() {
        return ReviewClassList.size();
    }




    class ProductViewHolder extends RecyclerView.ViewHolder{

        TextView place,star,para;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.place);
            star = itemView.findViewById(R.id.star);
            para = itemView.findViewById(R.id.para);
        }
    }
}









/*public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder {

    List<ReviewClass> fetchDataList;

    public ProductAdapterAdapter(List<ReviewClass> fetchDataList){
        this.fetchDataList=fetchDataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass viewHolderClass=(ViewHolderClass)holder;
        ReviewClass reviewClass = fetchDataList.get(position);
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
}*/

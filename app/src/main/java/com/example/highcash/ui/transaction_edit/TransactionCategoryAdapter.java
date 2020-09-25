package com.example.highcash.ui.transaction_edit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.highcash.R;
import com.example.highcash.data.db.model.TransactionCategory;
import com.example.highcash.ui.base.BaseViewHolder;
import com.example.highcash.helper.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionCategoryAdapter extends RecyclerView.Adapter<TransactionCategoryAdapter.TransactionCategoryViewHolder> {
    Context mContext;
    List<TransactionCategory> categories;
    int selected_position = -1;
    private CategoryAdapterListener listener;


    public TransactionCategoryAdapter(Context mContext, List<TransactionCategory> categories, CategoryAdapterListener listener) {
        this.categories = categories;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_account_category, parent, false);
        return new TransactionCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionCategoryViewHolder holder, int position) {
        TransactionCategory category = categories.get(position);
        holder.categoryTitle.setText(category.getName());
        Glide.with(mContext)
                .load(category.getCategoryImage())
                .into(holder.categoryImg);
        if (position == selected_position){
            holder.categoryCard.setCardBackgroundColor(AppUtils.getColor(mContext,R.color.colorPrimary));
            holder.categoryTitle.setTextColor(Color.WHITE);
        } else{
            holder.categoryCard.setCardBackgroundColor(Color.TRANSPARENT);
            holder.categoryTitle.setTextColor(Color.GRAY);
        }


        holder.categoryCard.setOnClickListener(v -> {
            if (selected_position != position){
                selected_position = position;
                notifyDataSetChanged();
                listener.animateCategory(category);
            }

        });
    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setSelectedCategoryPosition(TransactionCategory category){
        for (int i = 0 ;i<categories.size();i++){
            if(categories.get(i).getName().equals(category.getName())){
                selected_position = i;
                notifyDataSetChanged();
            }
        }
    }



    public static class TransactionCategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.transaction_category_card)
        CardView categoryCard;
        @BindView(R.id.transaction_category_name_text)
        TextView categoryTitle;
        @BindView(R.id.transaction_category_img)
        ImageView categoryImg;

        public TransactionCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface CategoryAdapterListener{
        void animateCategory(TransactionCategory category);
    }
}

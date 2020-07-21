package com.example.highcash.ui.account_editor.adapter;

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
import com.example.highcash.ui.base.BaseViewHolder;
import com.example.highcash.helper.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountCategoryAdapter extends RecyclerView.Adapter<AccountCategoryAdapter.AccountCategoryViewHolder> {
    Context mContext;
    List<AccountCategory> categories;
    int selection_position = -1;
    private CategoryAdapterListener listener;


    public AccountCategoryAdapter(Context mContext, List<AccountCategory> categories,CategoryAdapterListener listener) {
        this.categories = categories;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_account_category, parent, false);
        return new AccountCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountCategoryViewHolder holder, int position) {
        AccountCategory category = categories.get(position);
        holder.categoryTitle.setText(category.getName());
        Glide.with(mContext)
                .load(category.getCategoryImage())
                .into(holder.categoryImg);
        if (position == selection_position){
            holder.categoryCard.setCardBackgroundColor(AppUtils.getColor(mContext,R.color.colorPrimary));
            holder.categoryTitle.setTextColor(Color.WHITE);
        } else{
            holder.categoryCard.setCardBackgroundColor(Color.TRANSPARENT);
            holder.categoryTitle.setTextColor(Color.GRAY);
        }


        holder.categoryCard.setOnClickListener(v -> {
            if (selection_position != position){
                selection_position = position;
                notifyDataSetChanged();
                listener.animateCategory(category);
            }

        });
    }



    @Override
    public int getItemCount() {
        return categories.size();
    }



    public class AccountCategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.account_category_card)
        CardView categoryCard;
        @BindView(R.id.account_category_title)
        TextView categoryTitle;
        @BindView(R.id.account_category_image)
        ImageView categoryImg;

        public AccountCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface CategoryAdapterListener{
        void animateCategory(AccountCategory category);
    }
}

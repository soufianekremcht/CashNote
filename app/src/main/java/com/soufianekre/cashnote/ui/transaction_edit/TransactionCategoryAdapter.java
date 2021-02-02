package com.soufianekre.cashnote.ui.transaction_edit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.soufianekre.cashnote.R;
import com.soufianekre.cashnote.data.db.model.CashCategory;
import com.soufianekre.cashnote.ui.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionCategoryAdapter extends
        RecyclerView.Adapter<TransactionCategoryAdapter.TransactionCategoryViewHolder> {
    Context mContext;
    List<CashCategory> categories;
    int selected_position = -1;
    private final CategoryAdapterListener listener;


    public TransactionCategoryAdapter(Context mContext, List<CashCategory> categories, CategoryAdapterListener listener) {
        this.categories = categories;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new TransactionCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionCategoryViewHolder holder, int position) {
        CashCategory category = categories.get(position);
        holder.categoryTitle.setText(category.getName());
        Glide.with(holder.itemView)
                .asDrawable()
                .load(category.getImage())
                .into(holder.categoryImg);

        if (position == selected_position){
            //holder.categoryCard.setCardBackgroundColor(AppUtils.getColor(mContext,R.color.colorPrimary));
            //holder.categoryTitle.setTextColor(Color.WHITE);
            holder.categoryImg.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            holder.categoryImg.setBackgroundTintList(ColorStateList.valueOf(category.getColor()));
        } else{
            //holder.categoryCard.setCardBackgroundColor(Color.TRANSPARENT);
            //holder.categoryTitle.setTextColor(Color.GRAY);
            holder.categoryImg.setImageTintList(ColorStateList.valueOf(Color.BLACK));
            holder.categoryImg.setBackgroundTintList(ColorStateList.valueOf(
                    ContextCompat.getColor(mContext,R.color.disabled_button)));
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

    public void setSelectedCategoryPosition(CashCategory category){
        for (int i = 0 ;i<categories.size();i++){
            if(categories.get(i).getName().equals(category.getName())){
                selected_position = i;
                notifyDataSetChanged();
            }
        }
    }



    @SuppressLint("NonConstantResourceId")
    public static class TransactionCategoryViewHolder extends BaseViewHolder {
        @BindView(R.id.category_item_layout)
        LinearLayout categoryCard;
        @BindView(R.id.card_category_name_text)
        TextView categoryTitle;
        @BindView(R.id.card_category_img)
        ImageView categoryImg;

        public TransactionCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface CategoryAdapterListener{
        void animateCategory(CashCategory category);
    }
}

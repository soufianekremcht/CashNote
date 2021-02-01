package com.soufianekre.cashnote.ui.transaction_filter.filter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.soufianekre.cashnote.R;

import java.util.List;

public class MonthAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<String> months;
    public int item_selected_position = -1;

    public MonthAdapter(Context context,List<String> months) {

        this.mContext = context;
        this.months = months;
    }
    @Override
    public int getCount() {
        return months.size();
    }

    @Override
    public Object getItem(int position) {
        return months.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView monthText= new TextView(mContext);
        String month = months.get(position);
        SpannableString str = new SpannableString(months.get(position));
        monthText.setPadding(15,15,15,15);

        if (item_selected_position == position) {
            monthText.setTypeface(Typeface.DEFAULT_BOLD);
            //monthText.setBackgroundColor(ContextCompat.getColor(mContext,R.color.primary_light_green));
            str.setSpan(new BackgroundColorSpan(ContextCompat.getColor(mContext,R.color.primary_light_green))
                    , 0, month.length(), 0);
        }
        else{
            monthText.setTypeface(Typeface.DEFAULT);
            //monthText.setBackgroundColor(ContextCompat.getColor(mContext,android.R.color.transparent));
            str.setSpan(new BackgroundColorSpan(Color.TRANSPARENT),
                    0, month.length(), 0);
        }

        monthText.setText(str);

        return monthText;
    }

}

package com.soufianekre.highcash.ui.transaction_filter.filter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MonthAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> months;
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

        if (item_selected_position == position)
            str.setSpan(new BackgroundColorSpan(Color.YELLOW), 0, month.length(), 0);
        else
            str.setSpan(new BackgroundColorSpan(Color.WHITE), 0, month.length(), 0);

        monthText.setText(str);
        monthText.setPadding(15,15,15,15);
        return monthText;
    }

}

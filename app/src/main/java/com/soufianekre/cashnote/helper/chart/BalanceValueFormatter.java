package com.soufianekre.cashnote.helper.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class BalanceValueFormatter extends ValueFormatter
{

    private final DecimalFormat mFormat;
    private String suffix;

    public BalanceValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0 ");
    }

    public BalanceValueFormatter(String suffix) {
        mFormat = new DecimalFormat("###,###,##0 ");
        this.suffix = suffix;
    }

    @Override
    public String getFormattedValue(float value) {
        return mFormat.format(value);
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        if (axis instanceof XAxis) {
            return mFormat.format(value);
        } else if (value > 0) {
            return mFormat.format(value);
        } else{
            return mFormat.format(value);
        }
    }
}

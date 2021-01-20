package com.soufianekre.highcash.helper.chart;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import static com.soufianekre.highcash.helper.AppUtils.CURRENT_YEAR;

public class DayAxisFormatter extends ValueFormatter {

    private final String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };

    private final BarLineChartBase<?> chart;

    public DayAxisFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value) {

        int days = (int) value;

        int year = determineYear(days);

        int month = determineMonth(days);
        String monthName = mMonths[month % mMonths.length];
        String yearName = String.valueOf(year);

        if (chart.getVisibleXRange() > 30 * 6) {

            return monthName + " " + yearName;
        } else {
            // day of Month
            int dayOfMonth = determineDayOfMonth(days, month + 12 * (year - CURRENT_YEAR));
            String appendix = "th";

            /*
            switch (dayOfMonth) {
                case 1:
                case 21:
                case 31:
                    appendix = "st";
                    break;
                case 2:
                case 22:
                    appendix = "nd";
                    break;
                case 3:
                case 23:
                    appendix = "rd";
                    break;
            }
            */

            return dayOfMonth == 0 ? "" : dayOfMonth + " " + monthName;
        }
    }

    private int getDaysForMonth(int month, int year) {

        // month is 0-based

        if (month == 1) {
            boolean is29Feb = false;

            if (year < 1582)
                is29Feb = (year < 1 ? year + 1 : year) % 4 == 0;
            else if (year > 1582)
                is29Feb = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);

            return is29Feb ? 29 : 28;
        }

        if (month == 3 || month == 5 || month == 8 || month == 10)
            return 30;
        else
            return 31;
    }

    private int determineMonth(int dayOfYear) {

        int month = -1;
        int days = 0;

        while (days < dayOfYear) {
            month = month + 1;

            if (month >= 12)
                month = 0;

            int year = determineYear(days);
            days += getDaysForMonth(month, year);
        }

        return Math.max(month, 0);
    }

    private int determineDayOfMonth(int days, int month) {

        int count = 0;
        int daysForMonths = 0;

        while (count < month) {

            int year = determineYear(daysForMonths);
            daysForMonths += getDaysForMonth(count % 12, year);
            count++;
        }

        return days - daysForMonths;
    }


    // update this every year

    private int determineYear(int days) {
        // editable after every single year
        if (days <= 366)
            return CURRENT_YEAR;
        else if (days <= 730)
            return CURRENT_YEAR+1;
        else if (days <= 1094)
            return CURRENT_YEAR+2;
        else if (days <= 1458)
            return CURRENT_YEAR+3;
        else
            return CURRENT_YEAR+4;

    }
}

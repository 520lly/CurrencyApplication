package com.bank.bgfi.bgfibank.Util;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ayach on 8/2/16.
 */
public class AxisForma implements AxisValueFormatter {
    ArrayList<String> time;
    String o ;
    public AxisForma(ArrayList<String> time){

this.time = time ;
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {



        String date = (String)  time.toArray()[(int) value % time.size()];
        long timestamp = Long.parseLong(date) * 1000;
        SimpleDateFormat f = new SimpleDateFormat("dd,HH:mm");
        Date netDate = (new Date(timestamp));
        String s = f.format(netDate);
        return s ;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}

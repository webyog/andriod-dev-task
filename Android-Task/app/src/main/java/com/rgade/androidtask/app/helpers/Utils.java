package com.rgade.androidtask.app.helpers;


import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static String getDate(long ts){
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(ts);
        String ret=cal.get(Calendar.DAY_OF_MONTH)+" ";
        ret+=cal.getDisplayName(Calendar.MONTH,Calendar.SHORT, Locale.getDefault());
        return ret;
    }

}

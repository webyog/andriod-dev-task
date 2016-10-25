package com.rgade.androidtask.app.helpers;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String getDate(long ts){
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
        Date resultdate = new Date(ts);
        return sdf.format(resultdate);
    }
    public static String getFullDateTime(long ts){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, yyyy, HH:mm:ss a", Locale.getDefault());
        Date resultdate = new Date(ts);
        return sdf.format(resultdate);
    }



}

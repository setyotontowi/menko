package com.project.thisappistryingtomakeyoubetter.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class GeneralHelper {

    public static final int MODE_DAY = 1;
    public static final int MODE_HISTORY = 2;

    @Inject
    public GeneralHelper(){
        // empty constructor
    }


    public static SimpleDateFormat dateFormatter(){
        return new SimpleDateFormat("EEEE, dd MMMM", new Locale("id", "ID"));
    }

    public static SimpleDateFormat hourFormatter(){
        return new SimpleDateFormat("EEEE, dd MMMM: HH.mm.ss", new Locale("id", "ID"));
    }

    public static Date fromDate(Calendar calendar){
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = 0; int minute = 0; int second = 0;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hour, minute, second);

        return cal.getTime();
    }

    public static Date toDate(Calendar calendar){
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = 23; int minute = 59; int second = 59;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hour, minute, second);

        return cal.getTime();
    }
}

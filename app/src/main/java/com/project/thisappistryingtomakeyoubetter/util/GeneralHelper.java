package com.project.thisappistryingtomakeyoubetter.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GeneralHelper {
    public static SimpleDateFormat dateFormatter(){
        return new SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH);
    }

    public static Date fromDate(Calendar calendar){
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date-1);

        return cal.getTime();
    }

    public static Date toDate(Calendar calendar){
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);

        return cal.getTime();
    }
}

package com.project.thisappistryingtomakeyoubetter.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.project.thisappistryingtomakeyoubetter.R;

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

    public static void confirmationDialog(Context context, String message, ConfirmDialog confirm){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.button_positive, (dialogInterface, i) -> confirm.onPositive(dialogInterface));
        builder.setNegativeButton(R.string.button_negative, (dialogInterface, i) -> confirm.onNegative(dialogInterface));

        Dialog dialog = builder.create();
        dialog.show();
    }

    public interface ConfirmDialog{
        void onPositive(DialogInterface dialog);
        void onNegative(DialogInterface dialog);
    }
}

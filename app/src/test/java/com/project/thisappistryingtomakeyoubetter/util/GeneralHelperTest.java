package com.project.thisappistryingtomakeyoubetter.util;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class GeneralHelperTest {

    DateFormat format = new SimpleDateFormat("dd MMMM YYYY");

    @Test
    public void dateFormatter() {

    }

    @Test
    public void fromDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 0, 1);

        Calendar result = Calendar.getInstance();
        result.set(2019, 11, 31);
        Date date = result.getTime();
        Date input = GeneralHelper.fromDate(calendar);

        System.out.println(format.format(input));
        System.out.println(format.format(date));

        assertEquals(format.format(input), format.format(date));
    }

    @Test
    public void toDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 0, 1);

        Calendar result = Calendar.getInstance();
        result.set(2020, 0, 1);
        Date date = result.getTime();
        Date input = GeneralHelper.toDate(calendar);

        System.out.println(format.format(input));
        System.out.println(format.format(date));

        assertEquals(format.format(date), format.format(input));
    }


}
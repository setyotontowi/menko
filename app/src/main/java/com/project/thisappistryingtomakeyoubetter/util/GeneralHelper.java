package com.project.thisappistryingtomakeyoubetter.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class GeneralHelper {
    public static SimpleDateFormat dateFormatter(){
        return new SimpleDateFormat("EEEE, dd MMMM", Locale.ENGLISH);
    }
}

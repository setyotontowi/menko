package com.project.thisappistryingtomakeyoubetter;

import java.text.SimpleDateFormat;

public class GeneralHelper {
    public static SimpleDateFormat dateFormatter(){
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMMM");
        return format;
    }
}

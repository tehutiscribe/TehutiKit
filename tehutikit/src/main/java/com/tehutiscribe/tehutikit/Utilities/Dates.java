package com.tehutiscribe.tehutikit.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {
    public static String FormatStandardUSDateTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }

    public static SimpleDateFormat RailsDateTimeFormatter() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }
}

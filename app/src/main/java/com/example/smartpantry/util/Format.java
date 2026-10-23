package com.example.smartpantry.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class Format {

    private Format() { }

    public static String quantity(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.format(Locale.US, "%.2f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    public static String date(long epochMillis) {
        return new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date(epochMillis));
    }
}

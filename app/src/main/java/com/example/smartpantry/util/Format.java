package com.example.smartpantry.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Small helpers for showing quantities and dates to the user. */
public final class Format {

    private Format() { }

    /** 500.0 becomes "500", 0.5 becomes "0.5". */
    public static String quantity(double value) {
        if (value == Math.rint(value)) {
            return String.valueOf((long) value);
        }
        return String.format(Locale.US, "%.2f", value)
                .replaceAll("0+$", "")
                .replaceAll("\\.$", "");
    }

    /** Epoch milliseconds to a readable date such as "25 Oct 2026". */
    public static String date(long epochMillis) {
        return new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date(epochMillis));
    }
}

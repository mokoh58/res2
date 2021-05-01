package com.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class DateUtil {

    public static ZonedDateTime convertToZonedDateTime(Date dateToConvert) {
        ZoneId id = ZoneId.of("GMT+8");
        return ZonedDateTime.ofInstant(dateToConvert.toInstant(), id);
    }

    public static Date convertZDTToDate(ZonedDateTime dateToConvert) {
        Instant instant = dateToConvert.toInstant();
        Date date = Date.from(instant);
        return date;
    }
}

/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.util;

import java.time.*;
import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import com.google.cloud.Timestamp;

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

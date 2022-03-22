package com.cs.core.data;

import com.cs.core.technical.exception.CSFormatException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * Utility methods to manage date time in ISO format
 *
 * @author vallee
 */
public class ISODateTime {

  private static final SimpleDateFormat ISODATETIMEF = new SimpleDateFormat(
          "yyyy-MM-dd'T'HH:mm:ss");
  private static final SimpleDateFormat ISODATEF = new SimpleDateFormat("yyyy-MM-dd");
  private static final SimpleDateFormat ISOTIMEF = new SimpleDateFormat("'T'HH:mm:ss");

  /**
   * @param dt entry date time for conversion
   * @return a date conversion in UTC system time when defined or 0 by default
   */
  public static long toSystemDate(LocalDateTime dt) {
    return (dt == null ? 0
            : dt.toInstant(ZoneOffset.UTC)
                    .toEpochMilli());
  }

  /**
   * @param isoDateTimeExpression an iso date text expression
   * @return the conversion in UTC system time
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static long parseToLong(String isoDateTimeExpression) throws CSFormatException {
    try {
      isoDateTimeExpression = isoDateTimeExpression.trim()
              .replaceAll(" ", "");
      if (isoDateTimeExpression.length() >= 12) {
        return ISODATETIMEF.parse(isoDateTimeExpression)
                .getTime();
      } else if (isoDateTimeExpression.charAt(0) == 'T') {
        return ISOTIMEF.parse(isoDateTimeExpression)
                .getTime();
      } else {
        return ISODATEF.parse(isoDateTimeExpression)
                .getTime();
      }
    } catch (ParseException ex) {
      throw new CSFormatException("Wrong ISO Date format: " + isoDateTimeExpression, ex);
    }
  }

  /**
   * @param time system time
   * @return the converted ISO Date time
   */
  public static String toString(long time) {
    Date dateTime = new Date(time);
    return ISODATETIMEF.format(dateTime);
  }

  /**
   * return current system date in ISO Date format
   */
  public static String now() {
    return toString(System.currentTimeMillis());
  }
}

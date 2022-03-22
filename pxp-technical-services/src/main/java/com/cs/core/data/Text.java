package com.cs.core.data;

import java.util.Collection;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

/**
 * Static text management methods
 *
 * @author vallee
 */
public class Text {

  /**
   * Mirror of the unicode table from 00c0 to 017f without diacritics
   */
  private static final String REGULAR_CODES = "AAAAAAACEEEEIIII" + "DNOOOOO\u00d7\u00d8UUUUYI\u00df"
          + "aaaaaaaceeeeiiii" + "\u00f0nooooo\u00f7\u00f8uuuuy\u00fey" + "AaAaAaCcCcCcCcDd"
          + "DdEeEeEeEeEeGgGg" + "GgGgHhHhIiIiIiIi" + "IiJjJjKkkLlLlLlL" + "lLlNnNnNnnNnOoOo"
          + "OoOoRrRrRrSsSsSs" + "SsTtTtTtUuUuUuUu" + "UuUuWwYyYZzZzZzF";

  /**
   * Replace accented characters by regular ones cf. https://stackoverflow.com/questions/36626579/how-to-convert-accented-characters-in-java
   *
   * @param source the original UTF8 text
   * @return text with accented characters replacement
   */
  public static String removeDiacritic(String source) {
    char[] vysl = new char[source.length()];
    char one;
    for (int i = 0; i < source.length(); i++) {
      one = source.charAt(i);
      if (one >= '\u00c0' && one <= '\u017f') {
        one = REGULAR_CODES.charAt((int) one - '\u00c0');
      }
      vysl[i] = one;
    }
    return new String(vysl);
  }

  /**
   * Transform a business name into camel case code
   *
   * @param name business name in UTF8
   * @return corresponding camel case code
   */
  public static String toCode(String name) {
    // replace some characters by a blank space
    String spName = removeDiacritic(name).replaceAll("[:'\"_$@.&!|%<>=+%*/\\-(){}\\[\\]]", " ");
    // capitalize first letters and remove blanks
    return WordUtils.capitalizeFully(spName)
            .replaceAll("\\s+", "");
  }

  /**
   * @param code a string with trailing number like 'AXB#1234'
   * @return any trailing number at the end of code or 0
   */
  public static int getTrailingNumber(String code) {
    int i = code.length();
    while (i > 0 && Character.isDigit(code.charAt(i - 1))) {
      i--;
    }
    if (i == code.length()) {
      return 0;
    }
    return Integer.parseInt(code.substring(i));
  }

  /**
   * @param code a string with trailing number like 'AXB#1234'
   * @return the leading part without the number part
   */
  public static String getRadical(String code) {
    int i = code.length();
    while (i > 0 && Character.isDigit(code.charAt(i - 1))) {
      i--;
    }
    return code.substring(0, i);
  }

  /**
   * @param code a code with possible trailing number at end
   * @return an unique with incremented trailing number
   */
  public static String getNextCode(String code) {
    return String.format("%s%d", getRadical(code), getTrailingNumber(code) + 1);
  }

  /**
   * @param code a code with possible trailing number at end
   * @param nbDigits minimal number of digits for the trailing number
   * @return an unique with incremented trailing number
   */
  public static String getNextCode(String code, int nbDigits) {
    String intFormat = String.format("%%0%dd", nbDigits);
    return String.format("%s" + intFormat, getRadical(code), getTrailingNumber(code) + 1);
  }

  /**
   * Detect a String surrounded by characters ' and remove them Detect internal '' and replace them by simple '
   *
   * @param quotedStr an entry quoted string
   * @return the unescaped string
   */
  public static String unescapeQuotedString(String quotedStr) {
    if (quotedStr.isEmpty()) {
      return quotedStr;
    }
    while (!StringUtils.isEmpty(quotedStr) && quotedStr.charAt(0) == '\'') {
      quotedStr = quotedStr.substring(1, quotedStr.length( ) - 1);
    }
    while (!StringUtils.isEmpty(quotedStr) && quotedStr.charAt(quotedStr.length() - 1) == '\'') {
      quotedStr = quotedStr.substring(0, quotedStr.length() - 1);
    }
    return quotedStr.replaceAll("''", "'");
  }

  /**
   * Escape a String with adding simple quotes
   *
   * @param str any string
   * @return the quoted String as return
   */
  public static String escapeStringWithQuotes(String str) {
    return String.format("'%s'", str.replaceAll("'", "''"));
  }

  /**
   * Escape a Code with adding simple quotes
   *
   * @param str any string considered as code
   * @return the quoted String when the first character is illegal code or the code contains spaces
   */
  public static String escapeCSECode(String str) {
    if (Character.isDigit(str.charAt(0)) || str.charAt(0) == '-' || !str.matches("\\S+")) {
      return String.format("'%s'", str);
    }
    return str;
  }

  /**
   * @param strValue a trimmed string value
   * @return included double value for price or measurement or 0
   */
  public static double str2Num(String strValue) {
    if (strValue.isEmpty()) {
      return .0;
    }
    if (!Character.isDigit(strValue.charAt(0)) && strValue.charAt(0) != '-') {
      return .0; // not a double value
    }
    int indexOfDot = strValue.indexOf(".");
    int endDigit = 0;
    while (endDigit < strValue.length() && (Character.isDigit(strValue.charAt(endDigit))
            || (endDigit == 0 && strValue.charAt(0) == '-') || (strValue.charAt(endDigit)=='.' && indexOfDot == endDigit))) {
      endDigit++;
    }
    try {
      return Double.parseDouble(strValue.substring(0, endDigit));
    } catch (NumberFormatException ex) {
      // ignore
    }
    return .0;
  }

  /**
   * @param numValue a numeric value
   * @return its canonical String representation
   */
  public static String num2Str(double numValue) {
    return String.format("%.10f", numValue)
            .replaceFirst("0*$", "")
            .replaceFirst("\\.*$", "");
  }

  /**
   * Generate a sequenced string of nb placeHolders with a separator
   *
   * @param nb the size of the sequence
   * @param sep the separator
   * @param marker the place holder
   * @return the string of this sequence
   */
  public static String getStringSequence(int nb, char sep, String marker) {
    StringBuffer sequence = new StringBuffer();
    for (int i = 0; i < nb; i++) {
      sequence.append(marker).append(sep);
    }
    if (nb > 0) {
      sequence.setLength(sequence.length() - 1);
    }
    return sequence.toString();
  }

  /**
   * @param sep the separator
   * @param elements the elements to be joined
   * @return the equivalent of javascript join operation
   */
  public static <T> String join(String sep, Collection<T> elements) {
    if (elements.isEmpty()) {
      return "";
    }
    StringBuffer joinedStr = new StringBuffer();
    elements.forEach(element -> {
      joinedStr.append(element).append(sep);
    });
    joinedStr.setLength(joinedStr.length() - sep.length());
    return joinedStr.toString();
  }

  /**
   * @param sep the separator
   * @param elements the elements to be joined
   * @return the equivalent of javascript join operation
   */
  public static <T> String join(String sep, T... elements) {
    if (elements.length == 0) {
      return "";
    }
    StringBuffer joinedStr = new StringBuffer();
    for (T element : elements) {
      joinedStr.append(element).append(sep);
    }
    joinedStr.setLength(joinedStr.length() - sep.length());
    return joinedStr.toString();
  }

  /**
   *
   * @param sep Separator between the list of elements
   * @param elements the list of elements
   * @param format the format in which individual element needs to be modified.
   * @return
   */
  public static <T> String join(String sep, Collection<T> elements, String format) {
    if (elements.isEmpty()) {
      return "";
    }
    StringBuffer joinedStr = new StringBuffer();
    for (T element : elements) {
      joinedStr.append(String.format(format, element)).append(sep);
    };
    joinedStr.setLength(joinedStr.length() - sep.length());
    return joinedStr.toString();
  }

  /**
   * 
   * @param sep Separator between the list of elements
   * @param elements the list of elements
   * @param format the format in which individual element needs to be modified.
   * 
   * @return
   */
  public static <T> String join( String sep, Collection<T> elements , String format, Function<T,String> map) {
      if ( elements.isEmpty() )
        return "";
      StringBuffer joinedStr = new StringBuffer();
      for( T element : elements) {
        joinedStr.append(String.format(format, map.apply(element))).append( sep);
      };
      joinedStr.setLength(joinedStr.length() - sep.length());
      return joinedStr.toString();
    }
  
  /**
   * @param expression
   * @param pivot
   * @return the part of expression before pivot
   */
  public static String getBefore(String expression, String pivot) {
    int pos = expression.indexOf(pivot);
    if (pos <= 0) {
      return "";
    }
    return expression.substring(0, pos);
  }

  /**
   * @param expression
   * @param pivot
   * @return the part of expression after pivot
   */
  public static String getAfter(String expression, String pivot) {
    int pos = expression.indexOf(pivot);
    if (pos < 0) {
      return expression;
    }
    return expression.substring(pos + pivot.length());
  }
}

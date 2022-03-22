package com.cs.core.json;

import com.cs.core.data.LocaleID;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;
import org.json.simple.JSONValue;

import java.util.Collection;

/**
 * Class helper to directly build literal JSON
 *
 * @author vallee
 */
public class JSONBuilder {

  public static final StringBuffer VOID_FIELD = new StringBuffer();

  /**
   * @param key the JSON key field
   * @param value the value to be implemented
   * @return the JSON field representation in the form of "key":"value"
   */
  public static StringBuffer newJSONField(String key, LocaleID value) {
    return new StringBuffer("\"").append(key).append("\":\"").append(value).append("\"");
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented
   * @return the JSON field representation in the form of "key":"value"
   */
  public static StringBuffer newJSONField(String key, String value) {
    return new StringBuffer("\"").append(key).append("\":\"").append(value).append("\"");
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented with possible JSON special characters
   * @param escaped true to activate the escape capability
   * @return the escaped JSON field representation in the form of "key":"value"
   */
  public static StringBuffer newJSONField(String key, String value, boolean escaped) {
    if (escaped) {
      return new StringBuffer("\"").append(key).append("\":\"").append(JSONValue.escape(value)).append("\"");
    }
    return newJSONField(key, value);
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented
   * @param <T>
   * @return the JSON field representation in the form of "key":"value"
   */
  public static <T extends Enum<T>> StringBuffer newJSONField(String key, T value) {
    return new StringBuffer("\"").append(key).append("\":\"").append(value.toString()).append("\"");
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented
   * @return the JSON field representation in the form of "key":value
   */
  public static StringBuffer newJSONField(String key, long value) {
    return new StringBuffer("\"").append(key).append("\":").append(value);
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented
   * @return the JSON field representation in the form of "key":value
   */
  public static StringBuffer newJSONField(String key, int value) {
    return new StringBuffer("\"").append(key).append("\":").append(value);
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented
   * @return the JSON field representation in the form of "key":value
   */
  public static StringBuffer newJSONField(String key, double value) {
    return new StringBuffer("\"").append(key).append("\":").append(value);
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented
   * @return the JSON field representation in the form of "key":"true"
   */
  public static StringBuffer newJSONField(String key, boolean value) {
    return new StringBuffer("\"").append(key).append("\":").append(value);
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented as an open JSON string buffer structure
   * @return the JSON field representation in the form of "key":{buffer content}
   */
  public static StringBuffer newJSONField(String key, StringBuffer value) {
    return new StringBuffer("\"").append(key).append("\":{").append(value).append("}");
  }

  /**
   * @param key the JSON key field
   * @param value the value to be implemented as an JSON Content
   * @return the JSON field representation in the form of "key":{buffer content}
   */
  public static StringBuffer newJSONField(String key, IJSONContent value) {
    return newJSONField(key, value.toStringBuffer());
  }

  /**
   * @param key the JSON key field
   * @param cse a CS-Expression
   * @return the JSON field representation in the form of "key":"{cs-expression}"
   */
  public static StringBuffer newJSONField(String key, ICSEElement cse) {
    return newJSONField(key, cse.toString(), true);
  }

  /**
   * @param key the JSON key field
   * @param values the value as provided by a collection of DTO structures
   * @param <T>
   * @return the JSON field representation in the form of "key":[ "value",..]
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static <T extends ISimpleDTO> StringBuffer newJSONArray(String key, Collection<T> values)
          throws CSFormatException {
    if (values.isEmpty()) {
      return new StringBuffer("\"").append(key).append("\":[]");
    }
    StringBuffer json = new StringBuffer("\"").append(key).append("\":[");
    for (T value : values) {
      json.append(value.toJSON()).append(",");
    }
    json.setLength(json.length() - 1); // remove the last comma
    return json.append("]");
  }

  /**
   * @param key the JSON key field
   * @param values the values as provided by a simple collection of Strings
   * @return the JSON field representation in the form of "key":{buffer content}
   */
  public static StringBuffer newJSONStringArray(String key, Collection<String> values) {
    if (values.isEmpty()) {
      return new StringBuffer("\"").append(key).append("\":[]");
    }
    return new StringBuffer("\"").append(key).append("\":[\"").append(String.join("\",\"", values)).append("\"]");
  }

  /**
   * @param key the JSON key field
   * @param values the value as provided by a collection of Long
   * @return the JSON field representation in the form of "key":[value..}
   */
  public static StringBuffer newJSONLongArray(String key, Collection<Long> values) {
    if (values.isEmpty()) {
      return new StringBuffer("\"").append(key).append("\":[]");
    }
    StringBuffer json = new StringBuffer("\"").append(key).append("\":[");
    values.forEach((Number value) -> { // number is used here to indifferently
      // manage Long or Int
      json.append(value).append(",");
    });
    json.setLength(json.length() - 1); // remove the last comma
    return json.append("]");
  }

  /**
   * Helper method to assemble fields into a single json string
   *
   * @param jsonFields the fields to be assembled
   * @return a JSON string
   */
  public static String assembleJSON(StringBuffer... jsonFields) {
    StringBuffer json = new StringBuffer();
    json.append("{").append(assembleJSONBuffer(jsonFields)).append("}");
    return json.toString();
  }

  /**
   * Helper method to assemble fields into a single json buffer
   *
   * @param jsonFields the fields to be assembled
   * @return a buffer with coma separated fields
   */
  public static StringBuffer assembleJSONBuffer(StringBuffer... jsonFields) {
    int expectedSize = 0;
    for (StringBuffer jsonField : jsonFields) {
      expectedSize = expectedSize + jsonField.length() + 1;
    }

    StringBuffer jsonContent = new StringBuffer(expectedSize);
    for (StringBuffer jsonField : jsonFields) {
      if (jsonField.length() > 0) {
        jsonContent.append(jsonField).append(",");
      }
    }
    if (jsonContent.length() > 0) {
      jsonContent.setLength(jsonContent.length() - 1); // remove the last comma
    }
    return jsonContent;
  }
}

package com.cs.core.json;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.data.LocaleID;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A helper class that facilitates conversions back and forth to JSON
 *
 * @author vallee
 */
public class JSONContentParser {

  private final JSONContent content;

  /**
   * Default constructor
   */
  public JSONContentParser() {
    content = new JSONContent();
  }

  /**
   * Constructor from a JSON String
   *
   * @param json
   * @throws CSFormatException
   */
  public JSONContentParser(String json) throws CSFormatException {
    content = new JSONContent(json);
  }

  /**
   * Constructor from a JSON object
   *
   * @param json
   */
  public JSONContentParser(JSONObject json) {
    content = new JSONContent(json);
  }

  @Override
  public String toString() {
    return content.toString();
  }

  /**
   * @return true when the parsed content is empty
   */
  public boolean isEmpty() {
    return content.isEmpty();
  }

  /**
   * @return the JSON object representation of the content
   */
  public JSONObject toJSONObject() {
    return content.toJSONObject();
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as String
   */
  public String getString(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? "" : (String) value;
  }

  /**
   * @param <E>
   * @param enumType the enumeration class to be returned
   * @param key JSON field identifier
   * @return the corresponding content as enumeration
   */
  public <E extends Enum<E>> E getEnum(Class<E> enumType, String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? E.valueOf(enumType, "UNDEFINED") : E.valueOf(enumType, (String) value);
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as LocaleID
   */
  public LocaleID getLocaleID(String key) {
    return new LocaleID(getString(key));
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as boolean
   */
  public boolean getBoolean(String key) {
    Object value = content.toJSONObject().get(key);
    if (value == null) {
      return false;
    }
    return value instanceof Boolean ? (Boolean) value
            : Boolean.parseBoolean((String) content.toJSONObject().get(key));
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as long
   */
  public long getLong(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? 0
            : (Long) content.toJSONObject().get(key);
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as long internal ID
   */
  public long getIID(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? 0
            : (Long) content.toJSONObject().get(key);
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as int
   */
  public int getInt(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? 0
            : ((Number) content.toJSONObject().get(key)).intValue();
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as double
   */
  public double getDouble(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? 0
            : ((Number) content.toJSONObject().get(key)).doubleValue();
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as another JSONContentParser
   */
  public JSONContentParser getJSONParser(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? new JSONContentParser() : new JSONContentParser((JSONObject) value);
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as a JSONContent
   */
  public JSONContent getJSONContent(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? new JSONContent() : new JSONContent((JSONObject) value);
  }

  /**
   * @param key JSON field identifier
   * @return the corresponding content as JSONArray
   */
  public JSONArray getJSONArray(String key) {
    Object value = content.toJSONObject().get(key);
    return value == null ? new JSONArray() : (JSONArray) value;
  }

  /**
   * @param key JSON field identifier
   * @return the parsed content of the CSExpression
   * @throws CSFormatException
   */
  public ICSEElement getCSEElement(String key) throws CSFormatException {
    String cse = getString(key);
    if (cse.isEmpty()) {
      throw new CSFormatException("Empty CSE specification for tag " + key);
    }
    return (new CSEParser()).parseDefinition(cse);
  }
}

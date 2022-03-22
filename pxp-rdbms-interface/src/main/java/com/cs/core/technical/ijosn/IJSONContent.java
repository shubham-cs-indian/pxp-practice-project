package com.cs.core.technical.ijosn;

import com.cs.core.technical.exception.CSFormatException;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A representation of JSON content for any JSON fields
 *
 * @author vallee
 */
public interface IJSONContent {

  /**
   * @return true if the content is empty
   */
  public boolean isEmpty();

  /**
   * take a JSON content as plain text and load it
   *
   * @param json correctly formed and escaped JSON expression
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void fromString(String json) throws CSFormatException;

  /**
   * take a JSON extension content as plain text and only consider specific tags
   *
   * @param <E>
   * @param json
   * @param tags
   * @throws CSFormatException
   */
  public <E extends Enum<E>> void fromString(String json, Set<E> tags) throws CSFormatException;

  /**
   * for integration purpose, returns this JSON content as flat properties without enclosing brackets
   *
   * @return flat JSON property list like = "a":"4633","value":123
   */
  public StringBuffer toStringBuffer();

  /**
   * @param tag a JSON field identifier
   * @return true whenever the corresponding field exists in this JSON content
   */
  public boolean containsField(String tag);

  /**
   * Add or replace a JSON field inside the current object
   *
   * @param tag the json tag
   * @param value any value
   */
  public void setField(String tag, Object value);

  /**
   * Add or replace a JSON field inside the current object
   *
   * @param tag the json tag
   * @param value any JSON Content
   */
  public void setField(String tag, IJSONContent value);

  /**
   * Add or replace a JSON field inside the current object
   *
   * @param <E>
   * @param tag the json tag
   * @param value any enum value
   */
  public default <E extends Enum<E>> void setField(String tag, E value) {
    setField(tag, value.toString());
  }

  /**
   * Add or replace a JSON filed inside the current object
   *
   * @param tag the json tag
   * @param iids an array of long numbers
   */
  public void setLongArrayField(String tag, Collection<Long> iids);

  /**
   * Add or replace a JSON filed inside the current object
   *
   * @param tag the json tag
   * @param codes an array of String objects
   */
  public void setStringArrayField(String tag, Collection<String> codes);

  /**
   * @param tag the json tag
   * @param defaultValue initialize that default value if unknown
   * @return the value contained in this JSON content if already existing
   */
  public String getInitField(String tag, String defaultValue);

  /**
   * @param tag the json tag
   * @param defaultValue initialize that default value if unknown
   * @return the value contained in this JSON content if already existing
   */
  public int getInitField(String tag, int defaultValue);

  /**
   * @param tag the json tag
   * @param defaultValue initialize that default value if unknown
   * @return the value contained in this JSON content if already existing
   */
  public long getInitField(String tag, long defaultValue);

  /**
   * @param tag the json tag
   * @param defaultValue initialize that default value if unknown
   * @return the value contained in this JSON content if already existing
   */
  public double getInitField(String tag, double defaultValue);

  /**
   * @param tag the json tag
   * @param defaultValue initialize that default value if unknown
   * @return the value contained in this JSON content if already existing
   */
  public boolean getInitField(String tag, boolean defaultValue);

  /**
   * @param <E> an enumeration type
   * @param tag the json tag
   * @param enumType the enum type of E
   * @param defaultValue initialize that default value if unknown
   * @return the value contained in this JSON content if already existing
   */
  public <E extends Enum<E>> E getInitField(String tag, Class<E> enumType, E defaultValue);

  /**
   * @param tag the json tag
   * @param defaultValue initialize that default value if unknown
   * @return the value contained in this JSON content if already existing
   */
  public IJSONContent getInitField(String tag, IJSONContent defaultValue);

  /**
   * @param <T>
   * @param tag the json tag
   * @return a collection of typed elements or an empty collection by default
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public <T> List<T> getArrayField(String tag, Class<T> type) throws CSFormatException;

  /**
   * clear all contents
   */
  public void clear();

  /**
   * @param tag the json tag to be removed from the content
   */
  public void deleteField(String tag);

  /**
   * @return the JSON object representation of the content
   */
  public Object toJSONObject();
}

package com.cs.core.json;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * A wrapper of JSON content for DTOs
 *
 * @author vallee
 */
public class JSONContent implements IJSONContent {

  private JSONObject json;

  /**
   * Default constructor
   */
  public JSONContent() {
    json = new JSONObject();
  }

  /**
   * Constructor from a JSON String
   *
   * @param jsonContent
   * @throws CSFormatException
   */
  public JSONContent(String jsonContent) throws CSFormatException {
    json = StringToJSON(jsonContent);
  }

  /**
   * Constructor from a JSON object
   *
   * @param json
   */
  public JSONContent(JSONObject json) {
    this.json = json;
  }

  /**
   * Helper method to convert a string into a JSON object
   *
   * @param jsonContent
   * @return the converted JSONObject
   * @throws CSFormatException
   */
  public static JSONObject StringToJSON(String jsonContent) throws CSFormatException {
    try {
      if (jsonContent == null || jsonContent.isEmpty()) {
        return new JSONObject();
      }
      return (JSONObject) (new JSONParser()).parse(jsonContent);
    } catch (ParseException ex) {
      throw new CSFormatException("JSONContent error with " + jsonContent, ex);
    }
  }

  @Override
  public boolean isEmpty() {
    return json.isEmpty();
  }

  @Override
  public String toString() {
    return json.toJSONString();
  }

  @Override
  public void fromString(String json) throws CSFormatException {
    this.json = StringToJSON(json);
  }

  @Override
  public <E extends Enum<E>> void fromString(String json, Set<E> tags) throws CSFormatException {
    this.json.clear();
    JSONObject jsonInput = StringToJSON(json);
    tags.forEach(tag -> {
      if (jsonInput.containsKey(tag.toString())) {
        this.json.put(tag.toString(), jsonInput.get(tag.toString()));
      }
    });
  }

  /**
   * @param content source of JSON content
   */
  public void fromJSONContent(JSONContent content) {
    this.json = content.json;
  }

  /**
   * @return the JSON object representation of the content
   */
  public JSONObject toJSONObject() {
    return json;
  }

  @Override
  public StringBuffer toStringBuffer() {
    if (!json.isEmpty()) {
      String content = toString();
      return new StringBuffer(content.substring(1, content.length() - 1));
    }
    return new StringBuffer();
  }

  @Override
  public boolean containsField(String tag) {
    return json.containsKey(tag);
  }

  @Override
  public void setField(String tag, Object value) {
    json.put(tag, value);
  }

  @Override
  public void setField(String tag, IJSONContent value) {
    json.put(tag, ((JSONContent) value).toJSONObject());
  }

  @Override
  public void setLongArrayField(String tag, Collection<Long> iids) {
    JSONArray array = new JSONArray();
    iids.forEach((Long iid) -> {
      array.add(iid);
    });
    json.put(tag, array);
  }

  @Override
  public void setStringArrayField(String tag, Collection<String> codes) {
    JSONArray array = new JSONArray();
    codes.forEach((String code) -> {
      array.add(code);
    });
    json.put(tag, array);
  }

  @Override
  public String getInitField(String tag, String defaultValue) {
    if (json.get(tag) == null) {
      setField(tag, defaultValue);
      return defaultValue;
    }
    return (String) json.get(tag);
  }

  @Override
  public int getInitField(String tag, int defaultValue) {
    if (json.get(tag) == null) {
      setField(tag, defaultValue);
      return defaultValue;
    }
    Object value = json.get(tag);
    return ( value instanceof Long ? ((Long)value).intValue() : ((Integer)value).intValue() );
  }

  @Override
  public long getInitField(String tag, long defaultValue) {
    if (json.get(tag) == null) {
      setField(tag, defaultValue);
      return defaultValue;
    }
    Object value = json.get(tag);
    return ( value instanceof Long ? ((Long)value).longValue() : ((Integer)value).longValue() );
  }

  @Override
  public double getInitField(String tag, double defaultValue) {
    if (json.get(tag) == null) {
      setField(tag, defaultValue);
      return defaultValue;
    }
    return (Double) json.get(tag);
  }

  @Override
  public boolean getInitField(String tag, boolean defaultValue) {
    if (json.get(tag) == null) {
      setField(tag, defaultValue);
      return defaultValue;
    }
    Object value = json.get(tag);
    return value instanceof Boolean ? (Boolean) value : Boolean.parseBoolean((String) value);
  }

  @Override
  public <E extends Enum<E>> E getInitField(String tag, Class<E> enumType, E defaultValue) {
    if (json.get(tag) == null) {
      setField(tag, defaultValue);
      return defaultValue;
    }
    return E.valueOf(enumType, (String) json.get(tag));
  }

  @Override
  public IJSONContent getInitField(String tag, IJSONContent defaultValue) {
    if (json.get(tag) == null) {
      setField(tag, ((JSONContent) defaultValue).toJSONObject());
      return defaultValue;
    }
    return new JSONContent((JSONObject) json.get(tag));
  }

  @Override
  public <T> List<T> getArrayField(String tag, Class<T> type) throws CSFormatException {
    List<T> array = new ArrayList<>();
    if (!json.containsKey(tag)) {
      return array;
    }
    if (!(json.get(tag) instanceof JSONArray)) {
      throw new CSFormatException("Cannot convert non-array tag: " + tag + " into array");
    }
    ((JSONArray) json.get(tag)).forEach((elt) -> {
      array.add(type.cast(elt));
    });
    return array;
  }

  @Override
  public void deleteField(String tag) {
    json.remove(tag);
  }

  @Override
  public void clear() {
    json.clear();
  }
}

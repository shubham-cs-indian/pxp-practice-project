package com.cs.core.data;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import java.util.*;
import org.json.simple.JSONArray;

/**
 * A set that can only contain a single identifier in multiform
 *
 * @author vallee
 */
public class IdentifierSet {

  private final List<String> possibleKeys = new ArrayList<>();
  private final HashSet set = new HashSet();
  private String contentKey;

  /**
   * @param keys the possible set of keys used to define the identifier sets the keys must be ordered by priority
   */
  public IdentifierSet(String... keys) {
    possibleKeys.addAll(Arrays.asList(keys));
    contentKey = keys.length > 0 ? keys[0] : "";
  }

  /**
   * @return the size of the set
   */
  public int size() {
    return set.size();
  }

  public void clear() {
    set.clear();
  }

  /**
   * @return an iterator on the identifiers
   */
  public Iterator iterator() {
    return set.iterator();
  }

  /**
   * @param identifier tested ID
   * @return true if the identifier is contained in the set
   */
  public boolean contains(Object identifier) {
    return set.contains(identifier);
  }

  /**
   * @param key the key used to defined identifiers
   * @param identifiers the collection of identifiers
   */
  public void define(String key, Collection identifiers) {
    int keyIndex = possibleKeys.indexOf(key);
    if (keyIndex < 0) {
      return;
    }
    contentKey = key;
    set.clear();
    set.addAll(identifiers);
  }

  /**
   * Load the identifiers from a JSONArray that may correspond to either keys
   *
   * @param json
   */
  public void fromJSON(JSONContentParser json) {
    for (String key : possibleKeys) {
      JSONArray array = json.getJSONArray(key);
      if (array.isEmpty()) {
        continue;
      }
      set.clear();
      contentKey = key;
      set.addAll(array);
      break;
    }
  }

  /**
   * @return the content of identifiers as a collection of String
   */
  public Collection<String> toStrings() {
    return (HashSet<String>) set;
  }

  /**
   * @return the content of identifiers as a collection of Long
   */
  public Collection<Long> toLongs() {
    return (HashSet<Long>) set;
  }

  /**
   * @return a JSON Field compliant with JSONBuilder
   */
  public StringBuffer toJSONField() {
    if (contentKey.isEmpty() || set.isEmpty()) {
      return JSONBuilder.VOID_FIELD;
    }
    if (set.iterator()
            .next() instanceof String) {
      return JSONBuilder.newJSONStringArray(contentKey, toStrings());
    } else {
      return JSONBuilder.newJSONLongArray(contentKey, toLongs());
    }
  }

  /**
   * @param key the key to access the content
   * @return the collection of identifiers when the key is the one used to define the content or empty
   */
  public Collection getByKey(String key) {
    if (contentKey.equals(key)) {
      return set;
    } else {
      return new HashSet();
    }
  }

  /**
   * @return the key retained for content
   */
  public String getKey() {
    return contentKey;
  }
}

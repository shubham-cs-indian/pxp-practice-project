package com.cs.core.csexpress;

import com.cs.core.data.Text;
import com.cs.core.technical.icsexpress.ICSEMeta;

/**
 * Representation of a meta
 *
 * @author vallee
 */
public class CSEMeta extends CSEElement implements ICSEMeta {

  private final String key;
  private String value = "";

  /**
   * Single key constructor
   *
   * @param aKey the meta key
   */
  public CSEMeta(String aKey) {
    super(ElementType.META);
    key = aKey;
  }

  /**
   * Key, value constructor
   *
   * @param aKey the meta key
   * @param aValue the attached value
   */
  public CSEMeta(String aKey, String aValue) {
    super(ElementType.META);
    key = normalizeKey(aKey);
    if (aValue != null && !aValue.isEmpty()) {
      value = Text.unescapeQuotedString(aValue);
    }
  }

  /**
   * @param aKey any possible key
   * @return the key with prefix @ when not existing
   */
  public static String normalizeKey(String aKey) {
    return (aKey.charAt(0) == '@' ? aKey : '@' + aKey);
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public String getValue() {
    return value;
  }

  /**
   * Overwritten attached value
   *
   * @param aValue
   */
  public void setValue(String aValue) {
    value = Text.unescapeQuotedString(aValue);
  }

  @Override
  public StringBuffer toStringBuffer() {
    StringBuffer csExpression = new StringBuffer(key);
    if (value.isEmpty()) {
      return csExpression;
    }
    return csExpression.append("=")
            .append(Text.escapeStringWithQuotes(value));
  }

  @Override
  public int compareTo(Object t) {
    int comparison = super.compareTo(t);
    return (comparison != 0 ? comparison : key.compareTo(((CSEMeta) t).key));
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty() && key.isEmpty();
  }
}

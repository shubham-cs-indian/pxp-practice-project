package com.cs.core.csexpress;

import com.cs.core.data.Text;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEMeta;
import java.util.*;

/**
 * CSExpress abstract element implementation
 *
 * @author vallee
 */
public abstract class CSEElement implements ICSEElement {

  protected final HashMap<String, String> metaValues;
  protected final HashMap<Keyword, String> specifications;
  private final ElementType type;

  /**
   * @param type define type at construction
   */
  protected CSEElement(ElementType type) {
    this.type = type;
    this.metaValues = new HashMap();
    this.specifications = new HashMap();
  }

  @Override
  public final ElementType getType() {
    return type;
  }

  @Override
  public boolean containsSpecification(Keyword key) {
    return specifications.containsKey(key);
  }

  @Override
  public String getSpecification(Keyword spec) {
    if (specifications.containsKey(spec)) {
      return specifications.get(spec);
    }
    return "";
  }

  @Override
  public <E extends Enum<E>> E getSpecification(Class<E> enumType, Keyword key) {
    if (!specifications.containsKey(key)) {
      return E.valueOf(enumType, "UNDEFINED");
    }
    return E.valueOf(enumType, getSpecification(key));
  }

  /**
   * Check if this element consistently accept that specification
   *
   * @param keyword the specification keyword
   * @return false by default
   */
  public boolean accept(Keyword keyword) {
    return false;
  }

  /**
   * @param spec the specification key word
   * @param value the value to attach to the specification (empty to clear)
   */
  public final void setSpecification(Keyword spec, String value) {
    if (!accept(spec)) {
      return;
    }
    if (value.isEmpty()) {
      specifications.remove(spec);
    } else {
      specifications.put(spec, value);
    }
  }

  /**
   * @param spec the specification key word
   * @param value the enum value to attach to the specification (empty to clear)
   */
  public <E extends Enum<E>> void setSpecification(Keyword spec, E value) {
    setSpecification(spec, value.toString());
  }

  @Override
  public int compareTo(Object t) {
    // Compare types
    return this.type.ordinal() - ((CSEElement) t).type.ordinal();
  }

  @Override
  public String toString() {
    return toStringBuffer().toString();
  }

  @Override
  public boolean isEmpty() {
    return metaValues.isEmpty() && specifications.isEmpty();
  }

  @Override
  public void addMeta(ICSEMeta meta) {
    metaValues.put(meta.getKey(), meta.getValue());
  }

  @Override
  public ICSEMeta getMeta(String key) {
    String aKey = CSEMeta.normalizeKey(key);
    if (metaValues.containsKey(aKey)) {
      return new CSEMeta(aKey, metaValues.get(aKey));
    }
    return null;
  }

  @Override
  public void addMeta(IPXON.PXONMeta key, String value) {
    addMeta(new CSEMeta(key.toString(), value));
  }

  /**
   * @param key a meta key
   * @return the attached value
   */
  public String getMetaValue(String key) {
    CSEMeta keyMeta = new CSEMeta(key);
    if (metaValues.containsKey(keyMeta.getKey())) {
      return metaValues.get(keyMeta.getKey());
    }
    return "";
  }

  /**
   * @param key a meta key
   * @return the attached value
   */
  public String getMetaValue(IPXON.PXONMeta key) {
    return getMetaValue(key.toString());
  }

  /**
   * Check if a meta is defined in the object
   *
   * @param key the meta key
   * @return true if existing
   */
  public boolean hasMeta(String key) {
    CSEMeta keyMeta = new CSEMeta(key);
    return metaValues.containsKey(keyMeta.getKey());
  }

  /**
   * Check if a meta is defined in the object
   *
   * @param key the meta key
   * @return true if existing
   */
  public boolean hasMeta(IPXON.PXONMeta key) {
    return hasMeta(key.toString());
  }

  /**
   * @return the list of metas as CSExpression inclusion (ordered by alphabetics)
   */
  protected StringBuffer metaExpression() {
    StringBuffer csExpression = new StringBuffer();
    List<String> metaKeys = new ArrayList<>();
    metaKeys.addAll(metaValues.keySet());
    Collections.sort(metaKeys);
    metaKeys.forEach((key) -> {
      csExpression.append((new CSEMeta(key, metaValues.get(key))).toStringBuffer())
              .append(" ");
    });
    if (metaValues.size() > 0) {
      csExpression.setLength(csExpression.length() - 1);
    }
    return csExpression;
  }

  /**
   * @param excludeKeys specify the specifications that are not printed
   * @return the list of specifications as CSExpression inclusion (ordered by ordinal)
   */
  protected StringBuffer specificationExpression(Keyword... excludeKeys) {
    StringBuffer csExpression = new StringBuffer();
    List<Keyword> keywords = new ArrayList<>();
    keywords.addAll(specifications.keySet());
    Collections.sort(keywords, (k1, k2) -> {
      return k1.ordinal() - k2.ordinal();
    });
    Set<Keyword> exludedSet = new HashSet<>(Arrays.asList(excludeKeys));
    keywords.forEach((key) -> {
      if (!exludedSet.contains(key)) {
        if ( key != Keyword.$search) {
          csExpression.append(key)
              .append("=").append(specifications.get(key)).append(" ");
        } else {
          csExpression.append(key)
             .append("=").append(Text.escapeStringWithQuotes(specifications.get(key))).append(" ");
        }
      }
    });
    if (csExpression.length() > 0) {
      csExpression.setLength(csExpression.length() - 1);
    }
    return csExpression;
  }
}

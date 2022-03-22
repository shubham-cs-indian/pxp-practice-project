package com.cs.core.csexpress;

import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ICSEList implementation
 *
 * @author vallee
 */
public class CSEList extends CSEElement implements ICSEList {

  private final List<ICSEElement> items;

  /**
   * Default empty constructor
   */
  public CSEList() {
    super(ElementType.LIST);
    items = new ArrayList<>();
  }

  /**
   * Constructor with list initialization
   *
   * @param elements to be part of the list
   */
  public CSEList(ICSEElement... elements) {
    super(ElementType.LIST);
    items = new ArrayList<>();
    items.addAll(Arrays.asList(elements));
  }

  @Override
  public StringBuffer toStringBuffer() {
    StringBuffer csExpression = new StringBuffer().append('{');
    if (!metaValues.isEmpty()) {
      csExpression.append(metaExpression())
              .append(' ');
    }
    items.forEach(item -> {
      csExpression.append(item.toStringBuffer())
              .append(",");
    });
    if (items.isEmpty()) {
      csExpression.append('}');
    } else {
      csExpression.setCharAt(csExpression.length() - 1, '}');
    }
    return csExpression;
  }

  @Override
  public List<ICSEElement> getSubElements() {
    return items;
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty() && items.isEmpty();
  }
}

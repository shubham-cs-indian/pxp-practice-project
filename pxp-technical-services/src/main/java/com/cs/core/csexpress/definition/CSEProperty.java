package com.cs.core.csexpress.definition;

import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * CSExpression for property
 *
 * @author vallee
 */
public class CSEProperty extends CSEObject implements ICSEProperty {

  public CSEProperty() {
    super(ElementType.PROPERTY, CSEObjectType.Property);
  }

  @Override
  public int getSide() {
    if (!specifications.containsKey(Keyword.$side)) {
      return 0;
    }
    return Integer.parseInt(specifications.get(Keyword.$side));
  }

  /**
   * @param side the side to position on the property definition
   */
  public void setSide(int side) {
    specifications.put(Keyword.$side, String.format("%d", side));
  }

  @Override
  public ICSEObject getContext() {
    CSEObject ccse = new CSEObject(CSEObjectType.Context);
    if (!specifications.containsKey(Keyword.$cxt)) {
      return ccse; // empty
    }
    ccse.setCode(getSpecification(Keyword.$cxt));
    return ccse;
  }
}

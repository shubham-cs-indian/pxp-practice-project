package com.cs.core.csexpress.definition;

import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.icsexpress.definition.ICSETagValue;

/**
 * CSExpression for tag value code
 *
 * @author frva
 */
public class CSETagValue extends CSEObject implements ICSETagValue {

  public CSETagValue() {
    super(ElementType.TAGVALUE, CSEObjectType.TagValue);
  }

  public CSETagValue(String code, Integer range) {
    super(ElementType.TAGVALUE, CSEObjectType.TagValue);
    setCode(code);
    setSpecification(Keyword.$range, String.format("%d", range));
  }

  @Override
  public ICSEProperty getMasterProperty() {
    if (!specifications.containsKey(Keyword.$prop)) {
      return null;
    }
    CSEProperty master = new CSEProperty();
    String propSpec = specifications.get(Keyword.$prop);
    if (Character.isDigit(propSpec.charAt(0))) {
      master.setIID(propSpec);
    } else {
      master.setCode(propSpec);
    }
    return master;
  }

  @Override
  public int getRange() {
    if (!specifications.containsKey(Keyword.$range)) {
      return 100;
    }
    return Integer.valueOf(specifications.get(Keyword.$range));
  }

  @Override
  public boolean isDefaultRange() {
    return !specifications.containsKey(Keyword.$range);
  }

}

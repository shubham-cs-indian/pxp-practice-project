package com.cs.core.runtime.interactor.entity.datarule;

public class AttributeConflictingValue extends AbstractConflictingValue
    implements IAttributeConflictingValue {
  
  private static final long serialVersionUID = 1L;
  
  protected String          value;
  protected String          valueAsHtml;
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
  
  @Override
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  @Override
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
}

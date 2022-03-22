package com.cs.core.config.interactor.entity.attribute;

public class ConcatenatedHtmlOperator extends AbstractConcatenatedOperator
    implements IConcatenatedHtmlOperator {
  
  private static final long serialVersionUID = 1L;
  
  protected String          value;
  protected String          valueAsHtml;
  
  public String getValue()
  {
    return value;
  }
  
  public void setValue(String value)
  {
    this.value = value;
  }
  
  public String getValueAsHtml()
  {
    return valueAsHtml;
  }
  
  public void setValueAsHtml(String valueAsHtml)
  {
    this.valueAsHtml = valueAsHtml;
  }
}

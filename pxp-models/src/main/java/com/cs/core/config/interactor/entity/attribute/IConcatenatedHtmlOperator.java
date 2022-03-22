package com.cs.core.config.interactor.entity.attribute;

public interface IConcatenatedHtmlOperator extends IConcatenatedOperator {
  
  public static final String VALUE         = "value";
  public static final String VALUE_AS_HTML = "valueAsHtml";
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
}

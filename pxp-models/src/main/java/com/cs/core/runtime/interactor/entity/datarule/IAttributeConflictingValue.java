package com.cs.core.runtime.interactor.entity.datarule;

public interface IAttributeConflictingValue extends IConflictingValue {
  
  public static final String VALUE         = "value";
  public static final String VALUE_AS_HTML = "valueAsHtml";
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
}

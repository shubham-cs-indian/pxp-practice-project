package com.cs.core.config.interactor.model.context;

public interface IContextualPropertiesToInheritAttributeModel
    extends IContextualPropertiesToInheritPropertyModel {
  
  public static final String VALUE           = "value";
  public static final String VALUE_AS_NUMBER = "valueAsNumber";
  public static final String VALUE_AS_HTML   = "valueAsHtml";
  public static final String ATTRIBUTE_ID    = "attributeId";
  
  public String getValue();
  
  public void setValue(String value);
  
  public Double getValueAsNumber();
  
  public void setValueAsNumber(Double valueAsNumber);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
}

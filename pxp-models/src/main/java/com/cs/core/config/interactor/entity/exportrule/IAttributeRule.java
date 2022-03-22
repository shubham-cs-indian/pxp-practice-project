package com.cs.core.config.interactor.entity.exportrule;

public interface IAttributeRule {
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getOperator();
  
  public void setOperator(String operator);
}

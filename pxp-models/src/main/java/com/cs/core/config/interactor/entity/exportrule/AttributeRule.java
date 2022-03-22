package com.cs.core.config.interactor.entity.exportrule;

public class AttributeRule implements IAttributeRule {
  
  protected String attributeId;
  protected String value;
  protected String operator;
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
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
  public String getOperator()
  {
    return operator;
  }
  
  @Override
  public void setOperator(String operator)
  {
    this.operator = operator;
  }
}

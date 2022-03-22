package com.cs.core.config.interactor.entity.attribute;

public interface IConcatenatedAttributeOperator extends IConcatenatedOperator {
  
  public static final String ATTRIBUTE_ID = "attributeId";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
}

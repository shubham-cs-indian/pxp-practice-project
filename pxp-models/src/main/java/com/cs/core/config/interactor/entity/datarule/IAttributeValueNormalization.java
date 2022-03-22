package com.cs.core.config.interactor.entity.datarule;

public interface IAttributeValueNormalization extends INormalization {
  
  public static final String VALUE_ATTRIBUTE_ID = "valueAttributeId";
  
  public String getValueAttributeId();
  
  public void setValueAttributeId(String valueAttributeId);
}

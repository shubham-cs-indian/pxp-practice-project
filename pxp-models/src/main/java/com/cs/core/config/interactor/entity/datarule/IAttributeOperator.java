package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IAttributeOperator extends IConfigEntity {
  
  public static final String ATTRIBUTE_ID  = "attributeId";
  public static final String TYPE          = "type";
  public static final String ORDER         = "order";
  public static final String VALUE         = "value";
  public static final String VALUE_AS_HTML = "valueAsHtml";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getOperator();
  
  public void setOperator(String operator);
  
  public String getType();
  
  public void setType(String type);
  
  public Integer getOrder();
  
  public void setOrder(Integer order);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
}

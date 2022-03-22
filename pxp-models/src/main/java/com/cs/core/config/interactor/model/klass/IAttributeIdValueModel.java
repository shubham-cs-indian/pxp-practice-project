package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAttributeIdValueModel extends IModel {
  
  public static final String ATTRIBUTE_ID  = "attributeId";
  public static final String VALUE_AS_HTML = "valueAsHtml";
  public static final String VALUE         = "value";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public String getValue();
  
  public void setValue(String value);
}

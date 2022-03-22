package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISmartDocumentAttributeInstanceDataModel extends IModel {
  
  public static final String ATTRIBUTE_ID    = "attributeId";
  public static final String VALUE           = "value";
  public static final String VALUE_AS_HTML   = "valueAsHtml";
  public static final String VALUE_AS_NUMBER = "valueAsNumber";
  public static final String ATTRIBUTE_LABEL = "attributeLabel";
  public static final String ATTRIBUTE_TYPE  = "attributeType";
  public static final String DEFAULT_UNIT    = "defaultUnit";
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public Double getValueAsNumber();
  
  public void setValueAsNumber(Double valueAsNumber);
  
  public String getAttributeLabel();
  
  public void setAttributeLabel(String attributeLabel);
  
  public String getAttributeType();
  
  public void setAttributeType(String attributeType);
  
  public String getDefaultUnit();
  public void setDefaultUnit(String defaultUnit);
}

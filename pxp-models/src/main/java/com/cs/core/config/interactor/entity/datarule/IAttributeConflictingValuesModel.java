package com.cs.core.config.interactor.entity.datarule;

public interface IAttributeConflictingValuesModel extends IElementConflictingValuesModel {
  
  public static final String VALUE         = "value";
  public static final String VALUE_AS_HTML = "valueAsHtml";
  public static final String COUPLING_TYPE = "couplingType";
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
}

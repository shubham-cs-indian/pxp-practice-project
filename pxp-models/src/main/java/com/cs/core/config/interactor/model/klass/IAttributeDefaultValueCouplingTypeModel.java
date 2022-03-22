package com.cs.core.config.interactor.model.klass;

public interface IAttributeDefaultValueCouplingTypeModel extends IDefaultValueChangeModel {
  
  public static final String VALUE           = "value";
  public static final String VALUE_AS_HTML   = "valueAsHtml";
  public static final String IS_DEPENDENT    = "isDependent";
  public static final String VALUE_AS_NUMBER = "valueAsNumber";
  public static final String UNIT_SYMBOL     = "unitSymbol";
  
  public String getValue();
  
  public void setValue(String value);
  
  public String getValueAsHtml();
  
  public void setValueAsHtml(String valueAsHtml);
  
  public Boolean getIsDependent();
  
  public void setIsDependent(Boolean isDependent);
  
  public double getValueAsNumber();
  
  public void setValueAsNumber(double valueAsNumber);
  
  public String getUnitSymbol();
  
  public void setUnitSymbol(String unitSymbol);
}

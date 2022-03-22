package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDefaultValueCouplingTypeModel extends IModel {
  
  public static final String COUPLING_TYPE = "couplingType";
  public static final String IS_MANDATORY  = "isMandatory";
  public static final String IS_SHOULD     = "isShould";
  public static final String VALUE         = "value";
  public static final String IS_SKIPPED    = "isSkipped";
  public static final String IS_VALUE_CHANGED         = "isValueChanged";
  public static final String IS_COUPLING_TYPE_CHANGED = "isCouplingTypeChanged";
  
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
  public Boolean getIsMandatory();
  
  public void setIsMandatory(Boolean isMandatory);
  
  public Boolean getIsShould();
  
  public void setIsShould(Boolean isShould);
  
  public Boolean getIsSkipped();
  
  public void setIsSkipped(Boolean isSkipped);
  
  public Boolean getIsValueChanged();
  
  public void setIsValueChanged(Boolean isValueChanged);
  
  public Boolean getIsCouplingTypeChanged();
  
  public void setIsCouplingTypeChanged(Boolean isCouplingTypeChanged);
}

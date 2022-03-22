package com.cs.core.runtime.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IConflictingValue extends IEntity {
  
  public static final String COUPLING_TYPE = "couplingType";
  public static final String SOURCE        = "source";
  public static final String IS_MANDATORY  = "isMandatory";
  public static final String IS_SHOULD     = "isShould";
  
  public Boolean getIsMandatory();
  
  public void setIsMandatory(Boolean isMandatory);
  
  public Boolean getIsShould();
  
  public void setIsShould(Boolean isShould);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
  
  public IConflictingValueSource getSource();
  
  public void setSource(IConflictingValueSource source);
}

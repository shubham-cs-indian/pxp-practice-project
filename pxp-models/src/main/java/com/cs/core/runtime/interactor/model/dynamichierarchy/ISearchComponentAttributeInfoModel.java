package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface ISearchComponentAttributeInfoModel extends IConfigEntityInformationModel {
  
  public static final String DEFAULT_UNIT = "defaultUnit";
  public static final String PRECISION    = "precision";
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
}

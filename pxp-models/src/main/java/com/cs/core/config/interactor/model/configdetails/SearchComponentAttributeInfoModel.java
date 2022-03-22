package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.dynamichierarchy.ISearchComponentAttributeInfoModel;

public class SearchComponentAttributeInfoModel extends ConfigEntityInformationModel
    implements ISearchComponentAttributeInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String          defaultUnit;
  protected Integer         precision;
  
  public String getDefaultUnit()
  {
    return defaultUnit;
  }
  
  public void setDefaultUnit(String defaultUnit)
  {
    this.defaultUnit = defaultUnit;
  }
  
  public Integer getPrecision()
  {
    return precision;
  }
  
  public void setPrecision(Integer precision)
  {
    this.precision = precision;
  }
}

package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;

import java.util.List;

public interface ISearchHitInfoModel extends IConfigEntityTreeInformationModel {
  
  public static final String VALUES              = "values";
  public static final String DEFAULT_UNIT        = "defaultUnit";
  public static final String PRECISION           = "precision";
  public static final String CONTEXT_INSTANCE_ID = "contextInstanceId";
  
  public List<ISearchHitInfoModel> getValues();
  
  public void setValues(List<ISearchHitInfoModel> values);
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
  
  public String getContextInstanceId();
  
  public void setContextInstanceId(String contextInstanceId);
}

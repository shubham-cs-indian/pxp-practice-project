package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.config.interactor.entity.visualattribute.IVisualAttributeValidator;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IAttributeInfoModel extends IConfigEntityInformationModel {
  
  public static final String DEFAULT_UNIT   = "defaultUnit";
  public static final String PRECISION      = "precision";
  public static final String VALIDATOR      = "validator";
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
  
  public IVisualAttributeValidator getValidator();
  
  public void setValidator(IVisualAttributeValidator validator);
}

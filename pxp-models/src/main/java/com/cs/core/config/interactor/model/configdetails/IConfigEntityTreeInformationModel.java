package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IConfigEntityTreeInformationModel
    extends IConfigEntityInformationModel, ITreeEntity {
  
  public static final String TYPE         = "type";
  public static final String DEFAULT_UNIT = "defaultUnit";
  public static final String PRECISION    = "precision";
  
  public String getType();
  
  public void setType(String tagType);
  
  public String getDefaultUnit();
  
  public void setDefaultUnit(String defaultUnit);
  
  public Integer getPrecision();
  
  public void setPrecision(Integer precision);
}

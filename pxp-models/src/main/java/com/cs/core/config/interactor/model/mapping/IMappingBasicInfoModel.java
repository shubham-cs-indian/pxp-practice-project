package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IMappingBasicInfoModel extends IConfigEntityInformationModel {
  
  public static final String MAPPING_TYPE = "mappingType";
  
  public String getMappingType();
  
  public void setMappingType(String mappingType);
  
}

package com.cs.core.config.interactor.model.mapping;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class MappingBasicInfoModel extends ConfigEntityInformationModel
    implements IMappingBasicInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String          mappingType;
  
  @Override
  public String getMappingType()
  {
    return mappingType;
  }
  
  @Override
  public void setMappingType(String mappingType)
  {
    this.mappingType = mappingType;
  }
}

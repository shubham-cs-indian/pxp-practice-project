package com.cs.core.config.interactor.model.mapping;

public class GetPropertyGroupInfoRequestModel implements IGetPropertyGroupInfoRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          propertyCollectionId;
  protected String          mappingId;
  
  @Override
  public void setpropertyCollectionId(String propertyCollectionId)
  {
    this.propertyCollectionId = propertyCollectionId;
  }
  
  @Override
  public String getpropertyCollectionId()
  {
    return propertyCollectionId;
  }
  
  @Override
  public String getMappingId()
  {
    return mappingId;
  }
  
  @Override
  public void setMappingId(String mappingId)
  {
    this.mappingId = mappingId;
  }
}

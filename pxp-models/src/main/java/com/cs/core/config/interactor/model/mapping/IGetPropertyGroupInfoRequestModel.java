package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPropertyGroupInfoRequestModel extends IModel {
  
  public static final String PROPERTY_COLLECTION_ID = "propertyCollectionId";
  public static final String MAPPING_ID             = "mappingId";
  
  public void setpropertyCollectionId(String propertyCollectionId);
  
  public String getpropertyCollectionId();
  
  public String getMappingId();
  
  public void setMappingId(String mappingId);
}

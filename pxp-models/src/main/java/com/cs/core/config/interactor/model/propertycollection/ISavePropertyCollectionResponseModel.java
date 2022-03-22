package com.cs.core.config.interactor.model.propertycollection;

import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;

public interface ISavePropertyCollectionResponseModel extends IConfigResponseWithAuditLogModel {
  
  public static final String PROPERTY_COLLECTION_GET_RESPONSE = "propertyCollectionGetResponse";
  public static final String DELETED_PROPERTIES_FROM_SOURCE   = "deletedPropertiesFromSource";
  public static final String DEFAULT_VALUES_DIFF              = "defaultValuesDiff";
  
  public IGetPropertyCollectionModel getPropertyCollectionGetRespose();
  
  public void setPropertyCollectionGetResponse(IGetPropertyCollectionModel getResponse);
  
  public Map<String, List<String>> getDeletedPropertiesFromSource();
  
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
}

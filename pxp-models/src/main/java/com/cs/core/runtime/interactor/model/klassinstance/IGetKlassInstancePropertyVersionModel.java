package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetKlassInstancePropertyVersionModel extends IModel {
  
  public static final String PROPERTY_ID = "propertyId";
  public static final String VERSION_ID  = "versionId";
  
  public String getPropertyId();
  
  public void setPropertyId(String propertyId);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
}

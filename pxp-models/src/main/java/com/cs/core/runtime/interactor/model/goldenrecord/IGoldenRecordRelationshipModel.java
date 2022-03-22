package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGoldenRecordRelationshipModel extends IModel {
  
  public static final String PROPERTY_ID   = "propertyId";
  public static final String PROPERTY_TYPE = "propertyType";
  public static final String CONTENT_ID    = "contentId";
  public static final String LAST_MODIFIED = "lastModified";
  
  public String getPropertyId();
  
  public void setPropertyId(String propertyId);
  
  public String getPropertyType();
  
  public void setPropertyType(String propertyType);
  
  public String getcontentId();
  
  public void setcontentId(String contentId);
  
  public String getSupplierId();
  
  public void setSupplierId(String supplierId);
  
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
}

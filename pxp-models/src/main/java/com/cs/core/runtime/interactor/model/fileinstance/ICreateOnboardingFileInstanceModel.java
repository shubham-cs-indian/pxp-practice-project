package com.cs.core.runtime.interactor.model.fileinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.io.File;

public interface ICreateOnboardingFileInstanceModel extends IModel {
  
  public static final String ID                  = "id";
  public static final String TYPE                = "type";
  public static final String NAME                = "name";
  public static final String FILE                = "file";
  public static final String USER_ID             = "userId";
  public static final String EXTENSION           = "extension";
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public String getName();
  
  public void setName(String name);
  
  public File getFile();
  
  public void setFile(File file);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getExtension();
  
  public void setExtension(String extension);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
}

package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICreateInstanceModel extends IModel {
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public String getName();
  
  public void setName(String name);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public Boolean getIsFolder();
  
  public void setIsFolder(Boolean isFolder);
  
  public Boolean getHasCreatePermission();
  
  public void setHasCreatePermission(Boolean hasCreatePermission);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getPortalId();
  
  public void setPortalId(String portalId);
  
  public String getProcessInstanceId();
  
  public void setProcessInstanceId(String processInstanceId);
}

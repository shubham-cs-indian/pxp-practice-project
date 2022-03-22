package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;

public class CreateInstanceModel implements ICreateInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected String          type;
  protected String          id;
  protected String          originalInstanceId;
  protected String          name;
  protected String          parentId;
  protected Boolean         isFolder         = false;
  // NOTE: Added as a quick solution for onboarding creation. CHANGE LATER! :
  // ROHITH
  protected Boolean         hasCreatePermission;
  protected String          endpointId       = "-1";
  protected String          systemId         = "-1";
  
  protected String          organizationId;
  protected String          physicalCatalogId;
  protected String          portalId;
  protected String          processInstanceId;
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getPortalId()
  {
    return portalId;
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public Boolean getIsFolder()
  {
    return isFolder;
  }
  
  @Override
  public void setIsFolder(Boolean isFolder)
  {
    this.isFolder = isFolder;
  }
  
  @Override
  public Boolean getHasCreatePermission()
  {
    return this.hasCreatePermission;
  }
  
  public void setHasCreatePermission(Boolean hasCreatePermission)
  {
    this.hasCreatePermission = hasCreatePermission;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getOriginalInstanceId()
  {
    return originalInstanceId;
  }
  
  @Override
  public void setOriginalInstanceId(String originalInstanceId)
  {
    this.originalInstanceId = originalInstanceId;
  }
  
  @Override
  public String getProcessInstanceId()
  {
    return processInstanceId;
  }
  
  @Override
  public void setProcessInstanceId(String processInstanceId)
  {
    this.processInstanceId = processInstanceId;
  }
}

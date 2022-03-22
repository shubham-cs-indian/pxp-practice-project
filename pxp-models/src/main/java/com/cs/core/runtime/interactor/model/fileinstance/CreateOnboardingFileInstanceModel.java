package com.cs.core.runtime.interactor.model.fileinstance;

import java.io.File;

public class CreateOnboardingFileInstanceModel implements ICreateOnboardingFileInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          name;
  protected String          type;
  protected File            file;
  protected String          userId;
  protected String          extension;
  protected String          organizationId;
  protected String          endpointId;
  protected String          physicalCatalogId;
  
  @Override
  public String getExtension()
  {
    return extension;
  }
  
  @Override
  public void setExtension(String extension)
  {
    this.extension = extension;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String supplierId)
  {
    this.userId = supplierId;
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
  public File getFile()
  {
    return file;
  }
  
  @Override
  public void setFile(File file)
  {
    this.file = file;
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
    // TODO Auto-generated method stub
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
}

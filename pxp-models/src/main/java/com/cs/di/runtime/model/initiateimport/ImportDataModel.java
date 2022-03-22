package com.cs.di.runtime.model.initiateimport;

import java.util.List;

public class ImportDataModel implements IImportDataModel {
  
  private static final long             serialVersionUID = 1L;
  protected String                      entityType;
  protected byte[]                      fileData;
  protected String                      originalFilename;
  protected String                      importType;
  protected String                      processDefinitionId;
  protected String                      endpointId;
  protected List<String>                permissionTypes;
  protected List<String>                roleIds;
  
  @Override
  public byte[] getFileData()
  {
    return fileData;
  }
  
  @Override
  public void setFileData(byte[] fileData)
  {
    this.fileData = fileData;
    
  }
  
  @Override
  public String getOriginalFilename()
  {
    
    return originalFilename;
  }
  
  @Override
  public void setOriginalFilename(String originalFilename)
  {
    this.originalFilename = originalFilename;
    
  }
  
  @Override
  public String getentitytype()
  {
    
    return entityType;
  }
  
  @Override
  public void setentitytype(String entityType)
  {
    this.entityType = entityType;
    
  }
  
  @Override
  public String getImportType()
  {
    return importType;
  }

  @Override
  public void setImportType(String importType)
  {
    this.importType = importType;
  }

  @Override
  public String getProcessDefinitionId()
  {
    return processDefinitionId;
  }

  @Override
  public void setProcessDefinitionId(String processDefinitionId)
  {
    this.processDefinitionId = processDefinitionId;
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
  public List<String> getPermissionTypes()
  {
    return permissionTypes;
  }

  @Override
  public void setPermissionTypes(List<String> permissionTypes)
  {
    this.permissionTypes = permissionTypes;
  }

  @Override
  public List<String> getRoleIds()
  {
    return roleIds;
  }

  @Override
  public void setRoleIds(List<String> roleIds)
  {
    this.roleIds = roleIds;
  }  
}

package com.cs.di.runtime.model.initiateimport;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IImportDataModel extends IModel {
  
  public static final String ORIGINAL_FILENAME = "originalFilename";
  public static String       FILE_DATA         = "fileData";
  public static final String ENTITY_TYPE       = "entityType";
  public static final String IMPORT_TYPE       = "importType";
  public static final String PROCESS_DEFINITION_ID     = "processDefinitionId";
  public static final String PERMISSION_TYPES       = "permissionTypes";
  public static final String ROLE_IDS              = "roleIds";
  
  public byte[] getFileData();
  
  public void setFileData(byte[] fileData);
  
  public String getOriginalFilename();
  
  public void setOriginalFilename(String originalFilename);
  
  public String getentitytype();
  
  public void setentitytype(String entityType);

  public String getImportType();

  public void setImportType(String importType);
  
  public String getProcessDefinitionId();
  
  public void setProcessDefinitionId(String processDefinitionId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public List<String> getPermissionTypes();
  
  public void setPermissionTypes(List<String> permissionTypes);
  
  public List<String> getRoleIds();
  
  public void setRoleIds(List<String> roleIds);
  
}

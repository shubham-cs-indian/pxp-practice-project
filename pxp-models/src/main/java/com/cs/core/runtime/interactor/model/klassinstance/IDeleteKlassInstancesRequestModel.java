package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;

public interface IDeleteKlassInstancesRequestModel {
  
  public String HAS_DELETE_PERMISSION        = "hasDeletePermission";
  public String GLOBAL_PERMISSIONS           = "globalPermissions";
  public String ALL_DELETED_INSTANCE_DETAILS = "allDeleteInstanceDetails";
  public String IS_DELETE_FROM_ARCHIVE       = "isDeleteFromArchive";
  
  public Boolean getHasDeletePermission();
  
  public void setHasDeletePermission(Boolean hasDeletePermission);
  
  public Map<String, IGlobalPermission> getGlobalPermissions();
  
  public void setGlobalPermissions(Map<String, IGlobalPermission> globalPermissions);
  
  public List<IDeleteInstanceDetails> getAllDeleteInstanceDetails();
  
  public void setAllDeleteInstanceDetails(List<IDeleteInstanceDetails> allDeleteInstanceDetails);
  
  public Boolean getIsDeleteFromArchive();
  
  public void setIsDeleteFromArchive(Boolean isDeleteFromArchive);
}

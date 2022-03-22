package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.model.klass.IModifiedSectionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IModifiedSectionModel extends IModel {
  
  public static final String SEQUENCE             = "sequence";
  public static final String ID                   = "id";
  public static final String MODIFIED_PERMISSIONS = "modifiedPermissions";
  public static final String IS_MODIFIED          = "isModified";
  public static final String IS_SKIPPED           = "isSkipped";
  
  public int getSequence();
  
  public void setSequence(int sequence);
  
  public String getId();
  
  public void setId(String id);
  
  public List<IModifiedSectionPermissionModel> getModifiedPermissions();
  
  public void setModifiedPermissions(List<IModifiedSectionPermissionModel> modifiedPermissions);
  
  public Boolean getIsModified();
  
  public void setIsModified(Boolean isModified);
  
  public Boolean getIsSkipped();
  
  public void setIsSkipped(Boolean isSkipped);
}

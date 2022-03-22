package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ICreateOrSaveGlobalPermissionModel extends IModel {
  
  public static final String ID                        = "id";
  public static final String ENTITY_ID                 = "entityId";
  public static final String GLOBAL_PERMISSION         = "globalPermission";
  public static final String ADDED_ALLOWED_TEMPLATES   = "addedAllowedTemplates";
  public static final String DELETED_ALLOWED_TEMPLATES = "deletedAllowedTemplates";
  public static final String MODIFIED_DEFAULT_TEMPLATE = "modifiedDefaultTemplate";
  
  public String getId();
  
  public void setId(String id);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public List<String> getAddedAllowedTemplates();
  
  public void setAddedAllowedTemplates(List<String> addedAllowedTemplates);
  
  public List<String> getDeletedAllowedTemplates();
  
  public void setDeletedAllowedTemplates(List<String> deletedAllowedTemplates);
  
  public String getModifiedDefaultTemplate();
  
  public void setModifiedDefaultTemplate(String modifiedDefaultTemplate);
}

package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IGetGlobalPermissionWithAllowedTemplatesModel extends IModel {
  
  public static final String ID                = "id";
  public static final String GLOBAL_PERMISSION = "globalPermission";
  public static final String ALLOWED_TEMPLATES = "allowedTemplates";
  public static final String DEFAULT_TEMPLATE  = "defaultTemplate";
  
  public String getId();
  
  public void setId(String id);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public Set<String> getAllowedTemplates();
  
  public void setAllowedTemplates(Set<String> allowedTemplates);
  
  public String getDefaultTemplate();
  
  public void setDefaultTemplate(String defaultTemplate);
}

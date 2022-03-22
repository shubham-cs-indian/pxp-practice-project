package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Set;

public class GetGlobalPermissionWithAllowedTemplatesModel
    implements IGetGlobalPermissionWithAllowedTemplatesModel {
  
  private static final long   serialVersionUID = 1L;
  
  protected String            id;
  protected IGlobalPermission globalPermission;
  protected Set<String>       allowedTemplates;
  protected String            defaultTemplate;
  
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
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public Set<String> getAllowedTemplates()
  {
    return allowedTemplates;
  }
  
  @Override
  public void setAllowedTemplates(Set<String> allowedTemplates)
  {
    this.allowedTemplates = allowedTemplates;
  }
  
  @Override
  public String getDefaultTemplate()
  {
    return defaultTemplate;
  }
  
  @Override
  public void setDefaultTemplate(String defaultTemplate)
  {
    this.defaultTemplate = defaultTemplate;
  }
}

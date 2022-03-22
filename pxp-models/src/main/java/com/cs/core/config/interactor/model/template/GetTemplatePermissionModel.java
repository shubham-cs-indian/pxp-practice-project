package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplatePermission;
import com.cs.core.config.interactor.entity.template.Template;
import com.cs.core.config.interactor.entity.template.TemplatePermission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetTemplatePermissionModel implements IGetTemplatePermissionModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected ITemplate           templateDetails;
  protected ITemplatePermission templatePermissions;
  
  @Override
  public ITemplate getTemplateDetails()
  {
    return templateDetails;
  }
  
  @Override
  @JsonDeserialize(as = Template.class)
  public void setTemplateDetails(ITemplate templateDetails)
  {
    this.templateDetails = templateDetails;
  }
  
  @Override
  public ITemplatePermission getTemplatePermission()
  {
    return templatePermissions;
  }
  
  @Override
  @JsonDeserialize(as = TemplatePermission.class)
  public void setTemplatePermission(ITemplatePermission templatePermissions)
  {
    this.templatePermissions = templatePermissions;
  }
}

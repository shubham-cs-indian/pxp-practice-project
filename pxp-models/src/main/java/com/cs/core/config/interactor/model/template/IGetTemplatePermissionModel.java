package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplatePermission;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetTemplatePermissionModel extends IModel {
  
  public static final String TEMPLATE_DETAILS     = "templateDetails";
  public static final String TEMPLATE_PERMISSIONS = "templatePermission";
  
  public ITemplate getTemplateDetails();
  
  public void setTemplateDetails(ITemplate templateDetails);
  
  public ITemplatePermission getTemplatePermission();
  
  public void setTemplatePermission(ITemplatePermission templatePermissions);
}

package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.template.IGetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IPermissionRequestModel;
import com.cs.core.config.template.IGetTemplatePermissionService;

@Service
public class GetTemplatePermission extends AbstractGetConfigInteractor<IPermissionRequestModel, IGetTemplatePermissionModel>
    implements IGetTemplatePermission {
  
  @Autowired
  protected IGetTemplatePermissionService getTemplatePermissionService;
  
  @Override
  public IGetTemplatePermissionModel executeInternal(IPermissionRequestModel requestModel) throws Exception
  {
    return getTemplatePermissionService.execute(requestModel);
  }
}

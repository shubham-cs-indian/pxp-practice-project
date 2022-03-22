package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;
import com.cs.core.config.template.ISaveTemplatePermissionService;

@Service
public class SaveTemplatePermission
    extends AbstractSaveConfigInteractor<ISaveTemplatePermissionModel, IGetPermissionModel>
    implements ISaveTemplatePermission {
  
  @Autowired
  protected ISaveTemplatePermissionService saveTemplatePermissionService;
  
  @Override
  public IGetPermissionModel executeInternal(ISaveTemplatePermissionModel requestModel) throws Exception
  {
    return saveTemplatePermissionService.execute(requestModel);
  }
}

package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.permission.GetPermissionRequestModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;
import com.cs.core.config.interactor.model.template.ISaveTemplatePermissionModel;
import com.cs.core.config.strategy.usecase.permission.IGetPermissionStrategy;
import com.cs.core.config.strategy.usecase.template.ISaveTemplatePermissionStrategy;
import com.cs.core.config.template.ISaveTemplatePermissionService;

@Service
public class SaveTemplatePermissionService extends AbstractSaveConfigService<ISaveTemplatePermissionModel, IGetPermissionModel>
    implements ISaveTemplatePermissionService {
  
  @Autowired
  protected ISaveTemplatePermissionStrategy saveTemplatePermissionStrategy;
  
  @Autowired
  protected IGetPermissionStrategy          getTemplatePermissionStrategy;
  
  @Override
  public IGetPermissionModel executeInternal(ISaveTemplatePermissionModel requestModel) throws Exception
  {
    saveTemplatePermissionStrategy.execute(requestModel);
    IGetPermissionRequestModel getRequestModel = new GetPermissionRequestModel();
    getRequestModel.setRoleId(requestModel.getRoleId());
    getRequestModel.setEntityType(requestModel.getEntityType());
    getRequestModel.setId(requestModel.getEntityId());
    return getTemplatePermissionStrategy.execute(getRequestModel);
  }
}

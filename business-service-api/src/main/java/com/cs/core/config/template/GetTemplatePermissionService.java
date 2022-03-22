package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.template.IGetTemplatePermissionModel;
import com.cs.core.config.interactor.model.template.IPermissionRequestModel;
import com.cs.core.config.strategy.usecase.template.IGetTemplatePermissionStrategy;
import com.cs.core.config.template.IGetTemplatePermissionService;

@Service
public class GetTemplatePermissionService extends AbstractGetConfigService<IPermissionRequestModel, IGetTemplatePermissionModel>
    implements IGetTemplatePermissionService {
  
  @Autowired
  protected IGetTemplatePermissionStrategy getTemplatePermissionStrategy;
  
  @Override
  public IGetTemplatePermissionModel executeInternal(IPermissionRequestModel requestModel) throws Exception
  {
    return getTemplatePermissionStrategy.execute(requestModel);
  }
}

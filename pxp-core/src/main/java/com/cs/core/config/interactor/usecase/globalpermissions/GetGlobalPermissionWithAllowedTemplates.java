package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.core.config.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesRequestModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesStrategy;

@Service
public class GetGlobalPermissionWithAllowedTemplates extends
    AbstractGetConfigInteractor<IGetGlobalPermissionWithAllowedTemplatesRequestModel, IGetGlobalPermissionWithAllowedTemplatesModel>
    implements IGetGlobalPermissionWithAllowedTemplates {
  
  @Autowired
  protected IGetGlobalPermissionWithAllowedTemplatesService getGlobalPermissionWithAllowedTemplatesService;
  
  @Override
  public IGetGlobalPermissionWithAllowedTemplatesModel executeInternal(
      IGetGlobalPermissionWithAllowedTemplatesRequestModel model) throws Exception
  {
    return getGlobalPermissionWithAllowedTemplatesService.execute(model);
  }
}

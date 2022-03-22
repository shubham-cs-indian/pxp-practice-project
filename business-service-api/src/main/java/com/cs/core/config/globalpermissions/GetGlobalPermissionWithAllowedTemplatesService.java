package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesRequestModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionWithAllowedTemplatesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetGlobalPermissionWithAllowedTemplatesService extends AbstractGetConfigService<IGetGlobalPermissionWithAllowedTemplatesRequestModel, IGetGlobalPermissionWithAllowedTemplatesModel>
    implements IGetGlobalPermissionWithAllowedTemplatesService {
  
  @Autowired
  protected IGetGlobalPermissionWithAllowedTemplatesStrategy getGlobalPermissionWithAllowedTemplatesStrategy;
  
  @Override
  public IGetGlobalPermissionWithAllowedTemplatesModel executeInternal(
      IGetGlobalPermissionWithAllowedTemplatesRequestModel model) throws Exception
  {
    return getGlobalPermissionWithAllowedTemplatesStrategy.execute(model);
  }
}

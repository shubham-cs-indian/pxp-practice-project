package com.cs.core.config.globalpermissions;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForEntitiesModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForEntitiesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetGlobalPermissionForEntitiesService
    extends AbstractGetConfigService<IIdParameterModel, IGetGlobalPermissionForEntitiesModel>
    implements IGetGlobalPermissionForEntitiesService {
  
  @Autowired
  protected IGetGlobalPermissionForEntitiesStrategy getGlobalPermissionForEntitiesStrategy;
  
  @Override
  public IGetGlobalPermissionForEntitiesModel executeInternal(IIdParameterModel model) throws Exception
  {
    
    return getGlobalPermissionForEntitiesStrategy.execute(model);
  }
}

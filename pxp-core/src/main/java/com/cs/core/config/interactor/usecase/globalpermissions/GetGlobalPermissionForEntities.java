package com.cs.core.config.interactor.usecase.globalpermissions;

import com.cs.core.config.globalpermissions.IGetGlobalPermissionForEntitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForEntitiesModel;
import com.cs.core.config.strategy.usecase.globalpermissions.IGetGlobalPermissionForEntitiesStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetGlobalPermissionForEntities
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetGlobalPermissionForEntitiesModel>
    implements IGetGlobalPermissionForEntities {
  
  @Autowired
  protected IGetGlobalPermissionForEntitiesService getGlobalPermissionForEntitiesService;
  
  @Override
  public IGetGlobalPermissionForEntitiesModel executeInternal(IIdParameterModel model) throws Exception
  {
    
    return getGlobalPermissionForEntitiesService.execute(model);
  }
}

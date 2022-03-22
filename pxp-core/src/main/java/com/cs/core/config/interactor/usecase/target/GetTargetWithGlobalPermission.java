package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.target.IGetTargetWithGlobalPermissionService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTargetWithGlobalPermission
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetKlassWithGlobalPermissionModel>
    implements IGetTargetWithGlobalPermission {
  
  @Autowired
  IGetTargetWithGlobalPermissionService getTargetWithGlobalPermissionService;
  
  @Override
  public IGetKlassWithGlobalPermissionModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTargetWithGlobalPermissionService.execute(idModel);
  }
}

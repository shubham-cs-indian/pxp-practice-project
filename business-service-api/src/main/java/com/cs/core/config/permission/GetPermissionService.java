package com.cs.core.config.permission;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.permission.IGetPermissionModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionRequestModel;
import com.cs.core.config.strategy.usecase.permission.IGetPermissionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetPermissionService extends AbstractGetConfigService<IGetPermissionRequestModel, IGetPermissionModel>
    implements IGetPermissionService {
  
  @Autowired
  protected IGetPermissionStrategy getPermissionStrategy;
  
  @Override
  public IGetPermissionModel executeInternal(IGetPermissionRequestModel requestModel) throws Exception
  {
    return getPermissionStrategy.execute(requestModel);
  }
}

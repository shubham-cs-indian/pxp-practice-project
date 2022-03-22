package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.strategy.usecase.user.IGetUsersByRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetUsersByRoleService
    extends AbstractGetConfigService<IIdParameterModel, IGetGridUsersResponseModel>
    implements IGetUsersByRoleService {
  
  @Autowired
  IGetUsersByRoleStrategy getUsersByRoleStrategy;
  
  @Override
  public IGetGridUsersResponseModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return (IGetGridUsersResponseModel) getUsersByRoleStrategy.execute(dataModel);
  }
}

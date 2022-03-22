package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IGetGridUsersResponseModel;
import com.cs.core.config.user.IGetUsersByRoleService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetUsersByRole
    extends AbstractGetConfigInteractor<IIdParameterModel, IGetGridUsersResponseModel>
    implements IGetUsersByRole {
  
  @Autowired
  IGetUsersByRoleService getUsersByRoleService;
  
  @Override
  public IGetGridUsersResponseModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return (IGetGridUsersResponseModel) getUsersByRoleService.execute(dataModel);
  }
}

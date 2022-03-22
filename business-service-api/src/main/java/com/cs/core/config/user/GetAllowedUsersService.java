package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IGetAllowedUsersRequestModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.IGetAllowedUsersStrategy;

@Service
public class GetAllowedUsersService
    extends AbstractGetConfigService<IGetAllowedUsersRequestModel, IListModel<IUserModel>>
    implements IGetAllowedUsersService {
  
  @Autowired
  IGetAllowedUsersStrategy getAllowedUsersStrategy;
  
  @Override
  public IListModel<IUserModel> executeInternal(IGetAllowedUsersRequestModel dataModel) throws Exception
  {
    return getAllowedUsersStrategy.execute(dataModel);
  }
}

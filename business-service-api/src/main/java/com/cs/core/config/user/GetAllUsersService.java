package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.IGetAllUsersStrategy;

@Service
public class GetAllUsersService
    extends AbstractGetConfigService<IUserModel, IListModel<IUserInformationModel>>
    implements IGetAllUsersService {
  
  @Autowired
  IGetAllUsersStrategy neo4jGetUsersStrategy;
  
  @Override
  public IListModel<IUserInformationModel> executeInternal(IUserModel dataModel) throws Exception
  {
    return neo4jGetUsersStrategy.execute(dataModel);
  }
}

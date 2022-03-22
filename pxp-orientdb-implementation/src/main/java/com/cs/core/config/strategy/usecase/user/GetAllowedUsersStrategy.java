package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IGetAllowedUsersRequestModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAllowedUsersStrategy extends OrientDBBaseStrategy
    implements IGetAllowedUsersStrategy {
  
  @Override
  public IListModel<IUserModel> execute(IGetAllowedUsersRequestModel model) throws Exception
  {
    return execute(GET_ALLOWED_USERS, model, new TypeReference<ListModel<UserModel>>()
    {
      
    });
  }
}

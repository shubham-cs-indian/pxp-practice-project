package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetUsersDataStrategy extends OrientDBBaseStrategy
    implements IGetAllUsersDataStrategy {
  
  public static final String useCase = "GetUsers";
  
  @Override
  public IListModel<IUserModel> execute(IUserModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(useCase, requestMap, new TypeReference<ListModel<UserModel>>()
    {
      
    });
  }
}

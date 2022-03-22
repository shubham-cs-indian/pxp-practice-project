package com.cs.core.config.strategy.usecase.user;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.BulkSaveUsersResponseModel;
import com.cs.core.config.interactor.model.user.IBulkSaveUsersResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBSaveUsersStrategy extends OrientDBBaseStrategy implements ISaveUsersStrategy {
  
  @Override
  public IBulkSaveUsersResponseModel execute(IListModel<IUserModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(SAVE_USERS, requestMap, BulkSaveUsersResponseModel.class);
  }
}

package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.config.interactor.model.role.RoleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getRolesStrategy")
public class OrientDBGetRolesStrategy extends OrientDBBaseStrategy implements IGetAllRolesStrategy {
  
  @Override
  public IListModel<IRoleModel> execute(IRoleModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(GET_ROLES, requestMap, new TypeReference<ListModel<RoleModel>>()
    {
      
    });
  }
}

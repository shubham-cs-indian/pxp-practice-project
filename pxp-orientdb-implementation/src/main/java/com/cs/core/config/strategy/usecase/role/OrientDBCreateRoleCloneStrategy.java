package com.cs.core.config.strategy.usecase.role;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.role.GetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.ICreateRoleCloneModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component("createRoleCloneStrategy")
public class OrientDBCreateRoleCloneStrategy extends OrientDBBaseStrategy
    implements ICreateRoleCloneStrategy {
  
  @Override
  public IGetRoleStrategyModel execute(ICreateRoleCloneModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("roleClone", model);
    return execute(CREATE_ROLE_CLONE, requestMap, GetRoleStrategyModel.class);
  }
  
}

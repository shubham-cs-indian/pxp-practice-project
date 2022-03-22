package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.GetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getRoleStrategy")
public class OrientDBGetRoleStrategy extends OrientDBBaseStrategy implements IGetRoleStrategy {
  
  @Override
  public IGetRoleStrategyModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(GET_ROLE, requestMap, GetRoleStrategyModel.class);
  }
}

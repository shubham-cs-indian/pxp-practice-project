package com.cs.core.config.strategy.usecase.standard.role;

import com.cs.core.config.interactor.model.role.GetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.role.ICreateRoleStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("createStandardRolesStrategy")
public class OrientDBCreateStandardRolesStrategy extends OrientDBBaseStrategy
    implements ICreateRoleStrategy {
  
  public static final String useCase = "CreateStandardRole";
  
  @Override
  public IGetRoleStrategyModel execute(ICreateRoleModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("role", model);
    return execute(useCase, requestMap, GetRoleStrategyModel.class);
  }
}

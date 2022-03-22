package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.GetRolesAndKlassesForUserModel;
import com.cs.core.config.interactor.model.role.IGetRolesAndKlassesForUserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.user.IGetRolesAndKlassesForUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getRolesAndKlassesForUserStrategy")
public class OrientDBGetRolesAndKlassesForUserStrategy extends OrientDBBaseStrategy
    implements IGetRolesAndKlassesForUserStrategy {
  
  public static final String useCase = "GetRolesAndKlassesForUser";
  
  @Override
  public IGetRolesAndKlassesForUserModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, GetRolesAndKlassesForUserModel.class);
  }
}

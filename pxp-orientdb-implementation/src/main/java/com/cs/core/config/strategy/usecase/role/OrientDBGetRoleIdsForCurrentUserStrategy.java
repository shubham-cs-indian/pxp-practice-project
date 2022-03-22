package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.user.IGetRoleIdsForCurrentUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getRoleIdsForCurrentUser")
public class OrientDBGetRoleIdsForCurrentUserStrategy extends OrientDBBaseStrategy
    implements IGetRoleIdsForCurrentUserStrategy {
  
  public static final String useCase = "GetRoleIdsForCurrentUser";
  
  @Override
  public IIdsListParameterModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, IdsListParameterModel.class);
  }
}

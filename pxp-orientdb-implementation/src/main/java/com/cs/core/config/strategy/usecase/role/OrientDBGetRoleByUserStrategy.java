package com.cs.core.config.strategy.usecase.role;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;


@Component("getRoleByUserStrategy")
public class OrientDBGetRoleByUserStrategy extends OrientDBBaseStrategy
    implements IGetRoleByUserStrategy {
  
  @Override
  public IIdsListParameterModel execute(IIdParameterModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("userId", model.getId());
    return execute(GET_ROLE_BY_USER_ID, requestMap, IdsListParameterModel.class);
    
  }
  
}

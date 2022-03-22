package com.cs.core.config.strategy.usecase.role;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.DeleteReturnModel;
import com.cs.core.config.interactor.model.configdetails.IDeleteReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class OrientDBDeleteRolesStrategy extends OrientDBBaseStrategy
    implements IDeleteRoleStrategy {
  
  @Override
  public IDeleteReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(DELETE_ROLES, requestMap, DeleteReturnModel.class);
  }
}

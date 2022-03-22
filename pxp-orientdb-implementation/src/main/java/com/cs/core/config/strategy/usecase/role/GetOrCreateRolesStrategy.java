package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.entity.datarule.IMandatoryRole;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getOrCreateStandardRolesStrategy")
public class GetOrCreateRolesStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateRolesStrategy {
  
  public static final String useCase = "GetOrCreateRole";
  
  @Override
  public IListModel<IMandatoryRole> execute(IListModel<IRole> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("role", model);
    execute(useCase, requestMap);
    return null;
  }
}

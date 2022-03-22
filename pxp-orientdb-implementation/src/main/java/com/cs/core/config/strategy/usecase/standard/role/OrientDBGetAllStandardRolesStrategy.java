package com.cs.core.config.strategy.usecase.standard.role;

import com.cs.core.config.interactor.entity.datarule.IMandatoryRole;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IMandatoryRoleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.role.IGetAllStandardRolesStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetAllStandardRolesStrategy extends OrientDBBaseStrategy
    implements IGetAllStandardRolesStrategy {
  
  public static final String useCase = "GetStandardRoles";
  
  @Override
  public IListModel<IMandatoryRole> execute(IMandatoryRoleModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    return execute(useCase, requestMap, new TypeReference<ListModel<IMandatoryRole>>()
    {
      
    });
  }
}

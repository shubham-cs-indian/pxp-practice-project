package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.attribute.IGetRolesStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getRolesByIdsStrategy")
public class OrientdDBGetRolesByIdsStrategy extends OrientDBBaseStrategy
    implements IGetRolesStrategy {
  
  @Override
  public IListModel<IConfigEntityInformationModel> execute(IIdsListParameterModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(GET_ROLES_BY_IDS, requestMap,
        new TypeReference<ListModel<ConfigEntityInformationModel>>()
        {
          
        });
  }
}

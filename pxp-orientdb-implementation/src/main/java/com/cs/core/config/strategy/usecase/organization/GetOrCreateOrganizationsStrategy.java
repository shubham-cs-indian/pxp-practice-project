package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetOrCreateOrganizationsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateOrganizationsStrategy {
  
  public IListModel<IOrganizationModel> execute(IListModel<IOrganizationModel> model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getList());
    execute(GET_OR_CREATE_ORGANIZATIONS, requestMap);
    return null;
  }
}

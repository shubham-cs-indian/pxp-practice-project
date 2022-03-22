package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.endpoint.IGetMappedEndpointRequestModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.datarule.IGetDataRulesByUserIdStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getDataRulesByUserIdStrategy") public class OrientDBGetDataRulesByIdStrategy
    extends OrientDBBaseStrategy implements IGetDataRulesByUserIdStrategy {

  public static final String useCase = "GetDataRulesByUserId";

  @Override public IListModel<IDataRuleModel> execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IGetMappedEndpointRequestModel.CURRENT_USER_ID, model.getCurrentUserId());
    requestMap.put("roleId", model.getAdditionalProperty("roleId"));
    return execute(useCase, requestMap, new TypeReference<ListModel<DataRuleModel>>()
    {
    });
  }

}

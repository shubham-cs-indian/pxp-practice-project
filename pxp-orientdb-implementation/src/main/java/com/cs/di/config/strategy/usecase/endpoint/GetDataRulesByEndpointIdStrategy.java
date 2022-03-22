package com.cs.di.config.strategy.usecase.endpoint;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.datarule.IGetDataRulesByEndpointIdStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getDataRulesByEndpointIdStrategy")
public class GetDataRulesByEndpointIdStrategy extends OrientDBBaseStrategy
    implements IGetDataRulesByEndpointIdStrategy {
  
  public static final String useCase = "GetDataRulesByEndpointId";
  
  @Override
  public IListModel<IDataRuleModel> execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IIdParameterModel.ID, model.getId());
    return execute(useCase, requestMap, new TypeReference<ListModel<DataRuleModel>>()
    {
    });
  }
}
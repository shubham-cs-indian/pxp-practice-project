package com.cs.di.config.strategy.usecase.processevent;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.GetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.processevent.IGetAllJMSConsumerStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetAllJMSConsumerStrategy extends OrientDBBaseStrategy
    implements IGetAllJMSConsumerStrategy {
  
  @Override
  public IListModel<IGetProcessEventModel> execute() throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("initParams", "null");
    return execute(GET_CONSUMER_COMPONENTS, requestMap,
        new TypeReference<ListModel<GetProcessEventModel>>()
        {
        });
  }

}

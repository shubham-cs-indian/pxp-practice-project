package com.cs.core.config.strategy.usecase.system;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetOrCreateSystemsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateSystemsStrategy {
  
  public IListModel<ISystemModel> execute(IListModel<ICreateSystemModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getList());
    execute(GET_OR_CREATE_SYSTEMS, requestMap);
    return null;
  }
}

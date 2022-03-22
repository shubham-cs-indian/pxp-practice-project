package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.ITabModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetOrCreateTabsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateTabsStrategy {
  
  @Override
  public IListModel<ITabModel> execute(IListModel<ICreateTabModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model.getList());
    execute(GET_OR_CREATE_TABS, requestMap);
    return null;
  }
}

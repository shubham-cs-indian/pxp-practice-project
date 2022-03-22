package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.BulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkSaveTabStrategy extends OrientDBBaseStrategy implements IBulkSaveTabStrategy {
  
  @Override
  public IBulkSaveTabResponseModel execute(IListModel<ISaveTabModel> model) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put(IListModel.LIST, model.getList());
    return execute(BULK_SAVE_TAB, map, BulkSaveTabResponseModel.class);
  }
}

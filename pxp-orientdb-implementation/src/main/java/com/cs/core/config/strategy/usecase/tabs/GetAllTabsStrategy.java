package com.cs.core.config.strategy.usecase.tabs;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tabs.GetGridTabsModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetAllTabsStrategy extends OrientDBBaseStrategy implements IGetAllTabsStrategy {
  
  @Override
  public IGetGridTabsModel execute(IConfigGetAllRequestModel model) throws Exception
  {
    return execute(GET_ALL_TABS, model, GetGridTabsModel.class);
  }
}

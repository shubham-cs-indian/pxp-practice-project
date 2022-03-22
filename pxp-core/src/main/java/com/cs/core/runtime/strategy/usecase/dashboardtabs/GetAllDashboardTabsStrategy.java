package com.cs.core.runtime.strategy.usecase.dashboardtabs;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.dashboardtabs.IGetAllDashboardTabsRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetAllDashboardTabsStrategy extends OrientDBBaseStrategy
    implements IGetAllDashboardTabsStrategy {
  
  @Override
  public IListModel<IIdLabelCodeModel> execute(IGetAllDashboardTabsRequestModel model) throws Exception
  {
    return execute(GET_ALL_DASHBOARD_TABS, model, new TypeReference<ListModel<IdLabelCodeModel>>()
    {
      
    });
  }
}

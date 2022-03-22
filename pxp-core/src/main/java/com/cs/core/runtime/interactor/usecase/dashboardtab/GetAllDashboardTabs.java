package com.cs.core.runtime.interactor.usecase.dashboardtab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.dashboardtabs.GetAllDashboardTabsRequestModel;
import com.cs.core.config.interactor.model.dashboardtabs.IGetAllDashboardTabsRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.usecase.dashboardtabs.IGetAllDashboardTabsStrategy;

@Service
public class GetAllDashboardTabs
    extends AbstractRuntimeInteractor<IModel, IListModel<IIdLabelCodeModel>>
    implements IGetAllDashboardTabs {
  
  @Autowired
  protected IGetAllDashboardTabsStrategy getAllDashboardTabsStrategy;
  
  @Autowired
  protected ISessionContext              context;
  
  
  @Autowired
  protected TransactionThreadData        transactionThreadData;
  
  @Override
  public IListModel<IIdLabelCodeModel> executeInternal(IModel model) throws Exception
  {
    IGetAllDashboardTabsRequestModel requestModel = new GetAllDashboardTabsRequestModel();
    requestModel.setCurrentUserId(context.getUserId());
    requestModel.setPhysicalCatalogId(transactionThreadData.getTransactionData()
        .getPhysicalCatalogId());
    return getAllDashboardTabsStrategy.execute(requestModel);
  }
}

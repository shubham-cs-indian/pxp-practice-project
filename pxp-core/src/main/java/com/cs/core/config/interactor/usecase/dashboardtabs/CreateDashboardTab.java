package com.cs.core.config.interactor.usecase.dashboardtabs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabModel;
import com.cs.core.config.interactor.model.dashboardtabs.IDashboardTabResponseModel;
import com.cs.core.config.interactor.usecase.dashboardtab.ICreateDashboardTab;
import com.cs.core.config.strategy.usecase.dashboardtabs.ICreateDashboardTabStrategy;

@Service
public class CreateDashboardTab
    extends AbstractCreateConfigInteractor<IDashboardTabModel, IDashboardTabResponseModel>
    implements ICreateDashboardTab {
  
  @Autowired
  protected ICreateDashboardTabStrategy createDashboardTabStrategy;
  
  @Override
  public IDashboardTabResponseModel executeInternal(IDashboardTabModel model) throws Exception
  {
    return createDashboardTabStrategy.execute(model);
  }
}

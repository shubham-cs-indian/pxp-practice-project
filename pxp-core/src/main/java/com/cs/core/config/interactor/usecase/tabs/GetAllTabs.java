package com.cs.core.config.interactor.usecase.tabs;

import com.cs.core.config.businessapi.tabs.IGetAllTabsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;
import com.cs.core.config.interactor.usecase.tab.IGetAllTabs;
import com.cs.core.config.strategy.usecase.tabs.IGetAllTabsStrategy;

@Service
public class GetAllTabs
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridTabsModel>
    implements IGetAllTabs {
  
  @Autowired
  protected IGetAllTabsService getAllTabsAPI;
  
  @Override
  public IGetGridTabsModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getAllTabsAPI.execute(model);
  }
}

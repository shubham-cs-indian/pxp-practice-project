package com.cs.core.config.businessapi.tabs;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;
import com.cs.core.config.strategy.usecase.tabs.IGetAllTabsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllTabsService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridTabsModel>implements IGetAllTabsService {

  @Autowired
  protected IGetAllTabsStrategy getAllTabsStrategy;

  @Override public IGetGridTabsModel executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getAllTabsStrategy.execute(model);
  }
}

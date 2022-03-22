package com.cs.core.config.interactor.usecase.tabs;

import com.cs.core.config.businessapi.tabs.ICreateTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.usecase.tab.ICreateTab;
import com.cs.core.config.strategy.usecase.tabs.ICreateTabStrategy;

@Service
public class CreateTab extends AbstractCreateConfigInteractor<ICreateTabModel, IGetTabModel> implements ICreateTab {
  
  @Autowired
  protected ICreateTabService createTabAPI;
  
  @Override
  public IGetTabModel executeInternal(ICreateTabModel model) throws Exception
  {
    return createTabAPI.execute(model);
  }
}

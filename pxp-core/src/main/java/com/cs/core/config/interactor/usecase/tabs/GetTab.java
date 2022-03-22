package com.cs.core.config.interactor.usecase.tabs;

import com.cs.core.config.businessapi.tabs.IGetTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.usecase.tab.IGetTab;
import com.cs.core.config.strategy.usecase.tabs.IGetTabStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTab extends AbstractGetConfigInteractor<IIdParameterModel, IGetTabModel>
    implements IGetTab {
  
  @Autowired
  protected IGetTabService getTabAPI;
  
  @Override
  public IGetTabModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getTabAPI.execute(model);
  }
}

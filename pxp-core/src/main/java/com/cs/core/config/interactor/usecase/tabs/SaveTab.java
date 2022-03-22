package com.cs.core.config.interactor.usecase.tabs;

import com.cs.core.config.businessapi.tabs.ISaveTabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.interactor.usecase.tab.ISaveTab;
import com.cs.core.config.strategy.usecase.tabs.ISaveTabStrategy;

@Service
public class SaveTab extends AbstractSaveConfigInteractor<ISaveTabModel, IGetTabModel>
    implements ISaveTab {
  
  @Autowired
  protected ISaveTabService saveTabAPI;
  
  @Override
  public IGetTabModel executeInternal(ISaveTabModel model) throws Exception
  {
    return saveTabAPI.execute(model);
  }
}

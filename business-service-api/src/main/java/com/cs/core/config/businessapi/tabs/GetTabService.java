package com.cs.core.config.businessapi.tabs;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.strategy.usecase.tabs.IGetTabStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTabService extends AbstractGetConfigService<IIdParameterModel, IGetTabModel> implements IGetTabService {
  
  @Autowired
  protected IGetTabStrategy getTabStrategy;
  
  @Override
  public IGetTabModel executeInternal(IIdParameterModel model) throws Exception
  {
    return getTabStrategy.execute(model);
  }
}

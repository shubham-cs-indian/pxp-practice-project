package com.cs.core.config.businessapi.tabs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.strategy.usecase.tabs.ICreateTabStrategy;

@Service
public class CreateTabService extends AbstractCreateConfigService<ICreateTabModel, IGetTabModel> implements ICreateTabService {
  
  @Autowired
  protected ICreateTabStrategy createTabStrategy;
  
  @Autowired
  protected TabValidations tabValidations;
  
  @Override
  public IGetTabModel executeInternal(ICreateTabModel model) throws Exception
  {
    tabValidations.validate(model);
    return createTabStrategy.execute(model);
  }
}

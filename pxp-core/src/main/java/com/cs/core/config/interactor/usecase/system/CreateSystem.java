package com.cs.core.config.interactor.usecase.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.config.strategy.usecase.system.ICreateSystemStrategy;

@Service
public class CreateSystem extends AbstractCreateConfigInteractor<ICreateSystemModel, ISystemModel>
    implements ICreateSystem {
  
  @Autowired
  protected ICreateSystemStrategy createSystemStrategy;
  
  @Override
  public ISystemModel executeInternal(ICreateSystemModel model) throws Exception
  {
    return createSystemStrategy.execute(model);
  }
}

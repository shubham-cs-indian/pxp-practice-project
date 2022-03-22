package com.cs.core.config.interactor.usecase.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.target.ICreateTargetService;

@Service
public class CreateTarget extends AbstractCreateConfigInteractor<ITargetModel, IGetKlassEntityWithoutKPModel>
    implements ICreateTarget {
  
  @Autowired
  protected ICreateTargetService createTargetService;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(ITargetModel klassModel) throws Exception
  {
    return this.createTargetService.execute(klassModel);
  }
}

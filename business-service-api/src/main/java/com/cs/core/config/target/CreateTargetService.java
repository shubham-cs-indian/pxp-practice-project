package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.klass.AbstractCreateKlassService;
import com.cs.core.config.strategy.usecase.target.ICreateTargetStrategy;

@Service
public class CreateTargetService extends AbstractCreateKlassService<ITargetModel, IGetKlassEntityWithoutKPModel>
    implements ICreateTargetService {
  
  @Autowired
  protected ICreateTargetStrategy createTargetStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeCreateKlass(ITargetModel klassModel) throws Exception
  {
    return this.createTargetStrategy.execute(klassModel);
  }
}

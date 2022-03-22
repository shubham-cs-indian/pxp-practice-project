package com.cs.core.config.interactor.usecase.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.state.IGetStateResponseModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.usecase.state.ICreateStateStrategy;

@Service
public class CreateState extends AbstractCreateConfigInteractor<IStateModel, IGetStateResponseModel>
    implements ICreateState {
  
  @Autowired
  ICreateStateStrategy orientCreateStateStrategy;
  
  public IGetStateResponseModel executeInternal(IStateModel stateModel) throws Exception
  {
    IGetStateResponseModel getStateResponseModel = orientCreateStateStrategy.execute(stateModel);
    
    return getStateResponseModel;
  }
}

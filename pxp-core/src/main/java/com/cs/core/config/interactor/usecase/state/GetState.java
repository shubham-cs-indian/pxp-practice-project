package com.cs.core.config.interactor.usecase.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.usecase.state.IGetStateStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetState extends AbstractGetConfigInteractor<IIdParameterModel, IStateModel>
    implements IGetState {
  
  @Autowired
  IGetStateStrategy getStateStrategy;
  
  @Override
  public IStateModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getStateStrategy.execute(dataModel);
  }
}

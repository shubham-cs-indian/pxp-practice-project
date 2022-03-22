package com.cs.core.config.interactor.usecase.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.state.IGetStateResponseModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.usecase.state.ISaveStateStrategy;

@Service
public class SaveState extends AbstractSaveConfigInteractor<IStateModel, IGetStateResponseModel>
    implements ISaveState {
  
  @Autowired
  ISaveStateStrategy orientSaveStateStrategy;
  
  @Override
  public IGetStateResponseModel executeInternal(IStateModel stateModel) throws Exception
  {
    
    return orientSaveStateStrategy.execute(stateModel);
  }
}

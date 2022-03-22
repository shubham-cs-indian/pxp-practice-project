package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.strategy.usecase.target.IGetTargetStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTargetService extends AbstractGetConfigService<IIdParameterModel, ITargetModel>
    implements IGetTargetService {
  
  @Autowired
  IGetTargetStrategy getTargetStrategy;
  
  @Override
  public ITargetModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTargetStrategy.execute(idModel);
  }
}

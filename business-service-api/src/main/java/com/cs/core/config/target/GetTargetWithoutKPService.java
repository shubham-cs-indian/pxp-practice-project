package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.usecase.target.IGetTargetWithoutKPStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTargetWithoutKPService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetTargetWithoutKPService {
  
  @Autowired
  protected IGetTargetWithoutKPStrategy getTargetWithoutKPStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel idModel) throws Exception
  {
    return getTargetWithoutKPStrategy.execute(idModel);
  }
}

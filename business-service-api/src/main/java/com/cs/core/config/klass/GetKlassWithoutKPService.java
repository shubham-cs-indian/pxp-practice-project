package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.klass.IGetKlassWithoutKPService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassWithoutKPStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassWithoutKPService
    extends AbstractGetConfigService<IIdParameterModel, IGetKlassEntityWithoutKPModel>
    implements IGetKlassWithoutKPService {
  
  @Autowired
  protected IGetKlassWithoutKPStrategy getKlassWithoutKPStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return getKlassWithoutKPStrategy.execute(klassModel);
  }
}

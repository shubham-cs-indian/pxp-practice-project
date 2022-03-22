package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.GetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetKlassWithoutKPStrategy extends OrientDBBaseStrategy
    implements IGetKlassWithoutKPStrategy {
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IIdParameterModel model) throws Exception
  {
    return execute(GET_KLASS_WITHOUT_KP, model, GetKlassEntityWithoutKPModel.class);
  }
}

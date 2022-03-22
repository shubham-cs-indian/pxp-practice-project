package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.target.IGetTargetKlassesStrategy;
import com.cs.core.runtime.interactor.model.datarule.GetTargetKlassesStrategyModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesStrategyModel;
import org.springframework.stereotype.Component;

@Component("getTargetKlassesStrategy")
public class OrientDBGetTargetKlassesStrategy extends OrientDBBaseStrategy
    implements IGetTargetKlassesStrategy {
  
  @Override
  public IGetTargetKlassesStrategyModel execute(IGetTargetKlassesModel model) throws Exception
  {
    return execute(GET_TARGET_KLASSES, model, GetTargetKlassesStrategyModel.class);
  }
}

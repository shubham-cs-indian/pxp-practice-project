package com.cs.core.runtime.strategy.usecase.configdetails;


import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.IGetCloneWizardForRequestStrategyModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.clone.GetKlassInstancePropertiesForCloneModel;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;

@Component("getKlassInstancePropertiesForCloneStrategy")
public class GetKlassInstancePropertiesForCloneStrategy extends OrientDBBaseStrategy
    implements IGetKlassInstancePropertiesForCloneStrategy {
  
  public static final String useCase = "GetKlassInstancePropertiesForClone";
  
  @Override
  public IGetKlassInstancePropertiesForCloneModel execute(IGetCloneWizardForRequestStrategyModel model) throws Exception
  {
    return execute(useCase, model, GetKlassInstancePropertiesForCloneModel.class);
  }
  
}

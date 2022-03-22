package com.cs.core.config.strategy.usecase.language;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public class GetCurrentDefaultLanguageStrategy extends OrientDBBaseStrategy
    implements IGetCurrrentDefaultLanguageStrategy {
  
  @Override
  public IConfigEntityInformationModel execute(IModel model) throws Exception
  {
    return super.execute(GET_CURRENT_DEFAULT_LANGUAGE, model, ConfigEntityInformationModel.class);
  }
}

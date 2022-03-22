package com.cs.core.config.strategy.usecase.language;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.language.IGetOrCreateLanguageModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;

@Component
public class GetOrCreateLanguageStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateLanguageStrategy {
  
  @Override
  public IIdParameterModel execute(IGetOrCreateLanguageModel model) throws Exception
  {
    return execute(GET_OR_CREATE_LANGUAGE, model, IdParameterModel.class);
  }
}

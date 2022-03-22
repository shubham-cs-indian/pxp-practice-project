package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.GetLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.stereotype.Component;

@Component
public class GetLanguageByLocaleIdStrategy extends OrientDBBaseStrategy implements IGetLanguageByLocaleIdStrategy {
  
  @Override
  public IGetLanguageModel execute(IIdParameterModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_LANGUAGE_BY_LOCALE_ID, model, GetLanguageModel.class);
  }
}

package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.GetLanguageModel;
import com.cs.core.config.interactor.model.language.ICreateLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateLanguageStrategy extends OrientDBBaseStrategy
    implements ICreateLanguageStrategy {
  
  @Override
  public IGetLanguageModel execute(ICreateLanguageModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.CREATE_LANGUAGE, model, GetLanguageModel.class);
  }
}

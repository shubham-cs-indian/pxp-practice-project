package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.GetLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveLanguageStrategy extends OrientDBBaseStrategy implements ISaveLanguageStrategy {
  
  @Override
  public IGetLanguageModel execute(ILanguageModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.SAVE_LANGUAGE, model, GetLanguageModel.class);
  }
}

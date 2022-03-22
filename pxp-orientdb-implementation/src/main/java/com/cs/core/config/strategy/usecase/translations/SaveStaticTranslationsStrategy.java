package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.SaveTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class SaveStaticTranslationsStrategy extends OrientDBBaseStrategy
    implements ISaveStaticTranslationsStrategy {
  
  @Override
  public ISaveTranslationsResponseModel execute(ISaveTranslationsRequestModel model)
      throws Exception
  {
    return execute(SAVE_STATIC_TRANSLATIONS, model, SaveTranslationsResponseModel.class);
  }
}

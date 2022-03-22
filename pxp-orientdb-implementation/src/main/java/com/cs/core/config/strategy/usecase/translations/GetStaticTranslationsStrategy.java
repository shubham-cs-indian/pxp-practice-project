package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.GetTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetStaticTranslationsStrategy extends OrientDBBaseStrategy
    implements IGetStaticTranslationsStrategy {
  
  @Override
  public IGetTranslationsResponseModel execute(IGetTranslationsRequestModel model) throws Exception
  {
    return execute(GET_STATIC_TRANSLATIONS, model, GetTranslationsResponseModel.class);
  }
}

package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.GetTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetPropertiesTranslationsStrategy extends OrientDBBaseStrategy
    implements IGetPropertiesTranslationsStrategy {
  
  @Override
  public IGetTranslationsResponseModel execute(IGetTranslationsRequestModel model) throws Exception
  {
    return execute(GET_PROPERTIES_TRANSLATIONS, model, GetTranslationsResponseModel.class);
  }
}

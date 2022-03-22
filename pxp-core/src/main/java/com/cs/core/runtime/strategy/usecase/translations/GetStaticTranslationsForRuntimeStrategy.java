package com.cs.core.runtime.strategy.usecase.translations;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.GetStaticLabelTranslationsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IGetStaticLabelTranslationsResponseModel;
import com.cs.core.runtime.interactor.model.translations.IGetStaticTranslationsForRuntimeRequestModel;
import org.springframework.stereotype.Component;

@Component
public class GetStaticTranslationsForRuntimeStrategy extends OrientDBBaseStrategy
    implements IGetStaticTranslationsForRuntimeStrategy {
  
  @Override
  public IGetStaticLabelTranslationsResponseModel execute(
      IGetStaticTranslationsForRuntimeRequestModel model) throws Exception
  {
    return execute(GET_STATIC_TRANSLATIONS_FOR_RUNTIME, model,
        GetStaticLabelTranslationsResponseModel.class);
  }
}

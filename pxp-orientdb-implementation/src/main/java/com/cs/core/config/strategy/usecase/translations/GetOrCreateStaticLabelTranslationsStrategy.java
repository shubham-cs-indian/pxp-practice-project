package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.GetTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.ICreateStaticLabelTranslationsRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Component;

@Component
public class GetOrCreateStaticLabelTranslationsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateStaticLabelTranslationsStrategy {
  
  @Override
  public IModel execute(ICreateStaticLabelTranslationsRequestModel model) throws Exception
  {
    return execute(GET_OR_CREATE_STATIC_TRANSLATIONS, model, GetTranslationsResponseModel.class);
  }
}

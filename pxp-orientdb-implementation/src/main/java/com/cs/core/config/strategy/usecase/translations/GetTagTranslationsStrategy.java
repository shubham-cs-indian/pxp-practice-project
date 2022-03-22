package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.GetTagTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetTagTranslationsStrategy extends OrientDBBaseStrategy
    implements IGetTagTranslationsStrategy {
  
  @Override
  public IGetTagTranslationsResponseModel execute(IGetTagTranslationsRequestModel model)
      throws Exception
  {
    return execute(GET_TAG_TRANSLATIONS, model, GetTagTranslationsResponseModel.class);
  }
}

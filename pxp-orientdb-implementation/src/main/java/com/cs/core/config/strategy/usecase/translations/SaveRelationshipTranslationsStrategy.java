package com.cs.core.config.strategy.usecase.translations;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.SaveRelationshipTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class SaveRelationshipTranslationsStrategy extends OrientDBBaseStrategy
    implements ISaveRelationshipTranslationsStrategy {
  
  @Override
  public ISaveRelationshipTranslationsResponseModel execute(
      ISaveRelationshipTranslationsRequestModel model) throws Exception
  {
    return execute(SAVE_RELATIONSHIP_TRANSLATIONS, model,
        SaveRelationshipTranslationsResponseModel.class);
  }
}

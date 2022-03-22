package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.SaveRelationshipTranslationsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class CreateOrSaveRelationshipTranslationsStrategy extends OrientDBBaseStrategy
    implements ICreateOrSaveRelationshipTranslationsStrategy {
  
  @Override
  public ISaveRelationshipTranslationsResponseModel execute(
      ISaveRelationshipTranslationsRequestModel model) throws Exception
  {
    return execute(CREATE_OR_SAVE_RELATIONSHIP_TRANSLATIONS, model,
        SaveRelationshipTranslationsResponseModel.class);
  }
}

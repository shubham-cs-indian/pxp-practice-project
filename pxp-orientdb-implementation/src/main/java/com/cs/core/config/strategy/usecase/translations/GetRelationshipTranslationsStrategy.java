package com.cs.core.config.strategy.usecase.translations;

import com.cs.core.config.interactor.model.translations.GetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetRelationshipTranslationsStrategy extends OrientDBBaseStrategy
    implements IGetRelationshipTranslationsStrategy {
  
  @Override
  public IGetRelationshipTranslationsResponseModel execute(IGetTranslationsRequestModel model)
      throws Exception
  {
    return execute(GET_RELATIONSHIP_TRANSLATIONS, model,
        GetRelationshipTranslationsResponseModel.class);
  }
}

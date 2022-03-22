package com.cs.core.config.businessapi.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.translations.IGetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.translations.IGetRelationshipTranslationsStrategy;

@Service
public class GetRelationshipTranslationsService
    extends AbstractGetConfigService<IGetTranslationsRequestModel, IGetRelationshipTranslationsResponseModel>
    implements IGetRelationshipTranslationsService {
  
  @Autowired
  protected IGetRelationshipTranslationsStrategy getRelationshipTranslationsStrategy;
  
  @Override
  public IGetRelationshipTranslationsResponseModel executeInternal(IGetTranslationsRequestModel dataModel) throws Exception
  {
    return getRelationshipTranslationsStrategy.execute(dataModel);
  }
}

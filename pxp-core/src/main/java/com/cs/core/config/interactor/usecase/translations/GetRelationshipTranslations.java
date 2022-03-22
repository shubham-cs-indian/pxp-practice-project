package com.cs.core.config.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.translations.IGetRelationshipTranslationsService;
import com.cs.core.config.interactor.model.translations.IGetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;

@Service
public class GetRelationshipTranslations extends
    AbstractGetConfigInteractor<IGetTranslationsRequestModel, IGetRelationshipTranslationsResponseModel>
    implements IGetRelationshipTranslations {
  
  @Autowired
  protected IGetRelationshipTranslationsService getRelationshipTranslationsService;
  
  @Override
  public IGetRelationshipTranslationsResponseModel executeInternal(IGetTranslationsRequestModel dataModel)
      throws Exception
  {
    return getRelationshipTranslationsService.execute(dataModel);
  }
}

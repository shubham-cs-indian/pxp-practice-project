package com.cs.core.config.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.businessapi.translations.ISaveRelationshipTranslationsService;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;

@Service
public class SaveRelationshipTranslations extends
    AbstractSaveConfigInteractor<ISaveRelationshipTranslationsRequestModel, ISaveRelationshipTranslationsResponseModel>
    implements ISaveRelationshipTranslations {
  
  @Autowired
  protected ISaveRelationshipTranslationsService saveRelationshipTranslationsService;
  
  @Override
  public ISaveRelationshipTranslationsResponseModel executeInternal(ISaveRelationshipTranslationsRequestModel dataModel) throws Exception
  {
    return saveRelationshipTranslationsService.execute(dataModel);
  }
}

package com.cs.core.config.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.businessapi.translations.ISavePropertiesTranslationsService;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;

@Service
public class SavePropertiesTranslations extends
    AbstractSaveConfigInteractor<ISaveTranslationsRequestModel, ISaveTranslationsResponseModel>
    implements ISavePropertiesTranslations {
  
  @Autowired
  protected ISavePropertiesTranslationsService savePropertiesTranslationsService;
  
  @Override
  public ISaveTranslationsResponseModel executeInternal(ISaveTranslationsRequestModel dataModel)
      throws Exception
  {
    return savePropertiesTranslationsService.execute(dataModel);
  }
}

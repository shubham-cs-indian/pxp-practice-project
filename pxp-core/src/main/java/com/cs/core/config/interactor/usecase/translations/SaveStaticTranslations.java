package com.cs.core.config.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.businessapi.translations.ISaveStaticTranslationsService;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;

@Service
public class SaveStaticTranslations extends
    AbstractSaveConfigInteractor<ISaveTranslationsRequestModel, ISaveTranslationsResponseModel>
    implements ISaveStaticTranslations {
  
  @Autowired
  protected ISaveStaticTranslationsService saveStaticTranslationsService;
  
  @Override
  public ISaveTranslationsResponseModel executeInternal(ISaveTranslationsRequestModel dataModel)
      throws Exception
  {
    return saveStaticTranslationsService.execute(dataModel);
  }
}

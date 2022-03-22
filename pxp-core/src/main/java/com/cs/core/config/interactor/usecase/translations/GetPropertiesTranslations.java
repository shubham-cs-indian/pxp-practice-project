package com.cs.core.config.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.translations.IGetPropertiesTranslationsService;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;

@Service
public class GetPropertiesTranslations
    extends AbstractGetConfigInteractor<IGetTranslationsRequestModel, IGetTranslationsResponseModel>
    implements IGetPropertiesTranslations {
  
  @Autowired
  protected IGetPropertiesTranslationsService getPropertiesTranslationsService;
  
  @Override
  public IGetTranslationsResponseModel executeInternal(IGetTranslationsRequestModel dataModel)
      throws Exception
  {
    return getPropertiesTranslationsService.execute(dataModel);
  }
}

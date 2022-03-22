package com.cs.core.config.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.translations.IGetTagTranslationsService;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsResponseModel;

@Service
public class GetTagTranslations extends
    AbstractGetConfigInteractor<IGetTagTranslationsRequestModel, IGetTagTranslationsResponseModel>
    implements IGetTagTranslations {
  
  @Autowired
  protected IGetTagTranslationsService getTagTranslationsService;
  
  @Override
  public IGetTagTranslationsResponseModel executeInternal(IGetTagTranslationsRequestModel dataModel)
      throws Exception
  {
    return getTagTranslationsService.execute(dataModel);
  }
}

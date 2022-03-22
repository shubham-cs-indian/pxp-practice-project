package com.cs.core.config.businessapi.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;
import com.cs.core.config.strategy.usecase.translations.ISaveStaticTranslationsStrategy;

@Service
public class SaveStaticTranslationsService extends AbstractSaveConfigService<ISaveTranslationsRequestModel, ISaveTranslationsResponseModel>
    implements ISaveStaticTranslationsService {
  
  @Autowired
  protected ISaveStaticTranslationsStrategy saveStaticTranslationsStrategy;
  
  @Override
  public ISaveTranslationsResponseModel executeInternal(ISaveTranslationsRequestModel dataModel) throws Exception
  {
    return saveStaticTranslationsStrategy.execute(dataModel);
  }
}

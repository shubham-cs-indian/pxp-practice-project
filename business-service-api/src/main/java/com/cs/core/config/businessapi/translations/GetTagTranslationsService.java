package com.cs.core.config.businessapi.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsResponseModel;
import com.cs.core.config.strategy.usecase.translations.IGetTagTranslationsStrategy;

@Service
public class GetTagTranslationsService extends AbstractGetConfigService<IGetTagTranslationsRequestModel, IGetTagTranslationsResponseModel>
    implements IGetTagTranslationsService {
  
  @Autowired
  protected IGetTagTranslationsStrategy getTagTranslationsStrategy;
  
  @Override
  public IGetTagTranslationsResponseModel executeInternal(IGetTagTranslationsRequestModel dataModel) throws Exception
  {
    return getTagTranslationsStrategy.execute(dataModel);
  }
}

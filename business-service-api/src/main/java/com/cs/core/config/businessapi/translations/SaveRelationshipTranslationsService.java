package com.cs.core.config.businessapi.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveRelationshipTranslationsResponseModel;
import com.cs.core.config.strategy.usecase.translations.ISaveRelationshipTranslationsStrategy;

@Service
public class SaveRelationshipTranslationsService
    extends AbstractSaveConfigService<ISaveRelationshipTranslationsRequestModel, ISaveRelationshipTranslationsResponseModel>
    implements ISaveRelationshipTranslationsService {
  
  @Autowired
  protected ISaveRelationshipTranslationsStrategy saveRelationshipTranslationsStrategy;
  
  @Override
  public ISaveRelationshipTranslationsResponseModel executeInternal(ISaveRelationshipTranslationsRequestModel dataModel) throws Exception
  {
    return saveRelationshipTranslationsStrategy.execute(dataModel);
  }
}

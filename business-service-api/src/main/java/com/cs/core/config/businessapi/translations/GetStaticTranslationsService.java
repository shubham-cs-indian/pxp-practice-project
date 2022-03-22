package com.cs.core.config.businessapi.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
import com.cs.core.config.strategy.usecase.translations.IGetStaticTranslationsStrategy;

@Service
public class GetStaticTranslationsService extends AbstractGetConfigService<IGetTranslationsRequestModel, IGetTranslationsResponseModel>
    implements IGetStaticTranslationsService {
  
  @Autowired
  protected IGetStaticTranslationsStrategy getStaticTranslationsStrategy;
  
  /*  @Autowired
  TranslationUtils translationUtils;*/
  
  @Override
  public IGetTranslationsResponseModel executeInternal(IGetTranslationsRequestModel dataModel) throws Exception
  {
    /*dataModel.setSize(100000);
    IGetTranslationsResponseModel execute = getStaticTranslationsStrategy.execute(dataModel);
    translationUtils.prepareDataForExportOfStaticTranslation(dataModel.getEntityType(),
        dataModel.getEntityType(), execute);*/
    return getStaticTranslationsStrategy.execute(dataModel);
  }
}

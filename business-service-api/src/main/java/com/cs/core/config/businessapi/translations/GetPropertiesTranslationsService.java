package com.cs.core.config.businessapi.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
import com.cs.core.config.strategy.usecase.translations.IGetPropertiesTranslationsStrategy;

@Service
public class GetPropertiesTranslationsService extends AbstractGetConfigService<IGetTranslationsRequestModel, IGetTranslationsResponseModel>
    implements IGetPropertiesTranslationsService {
  
  @Autowired
  protected IGetPropertiesTranslationsStrategy getPropertiesTranslationsStrategy;
  
  /*  @Autowired
  TranslationUtils translationUtils;*/
  
  @Override
  public IGetTranslationsResponseModel executeInternal(IGetTranslationsRequestModel dataModel) throws Exception
  {
    /*dataModel.setSize(100000);
    IGetTranslationsResponseModel execute = getPropertiesTranslationsStrategy.execute(dataModel);
    translationUtils.prepareDataForExportOfStaticTranslation(
        dataModel.getEntityType() == null ? "" : dataModel.getEntityType(),
        dataModel.getEntityType() == null ? "Dynamic" : dataModel.getEntityType(), execute);*/
    return getPropertiesTranslationsStrategy.execute(dataModel);
  }
}

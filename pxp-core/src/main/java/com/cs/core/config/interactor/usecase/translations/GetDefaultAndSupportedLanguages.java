package com.cs.core.config.interactor.usecase.translations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.translations.IGetDefaultAndSupportedLanguagesService;
import com.cs.core.config.interactor.model.translations.IDefaultAndSupportingLanguagesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetDefaultAndSupportedLanguages
    extends AbstractGetConfigInteractor<IIdParameterModel, IDefaultAndSupportingLanguagesModel>
    implements IGetDefaultAndSupportedLanguages {
  
  @Autowired
  protected IGetDefaultAndSupportedLanguagesService getDefaultAndSupportedLanguagesService;
  
  @Override
  public IDefaultAndSupportingLanguagesModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getDefaultAndSupportedLanguagesService.execute(dataModel);
  }
}

package com.cs.core.config.interactor.usecase.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.language.IGetCurrentDefaultLanguageService;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetCurrentDefaultLanguage
    extends AbstractGetConfigInteractor<IModel, IConfigEntityInformationModel>
    implements IGetCurrentDefaultLanguage {
  
  @Autowired
  private IGetCurrentDefaultLanguageService getCurrentDefaultLanguageService;
  
  @Override
  public IConfigEntityInformationModel executeInternal(IModel dataModel) throws Exception
  {
    return getCurrentDefaultLanguageService.execute(dataModel);
  }
}

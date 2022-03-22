package com.cs.core.config.interactor.usecase.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.language.IGetLanguageService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetLanguage extends AbstractGetConfigInteractor<IIdParameterModel, IGetLanguageModel> implements IGetLanguage {
  
  @Autowired
  protected IGetLanguageService getLanguageService;
  
  @Override
  public IGetLanguageModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getLanguageService.execute(dataModel);
  }
}

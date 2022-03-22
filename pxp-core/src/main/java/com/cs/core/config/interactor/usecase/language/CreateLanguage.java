package com.cs.core.config.interactor.usecase.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.language.ICreateLanguageModel;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.language.ICreateLanguageService;

@Service
public class CreateLanguage
    extends AbstractCreateConfigInteractor<ICreateLanguageModel, IGetLanguageModel>
    implements ICreateLanguage {
  
  @Autowired
  protected ICreateLanguageService createLanguageService;

  @Override
  public IGetLanguageModel executeInternal(ICreateLanguageModel dataModel) throws Exception
  {
    return createLanguageService.execute(dataModel);
  }
  
  
}

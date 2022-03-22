package com.cs.core.config.interactor.usecase.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.language.ISaveLanguageService;

@Service
public class SaveLanguage extends AbstractSaveConfigInteractor<ILanguageModel, IGetLanguageModel> implements ISaveLanguage {
  
  @Autowired
  protected ISaveLanguageService saveLanguageService;
  
  @Override
  public IGetLanguageModel executeInternal(ILanguageModel dataModel) throws Exception
  {
    return saveLanguageService.execute(dataModel);
  }
}

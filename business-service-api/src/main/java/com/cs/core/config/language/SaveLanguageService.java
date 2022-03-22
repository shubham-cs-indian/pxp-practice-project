package com.cs.core.config.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.language.IGetLanguageModel;
import com.cs.core.config.interactor.model.language.ILanguageModel;
import com.cs.core.config.strategy.usecase.language.ISaveLanguageStrategy;

@Service
public class SaveLanguageService extends AbstractSaveConfigService<ILanguageModel, IGetLanguageModel> implements ISaveLanguageService {
  
  @Autowired
  protected ISaveLanguageStrategy saveLanguageStrategy;

  @Autowired
  protected LanguageValidations languageValidations;
  @Override
  public IGetLanguageModel executeInternal(ILanguageModel dataModel) throws Exception
  {
    languageValidations.validateLanguage(dataModel);
    return saveLanguageStrategy.execute(dataModel);
  }
}

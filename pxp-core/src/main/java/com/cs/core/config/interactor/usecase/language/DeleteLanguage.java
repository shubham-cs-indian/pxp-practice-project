package com.cs.core.config.interactor.usecase.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.config.language.IDeleteLanguageService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;

@Service
public class DeleteLanguage extends AbstractDeleteConfigInteractor<IDeleteLanguageRequestModel, IBulkDeleteReturnModel>
    implements IDeleteLanguage {
  
  @Autowired
  protected IDeleteLanguageService deleteLanguageService;
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IDeleteLanguageRequestModel dataModel) throws Exception
  {
    return deleteLanguageService.execute(dataModel);

  }
}

package com.cs.core.config.interactor.usecase.language;

import com.cs.config.interactor.usecase.base.IDeleteConfigInteractor;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;

public interface IDeleteLanguage extends IDeleteConfigInteractor<IDeleteLanguageRequestModel, IBulkDeleteReturnModel> {
  
}

package com.cs.core.config.language;

import com.cs.config.businessapi.base.IDeleteConfigService;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;

public interface IDeleteLanguageService extends IDeleteConfigService<IDeleteLanguageRequestModel, IBulkDeleteReturnModel> {
  
}

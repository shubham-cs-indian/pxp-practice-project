package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;

public interface IDeleteLanguageStrategy
    extends IConfigStrategy<IDeleteLanguageRequestModel, IBulkDeleteReturnModel> {
  
}

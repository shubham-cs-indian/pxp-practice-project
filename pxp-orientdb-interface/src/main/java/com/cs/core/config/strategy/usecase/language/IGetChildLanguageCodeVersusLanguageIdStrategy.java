package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.config.interactor.model.language.IGetChildLanguageCodeAgainstLanguageIdReturnModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetChildLanguageCodeVersusLanguageIdStrategy
    extends IConfigStrategy<IDeleteLanguageRequestModel, IGetChildLanguageCodeAgainstLanguageIdReturnModel> {
  
}

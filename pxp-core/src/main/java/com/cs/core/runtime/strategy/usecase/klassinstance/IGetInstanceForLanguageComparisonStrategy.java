package com.cs.core.runtime.strategy.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetInstanceForLanguageComparisonStrategy
    extends IRuntimeStrategy<ILanguageComparisonRequestModel, ILanguageComparisonResponseModel> {
  
}

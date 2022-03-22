package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.interactor.model.language.IGetLanguagesRequestModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetLanguagesStrategy
    extends IConfigStrategy<IGetLanguagesRequestModel, IGetLanguagesResponseModel> {
  
}

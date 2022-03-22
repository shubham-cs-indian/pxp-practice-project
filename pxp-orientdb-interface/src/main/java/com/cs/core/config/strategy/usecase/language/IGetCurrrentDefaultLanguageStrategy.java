package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetCurrrentDefaultLanguageStrategy
    extends IConfigStrategy<IModel, IConfigEntityInformationModel> {
  
}

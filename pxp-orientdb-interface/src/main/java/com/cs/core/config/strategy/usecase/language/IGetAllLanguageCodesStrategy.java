package com.cs.core.config.strategy.usecase.language;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAllLanguageCodesStrategy
    extends IConfigStrategy<IModel, IIdsListParameterModel> {
  
}

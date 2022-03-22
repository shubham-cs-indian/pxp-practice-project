package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetTypeKlassByIdStrategy
    extends IConfigStrategy<IIdParameterModel, IIdAndTypeModel> {
  
}

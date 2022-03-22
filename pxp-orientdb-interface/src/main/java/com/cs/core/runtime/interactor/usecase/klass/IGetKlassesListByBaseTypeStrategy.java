package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesByBaseTypeModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetKlassesListByBaseTypeStrategy
    extends IConfigStrategy<IGetKlassesByBaseTypeModel, IGetConfigDataEntityResponseModel> {
}

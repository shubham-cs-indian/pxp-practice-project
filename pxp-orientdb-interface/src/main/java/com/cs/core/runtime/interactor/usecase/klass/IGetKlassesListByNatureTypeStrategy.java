package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetKlassesListByNatureTypeStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel> {
}

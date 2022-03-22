package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;

public interface IGetFilterAndSortDataStrategy
    extends IConfigStrategy<IGetFilterAndSortDataRequestModel, IGetFilterInformationModel> {
  
}

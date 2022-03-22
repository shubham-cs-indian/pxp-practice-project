package com.cs.core.config.strategy.usecase.grideditpropertylist;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertiesResponseModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllGridEditablePropertiesStrategy
    extends IConfigStrategy<IConfigGetAllRequestModel, IGetGridEditPropertiesResponseModel> {
  
}

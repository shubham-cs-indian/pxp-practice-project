package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAvailableMasterTagsStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<IGetEntityModel>> {
  
}

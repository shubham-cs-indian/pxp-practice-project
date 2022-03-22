package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetAllTaskStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<IConfigTaskInformationModel>> {
  
}

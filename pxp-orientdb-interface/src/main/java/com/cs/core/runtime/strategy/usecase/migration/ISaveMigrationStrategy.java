package com.cs.core.runtime.strategy.usecase.migration;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IMigrationModel;

public interface ISaveMigrationStrategy
    extends IStrategy<IListModel<IMigrationModel>, IIdsListParameterModel> {
  
}

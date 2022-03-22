package com.cs.core.config.strategy.usecase.state;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.state.IStateInformationModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllStatesStrategy
    extends IConfigStrategy<IStateModel, IListModel<IStateInformationModel>> {
  
}

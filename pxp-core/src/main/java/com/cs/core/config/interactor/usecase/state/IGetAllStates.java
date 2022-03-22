package com.cs.core.config.interactor.usecase.state;

import com.cs.config.interactor.usecase.base.IGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.state.IStateInformationModel;
import com.cs.core.config.interactor.model.state.IStateModel;

public interface IGetAllStates
    extends IGetConfigInteractor<IStateModel, IListModel<IStateInformationModel>> {
  
}

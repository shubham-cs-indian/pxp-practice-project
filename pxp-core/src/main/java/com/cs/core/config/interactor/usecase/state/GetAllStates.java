package com.cs.core.config.interactor.usecase.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.state.IStateInformationModel;
import com.cs.core.config.interactor.model.state.IStateModel;
import com.cs.core.config.strategy.usecase.state.IGetAllStatesStrategy;

@Service
public class GetAllStates
    extends AbstractGetConfigInteractor<IStateModel, IListModel<IStateInformationModel>>
    implements IGetAllStates {
  
  @Autowired
  IGetAllStatesStrategy orientGetStatesStrategy;
  
  @Override
  public IListModel<IStateInformationModel> executeInternal(IStateModel dataModel) throws Exception
  {
    return orientGetStatesStrategy.execute(dataModel);
  }
}

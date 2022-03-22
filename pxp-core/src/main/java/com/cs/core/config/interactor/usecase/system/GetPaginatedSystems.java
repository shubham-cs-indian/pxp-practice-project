package com.cs.core.config.interactor.usecase.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.system.ISystemModel;
import com.cs.core.config.strategy.usecase.system.IGetPaginatedSystemsStrategy;

@Service
public class GetPaginatedSystems
    extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IListModel<ISystemModel>>
    implements IGetPaginatedSystems {
  
  @Autowired
  protected IGetPaginatedSystemsStrategy getPaginatedSystemsStrategy;
  
  @Override
  public IListModel<ISystemModel> executeInternal(IConfigGetAllRequestModel model) throws Exception
  {
    return getPaginatedSystemsStrategy.execute(model);
  }
}

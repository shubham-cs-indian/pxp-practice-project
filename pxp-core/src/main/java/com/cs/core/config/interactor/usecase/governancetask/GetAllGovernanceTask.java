package com.cs.core.config.interactor.usecase.governancetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.governancetask.IGetAllGovernanceTaskStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllGovernanceTask
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IConfigTaskInformationModel>>
    implements IGetAllGovernanceTask {
  
  @Autowired
  protected IGetAllGovernanceTaskStrategy getAllGovernanceTaskStrategy;
  
  @Override
  public IListModel<IConfigTaskInformationModel> executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllGovernanceTaskStrategy.execute(model);
  }
}

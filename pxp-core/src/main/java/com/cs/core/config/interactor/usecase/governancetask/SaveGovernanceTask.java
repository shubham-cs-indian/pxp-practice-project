package com.cs.core.config.interactor.usecase.governancetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.usecase.governancetask.ISaveGovernanceTaskStrategy;

@Service
public class SaveGovernanceTask
    extends AbstractSaveConfigInteractor<IListModel<ITaskModel>, IBulkSaveTasksResponseModel>
    implements ISaveGovernanceTask {
  
  @Autowired
  protected ISaveGovernanceTaskStrategy saveGovernanceTaskStrategy;
  
  @Override
  public IBulkSaveTasksResponseModel executeInternal(IListModel<ITaskModel> model) throws Exception
  {
    IBulkSaveTasksResponseModel returnModel = saveGovernanceTaskStrategy.execute(model);
    
    return returnModel;
  }
}

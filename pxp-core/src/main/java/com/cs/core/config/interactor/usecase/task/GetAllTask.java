package com.cs.core.config.interactor.usecase.task;

import com.cs.core.config.businessapi.task.IGetAllTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.task.IGetAllTaskStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllTask
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IConfigTaskInformationModel>>
    implements IGetAllTask {
  
  @Autowired
  IGetAllTaskService getAllTaskAPI;
  
  @Override
  public IListModel<IConfigTaskInformationModel> executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllTaskAPI.execute(model);
  }
}

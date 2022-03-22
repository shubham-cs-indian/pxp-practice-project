package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.task.IGetAllTaskStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigTaskInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllTasksService extends AbstractGetConfigService<IIdParameterModel, IListModel<IConfigTaskInformationModel>> implements IGetAllTaskService {
  
  @Autowired
  IGetAllTaskStrategy getAllTaskStrategy;
  
  @Override
  public IListModel<IConfigTaskInformationModel> executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllTaskStrategy.execute(model);
  }
}

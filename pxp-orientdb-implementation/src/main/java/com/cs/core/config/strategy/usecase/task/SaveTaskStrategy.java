package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.BulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveTaskStrategy extends OrientDBBaseStrategy implements ISaveTaskStrategy {
  
  @Override
  public IBulkSaveTasksResponseModel execute(IListModel<ITaskModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(SAVE_TASK, requestMap, BulkSaveTasksResponseModel.class);
  }
}

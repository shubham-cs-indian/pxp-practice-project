package com.cs.core.config.strategy.usecase.governancetask;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.BulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SaveGovernanceTaskStrategy extends OrientDBBaseStrategy
    implements ISaveGovernanceTaskStrategy {
  
  @Override
  public IBulkSaveTasksResponseModel execute(IListModel<ITaskModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(OrientDBBaseStrategy.SAVE_GOVERNANCE_TASK, requestMap,
        BulkSaveTasksResponseModel.class);
  }
}

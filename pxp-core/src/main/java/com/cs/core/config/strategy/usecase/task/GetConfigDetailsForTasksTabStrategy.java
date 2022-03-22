package com.cs.core.config.strategy.usecase.task;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.templating.GetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetConfigDetailsForTasksTabStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsForTasksTabStrategy {
  
  @Autowired
  ISessionContext context;
  
  @SuppressWarnings("unchecked")
  @Override
  public IGetConfigDetailsForTasksTabModel execute(IMulticlassificationRequestModel model)
      throws Exception
  {
    String userId = context.getUserId();
    Map<String, Object> map = ObjectMapperUtil.convertValue(model, HashMap.class);
    map.put("userId", userId);
    return execute(GET_TASKS_CONFIG_DETAILS, map, GetConfigDetailsForTasksTabModel.class);
  }
}

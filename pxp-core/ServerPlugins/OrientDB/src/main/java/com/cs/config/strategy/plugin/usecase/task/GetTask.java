package com.cs.config.strategy.plugin.usecase.task;

import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetTask extends AbstractOrientPlugin {
  
  public GetTask(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String taskId = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Vertex vertex = null;
    try {
      vertex = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
    }
    catch (NotFoundException e) {
      throw new TaskNotFoundException();
    }
    
    returnMap = TasksUtil.getTaskMapFromNode(vertex);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTask/*" };
  }
}

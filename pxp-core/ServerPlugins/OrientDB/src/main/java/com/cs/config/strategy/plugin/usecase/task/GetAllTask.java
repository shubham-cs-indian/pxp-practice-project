package com.cs.config.strategy.plugin.usecase.task;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllTask extends AbstractOrientPlugin {
  
  public GetAllTask(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex vertex : resultIterable) {
      Map<String, Object> map = new HashMap<>();
      map.put(CommonConstants.ID_PROPERTY, vertex.getProperty(CommonConstants.CODE_PROPERTY));
      map.put(ITaskModel.LABEL, UtilClass.getValueByLanguage(vertex, ITaskModel.LABEL));
      map.put(ITaskModel.COLOR, vertex.getProperty(ITaskModel.COLOR));
      map.put(ITaskModel.CODE, vertex.getProperty(ITask.CODE));
      list.add(map);
    }
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IListModel.LIST, list);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllTask/*" };
  }
}

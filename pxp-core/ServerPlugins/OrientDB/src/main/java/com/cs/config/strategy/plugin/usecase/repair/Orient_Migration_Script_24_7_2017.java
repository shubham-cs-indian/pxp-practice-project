// This plugin sets type of all the existing tasks(Config) to shared
package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_24_7_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_24_7_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    int count = 0;
    Iterator<Vertex> tasksIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK)
        .iterator();
    while (tasksIterator.hasNext()) {
      Vertex taskNode = tasksIterator.next();
      String type = CommonConstants.TASK_TYPE_SHARED;
      taskNode.setProperty(ITask.TYPE, type);
      count++;
    }
    UtilClass.getGraph()
        .commit();
    returnMap.put("Count", count);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_24_7_2017/*" };
  }
}

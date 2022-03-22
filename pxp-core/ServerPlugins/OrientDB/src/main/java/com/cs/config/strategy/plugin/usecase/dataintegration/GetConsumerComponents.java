package com.cs.config.strategy.plugin.usecase.dataintegration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.WorkflowType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetConsumerComponents extends AbstractOrientPlugin {
  
  public GetConsumerComponents(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConsumerComponents/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    ArrayList<Object> list = new ArrayList<>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    Iterable<Vertex> processEventNodes = UtilClass.getGraph()
        .command(new OCommandSQL("select * from `Process_Event`"))
        .execute();
    for (Vertex processEventNode : processEventNodes) {
      HashMap<String, Object> processEventMap = new HashMap<String, Object>();
      processEventMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), processEventNode));
      list.add(processEventMap);
    }
    returnMap.put(IListModel.LIST, list);
    return returnMap;
  }
}

package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.processevent.ProcessEventNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetProcessEventsByIdsResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetProcessEventsByIds extends AbstractOrientPlugin {
  
  public GetProcessEventsByIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProcessEventsByIds/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> processMap = new HashMap<String, Object>();
    Map<String, Map<String, Object>> processEventsMap = new HashMap<>();
    Map<String, Object> returnMap = new HashMap<>();
    List<String> idsList = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    Iterator<Vertex> processEventVertices = UtilClass
        .getVerticesByIds(idsList, VertexLabelConstants.PROCESS_EVENT)
        .iterator();
    if (!processEventVertices.hasNext()) {
      throw new ProcessEventNotFoundException();
    }
    while (processEventVertices.hasNext()) {
      Vertex processEventNode = processEventVertices.next();
      processMap = new HashMap<>();
      processMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), processEventNode));
      processEventsMap.put((String) processMap.get(IIdParameterModel.ID), processMap);
    }
    returnMap.put(IGetProcessEventsByIdsResponseModel.PROCESS_EVENTS, processEventsMap);
    return returnMap;
  }
}

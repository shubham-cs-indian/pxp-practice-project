package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.di.config.interactor.model.initializeworflowevent.IGetAllProcessEventsResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllExecutableProcessEvents extends AbstractOrientPlugin {
  
  public GetAllExecutableProcessEvents(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllExecutableProcessEvents/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String query = "select from " + VertexLabelConstants.PROCESS_EVENT + " where "
        + IProcessEvent.IS_EXECUTABLE + " = true";
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllProcessEventsResponseModel.LIST, list);
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> processesToReturn = new ArrayList<>();
    for (Vertex processNode : searchResults) {
      Map<String, Object> processMap = UtilClass.getMapFromVertex(Arrays.asList(
          CommonConstants.CODE_PROPERTY, IProcessEvent.LABEL, IProcessEvent.PROCESS_TYPE,
          IProcessEvent.CODE, IProcessEvent.EVENT_TYPE, IProcessEvent.IS_EXECUTABLE,
          IProcessEvent.IS_XML_MODIFIED, IProcessEvent.TRIGGERING_TYPE), processNode);
      processesToReturn.add(processMap);
    }
    return processesToReturn;
  }
}

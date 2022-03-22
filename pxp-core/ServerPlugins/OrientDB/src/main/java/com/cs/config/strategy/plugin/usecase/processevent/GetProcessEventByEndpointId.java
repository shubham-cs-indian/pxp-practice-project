package com.cs.config.strategy.plugin.usecase.processevent;

import com.cs.config.strategy.plugin.usecase.processevent.util.ProcessEventUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.processevent.IGetProcessEndpointEventModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GetProcessEventByEndpointId extends AbstractOrientPlugin {
  
  public GetProcessEventByEndpointId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetProcessEventByEndpointId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> processToReturn = new HashMap<>();
    String endpointId = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> process = new HashMap<>();
    String systemId = null;
    
    Vertex endpointVertex = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
    Iterator<Vertex> processesIterator = endpointVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.PROFILE_PROCESS_LINK)
        .iterator();
    
    if (processesIterator.hasNext()) {
      process = getProcesses(processesIterator.next(), new String());
      Iterator<Vertex> systemNodeIterator = endpointVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_SYSTEM)
          .iterator();
      if (systemNodeIterator.hasNext()) {
        Vertex systemVertex = systemNodeIterator.next();
        systemId = systemVertex.getProperty(CommonConstants.CODE_PROPERTY);
      }
    }
    
    processToReturn.put(IGetProcessEndpointEventModel.PROCESS_FLOW,
        process.get(IGetProcessEndpointEventModel.PROCESS_FLOW));
    processToReturn.put(IGetProcessEndpointEventModel.ENDPOINTID, endpointId);
    processToReturn.put(IGetProcessEndpointEventModel.SYSTEMID, systemId);
    processToReturn.put(IGetProcessEndpointEventModel.ID, process.get(CommonConstants.ID_PROPERTY));
    processToReturn.put(IGetProcessEndpointEventModel.PROCESS_LABEL,
        process.get(CommonConstants.LABEL_PROPERTY));
    processToReturn.put(IGetProcessEndpointEventModel.PROCESS_DEFINITION_ID,
        process.get(IGetProcessEndpointEventModel.PROCESS_DEFINITION_ID));
    return processToReturn;
  }
  
  private static Map<String, Object> getProcesses(Vertex processNode, String eventType)
      throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), processNode));
    ProcessEventUtils.getProcessEventNodeWithConfigInformation(processNode, returnMap,
        new HashMap<>());
    
    return returnMap;
  }
}

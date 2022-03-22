package com.cs.config.strategy.plugin.usecase.system;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrCreateSystems extends AbstractOrientPlugin {
  
  public static final List<String> FIELDS_TO_EXCLUDE = Arrays
      .asList(ICreateSystemModel.ENDPOINT_ID);
  
  public GetOrCreateSystems(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateSystems/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap.get("list");
    for (Map<String, Object> map : list) {
      String id = (String) map.get(ICreateSystemModel.ID);
      try {
        UtilClass.getVertexById(id, VertexLabelConstants.SYSTEM);
        continue; // It is already created. Don't try to create it again
      }
      catch (NotFoundException e) {
        // Do nothing
      }
      String endpointId = (String) requestMap.get(ICreateSystemModel.ENDPOINT_ID);
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.SYSTEM,
          CommonConstants.CODE_PROPERTY);
      Vertex systemVertex = UtilClass.createNode(map, vertexType, FIELDS_TO_EXCLUDE);
      if (endpointId != null) {
        Vertex endpointVertex = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
        endpointVertex.addEdge(RelationshipLabelConstants.HAS_SYSTEM, systemVertex);
      }
    }
    UtilClass.getGraph()
        .commit();
    return new HashMap<String, Object>();
  }
}

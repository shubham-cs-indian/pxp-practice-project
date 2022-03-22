package com.cs.config.strategy.plugin.usecase.system;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.system.ICreateSystemModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CreateSystem extends AbstractOrientPlugin {
  
  public static final List<String> FIELDS_TO_EXCLUDE = Arrays
      .asList(ICreateSystemModel.ENDPOINT_ID);
  
  public CreateSystem(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateSystem/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String endpointId = (String) requestMap.get(ICreateSystemModel.ENDPOINT_ID);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.SYSTEM,
        CommonConstants.CODE_PROPERTY);
    Vertex systemVertex = UtilClass.createNode(requestMap, vertexType, FIELDS_TO_EXCLUDE);
    if (endpointId != null) {
      Vertex endpointVertex = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
      endpointVertex.addEdge(RelationshipLabelConstants.HAS_SYSTEM, systemVertex);
    }
    UtilClass.getGraph()
        .commit();
    return UtilClass.getMapFromNode(systemVertex);
  }
}

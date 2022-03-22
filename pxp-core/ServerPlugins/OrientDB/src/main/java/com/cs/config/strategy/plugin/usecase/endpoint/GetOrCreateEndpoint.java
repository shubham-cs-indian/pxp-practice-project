package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.exception.validationontype.InvalidEnpointTypeException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrCreateEndpoint extends AbstractOrientPlugin {
  
  public GetOrCreateEndpoint(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<Map<String, Object>> endPoints = (List<Map<String, Object>>) map.get(IListModel.LIST);
    OrientGraph graph = UtilClass.getGraph();
    for (Map<String, Object> endPoint : endPoints) {
      String endPointId = (String) endPoint.get(IEndpoint.ID);
      try {
        UtilClass.getVertexById(endPointId, VertexLabelConstants.ENDPOINT);
      }
      catch (NotFoundException e) {
        try {
          UtilClass.validateOnType(Constants.ENDPOINT_TYPE_LIST,
              (String) endPoint.get(IEndpointModel.ENDPOINT_TYPE), true);
        }
        catch (InvalidTypeException ex) {
          throw new InvalidEnpointTypeException(ex);
        }
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENDPOINT,
            CommonConstants.CODE_PROPERTY);
        Vertex endPointNode = UtilClass.createNode(endPoint, vertexType, new ArrayList<>());
      }
    }
    
    graph.commit();
    
    return new HashMap<>();
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateEndpoint/*" };
  }
}

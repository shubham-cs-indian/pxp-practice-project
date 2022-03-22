package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetEndpoint extends AbstractOrientPlugin {
  
  public GetEndpoint(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String profileId = (String) map.get(CommonConstants.ID_PROPERTY);
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex profileNode = UtilClass.getVertexByIndexedId(profileId, VertexLabelConstants.ENDPOINT);
      List<Map<String, Object>> profilesList = new ArrayList<>();
      Map<String, Object> profileMap = UtilClass.getMapFromVertex(new ArrayList<>(), profileNode);
      returnMap.put(IGetEndpointForGridModel.ENDPOINT, profileMap);
      EndpointUtils.getMapFromProfileNode(profileNode, returnMap, new ArrayList<>());
      profilesList.add(returnMap);
      EndpointUtils.fillReferencedConfigDetails(profilesList, returnMap);
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEndpoint/*" };
  }
}

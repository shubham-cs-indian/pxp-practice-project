package com.cs.config.strategy.plugin.usecase.authorization;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class DeletePartnerAuthorizationMapping extends AbstractOrientPlugin {
  
  public DeletePartnerAuthorizationMapping(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> authrizationMappingIds = (List<String>) map.get(IIdsListParameterModel.IDS);
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> deletedIds = new ArrayList<>();
    for (String id : authrizationMappingIds) {
      Vertex authorizationMappingNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.AUTHORIZATION_MAPPING);
      authorizationMappingNode.remove();
      deletedIds.add(id);
    }
    graph.commit();
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("success", deletedIds);
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeletePartnerAuthorizationMapping/*" };
  }
}

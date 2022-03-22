package com.cs.config.strategy.plugin.usecase.asset;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetAssetsByIds extends AbstractOrientPlugin {
  
  public GetAssetsByIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    List<String> klassIds = new ArrayList<String>();
    OrientGraph graph = UtilClass.getGraph();
    klassIds = (List<String>) map.get("ids");
    
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    List<Map<String, Object>> attributesList = new ArrayList<>();
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ASSET);
      if (klassNode != null) {
        attributesList.add(AssetUtils.getAssetEntityMap(klassNode, null));
      }
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", attributesList);
    graph.commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAssetsByIds/*" };
  }
}

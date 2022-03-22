package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTargetsByIds extends AbstractOrientPlugin {
  
  public GetTargetsByIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> klassIds = (List<String>) requestMap.get("ids");
    
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    List<Map<String, Object>> attributesList = new ArrayList<>();
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_TARGET);
      if (klassNode != null) {
        attributesList.add(TargetUtils.getTargetEntityMap(klassNode, null));
      }
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", attributesList);
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTargetsByIds/*" };
  }
}

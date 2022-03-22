package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
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

public class GetKlassesById extends AbstractOrientPlugin {
  
  public GetKlassesById(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    List<String> klassIds = new ArrayList<String>();
    
    klassIds = (List<String>) map.get("ids");
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    
    List<Map<String, Object>> attributesList = new ArrayList<>();
    
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_KLASS);
      attributesList.add(KlassUtils.getKlassEntityMap(klassNode));
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", attributesList);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassesById/*" };
  }
}

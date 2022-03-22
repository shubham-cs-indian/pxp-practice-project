package com.cs.config.strategy.plugin.usecase.klass.supplier;

import com.cs.config.strategy.plugin.usecase.klass.supplier.util.SupplierUtils;
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
public class GetSuppliersByIds extends AbstractOrientPlugin {
  
  public GetSuppliersByIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> klassIds = (List<String>) map.get("ids");
    
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    List<Map<String, Object>> attributesList = new ArrayList<>();
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
      if (klassNode != null) {
        attributesList.add(SupplierUtils.getSupplierEntityMap(klassNode, null));
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
    return new String[] { "POST|GetSuppliersByIds/*" };
  }
}

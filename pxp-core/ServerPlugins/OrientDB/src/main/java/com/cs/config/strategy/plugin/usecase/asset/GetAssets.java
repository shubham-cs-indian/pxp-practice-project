package com.cs.config.strategy.plugin.usecase.asset;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetAssets extends AbstractOrientPlugin {
  
  public GetAssets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    Iterator<Vertex> assetNodesIterator = graph
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ASSET)
        .iterator();
    List<Map<String, Object>> assetsList = new ArrayList<>();
    
    while (assetNodesIterator.hasNext()) {
      Vertex assetNode = assetNodesIterator.next();
      assetsList.add(UtilClass.getPropertiesMapFromNode(assetNode));
    }
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", assetsList);
    graph.commit();
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAssets/*" };
  }
}

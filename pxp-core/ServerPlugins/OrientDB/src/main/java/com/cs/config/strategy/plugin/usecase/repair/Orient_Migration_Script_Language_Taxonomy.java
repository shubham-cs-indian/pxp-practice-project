package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.Iterator;
import java.util.Map;

public class Orient_Migration_Script_Language_Taxonomy extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_Language_Taxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Language_Taxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase()
        .commit();
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType rootVertexType = graph.getVertexType(VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    Integer count = 0;
    Iterator<Vertex> rootNodes = graph.getVerticesOfClass(VertexLabelConstants.ATTRIBUTION_TAXONOMY)
        .iterator();
    while (rootNodes.hasNext()) {
      Vertex rootNode = rootNodes.next();
      rootNode.removeProperty("isAttributionTaxonomy");
      count++;
      if (count % 100 == 0) {
        graph.commit();
      }
    }
    graph.commit();
    UtilClass.getDatabase()
        .commit();
    
    OrientVertexType attributionVertexType = graph
        .getVertexType(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    attributionVertexType.addSuperClass(rootVertexType);   
    return null;
  }
}

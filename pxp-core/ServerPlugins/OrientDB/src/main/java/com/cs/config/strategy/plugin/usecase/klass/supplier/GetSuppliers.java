package com.cs.config.strategy.plugin.usecase.klass.supplier;

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

public class GetSuppliers extends AbstractOrientPlugin {
  
  public GetSuppliers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterator<Vertex> targetNodesIterator = graph
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_SUPPLIER)
        .iterator();
    List<Map<String, Object>> targetsList = new ArrayList<>();
    
    while (targetNodesIterator.hasNext()) {
      Vertex targetNode = targetNodesIterator.next();
      targetsList.add(UtilClass.getPropertiesMapFromNode(targetNode));
    }
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", targetsList);
    graph.commit();
    
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSuppliers/*" };
  }
}

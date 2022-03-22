package com.cs.config.strategy.plugin.usecase.standardsupplier;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetStandardSuppliers extends AbstractOrientPlugin {
  
  public GetStandardSuppliers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Iterator<Vertex> standardUserNodes = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_STANDARD_SUPPLIER)
        .iterator();
    
    List<Map<String, Object>> userList = new ArrayList<>();
    
    while (standardUserNodes.hasNext()) {
      Vertex standardUserNode = standardUserNodes.next();
      Map<String, Object> returnMap = UtilClass.getMapFromNode(standardUserNode);
      userList.add(returnMap);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("list", userList);
    
    // UtilClass.getGraph().commit();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStandardSuppliers/*" };
  }
}

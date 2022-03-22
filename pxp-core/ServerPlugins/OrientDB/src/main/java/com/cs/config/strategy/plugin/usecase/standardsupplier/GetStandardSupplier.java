package com.cs.config.strategy.plugin.usecase.standardsupplier;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetStandardSupplier extends AbstractOrientPlugin {
  
  public GetStandardSupplier(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("id");
    
    Vertex userNode = UtilClass.getVertexById(userId,
        VertexLabelConstants.ENTITY_STANDARD_SUPPLIER);
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.putAll(UtilClass.getMapFromNode(userNode));
    
    // UtilClass.getGraph().commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStandardSupplier/*" };
  }
}

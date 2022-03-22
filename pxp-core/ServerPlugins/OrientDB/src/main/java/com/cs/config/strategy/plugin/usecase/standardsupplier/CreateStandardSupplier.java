package com.cs.config.strategy.plugin.usecase.standardsupplier;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateStandardSupplier extends AbstractOrientPlugin {
  
  public CreateStandardSupplier(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> userMap = (HashMap<String, Object>) requestMap.get("supplier");
    String userId = userMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_STANDARD_SUPPLIER, CommonConstants.CODE_PROPERTY);
    Vertex userNode = UtilClass.createNode(userMap, vertexType, new ArrayList<String>());
    userNode.setProperty(CommonConstants.CODE_PROPERTY, userId);
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.putAll(UtilClass.getMapFromNode(userNode));
    
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateStandardSupplier/*" };
  }
}

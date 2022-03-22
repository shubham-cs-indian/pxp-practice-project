package com.cs.config.strategy.plugin.usecase.standardsupplier;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetOrCreateStandardSupplier extends AbstractOrientPlugin {
  
  public GetOrCreateStandardSupplier(final OServerCommandConfiguration iConfiguration)
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
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Vertex userNode = null;
    
    try {
      userNode = UtilClass.getVertexByIndexedId(userId,
          VertexLabelConstants.ENTITY_STANDARD_SUPPLIER);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
    }
    catch (NotFoundException e) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_STANDARD_SUPPLIER, CommonConstants.CODE_PROPERTY,
          CommonConstants.USER_NAME_PROPERTY);
      userNode = UtilClass.createNode(userMap, vertexType, new ArrayList<>());
      userNode.setProperty(CommonConstants.CODE_PROPERTY, userId);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      UtilClass.getGraph()
          .commit();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateStandardSupplier/*" };
  }
}

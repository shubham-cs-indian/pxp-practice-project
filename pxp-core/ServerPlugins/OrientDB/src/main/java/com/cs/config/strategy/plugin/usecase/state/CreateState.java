package com.cs.config.strategy.plugin.usecase.state;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.HashMap;
import java.util.Map;

public class CreateState extends AbstractOrientPlugin {
  
  public CreateState(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> map = (HashMap<String, Object>) requestMap.get("state");
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.STATE,
        CommonConstants.CODE_PROPERTY);
    
    Vertex stateNode = UtilClass.createNode(map, vertexType);
    
    returnMap = UtilClass.getMapFromNode(stateNode);
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateState/*" };
  }
}

package com.cs.config.strategy.plugin.usecase.attribute;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.exception.ErrorCodes;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class GetAttribute extends AbstractOrientPlugin {
  
  public GetAttribute(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    try {
      Vertex vertex = UtilClass.getVertexByIndexedId((String) map.get("id"),
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      returnMap = (HashMap<String, Object>) AttributeUtils.getAttributeMap(vertex);
    }
    catch (NotFoundException e) {
      System.out.println("Failed for Attribute : " + map.get("id"));
      throw new AttributeNotFoundException("ATTRIBUTE_NOT_FOUND",
          ErrorCodes.ATTRIBUTE_NOT_FOOUND_ON_GET);
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAttribute/*" };
  }
}

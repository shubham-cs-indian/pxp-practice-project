package com.cs.config.strategy.plugin.usecase.klass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetTypesByKlassIds extends AbstractOrientPlugin {
  
  public GetTypesByKlassIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    List<String> klassIds = (List<String>) map.get("klassIds");
    Map<String,String> klassIdVsType = new HashMap<>();
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      klassIdVsType.put(UtilClass.getCode(klassNode), klassNode.getProperty(IKlass.TYPE));
    }
    return klassIdVsType;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTypesByKlassIds/*" };
  }
}

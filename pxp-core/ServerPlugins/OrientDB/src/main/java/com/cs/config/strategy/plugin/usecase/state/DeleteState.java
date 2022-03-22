package com.cs.config.strategy.plugin.usecase.state;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteState extends AbstractOrientPlugin {
  
  public DeleteState(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> dataRuleIds = (List<String>) requestMap.get("ids");
    
    List<String> deletedIds = new ArrayList<>();
    for (String id : dataRuleIds) {
      Vertex userNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.STATE);
      if (userNode != null) {
        userNode.remove();
      }
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("success", deletedIds);
    
    UtilClass.getGraph()
        .commit();
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteStates/*" };
  }
}

package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class DeleteOnboardingUser extends AbstractOrientPlugin {
  
  public DeleteOnboardingUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    List<String> supplierIds = new ArrayList<String>();
    
    supplierIds = (List<String>) map.get("ids");
    
    OrientGraph graph = UtilClass.getGraph();
    List<String> deletedIds = new ArrayList<>();
    for (String id : supplierIds) {
      Vertex supplierNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.ONBOARDING_USER);
      while (supplierNode != null) {
        supplierNode.remove();
      }
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("success", deletedIds);
    
    graph.commit();
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|DeleteOnboardingUser/*" };
  }
}

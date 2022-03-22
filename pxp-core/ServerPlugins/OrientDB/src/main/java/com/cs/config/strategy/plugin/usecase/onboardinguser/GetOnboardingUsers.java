package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOnboardingUsers extends AbstractOrientPlugin {
  
  public GetOnboardingUsers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    List<Map<String, Object>> suppliersList = new ArrayList<>();
    
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ONBOARDING_USER
            + " order by lastname asc, firstname.toLowerCase() asc"))
        .execute();
    for (Vertex supplierNode : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromNode(supplierNode));
      suppliersList.add(map);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put("list", suppliersList);
    graph.commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|GetAllOnboardingUsers/*" };
  }
}

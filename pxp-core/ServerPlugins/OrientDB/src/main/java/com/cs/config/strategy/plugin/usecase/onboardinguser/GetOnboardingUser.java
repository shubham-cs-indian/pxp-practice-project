package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.supplier.SupplierNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class GetOnboardingUser extends AbstractOrientPlugin {
  
  public GetOnboardingUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    String supplierId = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    supplierId = (String) map.get("id");
    
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex supplierNode = UtilClass.getVertexByIndexedId(supplierId,
          VertexLabelConstants.ONBOARDING_USER);
      returnMap.putAll(UtilClass.getMapFromNode(supplierNode));
    }
    catch (NotFoundException e) {
      throw new SupplierNotFoundException();
    }
    graph.commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|GetOnboardingUser/*" };
  }
}

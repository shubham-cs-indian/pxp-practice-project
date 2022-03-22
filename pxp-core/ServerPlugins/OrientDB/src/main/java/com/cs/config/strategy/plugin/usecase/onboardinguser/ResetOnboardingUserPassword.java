package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.supplier.SupplierNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ResetOnboardingUserPassword extends AbstractOrientPlugin {
  
  public ResetOnboardingUserPassword(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> supplierMap;
    supplierMap = (HashMap<String, Object>) map.get("onboardingUser");
    
    OrientGraph graph = UtilClass.getGraph();
    
    String supplierId = (String) supplierMap.get(CommonConstants.ID_PROPERTY);
    
    Vertex supplierNode = UtilClass.getVertexById(supplierId, VertexLabelConstants.ONBOARDING_USER);
    
    if (supplierNode == null) {
      throw new SupplierNotFoundException();
    }
    
    Object password = supplierMap.get("password");
    if (password != null) {
      supplierNode.setProperty("password", password);
    }
    
    HashMap<String, Object> returnMap = UtilClass.getMapFromNode(supplierNode);
    graph.commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|ResetOnboardingUserPassword/*" };
  }
}

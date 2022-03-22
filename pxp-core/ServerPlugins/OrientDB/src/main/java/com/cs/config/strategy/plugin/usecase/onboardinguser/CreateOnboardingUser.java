package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class CreateOnboardingUser extends AbstractOrientPlugin {
  
  public CreateOnboardingUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> supplierMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    supplierMap = (HashMap<String, Object>) map.get("onboardingUser");
    OrientGraph graph = UtilClass.getGraph();
    
    if (ValidationUtils.validateSupplierInfo(supplierMap)) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ONBOARDING_USER, CommonConstants.CODE_PROPERTY);
      Vertex userNode = UtilClass.createNode(supplierMap, vertexType, new ArrayList<>());
      if (((String) supplierMap.get(CommonConstants.ID_PROPERTY)).equals("admin")) {
        userNode.setProperty(CommonConstants.CODE_PROPERTY, "admin");
      }
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      graph.commit();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|CreateOnboardingUser/*" };
  }
}

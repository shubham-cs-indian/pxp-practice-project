package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.supplier.SupplierNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked" })
public class SaveOnboardingUser extends AbstractOrientPlugin {
  
  public SaveOnboardingUser(final OServerCommandConfiguration iConfiguration)
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
      String supplierId = (String) supplierMap.get(CommonConstants.ID_PROPERTY);
      
      Vertex supplierNode = null;
      
      try {
        supplierNode = UtilClass.getVertexByIndexedId(supplierId,
            VertexLabelConstants.ONBOARDING_USER);
        if (supplierMap.get("password") != null && supplierMap.get("password")
            .toString()
            .isEmpty()) {
          supplierMap.put("password", supplierNode.getProperty("password"));
        }
        UtilClass.saveNode(supplierMap, supplierNode, new ArrayList<>());
        /*SaveKlassUtil.manageLifeCycleStatusTag(supplierMap, supplierNode);
        supplierMap.remove(IKlassSaveModel.ADDED_LIFECYCLE_STATUS_TAGS);
        supplierMap.remove(IKlassSaveModel.DELETED_LIFECYCLE_STATUS_TAGS);*/
        SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(supplierMap, supplierNode);
        returnMap.putAll(UtilClass.getMapFromNode(supplierNode));
      }
      catch (NotFoundException e) {
        throw new SupplierNotFoundException();
      }
    }
    
    graph.commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveOnboardingUser/*" };
  }
}

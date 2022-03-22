package com.cs.imprt.config.strategy.plugin.usecase.vertextypes;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.vertextypes.CreateVertexTypes;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentEmbedded;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class CreateOnboardingVertexTypes extends CreateVertexTypes {
  
  public CreateOnboardingVertexTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    ODatabaseDocumentEmbedded database = UtilClass.getDatabase();
    OrientGraph graph = UtilClass.getGraph();
    super.createTypes(database, graph);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ONBOARDING_USER,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.VALUE, CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.PROCESS_EVENT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.STEP, CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateVertexType(VertexLabelConstants.COMPONENT,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_VALUE_MAPPING,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.NEXT_STEP,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.COMPONENT_OF,
        CommonConstants.CODE_PROPERTY);
    UtilClass.getOrCreateEdgeType(RelationshipLabelConstants.HAS_DATARULE,
        CommonConstants.CODE_PROPERTY);
    
    UtilClass.getOrCreateVertexType(VertexLabelConstants.PROPERTY_MAPPING,
        CommonConstants.CODE_PROPERTY);
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("success", "vertex types created");
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|CreateOnboardingVertexTypes/*" };
  }
}

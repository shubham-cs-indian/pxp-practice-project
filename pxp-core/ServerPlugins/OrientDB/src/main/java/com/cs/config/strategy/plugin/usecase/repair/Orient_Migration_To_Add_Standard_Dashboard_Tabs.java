package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class Orient_Migration_To_Add_Standard_Dashboard_Tabs extends AbstractOrientMigration {
  
  public Orient_Migration_To_Add_Standard_Dashboard_Tabs(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_To_Add_Standard_Dashboard_Tabs/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> publish = new HashMap<>();
    Map<String, Object> onboard = new HashMap<>();
    prepareMapForTabs(onboard,publish);
    //Create new dashboard tabs
    OrientVertexType vertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.DASHBOARD_TAB, CommonConstants.CODE_PROPERTY);
    Vertex onboardVertex = null;
    try {
      onboardVertex = UtilClass.getVertexByCode(SystemLevelIds.ONBOARD_TAB,
          VertexLabelConstants.DASHBOARD_TAB);
    }
    catch (Exception e) {
      onboardVertex = UtilClass.createNode(onboard, vertexType, new ArrayList<>());
    }
    
    Vertex publishVertex = null;
    try {
      publishVertex = UtilClass.getVertexByCode(SystemLevelIds.PUBLISH_TAB,
          VertexLabelConstants.DASHBOARD_TAB);
    }
    catch (Exception e) {
      publishVertex = UtilClass.createNode(publish, vertexType, new ArrayList<>());
    }
    
    Vertex dataIntegrationTab = UtilClass.getVertexByCode(SystemLevelIds.DEFAULT_DATA_INTEGRATION_TAB_ID,
        VertexLabelConstants.DASHBOARD_TAB);
    Vertex defaultDashboardTab = UtilClass.getVertexByCode(SystemLevelIds.DEFAULT_DASHBOARD_TAB_ID,
        VertexLabelConstants.DASHBOARD_TAB);
    Iterable<Vertex> nodes = dataIntegrationTab.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_DASHBOARD_TAB);
    for (Vertex node : nodes) {
      if(node.getProperty("endpointType") == null) {
        node.addEdge(RelationshipLabelConstants.HAS_DASHBOARD_TAB, defaultDashboardTab);
        continue;
      }
      
      if (node.getProperty("endpointType").equals(CommonConstants.ONBOARDING_ENDPOINT)) {
        node.addEdge(RelationshipLabelConstants.HAS_DASHBOARD_TAB, onboardVertex);
      }
      else {
        node.addEdge(RelationshipLabelConstants.HAS_DASHBOARD_TAB, publishVertex);
      }
    }
    //Remove dataIntegrationTab node
    UtilClass.getGraph().removeVertex(dataIntegrationTab);
    UtilClass.getGraph().commit();
    return null;
  }

  /**
   * @param onboard
   * @param publish
   */
  private void prepareMapForTabs(Map<String, Object> onboard, Map<String, Object> publish)
  {
    //Prepare node data for onboard tab
    onboard.put(CommonConstants.CODE_PROPERTY, SystemLevelIds.ONBOARD_TAB);
    onboard.put("label__en_US", "Onboard");
    onboard.put("label__de_DE", "Am Bord");
    onboard.put("label__es_ES", "A bordo");
    onboard.put("label__fr_FR", "A bord");
    onboard.put("defaultLabel", "Onboard");  
    //Prepare node data for publish tab
    publish.put(CommonConstants.CODE_PROPERTY, SystemLevelIds.PUBLISH_TAB);
    publish.put("label__en_US", "Publish");
    publish.put("label__de_DE", "Veroffentlichen");
    publish.put("label__es_ES", "Publicar");
    publish.put("label__fr_FR", "publier");
    publish.put("defaultLabel", "Publish");
  }
}

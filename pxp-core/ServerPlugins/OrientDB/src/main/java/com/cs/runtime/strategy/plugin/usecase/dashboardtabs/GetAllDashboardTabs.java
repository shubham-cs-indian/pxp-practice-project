package com.cs.runtime.strategy.plugin.usecase.dashboardtabs;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.dashboardtabs.IDashboardTab;
import com.cs.core.config.interactor.entity.governancerule.IKeyPerformanceIndicator;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.dashboardtabs.IGetAllDashboardTabsRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetAllDashboardTabs extends AbstractOrientPlugin {
  
  public GetAllDashboardTabs(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllDashboardTabs/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IGetAllDashboardTabsRequestModel.CURRENT_USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    String physicalCatalogId = (String) requestMap
        .get(IGetAllDashboardTabsRequestModel.PHYSICAL_CATALOG_ID);
    
    List<Map<String, Object>> returnList = new ArrayList<>();
    Set<String> tabIds = new HashSet<>();
    
    String vertexLabel = VertexLabelConstants.DASHBOARD_TAB;
    if (roleNode.getProperty(IRole.ROLE_TYPE)
        .equals(CommonConstants.SYSTEM_ADMIN_ROLE_TYPE)) {
      fillTabDetails(roleNode, returnList, RelationshipLabelConstants.HAS_SYSTEM, tabIds,
          RelationshipLabelConstants.HAS_ENDPOINT,
          RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION, VertexLabelConstants.SYSTEM,
          physicalCatalogId);
      vertexLabel = VertexLabelConstants.SYSTEM;
    }
    else {
      fillTabDetails(roleNode, returnList, RelationshipLabelConstants.HAS_DASHBOARD_TAB, tabIds,
          RelationshipLabelConstants.HAS_KPI_ROLE, null, VertexLabelConstants.GOVERNANCE_RULE_KPI,
          physicalCatalogId);
      
      fillTabDetails(roleNode, returnList, RelationshipLabelConstants.HAS_DASHBOARD_TAB, tabIds,
          RelationshipLabelConstants.HAS_ENDPOINT,
          RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS, VertexLabelConstants.ENDPOINT,
          physicalCatalogId);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    fillDashboardTabDetails(tabIds, returnList, vertexLabel);
    returnMap.put(IListModel.LIST, returnList);
    return returnMap;
  }
  
  private void fillTabDetails(Vertex roleNode, List<Map<String, Object>> returnList,
      String relationshipLabel, Set<String> tabIds, String roleEntityRelationshipLabel,
      String organizationEntityRelationshipLabel, String entityLabel, String physicalCatalogId)
      throws Exception
  {
    Boolean found = false;
    Iterable<Vertex> entityIterable = roleNode.getVertices(Direction.OUT,
        roleEntityRelationshipLabel);
    found = fillTabDetailsForEntity(returnList, relationshipLabel, tabIds, found, entityIterable);
    fillTabDetailsFromOrganization(roleNode, returnList, tabIds,
        organizationEntityRelationshipLabel, entityLabel, relationshipLabel, found,
        physicalCatalogId);
  }
  
  private void fillTabDetailsFromOrganization(Vertex roleNode, List<Map<String, Object>> returnList,
      Set<String> tabIds, String organizationEntityRelationshipLabel, String entityLabel,
      String relationshipLabel, Boolean found, String physicalCatalogId) throws Exception
  {
    Iterable<Vertex> entityIterable;
    if (!found && organizationEntityRelationshipLabel != null) {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      entityIterable = organizationNode.getVertices(Direction.OUT,
          organizationEntityRelationshipLabel);
      found = fillTabDetailsForEntity(returnList, relationshipLabel, tabIds, found, entityIterable);
    }
    if (!found) {
      entityIterable = getAllVericesByEntityLabel(entityLabel, physicalCatalogId);
      if (entityLabel.equals(VertexLabelConstants.SYSTEM)) {
        fillTabDetailsForEntity(returnList, tabIds, entityIterable);
      }
      else {
        fillTabDetailsForEntity(returnList, relationshipLabel, tabIds, found, entityIterable);
      }
    }
  }
  
  private Iterable<Vertex> getAllVericesByEntityLabel(String vertexLabel, String physicalCatalogId)
  {
    String query = "select from " + vertexLabel;
    if (vertexLabel.equals(VertexLabelConstants.GOVERNANCE_RULE_KPI)) {
      query += " where " + IKeyPerformanceIndicator.PHYSICAL_CATALOG_IDS + " contains '"
          + physicalCatalogId + "' OR " + IKeyPerformanceIndicator.PHYSICAL_CATALOG_IDS
          + ".size() = 0";
    }
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return resultIterable;
  }
  
  private Boolean fillTabDetailsForEntity(List<Map<String, Object>> returnList,
      String relationshipLabel, Set<String> tabIds, Boolean found, Iterable<Vertex> entityIterable)
  {
    for (Vertex entityVertex : entityIterable) {
      Iterable<Vertex> tabIterable = entityVertex.getVertices(Direction.OUT, relationshipLabel);
      for (Vertex tabVertex : tabIterable) {
        String tabId = UtilClass.getCodeNew(tabVertex);
        tabIds.add(tabId);
        found = true;
      }
    }
    return found;
  }
  
  private void fillTabDetailsForEntity(List<Map<String, Object>> returnList, Set<String> tabIds,
      Iterable<Vertex> entityIterable)
  {
    for (Vertex entityVertex : entityIterable) {
      String tabId = UtilClass.getCodeNew(entityVertex);
      tabIds.add(tabId);
    }
  }
  
  private void fillDashboardTabDetails(Set<String> tabIds, List<Map<String, Object>> returnList,
      String vertexLabel) throws Exception
  {
    String query = "select from " + vertexLabel + " where code in" + EntityUtil.quoteIt(tabIds)
        + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + " asc";
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Vertex tabVertex : resultIterable) {
      Map<String, Object> returnMap = new HashMap<>();
      Map<String, Object> tabMap = UtilClass.getMapFromNode(tabVertex);
      returnMap.put(IDashboardTab.ID, tabMap.get(IDashboardTab.ID));
      returnMap.put(IDashboardTab.LABEL, tabMap.get(IDashboardTab.LABEL));
      returnMap.put(IDashboardTab.CODE, tabMap.get(IDashboardTab.CODE));
      returnList.add(returnMap);
    }
  }
}

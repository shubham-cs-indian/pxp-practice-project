package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridKpiRulesModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleModel;
import com.cs.core.config.interactor.model.governancerule.IGetAllKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IKeyPerformanceIndexLimitedInfoModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetAllKeyPerformanceIndex extends AbstractOrientPlugin {
  
  List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IKeyPerformanceIndexLimitedInfoModel.LABEL, CommonConstants.CODE_PROPERTY);
  
  public GetAllKeyPerformanceIndex(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> kpiList = new ArrayList<>();
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    Map<String, Object> referencedDashboardTabs = new HashMap<>();
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    Long count = new Long(0);
    String query = "select from " + VertexLabelConstants.GOVERNANCE_RULE_KPI + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    kpiList = executeQueryAndPrepareResponse(query, referencedDashboardTabs);
    
    String countQuery = "select count(*) from " + VertexLabelConstants.GOVERNANCE_RULE_KPI
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    /*Map<String, Object> referencedRoles = new HashMap<>();
    GovernanceRuleUtil.fillReferencedRACIVSRoles(referencedRoles);*/
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForGridKpiRulesModel.REFERENCED_DASHBOARD_TABS,
        referencedDashboardTabs);
    Map<String, Object> response = new HashMap<>();
    response.put(IGetAllKeyPerformanceIndexModel.KPI_LIST, kpiList);
    response.put(IGetAllKeyPerformanceIndexModel.COUNT, count);
    response.put(IGetAllKeyPerformanceIndexModel.CONFIG_DETAILS, configDetails);
    // response.put(IGetAllKeyPerformanceIndexModel.REFERENCED_ROLES,
    // referencedRoles);
    return response;
  }
  
  /*  private void fillCandidates(List<String> candidates, Vertex roleNode,String kpiId)
  {
    Iterable<Edge> kpiUsersEdges = roleNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_CANDIDATE);
    for (Edge kpiUsersEdge : kpiUsersEdges) {
      if(kpiUsersEdge.getProperty("kpiId").equals(kpiId))
      {
        Vertex userNode = kpiUsersEdge.getVertex(Direction.IN);
        candidates.add(userNode.getProperty(CommonConstants.CODE_PROPERTY));
      }
    }
  }*/
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllKeyPerformanceIndex/*" };
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query,
      Map<String, Object> referencedDashboardTabs) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> kpiVertices = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> kpisToReturn = new ArrayList<>();
    for (Vertex kpiVertex : kpiVertices) {
      // String kpiId = kpiVertex.getProperty(CommonConstants.CODE_PROPERTY);
      // Iterable<Vertex> roleNodes = kpiVertex.getVertices(Direction.OUT,
      // RelationshipLabelConstants.HAS_RACIVS_ROLE);
      Map<String, Object> kpiMap = UtilClass.getMapFromVertex(fieldsToFetch, kpiVertex);
      Iterator<Vertex> vertices = kpiVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DASHBOARD_TAB)
          .iterator();
      if (vertices.hasNext()) {
        Vertex dashboardVertex = vertices.next();
        String dashboardTabId = UtilClass.getCodeNew(dashboardVertex);
        kpiMap.put(IBulkSaveKpiRuleModel.DASHBOARD_TAB_ID, dashboardTabId);
        Map<String, Object> referencedDashboard = new HashMap<>();
        referencedDashboard.put(IIdLabelCodeModel.ID, dashboardTabId);
        referencedDashboard.put(IIdLabelCodeModel.LABEL,
            UtilClass.getValueByLanguage(dashboardVertex, IIdLabelModel.LABEL));
        referencedDashboard.put(IIdLabelCodeModel.CODE,
            dashboardVertex.getProperty(CommonConstants.CODE_PROPERTY));
        referencedDashboardTabs.put(dashboardTabId, referencedDashboard);
      }
      /*      Map<String, Object> roleMap = new HashMap<>();
      for(Vertex roleNode: roleNodes)
      {
        String roleId = roleNode.getProperty(CommonConstants.CODE_PROPERTY);
        List<String> candidates = new ArrayList<>();
        fillCandidates(candidates, roleNode, kpiId);
      
        roleMap.put(roleId, candidates);
      }
      kpiMap.put(IKeyPerformanceIndexLimitedInfoModel.ROLES, roleMap);*/
      kpisToReturn.add(kpiMap);
    }
    return kpisToReturn;
  }
}

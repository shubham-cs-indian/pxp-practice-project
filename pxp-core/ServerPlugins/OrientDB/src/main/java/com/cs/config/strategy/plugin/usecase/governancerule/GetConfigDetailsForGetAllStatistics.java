package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IKeyPerformanceIndicator;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.governancerule.IKPIRuleValidationCriteriaModel;
import com.cs.core.config.interactor.model.governancerule.IReferencedKPIModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.statistics.ICategoryModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.IteratorUtils;

public class GetConfigDetailsForGetAllStatistics extends AbstractOrientPlugin {
  
  public GetConfigDetailsForGetAllStatistics(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetAllStatistics/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IGetAllStatisticsRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    Map<String, Object> mapToReturn = new HashMap<>();
    if (roleNode.getProperty(IRole.ROLE_TYPE)
        .equals(CommonConstants.SYSTEM_ADMIN_ROLE_TYPE)) {
      mapToReturn.put(IGetAllKPIStatisticsModel.TOTAL_COUNT, 0);
      return mapToReturn;
    }
    Long from = Long.valueOf(requestMap.get(IGetAllStatisticsRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetAllStatisticsRequestModel.SIZE)
        .toString());
    
    String dashboardTabId = (String) requestMap.get(IGetAllStatisticsRequestModel.DASHBOARD_TAB_ID);
    Long count = new Long(0);
    String languageConvertedLabel = EntityUtil
        .getLanguageConvertedField(IKeyPerformanceIndicator.LABEL);
    
    // get only that KPI which are valid(for organization, endpoint and physical
    // catalogId)
    String filterQuery = getFilterCondition(requestMap);
    String dashboardTabConditionQuery = " out('" + RelationshipLabelConstants.HAS_DASHBOARD_TAB
        + "').code = '" + dashboardTabId + "'";
    
    String query = null;
    String countQuery = null;
    
    Iterator<Edge> iterator = roleNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_KPI_ROLE)
        .iterator();
    String roleRid = null;
    
    // if KPI is added in role (only this KPI has permission)
    if (iterator.hasNext()) {
      roleRid = roleNode.getId()
          .toString();
    }
    query = getNonEmptyKpiRulesQuery(roleRid) + " AND " + dashboardTabConditionQuery + " AND "
        + filterQuery + " order by " + languageConvertedLabel + " asc" + " skip " + from + " limit "
        + size;
    countQuery = getKpiRuleValidationCountQuery(roleRid) + " AND " + dashboardTabConditionQuery
        + " AND " + filterQuery;
    
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    OrientGraph graph = UtilClass.getGraph();
    
    Iterable<Vertex> kpiVertices = graph.command(new OCommandSQL(query))
        .execute();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Map<String, Object> referencedKlasses = new HashMap<>();
    
    Map<String, Object> referencedKpi = new HashMap<>();
    List<Map<String, Object>> kpiList = new ArrayList<>();
    for (Vertex kpiVertex : kpiVertices) {
      Map<String, Object> kpiMap = new HashMap<>();
      Iterator<Vertex> taxonomyVertices = kpiVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.LINK_TAXONOMY)
          .iterator();
      List<Vertex> taxonomyVerticesList = IteratorUtils.toList(taxonomyVertices);
      List<Map<String, Object>> categories = new ArrayList<>();
      for (Vertex taxonomyVertex : taxonomyVerticesList) {
        Map<String, Object> taxonomyMap = new HashMap<>();
        String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
        referencedTaxonomies.put(taxonomyId,
            UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
                CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY), taxonomyVertex));
        taxonomyMap.put(ICategoryModel.TYPE_ID, taxonomyId);
        taxonomyMap.put(ICategoryModel.TYPE, "taxonomy");
        taxonomyMap.put(ICategoryModel.PARENT_ID, null);
        categories.add(taxonomyMap);
        kpiMap.put(IKPIStatisticsModel.CATEGORY_TYPE, "taxonomy");
      }
      
      List<String> typeIds = new ArrayList<>();
      
      Iterator<Vertex> klassVertices = kpiVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.LINK_KLASS)
          .iterator();
      List<Vertex> klassVerticesList = IteratorUtils.toList(klassVertices);
      if (taxonomyVerticesList == null || taxonomyVerticesList.isEmpty()) {
        for (Vertex klass : klassVerticesList) {
          Map<String, Object> klassMap = new HashMap<>();
          String klassId = UtilClass.getCodeNew(klass);
          referencedKlasses.put(klassId,
              UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
                  CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY), klass));
          klassMap.put(ICategoryModel.TYPE_ID, klassId);
          klassMap.put(ICategoryModel.TYPE, "klass");
          klassMap.put(ICategoryModel.PARENT_ID, null);
          categories.add(klassMap);
          kpiMap.put(IKPIStatisticsModel.CATEGORY_TYPE, "klass");
        }
      }
      else {
        for (Vertex klass : klassVerticesList) {
          typeIds.add(UtilClass.getCodeNew(klass));
        }
      }
      String kpiId = kpiVertex.getProperty(CommonConstants.CODE_PROPERTY);
      kpiMap.put(IKPIStatisticsModel.CATEGORIES, categories);
      kpiMap.put(IKPIStatisticsModel.KPI_ID, kpiId);
      kpiMap.put(IKPIStatisticsModel.CATEGORY_TYPE, "taxonomy");
      kpiMap.put(IKPIStatisticsModel.TYPE_IDS, typeIds);
      kpiMap.put(IKPIStatisticsModel.PATH, new ArrayList<>());
      fillReferencedKPI(kpiVertex, referencedKpi);
      kpiList.add(kpiMap);
    }
    mapToReturn.put(IGetAllKPIStatisticsModel.TOTAL_COUNT, count);
    mapToReturn.put(IGetAllKPIStatisticsModel.DATA_GOVERNANCE, kpiList);
    mapToReturn.put(IGetAllKPIStatisticsModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    mapToReturn.put(IGetAllKPIStatisticsModel.REFERENCED_KLASSES, referencedKlasses);
    mapToReturn.put(IGetAllKPIStatisticsModel.REFERENCED_KPI, referencedKpi);
    
    return mapToReturn;
  }
  
  /**
   * @author Lokesh
   * @param requestMap
   * @return
   */
  private String getFilterCondition(Map<String, Object> requestMap)
  {
    String organizationId = (String) requestMap.get(IGetAllStatisticsRequestModel.ORGANIZATION_ID);
    String endpointId = (String) requestMap.get(IGetAllStatisticsRequestModel.ENDPOINT_ID);
    String physicalCatalogId = (String) requestMap
        .get(IGetAllStatisticsRequestModel.PHYSICAL_CATALOG_ID);
    
    String queryFilter = "";
    queryFilter += " (( OUT('" + RelationshipLabelConstants.KPI_ORGANISATION_LINK
        + "').code contains '" + organizationId + "' ) OR OUT('"
        + RelationshipLabelConstants.KPI_ORGANISATION_LINK + "').size() = 0 )";
    
    queryFilter += " AND ( " + IDataRule.PHYSICAL_CATALOG_IDS + " CONTAINS '" + physicalCatalogId
        + "' OR " + IDataRule.PHYSICAL_CATALOG_IDS + ".size() = 0 ) ";
    
    if (physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      queryFilter += " AND (( OUT('" + RelationshipLabelConstants.KPI_ENDPOINT_LINK + "').code = '"
          + endpointId + "' ) OR OUT('" + RelationshipLabelConstants.KPI_ENDPOINT_LINK
          + "').size() = 0)";
    }
    
    return queryFilter;
  }
  
  private String getFilterByDashboardQuery(String roleRid, String dashboardId,
      String relationshipLabel, Long from, Long size, String filterQuery)
  {
    String selectedDashboardQuery = "Select expand(out('" + RelationshipLabelConstants.HAS_KPI
        + "')) from (select from (Select expand(in('" + RelationshipLabelConstants.HAS_KPI
        + "')) from ( select from " + "(select expand(out('" + relationshipLabel + "')) from "
        + roleRid + ") where out ('" + RelationshipLabelConstants.HAS_DASHBOARD_TAB
        + "').code contains " + EntityUtil.quoteIt(dashboardId) + " AND " + filterQuery + " ) "
        + " ) where out('" + RelationshipLabelConstants.HAS_GOVERNANCE_RULE + "').size() != 0)"
        + " order by " + EntityUtil.getLanguageConvertedField(IKeyPerformanceIndicator.LABEL)
        + " asc " + " skip " + from + " limit " + size;
    
    return selectedDashboardQuery;
  }
  
  private String getFilterByDashboardCountQuery(String roleRid, String dashboardId,
      String relationshipLabel, Long from, Long size, String filterQuery)
  {
    /*String countQuery = "select count(*) from (select expand(out('" + relationshipLabel
        + "')) from " + roleRid + ") WHERE " + filterQuery + " AND " + kpiRuleValidationQuery;
    */
    String countQuery = "select count(*) from ( Select expand(out('"
        + RelationshipLabelConstants.HAS_KPI + "')) from (select from (Select expand(in('"
        + RelationshipLabelConstants.HAS_KPI + "')) from ( select from " + "(select expand(out('"
        + relationshipLabel + "')) from " + roleRid + ") where out ('"
        + RelationshipLabelConstants.HAS_DASHBOARD_TAB + "').code contains "
        + EntityUtil.quoteIt(dashboardId) + " AND " + filterQuery + " ) " + " ) where out('"
        + RelationshipLabelConstants.HAS_GOVERNANCE_RULE + "').size() != 0))";
    
    return countQuery;
  }
  
  private String getNonEmptyKpiRulesQuery(String roleRid)
  {
    String kpisToFetch = getKPIsToFetch(roleRid);
    String kpiRuleValidationQuery = "Select from " + kpisToFetch + " where in('"
        + RelationshipLabelConstants.HAS_KPI + "').out('"
        + RelationshipLabelConstants.HAS_GOVERNANCE_RULE + "').size() != 0 ";
    return kpiRuleValidationQuery;
  }
  
  private String getKPIsToFetch(String roleRid)
  {
    String kpisToFetch = null;
    // if KPI is added in role (only this KPI has permission)
    if (roleRid != null) {
      kpisToFetch = "(select expand(out('" + RelationshipLabelConstants.HAS_KPI_ROLE + "')) from "
          + roleRid + ") ";
    }
    else {
      // KPI is not added in role ( all KPI has permission)
      kpisToFetch = VertexLabelConstants.GOVERNANCE_RULE_KPI;
    }
    return kpisToFetch;
  }
  
  private String getKpiRuleValidationCountQuery(String roleRid)
  {
    String kpIsToFetch = getKPIsToFetch(roleRid);
    String kpiRuleValidationQuery = "Select count(*) from " + kpIsToFetch + " where in('"
        + RelationshipLabelConstants.HAS_KPI + "').out('"
        + RelationshipLabelConstants.HAS_GOVERNANCE_RULE + "').size() != 0 ";
    return kpiRuleValidationQuery;
  }
  
  protected void fillReferencedKPI(Vertex kpiNode, Map<String, Object> referencedKpi)
  {
    Map<String, Object> referencedKpiInfoMap = new HashMap<>();
    referencedKpiInfoMap.put(IReferencedKPIModel.LABEL,
        UtilClass.getValueByLanguage(kpiNode, CommonConstants.LABEL_PROPERTY));
    referencedKpiInfoMap.put(IReferencedKPIModel.CODE,
        kpiNode.getProperty(IKeyPerformanceIndicator.CODE));
    Iterable<Vertex> ruleBlockVertices = kpiNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KPI);
    Map<String, Object> thresholds = new HashMap<>();
    Map<String, Boolean> kPIRuleValidationCriteria = new HashMap<>();
    for (Vertex ruleBlock : ruleBlockVertices) {
      String type = ruleBlock.getProperty(IGovernanceRuleBlock.TYPE);
      Iterable<Vertex> goverenceRuleIntermediate = ruleBlock.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_GOVERNANCE_RULE);
      Boolean isKPIRuleValidationCriteriaValid = goverenceRuleIntermediate.iterator()
          .hasNext();
      Map<String, Object> threshold = ruleBlock.getProperty(IGovernanceRuleBlock.THRESHOLD);
      if (type.equals(CommonConstants.ACCURACY_BLOCK)) {
        thresholds.put("accuracy", threshold);
        kPIRuleValidationCriteria.put(IKPIRuleValidationCriteriaModel.IS_ACCURACY_VALID,
            isKPIRuleValidationCriteriaValid);
        referencedKpiInfoMap.put("accuracyId", ruleBlock.getProperty(CommonConstants.CODE_PROPERTY));
      }
      else if (type.equals(CommonConstants.CONFORMITY_BLOCK)) {
        thresholds.put("conformity", threshold);
        kPIRuleValidationCriteria.put(IKPIRuleValidationCriteriaModel.IS_CONFORMITY_VALID,
            isKPIRuleValidationCriteriaValid);
        referencedKpiInfoMap.put("conformityId", ruleBlock.getProperty(CommonConstants.CODE_PROPERTY));
      }
      else if (type.equals(CommonConstants.COMPLETENESS_BLOCK)) {
        thresholds.put("completeness", threshold);
        kPIRuleValidationCriteria.put(IKPIRuleValidationCriteriaModel.IS_COMPLETENESS_VALID,
            isKPIRuleValidationCriteriaValid);
        referencedKpiInfoMap.put("completenessId", ruleBlock.getProperty(CommonConstants.CODE_PROPERTY));
      }
      else if (type.equals(CommonConstants.UNIQUENESS_BLOCK)) {
        thresholds.put("uniqueness", threshold);
        kPIRuleValidationCriteria.put(IKPIRuleValidationCriteriaModel.IS_UNIQUENESS_VALID,
            isKPIRuleValidationCriteriaValid);
        referencedKpiInfoMap.put("uniquenessId", ruleBlock.getProperty(CommonConstants.CODE_PROPERTY));
      }
    }
    referencedKpiInfoMap.put(IReferencedKPIModel.THRESHOLDS, thresholds);
    referencedKpiInfoMap.put(IReferencedKPIModel.KPI_RULE_VALIDATION_CRITERIA,
        kPIRuleValidationCriteria);
    referencedKpi.put(UtilClass.getCodeNew(kpiNode), referencedKpiInfoMap);
  }
}

package com.cs.config.strategy.plugin.usecase.governancerule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.ListUtils;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.governancerule.IKeyPerformanceIndicator;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKPIDiffModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKPIResponseModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKeyPerformanceIndexModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class SaveKeyPerformanceIndex extends AbstractOrientPlugin {
  
  public SaveKeyPerformanceIndex(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> kpiMap = (Map<String, Object>) map.get("kpi");
    String kpiId = (String) kpiMap.get(ISaveKeyPerformanceIndexModel.ID);
    Vertex kpiNode = UtilClass.getVertexByIndexedId(kpiId,
        VertexLabelConstants.GOVERNANCE_RULE_KPI);
    List<String> availableCatalogList = (List<String>) kpiMap
        .get(CommonConstants.PHYSICAL_CATALOG_IDS);
    Map<String, Object> saveReturnMap = new HashMap<>();
    
    List<String> oldEndpointIds = new ArrayList<String>();
    List<String> oldOrganizationIds = new ArrayList<String>();
    List<String> oldPhysicalCatalogIds = new ArrayList<String>();
    List<String> oldPortalIds = new ArrayList<String>();
    
    fillOldKPIProperties(kpiNode, oldEndpointIds, oldOrganizationIds, oldPhysicalCatalogIds,
        oldPortalIds);
    
    List<String> deletedRules = (List<String>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.DELETED_RULES);
    Boolean isPropertyExistInDeletedRule = false;
    
    if (!deletedRules.isEmpty()) {
      isPropertyExistInDeletedRule = isPropertyExistInDeletedRule(kpiNode, deletedRules);
    }
    
    SaveGovernanceRuleUtils.saveKPI(kpiMap);
    AuditLogUtils.fillAuditLoginfo(saveReturnMap, kpiNode, Entities.KPI, Elements.UNDEFINED);
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> kpiReponseMap = GovernanceRuleUtil.getKPIFromNode(kpiNode);
    
    Map<String, Object> kpiDiff = prepareKPIDiff(kpiReponseMap, oldPhysicalCatalogIds, oldPortalIds,
        oldOrganizationIds, oldEndpointIds, availableCatalogList, kpiNode,
        isPropertyExistInDeletedRule);
    
    saveReturnMap.put(ISaveKPIResponseModel.STRATEGY_RESPONSE, kpiReponseMap);
    saveReturnMap.put(ISaveKPIResponseModel.SAVE_KPI_DIFF, kpiDiff);
    
    return saveReturnMap;
  }
  
  private boolean isPropertyExistInKPI(Vertex kpiNode)
  {
    Boolean isPropertyExist = false;
    Iterable<Vertex> governanceRuleBlockVertices = kpiNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KPI);
    for (Vertex ruleBlockVertex : governanceRuleBlockVertices) {
      if (isPropertyExist) {
        break;
      }
      Iterable<Vertex> governanceRuleVertices = ruleBlockVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_GOVERNANCE_RULE);
      for (Vertex governanceRule : governanceRuleVertices) {
        if (isPropertyExist) {
          break;
        }
        Iterable<Vertex> attributeIntermediates = governanceRule.getVertices(Direction.OUT,
            RelationshipLabelConstants.GOVERNANCE_RULE_ATTRIBUTE);
        for (Vertex attributeIntermediate : attributeIntermediates) {
          isPropertyExist = true;
          break;
        }
        
        Iterable<Vertex> tagIntermediates = governanceRule.getVertices(Direction.OUT,
            RelationshipLabelConstants.GOVERNANCE_RULE_TAG);
        for (Vertex tagIntermediate : tagIntermediates) {
          isPropertyExist = true;
          break;
        }
      }
    }
    return isPropertyExist;
  }
  
  private boolean isPropertyExistInDeletedRule(Vertex kpiNode, List<String> deletedRules)
  {
    Boolean isPropertyExistInDeletedRule = false;
    Iterable<Vertex> governanceRuleBlockVertices = kpiNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KPI);
    for (Vertex ruleBlockVertex : governanceRuleBlockVertices) {
      if (isPropertyExistInDeletedRule) {
        break;
      }
      Iterable<Vertex> governanceRuleVertices = ruleBlockVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_GOVERNANCE_RULE);
      for (Vertex governanceRule : governanceRuleVertices) {
        if (isPropertyExistInDeletedRule || !deletedRules
            .contains(governanceRule.getProperty(CommonConstants.CODE_PROPERTY))) {
          break;
        }
        Iterable<Vertex> attributeIntermediates = governanceRule.getVertices(Direction.OUT,
            RelationshipLabelConstants.GOVERNANCE_RULE_ATTRIBUTE);
        for (Vertex attributeIntermediate : attributeIntermediates) {
          isPropertyExistInDeletedRule = true;
          break;
        }
        
        Iterable<Vertex> tagIntermediates = governanceRule.getVertices(Direction.OUT,
            RelationshipLabelConstants.GOVERNANCE_RULE_TAG);
        for (Vertex tagIntermediate : tagIntermediates) {
          isPropertyExistInDeletedRule = true;
          break;
        }
      }
    }
    return isPropertyExistInDeletedRule;
  }
  
  private Map<String, Object> prepareKPIDiff(Map<String, Object> reponseMap,
      List<String> existingPhysicalCatalogIds, List<String> oldPortalIds,
      List<String> oldOrganizationIds, List<String> oldEndpointIds,
      List<String> availablePhysicalCatalog, Vertex kpiNode, Boolean isPropertyExistInDeletedRule)
  {
    Map<String, Object> kpiMapToReturn = (Map<String, Object>) reponseMap
        .get(IGetKeyPerformanceIndexModel.KEY_PERFORMANCE_INDEX);
    
    List<String> newPhysicalCatalogIds = (List<String>) kpiMapToReturn
        .get(IKeyPerformanceIndicator.PHYSICAL_CATALOG_IDS);
    List<String> newEndpointIds = (List<String>) kpiMapToReturn
        .get(IKeyPerformanceIndicator.ENDPOINTS);
    List<String> newOrganizationIds = (List<String>) kpiMapToReturn
        .get(IKeyPerformanceIndicator.ORGANIZATIONS);
    List<String> physicalCatalogIdsToEvaluate = new ArrayList<>(newPhysicalCatalogIds);
    List<String> endpointIdsToEvaluate = new ArrayList<>(newEndpointIds);
    List<String> partnerIdsToEvaluate = new ArrayList<>(newOrganizationIds);
    
    Boolean isPhysicalCatalogChanged = false;
    if (existingPhysicalCatalogIds.isEmpty()) {
      existingPhysicalCatalogIds.addAll(availablePhysicalCatalog);
    }
    
    if (newPhysicalCatalogIds.isEmpty()) {
      newPhysicalCatalogIds.addAll(availablePhysicalCatalog);
    }
    
    if (newPhysicalCatalogIds.size() == existingPhysicalCatalogIds.size()) {
      List<String> intersection = ListUtils.intersection(existingPhysicalCatalogIds,
          newPhysicalCatalogIds);
      if (intersection.size() == newPhysicalCatalogIds.size()) {
        isPhysicalCatalogChanged = false;
      }
      else {
        isPhysicalCatalogChanged = true;
      }
    }
    else {
      isPhysicalCatalogChanged = true;
    }
    
    /*   // physical Catalog
    if (oldPhysicalCatalogIds.isEmpty() && !newPhysicalCatalogIds.isEmpty()) {
    
       As before catalog is empty(means all is selected) and now
       something is added hence we have to consider all catalogs to
       calculate statistics
       hence doing empty means all are considered
      physicalCatalogIdsToEvaluate.clear();
    }
    else if (!newPhysicalCatalogIds.isEmpty()) {
      oldPhysicalCatalogIds.removeAll(newPhysicalCatalogIds);
      physicalCatalogIdsToEvaluate.addAll(oldPhysicalCatalogIds);
    
    }    */
    
    // Endpoints
    if (oldEndpointIds.isEmpty() && !newEndpointIds.isEmpty()) {
      // as before catalog is empty(means all is selected) and now something is
      // added hence we have
      // to consider all endpoints to calculate statistics
      // hence doing empty means all are considered
      endpointIdsToEvaluate.clear();
    }
    else if (!newEndpointIds.isEmpty()) {
      oldEndpointIds.removeAll(newEndpointIds);
      endpointIdsToEvaluate.addAll(oldEndpointIds);
    }
    
    // Partners
    if (oldOrganizationIds.isEmpty() && !newOrganizationIds.isEmpty()) {
      // as before catalog is empty(means all is selected) and now something is
      // added hence we have
      // to consider all partners to calculate statistics
      // hence doing empty means all are considered
      partnerIdsToEvaluate.clear();
    }
    else if (!newOrganizationIds.isEmpty()) {
      oldOrganizationIds.removeAll(newOrganizationIds);
      partnerIdsToEvaluate.addAll(oldOrganizationIds);
    }
    
    Boolean isPropertyExist = isPropertyExistInKPI(kpiNode);
    
    Map<String, Object> kpiDiffMap = new HashMap<>();
    kpiDiffMap.put(ISaveKPIDiffModel.CATALOG_CHANGED_STATUS, isPhysicalCatalogChanged);
    kpiDiffMap.put(ISaveKPIDiffModel.PHYSICAL_CATALOG_IDS, physicalCatalogIdsToEvaluate);
    kpiDiffMap.put(ISaveKPIDiffModel.PARTNER_IDS, partnerIdsToEvaluate);
    kpiDiffMap.put(ISaveKPIDiffModel.ENDPOINT_IDS, endpointIdsToEvaluate);
    kpiDiffMap.put(ISaveKPIDiffModel.IS_PROPERTY_EXIST, isPropertyExist);
    kpiDiffMap.put(ISaveKPIDiffModel.IS_PROPERTY_EXIST_IN_DELETED_RULE,
        isPropertyExistInDeletedRule);
    
    return kpiDiffMap;
  }
  
  private void fillOldKPIProperties(Vertex kpiNode, List<String> oldEndpointIds,
      List<String> oldOrganizationIds, List<String> oldPhysicalCatalogIds,
      List<String> oldPortalIds)
  {
    List<String> catotlogIds = kpiNode.getProperty(IKeyPerformanceIndicator.PHYSICAL_CATALOG_IDS);
    catotlogIds = (catotlogIds == null) ? new ArrayList<>() : catotlogIds;
    
    oldPhysicalCatalogIds.addAll(catotlogIds);
    
    Iterable<Vertex> endpointVertices = kpiNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KPI_ENDPOINT_LINK);
    oldEndpointIds.addAll(UtilClass.getCodes(endpointVertices));
    
    Iterable<Vertex> orgNodes = kpiNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KPI_ORGANISATION_LINK);
    oldOrganizationIds.addAll(UtilClass.getCodes(orgNodes));
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveKeyPerformanceIndex/*" };
  }
}

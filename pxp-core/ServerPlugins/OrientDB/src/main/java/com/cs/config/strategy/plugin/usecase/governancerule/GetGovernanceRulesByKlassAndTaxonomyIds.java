package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.IteratorUtils;

public class GetGovernanceRulesByKlassAndTaxonomyIds extends AbstractOrientPlugin {
  
  public GetGovernanceRulesByKlassAndTaxonomyIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGovernanceRulesByKlassAndTaxonomyIds/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Set<Map<String, Object>> kpiSet = new HashSet<Map<String, Object>>();
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    String organisationId = (String) requestMap
        .get(IMulticlassificationRequestModel.ORAGANIZATION_ID);
    String endpointId = (String) requestMap.get(IMulticlassificationRequestModel.ENDPOINT_ID);
    String physicalCatalogId = (String) requestMap
        .get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID);
    
    Iterable<Vertex> klassNodes = UtilClass.getVerticesByIds(klassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Iterable<Vertex> taxonomyNodes = UtilClass.getVerticesByIds(taxonomyIds,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    
    Set<Vertex> applicableKPIs = new HashSet<>();
    Set<Vertex> allKPIsLinkedToKlasses = new HashSet<>();
    Set<Vertex> allKPIsLinkedToTaxonomies = new HashSet<>();
    
    for (Vertex klassNode : klassNodes) {
      String query = getKPIQuery(klassNode, organisationId, physicalCatalogId, endpointId,
          RelationshipLabelConstants.LINK_KLASS);
      Iterable<Vertex> kpiNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex kpiNode : kpiNodes) {
        allKPIsLinkedToKlasses.add(kpiNode);
        Iterator<Vertex> taxonomyIterator = kpiNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.LINK_TAXONOMY)
            .iterator();
        List<Vertex> taxonomyList = IteratorUtils.toList(taxonomyIterator);
        if (taxonomyList.size() == 0) {
          applicableKPIs.add(kpiNode);
        }
      }
    }
    
    for (Vertex taxonomyNode : taxonomyNodes) {
      String query = getKPIQuery(taxonomyNode, organisationId, physicalCatalogId, endpointId,
          RelationshipLabelConstants.LINK_TAXONOMY);
      Iterable<Vertex> kpiNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex kpiNode : kpiNodes) {
        allKPIsLinkedToTaxonomies.add(kpiNode);
        Iterator<Vertex> klassIterator = kpiNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.LINK_KLASS)
            .iterator();
        List<Vertex> klassList = IteratorUtils.toList(klassIterator);
        if (klassList.size() == 0) {
          applicableKPIs.add(kpiNode);
        }
      }
    }
    
    allKPIsLinkedToTaxonomies.retainAll(allKPIsLinkedToKlasses);
    applicableKPIs.addAll(allKPIsLinkedToTaxonomies);
    
    for (Vertex kpiVertex : applicableKPIs) {
      fillKPIList(kpiSet, kpiVertex);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put(CommonConstants.LIST_PROPERTY, kpiSet);
    
    return response;
  }
  
  private void fillKPIList(Set<Map<String, Object>> kpiSet, Vertex kpiNode) throws Exception
  {
    Map<String, Object> kpiMap = GovernanceRuleUtil.getKPIFromNode(kpiNode);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_ATTRIBUTES);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_TAGS);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_TASK);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_KLASSES);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_TAXONOMIES);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_ROLES);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_ORANIZATIONS);
    kpiMap.remove(IGetKeyPerformanceIndexModel.REFERENCED_ENDPOINTS);
    kpiSet.add(kpiMap);
  }
  
  protected String getKPIQuery(Vertex klass, String organisationId, String physicalCatalogId,
      String endpointId, String relationshipLabel)
  {
    String query = "SELECT FROM (SELECT EXPAND(IN('" + relationshipLabel + "')) FROM "
        + klass.getId() + " )";
    
    // get kpi if
    // 1. kpi is connected with the provided organization
    // 2. kpi is not link with any organization(i.e it is link with all the
    // organization)
    query = query + " WHERE (( OUT('" + RelationshipLabelConstants.KPI_ORGANISATION_LINK
        + "').code CONTAINS '" + organisationId + "' ) OR OUT('"
        + RelationshipLabelConstants.KPI_ORGANISATION_LINK + "').size() = 0 ) AND ";
    
    // get kpi if
    // 1. physicalCatalogIds contains physicalCatalogId
    // 2. physicalCatalogIdy is empty(i.e applicable for all physicalCatalogs)
    query = query + " ( " + IDataRule.PHYSICAL_CATALOG_IDS + " CONTAINS '" + physicalCatalogId
        + "' OR " + IDataRule.PHYSICAL_CATALOG_IDS + ".size() = 0 ) ";
    
    if (physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      // get kpi if
      // 1. kpi is connected with the provided endpoint
      // 2. kpi is not link with any endpoint(i.e it is link with all the
      // endpoint)
      query = query + " AND (( OUT('" + RelationshipLabelConstants.KPI_ENDPOINT_LINK
          + "').code CONTAINS '" + endpointId + "' ) OR OUT('"
          + RelationshipLabelConstants.KPI_ENDPOINT_LINK + "').size() = 0)";
    }
    
    return query;
  }
}

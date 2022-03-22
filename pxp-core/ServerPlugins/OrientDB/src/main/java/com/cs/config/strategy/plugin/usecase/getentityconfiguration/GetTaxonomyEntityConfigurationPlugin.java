package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTaxonomyEntityConfigurationPlugin extends AbstractOrientPlugin {
  
  public GetTaxonomyEntityConfigurationPlugin(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTaxonomyEntityConfigurationPlugin/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> Ids = (List<String>) requestMap.get(IGetEntityConfigurationRequestModel.IDS);
    Map<String, Object> responseMap = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Map<String, Object>> usageSummary = new ArrayList<Map<String, Object>>();
    for (String id : Ids) {
      Vertex taxonomyNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      Map<String, Object> details = getTaxonomyEntityUsageSummary(id, taxonomyNode);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
      
      /* if parent has no usage then search for their child usages */
      Map<String, Object> summary = (Map<String, Object>) details
          .get(IGetEntityConfigurationResponseModel.USAGE_SUMMARY);
      if (summary.get(IUsageSummary.TOTAL_COUNT)
          .equals(0)) {
        Iterable<Vertex> childNodes = GetEntityConfigurationUtils
            .getChildNodesFromParentNodeId(taxonomyNode.getId()
                .toString());
        for (Vertex childNode : childNodes) {
          String cid = UtilClass.getCodeNew(childNode);
          if (cid != id) {
            Map<String, Object> childDetails = getTaxonomyEntityUsageSummary(
                UtilClass.getCodeNew(childNode), childNode);
            Map<String, Object> childSummary = (Map<String, Object>) childDetails
                .get(IGetEntityConfigurationResponseModel.USAGE_SUMMARY);
            if (!childSummary.get(IUsageSummary.TOTAL_COUNT)
                .equals(0)) {
              /*GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(childDetails,
              referenceData, usageSummary);*/
              responseMap.put(IGetEntityConfigurationResponseModel.HAS_CHILD_DEPENDENCY, true);
              break;
            }
          }
        }
      }
    }
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of taxonomy type entity
   *
   * @author yogesh.mandaokar
   * @param id
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getTaxonomyEntityUsageSummary(String id, Vertex taxonomyNode)
      throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Map<String, Object> usageSummary = GetEntityConfigurationUtils.getUsageSummary(taxonomyNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    String nodeId = taxonomyNode.getId()
        .toString();
    
    /* Rule configuration */
    GetEntityConfigurationUtils.elaborateRules(
        GetEntityConfigurationUtils.getVerticesByGraphAPI(taxonomyNode, Direction.OUT,
            RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK),
        referenceData, usedBy, usageSummary);
    
    /* KPI configuration */
    GetEntityConfigurationUtils.setByGraphAPI(taxonomyNode, Direction.IN,
        RelationshipLabelConstants.LINK_TAXONOMY, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KPI);
    
    /* Golden Record configuration */
    GetEntityConfigurationUtils.setByGraphAPI(taxonomyNode, Direction.IN,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_TAXONOMY_LINK, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.GOLDEN_RECORD);
    
    /* Workflow configuration */
    GetEntityConfigurationUtils.setByGraphAPI(taxonomyNode, Direction.IN,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_PROCESS, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.WORKFLOW);
    
    /* Mapping configuration */
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.MAPPED_TO_ENTITY,
        RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE, EntityConfigurationConstants.MAPPING);
    
    /* Partner configuration */
    GetEntityConfigurationUtils.setByGraphAPI(taxonomyNode, Direction.IN,
        RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.PARTNER);
    
    /* Partner configuration */
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES,
        RelationshipLabelConstants.ORGANIZATION_ROLE_LINK,
        EntityConfigurationConstants.PARTNER_ROLE);
    
    /* Property Collection */
    GetEntityConfigurationUtils.setByGraphAPI(taxonomyNode, Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.PROPERTY_COLLECTION);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}

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

public class GetAttributeEntityConfiguration extends AbstractOrientPlugin {
  
  public GetAttributeEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAttributeEntityConfiguration/*" };
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
      Map<String, Object> details = getAttributeEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of attribute type entity
   *
   * @author yogesh.mandaokar
   * @param id
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getAttributeEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex attributeNode = (Vertex) UtilClass.getVertexByIndexedId(id,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    Map<String, Object> usageSummary = GetEntityConfigurationUtils.getUsageSummary(attributeNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    String nodeId = attributeNode.getId()
        .toString();
    
    /* Property collection */
    GetEntityConfigurationUtils.setByGraphAPI(attributeNode, Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.PROPERTY_COLLECTION);
    
    /* Rule configuration */
    String query = GetEntityConfigurationUtils.getTwoLevelQuery(nodeId, Direction.IN, Direction.IN,
        RelationshipLabelConstants.ATTRIBUTE_DATA_RULE_LINK,
        RelationshipLabelConstants.ATTRIBUTE_DATA_RULE);
    GetEntityConfigurationUtils.elaborateRules(
        GetEntityConfigurationUtils.getVerticesByQuery(query), referenceData, usedBy, usageSummary);
    
    /* KPI Configuration */
    GetEntityConfigurationUtils.setKPIConfiguration(nodeId, referenceData, usedBy, usageSummary,
        RelationshipLabelConstants.GOVERNANCE_RULE_ATTR_LINK,
        RelationshipLabelConstants.GOVERNANCE_RULE_ATTRIBUTE,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, EntityConfigurationConstants.KPI);
    
    /* Golden record configuration (match) */
    GetEntityConfigurationUtils.setByGraphAPI(attributeNode, Direction.IN,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ATTRIBUTE_LINK, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.GOLDEN_RECORD_MATCH);
    
    /* Golden record configuration (merge) */
    GetEntityConfigurationUtils.setGoldenRecordMergeConfiguration(nodeId, referenceData, usedBy,
        usageSummary, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        EntityConfigurationConstants.GOLDEN_RECORD_MERGE,
        RelationshipLabelConstants.EFFECT_TYPE_ENTITY_LINK);
    
    /* concatenated attribute */
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK,
        RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK,
        EntityConfigurationConstants.CONCATINATED_ATTRIBUTE);
    
    /* calculated attribute */
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.OPERATOR_ATTRIBUTE_LINK,
        RelationshipLabelConstants.ATTRIBUTE_OPERATOR_LINK,
        EntityConfigurationConstants.CALCULATED_ATTRIBUTE);
    
    /* relationship data transfer */
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.OUT, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE,
        RelationshipLabelConstants.HAS_PROPERTY,
        EntityConfigurationConstants.RELATIONSHIP_DATA_TRANSFER);
    
    /* contextual data transfer */
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary,
        RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK,
        RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK,
        EntityConfigurationConstants.CONTEXUAL_DATA_TRANSFER);
    
    /* Mapping */
    /* GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.MAPPED_TO_ENTITY,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, EntityConfigurationConstants.MAPPING);*/
    
    /* Workflow */
    GetEntityConfigurationUtils.setByGraphAPI(attributeNode, Direction.IN,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_PROCESS, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.WORKFLOW);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}

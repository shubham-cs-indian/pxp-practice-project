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

public class GetKlassEntityConfiguration extends AbstractOrientPlugin {
  
  public GetKlassEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassEntityConfiguration/*" };
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
      Vertex klassNode = (Vertex) UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Map<String, Object> details = getKlassEntityUsageSummary(id, klassNode);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
      
      /* if parent has no usage then search for their child usages */
      Map<String, Object> summary = (Map<String, Object>) details
          .get(IGetEntityConfigurationResponseModel.USAGE_SUMMARY);
      if (summary.get(IUsageSummary.TOTAL_COUNT)
          .equals(0)) {
        Iterable<Vertex> childNodes = GetEntityConfigurationUtils
            .getChildNodesFromParentNodeId(klassNode.getId()
                .toString());
        for (Vertex childNode : childNodes) {
          String cid = UtilClass.getCodeNew(childNode);
          if (cid != id) {
            Map<String, Object> childDetails = getKlassEntityUsageSummary(
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
   * Description: get usage summary of klass type entity
   *
   * @author Snehal.Kurkute
   * @param id
   * @return
   * @throws Exception
   */
  private Map<String, Object> getKlassEntityUsageSummary(String id, Vertex klassNode)
      throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Map<String, Object> usageSummary = GetEntityConfigurationUtils.getUsageSummary(klassNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    String nodeId = klassNode.getId()
        .toString();
    
    /* Class Rule */
    GetEntityConfigurationUtils
        .elaborateRules(
            GetEntityConfigurationUtils.getVerticesByGraphAPI(klassNode, Direction.OUT,
                RelationshipLabelConstants.HAS_KLASS_RULE_LINK),
            referenceData, usedBy, usageSummary);
    
    /* KPI */
    
    GetEntityConfigurationUtils.setByGraphAPI(klassNode, Direction.IN,
        RelationshipLabelConstants.LINK_KLASS, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KPI);
    
    /* Golden Record */
    
    GetEntityConfigurationUtils.setByGraphAPI(klassNode, Direction.IN,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_KLASS_LINK, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.GOLDEN_RECORD);
    
    /* Partner */
    
    GetEntityConfigurationUtils.setByGraphAPI(klassNode, Direction.IN,
        RelationshipLabelConstants.HAS_AVAILABLE_KLASSES, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.PARTNER);
    
    /* Roles */
    
    GetEntityConfigurationUtils.setByGraphAPI(klassNode, Direction.IN,
        RelationshipLabelConstants.HAS_TARGET_KLASSES, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.ROLE);
    
    /* Mapping */
    
    GetEntityConfigurationUtils.setTwoLevelQuery(nodeId, Direction.IN, Direction.IN, referenceData,
        usedBy, usageSummary, RelationshipLabelConstants.MAPPED_TO_ENTITY,
        RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE, EntityConfigurationConstants.MAPPING);
    
    /* Workflow */
    
    GetEntityConfigurationUtils.setByGraphAPI(klassNode, Direction.IN,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_PROCESS, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.WORKFLOW);
    
    /* Klass(emb) */
    
    Iterable<Vertex> vertices = GetEntityConfigurationUtils.getVerticesByGraphAPI(klassNode,
        Direction.IN, RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    List<Vertex> klass = new ArrayList<Vertex>();
    List<Vertex> taxonomy = new ArrayList<Vertex>();
    for (Vertex vertex : vertices) {
      if (vertex.getProperty("@class")
          .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
        taxonomy.add(vertex);
      }
      else {
        klass.add(vertex);
      }
    }
    
    /* Klass */
    GetEntityConfigurationUtils.elaborateKlass(klass, referenceData, usedBy, usageSummary);
    
    /* Taxonomy */
    GetEntityConfigurationUtils.setConfigurationDetails(taxonomy, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.TAXONOMY);
    
    /* Klass(PID) */
    String query = "select from (select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "').in('"
        + RelationshipLabelConstants.HAS_PROPERTY + "').in('"
        + RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF + "')) from " + nodeId
        + ") where code != '" + id + "'";
    
    GetEntityConfigurationUtils.elaborateKlass(
        GetEntityConfigurationUtils.getVerticesByQuery(query), referenceData, usedBy, usageSummary);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}

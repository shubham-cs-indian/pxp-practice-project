package com.cs.config.strategy.plugin.usecase.governancerule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.governancerule.IDrillDown;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IKPITag;
import com.cs.core.config.interactor.entity.governancerule.IKeyPerformanceIndicator;
import com.cs.core.config.interactor.entity.governancerule.ITargetFilters;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.endpoint.EndpointNotFoundException;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GovernanceRuleUtil {
  
  /* public static Map<String, Object> getGovernanceRuleFromNode(Vertex governanceRule)
  {
  
    Map<String, Object> returnMap = new HashMap<>();
    returnMap = UtilClass.getMapFromNode(governanceRule);
    Iterable<Vertex> ruleGroupVertices = governanceRule.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_GOVERNANCE_RULE_GROUP);
    List<Map<String,Object>> rules = new ArrayList<>();
    for (Vertex governanceRuleGroup : ruleGroupVertices) {
      Map<String,Object> rule = new HashMap<>();
      List<Map<String, Object>> attributes = new ArrayList<>();
      List<Map<String, Object>> relationships = new ArrayList<>();
      List<Map<String, Object>> roles = new ArrayList<>();
      List<Map<String, Object>> tags = new ArrayList<>();
  
      fillAttributeRulesData(governanceRuleGroup, attributes);
      fillRoleRulesData(governanceRuleGroup, roles);
      fillRelationshipRulesData(governanceRuleGroup, relationships);
      fillTagRulesData(governanceRuleGroup, tags);
  
      rule.put(IRules.ID, UtilClass.getCode(governanceRuleGroup));
      rule.put(IRules.LABEL, governanceRuleGroup.getProperty(CommonConstants.LABEL_PROPERTY));
      rule.put(IRules.ATTRIBUTES, attributes);
      rule.put(IRules.RELATIONSHIPS, relationships);
      rule.put(IRules.ROLES, roles);
      rule.put(IRules.TAGS, tags);
      rules.add(rule);
    }
    returnMap.put(IGovernanceRuleModel.RULES, rules);
    Iterator<Vertex> taskVertices = governanceRule.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TASK).iterator();
    if(taskVertices.hasNext()){
      returnMap.put(IGovernanceRuleModel.TASK, UtilClass.getCode(taskVertices.next()));
    }
  
    return returnMap;
  }*/
  
  public static void fillAttributeRulesData(Vertex governanceRule,
      List<Map<String, Object>> attributes, Map<String, Object> returnMap)
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_ATTRIBUTES);
    Iterable<Vertex> attributeIntermediates = governanceRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_ATTRIBUTE);
    for (Vertex attributeIntermediate : attributeIntermediates) {
      Iterable<Vertex> attributeVertices = attributeIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_ATTR_LINK);
      Vertex attribute = attributeVertices.iterator()
          .next();
      Map<String, Object> attributeMap = UtilClass.getMapFromNode(attributeIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = attribute.getProperty(CommonConstants.CODE_PROPERTY);
      attributeMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      referencedAttributes.put(entityId, AttributeUtils.getAttributeMap(attribute));
      Iterable<Vertex> rules = attributeIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        String attributeLinkId = null;
        Iterator<Vertex> linkedAttributeVertices = rule
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_ATTRIBUTE_LINK)
            .iterator();
        
        if (linkedAttributeVertices.hasNext()) {
          Vertex linkedAttribute = linkedAttributeVertices.next();
          attributeLinkId = linkedAttribute.getProperty(CommonConstants.CODE_PROPERTY);
          referencedAttributes.put(attributeLinkId,
              AttributeUtils.getAttributeMap(linkedAttribute));
        }
        
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, attributeLinkId);
        rulesList.add(ruleMap);
      }
      attributeMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      attributes.add(attributeMap);
    }
  }
  
  public static void fillRoleRulesData(Vertex governanceRuleGroup, List<Map<String, Object>> roles,
      Map<String, Object> returnMap)
  {
    Map<String, Object> referencedRoles = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_ROLES);
    Iterable<Vertex> roleIntermediates = governanceRuleGroup.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_ROLE);
    for (Vertex roleIntermediate : roleIntermediates) {
      Iterable<Vertex> roleVertices = roleIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_ROLE_LINK);
      Vertex role = roleVertices.iterator()
          .next();
      Map<String, Object> roleMap = UtilClass.getMapFromNode(roleIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = role.getProperty(CommonConstants.CODE_PROPERTY);
      roleMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      referencedRoles.put(entityId, RoleUtils.getRoleEntityMap(role));
      Iterable<Vertex> rules = roleIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        List<String> values = new ArrayList<>();
        Iterable<Vertex> users = rule.getVertices(Direction.OUT,
            RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK);
        for (Vertex user : users) {
          String userId = user.getProperty(CommonConstants.CODE_PROPERTY);
          values.add(userId);
        }
        ruleMap.put(CommonConstants.VALUES_PROPERTY, values);
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, null);
        rulesList.add(ruleMap);
      }
      roleMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      roles.add(roleMap);
    }
  }
  
  public static void fillRelationshipRulesData(Vertex governanceRuleGroup,
      List<Map<String, Object>> relationships)
  {
    Iterable<Vertex> relationshipIntermediates = governanceRuleGroup.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_RELATIONSHIP);
    for (Vertex relationshipIntermediate : relationshipIntermediates) {
      Iterable<Vertex> relationshipVertices = relationshipIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_REL_LINK);
      Vertex relationship = relationshipVertices.iterator()
          .next();
      Map<String, Object> relationshipMap = UtilClass.getMapFromNode(relationshipIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = relationship.getProperty(CommonConstants.CODE_PROPERTY);
      relationshipMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = relationshipIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        ruleMap.put(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY, null);
        rulesList.add(ruleMap);
      }
      relationshipMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      relationships.add(relationshipMap);
    }
  }
  
  public static void fillTagRulesData(Vertex governanceRuleGroup, List<Map<String, Object>> tags,
      Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> referencedTags = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_TAGS);
    Iterable<Vertex> tagIntermediates = governanceRuleGroup.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_TAG);
    for (Vertex tagIntermediate : tagIntermediates) {
      Iterable<Vertex> tagVertices = tagIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_TAG_LINK);
      Vertex tag = tagVertices.iterator()
          .next();
      Map<String, Object> tagMap = UtilClass.getMapFromNode(tagIntermediate);
      List<Map<String, Object>> rulesList = new ArrayList<>();
      String entityId = tag.getProperty(CommonConstants.CODE_PROPERTY);
      tagMap.put(CommonConstants.ENTITY_ID_PROPERTY, entityId);
      Iterable<Vertex> rules = tagIntermediate.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
      referencedTags.put(entityId, TagUtils.getTagMap(tag, true));
      for (Vertex rule : rules) {
        Map<String, Object> ruleMap = new HashMap<>();
        ruleMap = UtilClass.getMapFromNode(rule);
        List<Map<String, Object>> tagValues = new ArrayList<>();
        ruleMap.put(CommonConstants.TAG_VALUES, tagValues);
        Iterable<Edge> ruleTagValueEdges = rule.getEdges(Direction.OUT,
            RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK);
        String type = (String) ruleMap.get(CommonConstants.TYPE_PROPERTY);
        if (!type.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            && !type.equals(CommonConstants.EMPTY_PROPERTY)) {
          for (Edge ruleTagValueEdge : ruleTagValueEdges) {
            Map<String, Object> tagValue = new HashMap<>();
            int to = ruleTagValueEdge.getProperty(CommonConstants.TO_PROPERTY);
            int from = ruleTagValueEdge.getProperty(CommonConstants.FROM_PROPERTY);
            String tagId = ruleTagValueEdge.getProperty(CommonConstants.ENTITY_ID_PROPERTY);
            tagValue.put(CommonConstants.TO_PROPERTY, to);
            tagValue.put(CommonConstants.FROM_PROPERTY, from);
            tagValue.put(CommonConstants.ID_PROPERTY, tagId);
            tagValues.add(tagValue);
          }
          ruleMap.put(CommonConstants.TAG_VALUES, tagValues);
        }
        rulesList.add(ruleMap);
      }
      tagMap.put(CommonConstants.RULES_PROPERTY, rulesList);
      tags.add(tagMap);
    }
  }
  
  /* public static void fillConfigDetails(Map<String, Object> returnMap, Vertex governanceRuleNode) throws Exception
  {
    List<Map<String,Object>> rules = (List<Map<String, Object>>) returnMap.get(IGovernanceRuleModel.RULES);
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedRoles = new HashMap<>();
    Map<String,Object> referencedRelationships = new HashMap<>();
  
    for (Map<String, Object> rule : rules) {
      List<Map<String, Object>> attributes = (List<Map<String, Object>>) rule.get(IRules.ATTRIBUTES);
      for (Map<String, Object> attribute : attributes) {
        String attributeId = (String) attribute.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedAttributes.put(attributeId, getReferencedAttribute(attributeId));
      }
  
      List<Map<String, Object>> tags = (List<Map<String, Object>>) rule.get(IRules.TAGS);
      for (Map<String, Object> tag : tags) {
        String tagId = (String) tag.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedTags.put(tagId, getReferencedTag(tagId));
      }
  
      List<Map<String, Object>> roles = (List<Map<String, Object>>) rule.get(IRules.ROLES);
      for (Map<String, Object> role : roles) {
        String roleId = (String) role.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedRoles.put(roleId, getReferencedRole(roleId));
      }
  
      List<Map<String, Object>> relationships = (List<Map<String, Object>>) rule.get(IRules.RELATIONSHIPS);
      for (Map<String, Object> relationship : relationships) {
        String relationshipId = (String) relationship.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedRelationships.put(relationshipId, getReferencedRelationship(relationshipId));
      }
    }
  
    String taskId = (String) returnMap.get(IGovernanceRuleModel.TASK);
    Map<String,Object> referencedTask = new HashMap<>();
    if(taskId != null && !taskId.isEmpty()){
      Vertex taskVertex = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
      referencedTask = TasksUtil.getTaskMapFromNode(taskVertex);
    }
  
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForGovernanceRuleModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    configDetails.put(IConfigDetailsForGovernanceRuleModel.REFERENCED_TAGS, referencedTags);
    configDetails.put(IConfigDetailsForGovernanceRuleModel.REFERENCED_ROLES, referencedRoles);
    configDetails.put(IConfigDetailsForGovernanceRuleModel.REFERENCED_RELATIONSHIPS, referencedRelationships);
    configDetails.put(IConfigDetailsForGovernanceRuleModel.REFERENCED_TASK, referencedTask);
  
    returnMap.put(IGovernanceRuleModel.CONFIG_DETAILS, configDetails);
  }*/
  
  private static Map<String, Object> getReferencedAttribute(String attributeId) throws Exception
  {
    Vertex attributeNode = null;
    try {
      attributeNode = UtilClass.getVertexById(attributeId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    }
    catch (NotFoundException e) {
      throw new AttributeNotFoundException();
    }
    Map<String, Object> referencedAttribute = AttributeUtils.getAttributeMap(attributeNode);
    return referencedAttribute;
  }
  
  private static Map<String, Object> getReferencedTag(String tagId) throws Exception
  {
    Vertex tagNode;
    try {
      tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    return TagUtils.getTagMap(tagNode, true);
  }
  
  private static Map<String, Object> getReferencedRole(String roleId) throws Exception
  {
    Vertex roleNode;
    try {
      roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    }
    catch (NotFoundException e) {
      throw new RoleNotFoundException();
    }
    return RoleUtils.getRoleEntityMap(roleNode);
  }
  
  private static Map<String, Object> getReferencedRelationship(String relationshipId)
      throws Exception
  {
    Vertex relationshipNode;
    try {
      relationshipNode = UtilClass.getVertexById(relationshipId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      throw new RelationshipNotFoundException();
    }
    return UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, CommonConstants.LABEL_PROPERTY),
        relationshipNode);
  }
  
  public static void deleteIntermediateVerticesWithInDirection(Vertex node, String edgeLabel)
  {
    Iterable<Vertex> nodesToDelete = node.getVertices(Direction.IN, edgeLabel);
    for (Vertex nodeToDelete : nodesToDelete) {
      deleteIntermediateWithRuleNodes(nodeToDelete);
    }
  }
  
  public static void deleteIntermediateWithRuleNodes(Vertex intermediate)
  {
    Iterable<Vertex> rules = intermediate.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
    UtilClass.deleteVertices(rules);
    intermediate.remove();
  }
  
  public static void deleteRuleNodesLinkedToEntityNode(Vertex entity, String edgeLabel)
  {
    Iterable<Vertex> nodesToDelete = entity.getVertices(Direction.IN, edgeLabel);
    for (Vertex nodeToDelete : nodesToDelete) {
      deleteRuleNode(nodeToDelete);
    }
  }
  
  public static void deleteRuleNode(Vertex ruleNodeToBeDeleted)
  {
    Iterable<Vertex> intermediateVertices = ruleNodeToBeDeleted.getVertices(Direction.IN,
        RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
    for (Vertex intermediateVertex : intermediateVertices) {
      /*String query = "select count(*) from " + VertexLabelConstants.GOVERNANCE_RULE
          + " where in('Governance_Rule_Link').code = '" + UtilClass.getCode(intermediateVertex)
          + "'";
      Long ruleNodeCount = EntityUtil.executeCountQueryToGetTotalCount(query);*/
      Long ruleNodeCount = 0l;
      Iterable<Vertex> ruleNodes = intermediateVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
      for (Vertex ruleNode : ruleNodes) {
        ruleNodeCount++;
      }
      if (ruleNodeCount == 1) {
        intermediateVertex.remove();
      }
    }
    ruleNodeToBeDeleted.remove();
  }
  
  public static void deleteLinkedNodes(Vertex governanceRuleNode)
  {
    Iterable<Vertex> attributeIntermediates = governanceRuleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_ATTRIBUTE);
    for (Vertex attributeIntermediate : attributeIntermediates) {
      deleteIntermediateWithRuleNodes(attributeIntermediate);
    }
    
    Iterable<Vertex> tagIntermediates = governanceRuleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_TAG);
    for (Vertex tagIntermediate : tagIntermediates) {
      deleteIntermediateWithRuleNodes(tagIntermediate);
    }
    
    Iterable<Vertex> roleIntermediates = governanceRuleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_ROLE);
    for (Vertex roleIntermediate : roleIntermediates) {
      deleteIntermediateWithRuleNodes(roleIntermediate);
    }
    
    Iterable<Vertex> relationshipIntermediates = governanceRuleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_RELATIONSHIP);
    for (Vertex relationshipIntermediate : relationshipIntermediates) {
      deleteIntermediateWithRuleNodes(relationshipIntermediate);
    }
  }
  
  public static void deleteKPI(Vertex kpiNode, List<String> deletedRuleIds)
  {
    Iterable<Vertex> governanceRuleBlockNodes = kpiNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KPI);
    for (Vertex governanceRuleBlockNode : governanceRuleBlockNodes) {
      deleteGovernanceRuleBlock(governanceRuleBlockNode);
      deletedRuleIds.add(governanceRuleBlockNode.getProperty(CommonConstants.CODE_PROPERTY));
      governanceRuleBlockNode.remove();
    }
    Iterator<Vertex> drillDownNodes = kpiNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DRILL_DOWN)
        .iterator();
    if (drillDownNodes.hasNext()) {
      Vertex drillDownNode = drillDownNodes.next();
      Iterable<Vertex> drillDownLevelNodes = drillDownNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_DRILL_DOWN_LEVEL);
      for (Vertex drillDownLevelNode : drillDownLevelNodes) {
        drillDownLevelNode.remove();
      }
      drillDownNode.remove();
    }
  }
  
  private static void deleteGovernanceRuleBlock(Vertex governanceRuleBlockNode)
  {
    Iterable<Vertex> governanceRuleNodes = governanceRuleBlockNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_GOVERNANCE_RULE);
    for (Vertex governanceRuleNode : governanceRuleNodes) {
      deleteLinkedNodes(governanceRuleNode);
      
      governanceRuleNode.remove();
    }
  }
  
  public static Map<String, Object> getKPIFromNode(Vertex kpiNode) throws Exception
  {
    Map<String, Object> returnMap = getMapToReturn();
    Map<String, Object> kpiMap = UtilClass.getMapFromNode(kpiNode);
    
    // Dashboard Tab
    String dashboardTabId = TabUtils.getDashboardTabNode(kpiNode);
    kpiMap.put(IEndpointModel.DASHBOARD_TAB_ID, dashboardTabId);
    returnMap.put(IGetKeyPerformanceIndexModel.KEY_PERFORMANCE_INDEX, kpiMap);
    TabUtils.fillReferencedDashboardTabs(Arrays.asList(dashboardTabId),
        (Map<String, Object>) returnMap
            .get(IGetKeyPerformanceIndexModel.REFERENCED_DASHBOARD_TABS));
    
    Map<String, Object> referencedRules = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_RULES);
    Map<String, Object> referencedTasks = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_TASK);
    List<String> ruleBlockIds = new ArrayList<>();
    Iterable<Vertex> governanceRuleBlockVertices = kpiNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KPI);
    for (Vertex ruleBlockVertex : governanceRuleBlockVertices) {
      
      String ruleBlockId = ruleBlockVertex.getProperty(CommonConstants.CODE_PROPERTY);
      ruleBlockIds.add(ruleBlockId);
      Iterable<Vertex> governanceRuleVertices = ruleBlockVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_GOVERNANCE_RULE);
      Map<String, Object> ruleMap = UtilClass.getMapFromNode(ruleBlockVertex);
      List<Map<String, Object>> rules = new ArrayList<>();
      for (Vertex governanceRule : governanceRuleVertices) {
        Map<String, Object> rule = new HashMap<>();
        List<Map<String, Object>> attributes = new ArrayList<>();
        List<Map<String, Object>> relationships = new ArrayList<>();
        List<Map<String, Object>> roles = new ArrayList<>();
        List<Map<String, Object>> tags = new ArrayList<>();
        List<String> klassIds = new ArrayList<>();
        List<String> taxonomyIds = new ArrayList<>();
        
        fillAttributeRulesData(governanceRule, attributes, returnMap);
        fillRoleRulesData(governanceRule, roles, returnMap);
        // fillRelationshipRulesData(governanceRule, relationships);
        fillTagRulesData(governanceRule, tags, returnMap);
        // fillKlassAndTaxonomyIds(governanceRule,klassIds,taxonomyIds,returnMap);
        
        rule.put(IGovernanceRule.ID, UtilClass.getCodeNew(governanceRule));
        rule.put(IGovernanceRule.CODE, UtilClass.getCode(governanceRule));
        rule.put(IGovernanceRule.LABEL,
            UtilClass.getValueByLanguage(governanceRule, CommonConstants.LABEL_PROPERTY));
        rule.put(IGovernanceRule.TYPE, governanceRule.getProperty(CommonConstants.TYPE_PROPERTY));
        rule.put(IGovernanceRule.ATTRIBUTES, attributes);
        rule.put(IGovernanceRule.RELATIONSHIPS, relationships);
        rule.put(IGovernanceRule.ROLES, roles);
        rule.put(IGovernanceRule.TAGS, tags);
        rule.put(IGovernanceRule.KLASS_IDS, klassIds);
        rule.put(IGovernanceRule.TAXONOMY_IDS, taxonomyIds);
        rules.add(rule);
      }
      
      Iterator<Vertex> taskVertices = ruleBlockVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TASK)
          .iterator();
      if (taskVertices.hasNext()) {
        Vertex taskVertex = taskVertices.next();
        String taskId = UtilClass.getCodeNew(taskVertex);
        ruleMap.put(IGovernanceRuleBlock.TASK, taskId);
        referencedTasks.put(taskId, TasksUtil.getTaskMapFromNode(taskVertex));
      }
      ruleMap.put(IGovernanceRuleBlock.RULES, rules);
      referencedRules.put(ruleBlockId, ruleMap);
    }
    fillKPITags(kpiNode, returnMap);
    fillKPIRoles(kpiNode, returnMap);
    List<String> klassIdsLinkedToKpi = new ArrayList<>();
    List<String> taxonomyIdsLinkedToKpi = new ArrayList<>();
    fillKlassAndTaxonomyIds(kpiNode, klassIdsLinkedToKpi, taxonomyIdsLinkedToKpi, returnMap);
    fillDrillDownLevels(kpiNode, returnMap);
    
    fillOrganizations(kpiMap, kpiNode, returnMap);
    fillEndpoints(kpiMap, kpiNode, returnMap);
    
    Map<String, Object> targetFilters = new HashMap<>();
    targetFilters.put(ITargetFilters.KLASS_IDS, klassIdsLinkedToKpi);
    targetFilters.put(ITargetFilters.TAXONOMY_IDS, taxonomyIdsLinkedToKpi);
    kpiMap.put(IKeyPerformanceIndicator.RULES, ruleBlockIds);
    kpiMap.put(IKeyPerformanceIndicator.TARGET_FILTERS, targetFilters);
    return returnMap;
  }
  
  private static void fillEndpoints(Map<String, Object> kpiMap, Vertex kpiNode,
      Map<String, Object> returnMap) throws Exception
  {
    List<String> endpoints = new ArrayList<>();
    Iterable<Vertex> endpointVertices = kpiNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KPI_ENDPOINT_LINK);
    for (Vertex endpointvertex : endpointVertices) {
      endpoints.add(UtilClass.getCodeNew(endpointvertex));
    }
    
    Map<String, Object> referencedEndpoints = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_ENDPOINTS);
    for (String endpoint : endpoints) {
      referencedEndpoints.put(endpoint, getReferencedEndpoint(endpoint));
    }
    kpiMap.put(IDataRule.ENDPOINTS, endpoints);
  }
  
  private static Map<String, Object> getReferencedEndpoint(String endpoint) throws Exception
  {
    final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IEndpoint.LABEL, IEndpoint.CODE);
    Vertex endpointNode;
    Map<String, Object> mapFromNode = new HashMap<>();
    try {
      endpointNode = UtilClass.getVertexByIndexedId(endpoint, VertexLabelConstants.ENDPOINT);
      mapFromNode = UtilClass.getMapFromVertex(fieldsToFetch, endpointNode);
    }
    catch (NotFoundException e) {
      throw new EndpointNotFoundException();
    }
    return mapFromNode;
  }
  
  private static void fillOrganizations(Map<String, Object> kpiMap, Vertex kpiNode,
      Map<String, Object> returnMap) throws Exception
  {
    List<String> organizations = new ArrayList<>();
    Iterable<Vertex> orgNodes = kpiNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KPI_ORGANISATION_LINK);
    for (Vertex orgNode : orgNodes) {
      organizations.add(UtilClass.getCodeNew(orgNode));
    }
    Map<String, Object> referencedOrganizations = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_ORANIZATIONS);
    for (String organization : organizations) {
      referencedOrganizations.put(organization, getReferencedOrganization(organization));
    }
    kpiMap.put(IDataRule.ORGANIZATIONS, organizations);
  }
  
  private static Map<String, Object> getReferencedOrganization(String organization) throws Exception
  {
    final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IOrganization.LABEL, IOrganization.CODE);
    
    Vertex organizationNode;
    Map<String, Object> mapFromNode = new HashMap<>();
    try {
      organizationNode = UtilClass.getVertexByIndexedId(organization,
          VertexLabelConstants.ORGANIZATION);
      mapFromNode = UtilClass.getMapFromVertex(fieldsToFetch, organizationNode);
    }
    catch (NotFoundException e) {
      throw new OrganizationNotFoundException();
    }
    return mapFromNode;
  }
  
  public static void fillKPITags(Vertex kpiVertex, Map<String, Object> returnMap) throws Exception
  {
    List<Map<String, Object>> kpiTags = new ArrayList<>();
    Map<String, Object> referencedTags = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_TAGS);
    Iterable<Edge> kpiTagEdges = kpiVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KPI_TAG);
    for (Edge kpiTagEdge : kpiTagEdges) {
      String tagId = kpiTagEdge.getProperty(IKPITag.TAG_ID);
      Vertex tagVertex = null;
      try {
        tagVertex = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        continue;
      }
      referencedTags.put(tagId, TagUtils.getTagMap(tagVertex, true));
      Map<String, Object> kpiTagMap = new HashMap<>();
      kpiTagMap.put(IKPITag.TAG_ID, tagId);
      List<String> tagValueIds = new ArrayList<>();
      kpiTagMap.put(IKPITag.TAG_VALUES, tagValueIds);
      Vertex kpiTagNode = kpiTagEdge.getVertex(Direction.IN);
      Iterable<Edge> kpiTagValueEdges = kpiTagNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_KPI_TAG_VALUE);
      for (Edge kpiTagValueEdge : kpiTagValueEdges) {
        tagValueIds.add(kpiTagValueEdge.getProperty("tagValueId"));
      }
      kpiTags.add(kpiTagMap);
    }
    Map<String, Object> kpiMap = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.KEY_PERFORMANCE_INDEX);
    kpiMap.put(IKeyPerformanceIndicator.KPI_TAGS, kpiTags);
    returnMap.put(IGetKeyPerformanceIndexModel.REFERENCED_TAGS, referencedTags);
  }
  
  public static void fillKPIRoles(Vertex kpiVertex, Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> kpiMap = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.KEY_PERFORMANCE_INDEX);
    String kpiId = UtilClass.getCodeNew(kpiVertex);
    Map<String, Object> kpiRoles = new HashMap<>();
    Iterable<Vertex> roleVertices = kpiVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_RACIVS_ROLE);
    for (Vertex role : roleVertices) {
      List<String> candidates = new ArrayList<>();
      kpiRoles.put(UtilClass.getCodeNew(role), candidates);
      Iterable<Edge> hasCandidateEdges = role.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_CANDIDATE);
      for (Edge hasCandidateEdge : hasCandidateEdges) {
        if (hasCandidateEdge.getProperty("kpiId")
            .equals(kpiId)) {
          Vertex user = hasCandidateEdge.getVertex(Direction.IN);
          candidates.add(UtilClass.getCodeNew(user));
        }
      }
    }
    /*  kpiMap.put(IKeyPerformanceIndicator.ROLES, kpiRoles);
    Map<String,Object> referencedRoles =  (Map<String, Object>) returnMap.get(IGetKeyPerformanceIndexModel.REFERENCED_ROLES);
    fillReferencedRACIVSRoles(referencedRoles);*/
  }
  
  /* public static void fillReferencedDetails(Map<String,Object> returnMap) throws Exception
  {
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedTags = (Map<String, Object>) returnMap.get(IGetKeyPerformanceIndexModel.REFERENCED_TAGS);
    Map<String, Object> referencedRoles = new HashMap<>();
    Map<String,Object> referencedTask = new HashMap<>();
    Map<String,Object> referencedRules = (Map<String, Object>) returnMap.get(IGetKeyPerformanceIndexModel.REFERENCED_RULES);
    for (Map.Entry<String, Object> entry : referencedRules.entrySet()) {
      Map<String,Object> ruleMap = (Map<String, Object>) entry.getValue();
      List<Map<String,Object>> rules = (List<Map<String, Object>>) ruleMap.get(IGovernanceRuleBlock.RULES);
      for (Map<String, Object> rule : rules) {
        fillConfigDetails(rule, referencedAttributes, referencedTags, referencedRoles);
      }
      String taskId = (String) ruleMap.get(IGovernanceRuleBlock.TASK);
      if(taskId != null && !taskId.isEmpty()){
        Vertex taskVertex = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
        Map<String,Object> taskMap = TasksUtil.getTaskMapFromNode(taskVertex);
        referencedTask.put(taskId, taskMap);
      }
    }
    returnMap.put(IGetKeyPerformanceIndexModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    returnMap.put(IGetKeyPerformanceIndexModel.REFERENCED_TAGS, referencedTags);
    returnMap.put(IGetKeyPerformanceIndexModel.REFERENCED_ROLES, referencedRoles);
    returnMap.put(IGetKeyPerformanceIndexModel.REFERENCED_TASK, referencedTask);
  }*/
  
  /*public static void fillConfigDetails(Map<String,Object> rule, Map<String,Object> referencedAttributes, Map<String,Object> referencedTags,
      Map<String,Object> referencedRoles) throws Exception
  {
      List<Map<String, Object>> attributes = (List<Map<String, Object>>) rule.get(IGovernanceRule.ATTRIBUTES);
      for (Map<String, Object> attribute : attributes) {
        String attributeId = (String) attribute.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedAttributes.put(attributeId, getReferencedAttribute(attributeId));
      }
  
      List<Map<String, Object>> tags = (List<Map<String, Object>>) rule.get(IGovernanceRule.TAGS);
      for (Map<String, Object> tag : tags) {
        String tagId = (String) tag.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedTags.put(tagId, getReferencedTag(tagId));
      }
  
      List<Map<String, Object>> roles = (List<Map<String, Object>>) rule.get(IGovernanceRule.ROLES);
      for (Map<String, Object> role : roles) {
        String roleId = (String) role.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedRoles.put(roleId, getReferencedRole(roleId));
      }
  
      fillReferencedRACIVSRoles(referencedRoles);
      List<Map<String, Object>> relationships = (List<Map<String, Object>>) rule.get(IGovernanceRule.RELATIONSHIPS);
      for (Map<String, Object> relationship : relationships) {
        String relationshipId = (String) relationship.get(IGovernanceRuleIntermediateEntity.ENTITY_ID);
        referencedRelationships.put(relationshipId, getReferencedRelationship(relationshipId));
      }
  }*/
  
  public static void fillKlassAndTaxonomyIds(Vertex entityVertex, List<String> klassIds,
      List<String> taxonomyIds, Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> referencedKlasses = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_KLASSES);
    List<String> fieldstoFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY,
        CommonConstants.ICON_PROPERTY, CommonConstants.TYPE);
    Iterable<Vertex> klassVertices = entityVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.LINK_KLASS);
    for (Vertex klassNode : klassVertices) {
      String klassId = UtilClass.getCodeNew(klassNode);
      klassIds.add(klassId);
      referencedKlasses.put(klassId, UtilClass.getMapFromVertex(fieldstoFetch, klassNode));
    }
    
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) returnMap
        .get(IGetKeyPerformanceIndexModel.REFERENCED_TAXONOMIES);
    Iterable<Vertex> taxonomyVertices = entityVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.LINK_TAXONOMY);
    for (Vertex taxonomyNode : taxonomyVertices) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyNode);
      taxonomyIds.add(taxonomyId);
      Map<String, Object> taxonomyMap = new HashMap<>();
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyNode);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
    }
  }
  
  /* public static void fillReferencedRACIVSRoles(Map<String,Object> referencedRoles) throws Exception
  {
    List<String> RACIVSRoles = new ArrayList<>();
    RACIVSRoles.add(SystemLevelIds.RESPONSIBLE_ROLE);
    RACIVSRoles.add(SystemLevelIds.ACCOUNTABLE_ROLE);
    RACIVSRoles.add(SystemLevelIds.CONSULTED_ROLE);
    RACIVSRoles.add(SystemLevelIds.INFORMED_ROLE);
    RACIVSRoles.add(SystemLevelIds.VERIFY_ROLE);
    RACIVSRoles.add(SystemLevelIds.SIGN_OFF_ROLE);
  
    Iterable<Vertex> roleVertices = UtilClass.getVerticesByIds(RACIVSRoles, VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      referencedRoles.put(roleVertex.getProperty(CommonConstants.CODE_PROPERTY), RoleUtils.getRoleEntityMap(roleVertex));
    }
  }*/
  
  public static Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGetKeyPerformanceIndexModel.KEY_PERFORMANCE_INDEX, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_ATTRIBUTES, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_KLASSES, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_ROLES, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_TAGS, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_TASK, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_TAXONOMIES, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_RULES, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_DASHBOARD_TABS, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_ENDPOINTS, new HashMap<>());
    mapToReturn.put(IGetKeyPerformanceIndexModel.REFERENCED_ORANIZATIONS, new HashMap<>());
    return mapToReturn;
  }
  
  public static String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE, ITaxonomy.CODE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.ID, UtilClass.getCodeNew(taxonomyVertex));
    taxonomyMap.put(IReferencedArticleTaxonomyModel.LABEL,
        UtilClass.getValueByLanguage(taxonomyVertex, CommonConstants.LABEL_PROPERTY));
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CODE,
        taxonomyVertex.getProperty(CommonConstants.CODE_PROPERTY));
    UtilClass.fetchIconInfo(taxonomyVertex, taxonomyMap);
    return MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap,
        taxonomyVertex);
  }
  
  public static void fillDrillDownLevels(Vertex kpiVertex, Map<String, Object> returnMap)
      throws Exception
  {
    Iterator<Vertex> drillDownIterator = kpiVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DRILL_DOWN)
        .iterator();
    if (drillDownIterator.hasNext()) {
      Vertex drillDownNode = drillDownIterator.next();
      List<Map<String, Object>> drillDownLevels = new ArrayList<>();
      Map<String, Object> referencedTags = (Map<String, Object>) returnMap
          .get(IGetKeyPerformanceIndexModel.REFERENCED_TAGS);
      Iterable<Vertex> levelVertices = drillDownNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_DRILL_DOWN_LEVEL);
      for (Vertex levelVertex : levelVertices) {
        Map<String, Object> levelMap = UtilClass.getMapFromNode(levelVertex);
        if (levelMap.get(IDrillDown.TYPE)
            .equals("tag")) {
          Iterable<Edge> hasLevelTagEdges = levelVertex.getEdges(Direction.OUT,
              RelationshipLabelConstants.HAS_LEVEL_TAG);
          List<String> typeIds = new ArrayList<>();
          for (Edge hasLeveledge : hasLevelTagEdges) {
            String tagId = hasLeveledge.getProperty("tagId");
            typeIds.add(tagId);
            Vertex tagNode = hasLeveledge.getVertex(Direction.IN);
            referencedTags.put(tagId, TagUtils.getTagMap(tagNode, true));
          }
          levelMap.put(IDrillDown.TYPE_IDS, typeIds);
        }
        drillDownLevels.add(levelMap);
      }
      Map<String, Object> kpiMap = (Map<String, Object>) returnMap
          .get(IGetKeyPerformanceIndexModel.KEY_PERFORMANCE_INDEX);
      kpiMap.put(IKeyPerformanceIndicator.DRILL_DOWNS, drillDownLevels);
    }
  }
  
  public static void deleteKPITag(Vertex tagNode)
  {
    Iterable<Vertex> kpiTagNodes = tagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KPI_TAG);
    for (Vertex kpiTagNode : kpiTagNodes) {
      kpiTagNode.remove();
    }
  }
}

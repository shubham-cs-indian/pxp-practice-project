package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.governancerule.IDrillDown;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleEntityRule;
import com.cs.core.config.interactor.entity.governancerule.IKeyPerformanceIndicator;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.config.interactor.model.datarule.IDataRuleEntityRule;
import com.cs.core.config.interactor.model.datarule.ISaveDataRuleModel;
import com.cs.core.config.interactor.model.governancerule.IAddedKPIRoleModel;
import com.cs.core.config.interactor.model.governancerule.IModifiedDrillDownModel;
import com.cs.core.config.interactor.model.governancerule.IModifiedGovernanceRuleEntityModel;
import com.cs.core.config.interactor.model.governancerule.IModifiedKPIRoleModel;
import com.cs.core.config.interactor.model.governancerule.IModifiedKPIRuleModel;
import com.cs.core.config.interactor.model.governancerule.IModifiedRuleGroupModel;
import com.cs.core.config.interactor.model.governancerule.IModifiedTargetFiltersModel;
import com.cs.core.config.interactor.model.governancerule.ISaveKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModifiedTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagValuesModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.IteratorUtils;

@SuppressWarnings("unchecked")
public class SaveGovernanceRuleUtils {
  
  /* public static void saveGovernanceRule(Map<String, Object> ruleMap) throws Exception
  {
  
    Vertex governanceRule = UtilClass.getVertexById((String) ruleMap.get(CommonConstants.ID_PROPERTY),VertexLabelConstants.GOVERNANCE_RULES);
    governanceRule.setProperty(IGovernanceRule.LABEL, ruleMap.get(ISaveGovernanceRuleModel.LABEL));
    governanceRule.setProperty(IGovernanceRule.UNIT, ruleMap.get(ISaveGovernanceRuleModel.UNIT));
    governanceRule.setProperty(IGovernanceRule.THRESHOLD, ruleMap.get(ISaveGovernanceRuleModel.THRESHOLD));
  
    String taskId = (String) ruleMap.get(ISaveGovernanceRuleModel.TASK);
    Iterator<Edge> edges = governanceRule.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TASK).iterator();
    if(edges.hasNext()){
      Edge edge = edges.next();
      edge.remove();
    }
    if(taskId != null && !taskId.isEmpty()) {
      Vertex taskVertex = null;
      try{
        taskVertex = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
      }
      catch(NotFoundException e) {
        throw new TaskNotFoundException();
      }
      governanceRule.addEdge(RelationshipLabelConstants.HAS_TASK, taskVertex);
    }
  
  
    handleAddedRuleGroups(ruleMap,governanceRule);
    handleModifiedRuleGroups(ruleMap);
    handleDeletedRuleGroups(ruleMap);
  
    String label = (String) ruleMap.get(CommonConstants.LABEL_PROPERTY);
    Vertex governanceRule = UtilClass.getVertexById((String) ruleMap.get(CommonConstants.ID_PROPERTY),VertexLabelConstants.GOVERNANCE_RULES);
    governanceRule.setProperty(CommonConstants.LABEL_PROPERTY, label);
    handleSavedKlassIdsAndTaxonomyIds(governanceRule,ruleMap);
    handleSavedAttributeGovernanceRules(governanceRule, ruleMap);
    handleSavedRoleGovernanceRules(governanceRule, ruleMap);
    handleSavedRelationshipGovernanceRules(governanceRule, ruleMap);
    handleSavedTagGovernanceRules(governanceRule, ruleMap);
  }*/
  
  /* public static void handleAddedRuleGroups(Map<String,Object> ruleMap, Vertex governanceRule)
  {
    List<String> fieldsToIgnore = Arrays.asList(IRules.ATTRIBUTES,IRules.TAGS,IRules.ROLES,IRules.RELATIONSHIPS);
    List<Map<String,Object>> addedRuleGroups = (List<Map<String, Object>>) ruleMap.get(ISaveGovernanceRuleModel.ADDED_RULE_GROUPS);
    OrientVertexType ruleGroupVertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.GOVERNANCE_RULE_GROUP, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> addedRuleGroup : addedRuleGroups) {
      Vertex ruleGroup = UtilClass.createNode(addedRuleGroup, ruleGroupVertexType,fieldsToIgnore);
      governanceRule.addEdge(RelationshipLabelConstants.HAS_GOVERNANCE_RULE_GROUP, ruleGroup);
    }
  }*/
  
  /*public static void handleModifiedRuleGroups(Map<String,Object> ruleMap) throws Exception
  {
    List<Map<String,Object>> modifiedRuleGroups = (List<Map<String, Object>>) ruleMap.get(ISaveGovernanceRuleModel.MODIFIED_RULE_GROUPS);
    for (Map<String, Object> modifiedRuleGroup : modifiedRuleGroups) {
      String label = (String) modifiedRuleGroup.get(IModifiedRuleGroupModel.LABEL);
      Vertex governanceRuleGroup = UtilClass.getVertexById((String) modifiedRuleGroup.get(IModifiedRuleGroupModel.ID),VertexLabelConstants.GOVERNANCE_RULE_GROUP);
      governanceRuleGroup.setProperty(CommonConstants.LABEL_PROPERTY, label);
      handleSavedAttributeGovernanceRules(governanceRuleGroup, modifiedRuleGroup);
      handleSavedRoleGovernanceRules(governanceRuleGroup, modifiedRuleGroup);
      handleSavedRelationshipGovernanceRules(governanceRuleGroup, modifiedRuleGroup);
      handleSavedTagGovernanceRules(governanceRuleGroup, modifiedRuleGroup);
    }
  }*/
  
  /* public static void handleDeletedRuleGroups(Map<String,Object> ruleMap) throws Exception
  {
    List<String> deletedRuleGroupIds = (List<String>) ruleMap.get(ISaveGovernanceRuleModel.DELETED_RULE_GROUPS);
    for (String deletedruleGroupId : deletedRuleGroupIds) {
      Vertex ruleGroup = null;
      try{
        ruleGroup = UtilClass.getVertexById(deletedruleGroupId, VertexLabelConstants.GOVERNANCE_RULE_GROUP);
      }
      catch(NotFoundException e)
      {
        continue;
      }
      GovernanceRuleUtil.deleteLinkedNodes(ruleGroup);
      ruleGroup.remove();
    }
  }*/
  
  /* public static void handleSavedKlassIdsAndTaxonomyIds(Vertex governanceRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<String> addedKlassIds = (List<String>) ruleMap.get(ISaveGovernanceRuleModel.ADDED_KLASS_IDS);
    List<String> deletedKlassIds = (List<String>) ruleMap.get(ISaveGovernanceRuleModel.DELETED_KLASS_IDS);
    List<String> addedTaxonomyIds = (List<String>) ruleMap.get(ISaveGovernanceRuleModel.ADDED_TAXONOMY_IDS);
    List<String> deletedTaxonomyIds = (List<String>) ruleMap.get(ISaveGovernanceRuleModel.DELETED_TAXONOMY_IDS);
    Iterable<Vertex> klassVertices = UtilClass.getVerticesByIds(addedKlassIds, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Iterator<Vertex> linkedKlassVerticeIterator = governanceRule.getVertices(Direction.OUT, RelationshipLabelConstants.LINK_KLASS).iterator();
    List<Vertex> linkedKlassVertices = IteratorUtils.toList(linkedKlassVerticeIterator);
    for (Vertex klass : klassVertices) {
      if (linkedKlassVertices.contains(klass)) {
        continue;
      }
      governanceRule.addEdge(RelationshipLabelConstants.LINK_KLASS, klass);
    }
    List<Edge> edgesToRemove = new ArrayList<>();
    Iterable<Edge> linkKlassEdges = governanceRule.getEdges(Direction.OUT, RelationshipLabelConstants.LINK_KLASS);
    for (Edge linkKlassEdge : linkKlassEdges) {
     String klassId = UtilClass.getCode(linkKlassEdge.getVertex(Direction.IN));
     if(deletedKlassIds.contains(klassId)){
       edgesToRemove.add(linkKlassEdge);
     }
    }
  
    Iterable<Vertex> taxonomyVertices = UtilClass.getVerticesByIds(addedTaxonomyIds, VertexLabelConstants.KLASS_TAXONOMY);
    Iterator<Vertex> linkedTaxonomyVerticeIterator = governanceRule.getVertices(Direction.OUT, RelationshipLabelConstants.LINK_KLASS).iterator();
    List<Vertex> linkedTaxonomyVertices = IteratorUtils.toList(linkedTaxonomyVerticeIterator);
    for (Vertex taxonomy : taxonomyVertices) {
      if (linkedTaxonomyVertices.contains(taxonomy)) {
        continue;
      }
      governanceRule.addEdge(RelationshipLabelConstants.LINK_TAXONOMY, taxonomy);
    }
    Iterable<Edge> linkTaxonomyEdges = governanceRule.getEdges(Direction.OUT, RelationshipLabelConstants.LINK_TAXONOMY);
    for (Edge linkTaxonomyEdge : linkTaxonomyEdges) {
     String taxonomyId = UtilClass.getCode(linkTaxonomyEdge.getVertex(Direction.IN));
     if(deletedTaxonomyIds.contains(taxonomyId)){
       edgesToRemove.add(linkTaxonomyEdge);
     }
    }
  
    for (Edge edge : edgesToRemove) {
      edge.remove();
    }
  }*/
  public static void handleSavedAttributeGovernanceRules(Vertex governanceRule,
      Map<String, Object> ruleMap) throws Exception
  {
    List<Map<String, Object>> addedAttributeRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedKPIRuleModel.ADDED_ATTRIBUTE_RULES);
    List<String> deletedAttributeRuleIds = (List<String>) ruleMap
        .get(IModifiedKPIRuleModel.DELETED_ATTRIBUTE_RULES);
    List<Map<String, Object>> modifiedAttributeRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedKPIRuleModel.MODIFIED_ATTRIBUTE_RULES);
    handleAddedAttributeRules(governanceRule, addedAttributeRules);
    handleDeletedAttributeRules(governanceRule, deletedAttributeRuleIds);
    handleModifiedAttributeRules(governanceRule, modifiedAttributeRules);
  }
  
  public static void handleAddedAttributeRules(Vertex governanceRule,
      List<Map<String, Object>> addedAttributeRules) throws Exception
  {
    OrientVertexType attributeIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_ATTR_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> attributeRule : addedAttributeRules) {
      String entityId = (String) attributeRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) attributeRule.get(CommonConstants.RULES_PROPERTY);
      attributeRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      attributeRule.remove(CommonConstants.RULES_PROPERTY);
      Vertex attributeRuleVertex = UtilClass.createNode(attributeRule,
          attributeIntermediateVertexType, new ArrayList<>());
      Vertex attribute = UtilClass.getVertexById(entityId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      attributeRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_ATTR_LINK, attribute);
      governanceRule.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_ATTRIBUTE,
          attributeRuleVertex);
      
      for (Map<String, Object> rule : rules) {
        String attributeLinkId = (String) rule.get(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        rule.remove(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType, new ArrayList<>());
        attributeRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
        if (attributeLinkId != null && !attributeLinkId.isEmpty()) {
          Vertex attributeToLink = UtilClass.getVertexById(attributeLinkId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_LINK, attributeToLink);
        }
      }
    }
  }
  
  public static void handleDeletedAttributeRules(Vertex dataRule,
      List<String> deletedAttributeRuleIds) throws Exception
  {
    for (String id : deletedAttributeRuleIds) {
      try {
        Vertex attributeIntermediate = UtilClass.getVertexById(id,
            VertexLabelConstants.GOVERNANCE_RULE_ATTR_INTERMEDIATE);
        deleteIntermediateWithRuleNodes(attributeIntermediate);
      }
      catch (NotFoundException e) {
        // node already deleted. Do nothing
      }
    }
  }
  
  public static void deleteIntermediateWithRuleNodes(Vertex intermediate)
  {
    Iterable<Vertex> rules = intermediate.getVertices(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_LINK);
    UtilClass.deleteVertices(rules);
    intermediate.remove();
  }
  
  public static void handleModifiedAttributeRules(Vertex governanceRuleGroup,
      List<Map<String, Object>> modifiedAttributeRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedAttribute : modifiedAttributeRules) {
      
      String id = (String) modifiedAttribute.get(CommonConstants.ID_PROPERTY);
      Vertex attibuteIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.GOVERNANCE_RULE_ATTR_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedAttribute
          .get(IModifiedGovernanceRuleEntityModel.ADDED_RULES);
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedAttribute
          .get(IModifiedGovernanceRuleEntityModel.MODIFIED_RULES);
      List<String> deletedRules = (List<String>) modifiedAttribute
          .get(IModifiedGovernanceRuleEntityModel.DELETED_RULES);
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        String attributeLinkId = (String) addedRule.get(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        addedRule.remove(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType, new ArrayList<>());
        attibuteIntermediate.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
        if (attributeLinkId != null && !attributeLinkId.isEmpty()) {
          Vertex attributeToLink = UtilClass.getVertexById(attributeLinkId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_LINK, attributeToLink);
        }
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        String to = (String) modifiedRule.get(CommonConstants.TO_PROPERTY);
        String from = (String) modifiedRule.get(CommonConstants.FROM_PROPERTY);
        String type = (String) modifiedRule.get(CommonConstants.TYPE_PROPERTY);
        List<String> values = (List<String>) modifiedRule.get(CommonConstants.VALUES_PROPERTY);
        Boolean shouldCompareWithSystemDate = (Boolean) modifiedRule
            .get(IDataRuleEntityRule.SHOULD_COMPARE_WITH_SYSTEM_DATE);
        String attributeLinkId = (String) modifiedRule
            .get(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        ruleNode.setProperty(CommonConstants.TO_PROPERTY, to);
        ruleNode.setProperty(CommonConstants.FROM_PROPERTY, from);
        ruleNode.setProperty(CommonConstants.TYPE_PROPERTY, type);
        ruleNode.setProperty(CommonConstants.VALUES_PROPERTY, values);
        if (shouldCompareWithSystemDate != null) {
          ruleNode.setProperty(IGovernanceRuleEntityRule.SHOULD_COMPARE_WITH_SYSTEM_DATE,
              shouldCompareWithSystemDate);
        }
        Iterable<Edge> hasAttributeLinkEdge = ruleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_ATTRIBUTE_LINK);
        for (Edge attributeLink : hasAttributeLinkEdge) {
          attributeLink.remove();
        }
        if (attributeLinkId != null && !attributeLinkId.isEmpty()) {
          Vertex attributeToLink = UtilClass.getVertexById(attributeLinkId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_LINK, attributeToLink);
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleSavedRoleGovernanceRules(Vertex governanceRule,
      Map<String, Object> ruleMap) throws Exception
  {
    List<Map<String, Object>> addedRoleGovernanceRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedRuleGroupModel.ADDED_ROLE_RULES);
    List<String> deletedRoleGovernanceRules = (List<String>) ruleMap
        .get(IModifiedRuleGroupModel.DELETED_ROLE_RULES);
    List<Map<String, Object>> modifiedRoleGovernanceRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedRuleGroupModel.MODIFIED_ROLE_RULES);
    handleAddedRoleGovernanceRules(governanceRule, addedRoleGovernanceRules);
    handleDeletedRoleGovernanceRules(governanceRule, deletedRoleGovernanceRules);
    handleModifiedRoleGovernanceRules(governanceRule, modifiedRoleGovernanceRules);
  }
  
  public static void handleAddedRoleGovernanceRules(Vertex governanceRule,
      List<Map<String, Object>> addedRoleRules) throws Exception
  {
    OrientVertexType roleIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_ROLE_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> roleRule : addedRoleRules) {
      String entityId = (String) roleRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) roleRule.get(CommonConstants.RULES_PROPERTY);
      roleRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      roleRule.remove(CommonConstants.RULES_PROPERTY);
      Vertex roleRuleVertex = UtilClass.createNode(roleRule, roleIntermediateVertexType,
          new ArrayList<>());
      Vertex role = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      roleRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_ROLE_LINK, role);
      governanceRule.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_ROLE, roleRuleVertex);
      
      for (Map<String, Object> rule : rules) {
        List<String> values = (List<String>) rule.get(CommonConstants.VALUES_PROPERTY);
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType, new ArrayList<>());
        roleRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
        for (String userId : values) {
          Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          ruleNode.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK, user);
        }
      }
    }
  }
  
  public static void handleDeletedRoleGovernanceRules(Vertex governanceRule,
      List<String> deletedRoleGovernanceRuleIds) throws Exception
  {
    for (String id : deletedRoleGovernanceRuleIds) {
      Vertex roleIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.GOVERNANCE_RULE_ROLE_INTERMEDIATE);
      deleteIntermediateWithRuleNodes(roleIntermediate);
    }
  }
  
  public static void handleModifiedRoleGovernanceRules(Vertex dataRule,
      List<Map<String, Object>> modifiedRoleGovernanceRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedRoleGovernanceRule : modifiedRoleGovernanceRules) {
      String modifiedRoleGovernanceRuleId = (String) modifiedRoleGovernanceRule
          .get(CommonConstants.ID_PROPERTY);
      Vertex roleIntermediate = UtilClass.getVertexById(modifiedRoleGovernanceRuleId,
          VertexLabelConstants.GOVERNANCE_RULE_ROLE_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedRoleGovernanceRule
          .get(IModifiedGovernanceRuleEntityModel.ADDED_RULES);
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedRoleGovernanceRule
          .get(IModifiedGovernanceRuleEntityModel.MODIFIED_RULES);
      List<String> deletedRules = (List<String>) modifiedRoleGovernanceRule
          .get(IModifiedGovernanceRuleEntityModel.DELETED_RULES);
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType, new ArrayList<>());
        roleIntermediate.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
        List<String> values = (List<String>) addedRule.get(CommonConstants.VALUES_PROPERTY);
        for (String userId : values) {
          Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          ruleNode.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK, user);
        }
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        String to = (String) modifiedRule.get(CommonConstants.TO_PROPERTY);
        String from = (String) modifiedRule.get(CommonConstants.FROM_PROPERTY);
        String type = (String) modifiedRule.get(CommonConstants.TYPE_PROPERTY);
        List<String> values = (List<String>) modifiedRule.get(CommonConstants.VALUES_PROPERTY);
        ruleNode.setProperty(CommonConstants.TO_PROPERTY, to);
        ruleNode.setProperty(CommonConstants.FROM_PROPERTY, from);
        ruleNode.setProperty(CommonConstants.TYPE_PROPERTY, type);
        ruleNode.setProperty(CommonConstants.VALUES_PROPERTY, values);
        Iterable<Edge> ruleUserEdges = ruleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK);
        for (Edge ruleUserEdge : ruleUserEdges) {
          ruleUserEdge.remove();
        }
        for (String userId : values) {
          Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          ruleNode.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_USER_LINK, user);
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleSavedRelationshipGovernanceRules(Vertex governanceRuleGroup,
      Map<String, Object> ruleMap) throws Exception
  {
    List<Map<String, Object>> addedRelationshipRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedRuleGroupModel.ADDED_RELATIONSHIP_RULES);
    List<String> deletedRelationshipRuleIds = (List<String>) ruleMap
        .get(IModifiedRuleGroupModel.DELETED_RELATIONSHIP_RULES);
    List<Map<String, Object>> modifiedRelationshipRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedRuleGroupModel.MODIFIED_RELATIONSHIP_RULES);
    handleAddedRelationshipRules(governanceRuleGroup, addedRelationshipRules);
    handleDeletedRelationshipRules(governanceRuleGroup, deletedRelationshipRuleIds);
    handleModifiedRelationshipRules(governanceRuleGroup, modifiedRelationshipRules);
  }
  
  public static void handleAddedRelationshipRules(Vertex governanceRuleGroup,
      List<Map<String, Object>> addedRelationshipRules) throws Exception
  {
    OrientVertexType relationshipIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_REL_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> relationshipRule : addedRelationshipRules) {
      String entityId = (String) relationshipRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) relationshipRule.get(CommonConstants.RULES_PROPERTY);
      relationshipRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      relationshipRule.remove(CommonConstants.RULES_PROPERTY);
      Vertex relationshipRuleVertex = UtilClass.createNode(relationshipRule,
          relationshipIntermediateVertexType, new ArrayList<>());
      Vertex relationship = UtilClass.getVertexById(entityId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      relationshipRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_REL_LINK,
          relationship);
      governanceRuleGroup.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_RELATIONSHIP,
          relationshipRuleVertex);
      for (Map<String, Object> rule : rules) {
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType, new ArrayList<>());
        relationshipRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
      }
    }
  }
  
  public static void handleDeletedRelationshipRules(Vertex dataRule,
      List<String> deletedRelationshipRuleIds) throws Exception
  {
    for (String id : deletedRelationshipRuleIds) {
      Vertex relationshipIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.GOVERNANCE_RULE_REL_INTERMEDIATE);
      deleteIntermediateWithRuleNodes(relationshipIntermediate);
    }
  }
  
  public static void handleModifiedRelationshipRules(Vertex dataRule,
      List<Map<String, Object>> modifiedRelationshipRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedRelationshipRule : modifiedRelationshipRules) {
      String modifiedRelationshipRuleId = (String) modifiedRelationshipRule
          .get(CommonConstants.ID_PROPERTY);
      Vertex relationshipIntermediate = UtilClass.getVertexById(modifiedRelationshipRuleId,
          VertexLabelConstants.GOVERNANCE_RULE_REL_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedRelationshipRule
          .get(IModifiedGovernanceRuleEntityModel.ADDED_RULES);
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedRelationshipRule
          .get(IModifiedGovernanceRuleEntityModel.MODIFIED_RULES);
      List<String> deletedRules = (List<String>) modifiedRelationshipRule
          .get(IModifiedGovernanceRuleEntityModel.DELETED_RULES);
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType, new ArrayList<>());
        relationshipIntermediate.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        String to = (String) modifiedRule.get(CommonConstants.TO_PROPERTY);
        String from = (String) modifiedRule.get(CommonConstants.FROM_PROPERTY);
        String type = (String) modifiedRule.get(CommonConstants.TYPE_PROPERTY);
        List<String> values = (List<String>) modifiedRule.get(CommonConstants.VALUES_PROPERTY);
        ruleNode.setProperty(CommonConstants.TO_PROPERTY, to);
        ruleNode.setProperty(CommonConstants.FROM_PROPERTY, from);
        ruleNode.setProperty(CommonConstants.TYPE_PROPERTY, type);
        ruleNode.setProperty(CommonConstants.VALUES_PROPERTY, values);
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleSavedTagGovernanceRules(Vertex governanceRuleGroup,
      Map<String, Object> ruleMap) throws Exception
  {
    List<Map<String, Object>> addedTagRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedRuleGroupModel.ADDED_TAG_RULES);
    List<String> deletedTagRuleIds = (List<String>) ruleMap
        .get(IModifiedRuleGroupModel.DELETED_TAG_RULES);
    List<Map<String, Object>> modifiedTagRules = (List<Map<String, Object>>) ruleMap
        .get(IModifiedRuleGroupModel.MODIFIED_TAG_RULES);
    handleAddedTagRules(governanceRuleGroup, addedTagRules);
    handleDeletedTagRules(governanceRuleGroup, deletedTagRuleIds);
    handleModifiedTagRules(governanceRuleGroup, modifiedTagRules);
  }
  
  public static void handleAddedTagRules(Vertex governanceRule,
      List<Map<String, Object>> addedTagRules) throws Exception
  {
    OrientVertexType tagIntermediateVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_TAG_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> tagRule : addedTagRules) {
      String entityId = (String) tagRule.get(CommonConstants.ENTITY_ID_PROPERTY);
      List<Map<String, Object>> rules = new ArrayList<>();
      rules = (List<Map<String, Object>>) tagRule.get(CommonConstants.RULES_PROPERTY);
      tagRule.remove(CommonConstants.ENTITY_ID_PROPERTY);
      tagRule.remove(CommonConstants.RULES_PROPERTY);
      Vertex tagRuleVertex = UtilClass.createNode(tagRule, tagIntermediateVertexType,
          new ArrayList<>());
      Vertex tag = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      tagRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_TAG_LINK, tag);
      governanceRule.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_TAG, tagRuleVertex);
      for (Map<String, Object> rule : rules) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) rule
            .get(CommonConstants.TAG_VALUES);
        rule.remove(CommonConstants.TAG_VALUES);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.createNode(rule, ruleNodeVertexType, new ArrayList<>());
        tagRuleVertex.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
          int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
          String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
          Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          ruleNode.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK, tagValueNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(CommonConstants.TO_PROPERTY, to);
          propertyMap.put(CommonConstants.FROM_PROPERTY, from);
          idPropertyMap.put(id, propertyMap);
        }
        setTagValueEdgeProperty(idPropertyMap, ruleNode,
            RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK);
      }
    }
  }
  
  public static void handleDeletedTagRules(Vertex dataRule, List<String> deletedTagRuleIds)
      throws Exception
  {
    for (String id : deletedTagRuleIds) {
      Vertex tagIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.GOVERNANCE_RULE_TAG_INTERMEDIATE);
      deleteIntermediateWithRuleNodes(tagIntermediate);
    }
  }
  
  public static void handleModifiedTagRules(Vertex dataRule,
      List<Map<String, Object>> modifiedTagRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedTagRule : modifiedTagRules) {
      String modifiedTagRuleId = (String) modifiedTagRule.get(CommonConstants.ID_PROPERTY);
      Vertex tagIntermediate = UtilClass.getVertexById(modifiedTagRuleId,
          VertexLabelConstants.GOVERNANCE_RULE_TAG_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedTagRule
          .get(IModifiedGovernanceRuleEntityModel.ADDED_RULES);
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedTagRule
          .get(IModifiedGovernanceRuleEntityModel.MODIFIED_RULES);
      List<String> deletedRules = (List<String>) modifiedTagRule
          .get(IModifiedGovernanceRuleEntityModel.DELETED_RULES);
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) addedRule
            .get(CommonConstants.TAG_VALUES);
        addedRule.remove(CommonConstants.TAG_VALUES);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType, new ArrayList<>());
        tagIntermediate.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_LINK, ruleNode);
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
          int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
          String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
          Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          ruleNode.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK, tagValueNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(CommonConstants.TO_PROPERTY, to);
          propertyMap.put(CommonConstants.FROM_PROPERTY, from);
          idPropertyMap.put(id, propertyMap);
        }
        setTagValueEdgeProperty(idPropertyMap, ruleNode,
            RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK);
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        String oldType = ruleNode.getProperty(CommonConstants.TYPE_PROPERTY);
        String newType = (String) modifiedRule.get(CommonConstants.TYPE_PROPERTY);
        ruleNode.setProperty(CommonConstants.TYPE_PROPERTY, newType);
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) modifiedRule
            .get(CommonConstants.TAG_VALUES);
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
          int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
          String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(CommonConstants.TO_PROPERTY, to);
          propertyMap.put(CommonConstants.FROM_PROPERTY, from);
          idPropertyMap.put(id, propertyMap);
        }
        if (oldType.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            || oldType.equals(CommonConstants.EMPTY_PROPERTY)) {
          if (!newType.equals(CommonConstants.NOT_EMPTY_PROPERTY)
              && !newType.equals(CommonConstants.EMPTY_PROPERTY)) {
            createRuleTagValueEdges(ruleNode, tagValues);
          }
        }
        if (!newType.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            && !newType.equals(CommonConstants.EMPTY_PROPERTY)) {
          setTagValueEdgeProperty(idPropertyMap, ruleNode,
              RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK);
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId,
            VertexLabelConstants.GOVERNANCE_RULE);
        ruleNode.remove();
      }
    }
  }
  
  public static void setTagValueEdgeProperty(Map<String, Map<String, Object>> idPropertyMap,
      Vertex node, String edgeLabel)
  {
    Iterable<Edge> edges = node.getEdges(Direction.OUT, edgeLabel);
    for (Edge edge : edges) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      String tagId = tagValueNode.getProperty(CommonConstants.CODE_PROPERTY);
      Map<String, Object> propertyMap = new HashMap<>();
      propertyMap = (Map<String, Object>) idPropertyMap.get(tagId);
      int to = (int) propertyMap.get(CommonConstants.TO_PROPERTY);
      int from = (int) propertyMap.get(CommonConstants.FROM_PROPERTY);
      edge.setProperty(CommonConstants.TO_PROPERTY, to);
      edge.setProperty(CommonConstants.FROM_PROPERTY, from);
      edge.setProperty(CommonConstants.ENTITY_ID_PROPERTY, tagId);
    }
  }
  
  public static void createRuleTagValueEdges(Vertex ruleNode, List<Map<String, Object>> tagValues)
      throws Exception
  {
    
    Iterable<Edge> edges = ruleNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK);
    if (!(edges.iterator()
        .hasNext())) {
      for (Map<String, Object> tagValue : tagValues) {
        String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
        Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        ruleNode.addEdge(RelationshipLabelConstants.GOVERNANCE_RULE_TAG_VALUE_LINK, tagValueNode);
      }
    }
  }
  
  public static void saveKPI(Map<String, Object> kpiMap) throws Exception
  {
    Vertex kpiVertex = UtilClass.getVertexById(
        (String) kpiMap.get(ISaveKeyPerformanceIndexModel.ID),
        VertexLabelConstants.GOVERNANCE_RULE_KPI);
    kpiVertex.setProperty(EntityUtil.getLanguageConvertedField(IKeyPerformanceIndicator.LABEL),
        kpiMap.get(ISaveKeyPerformanceIndexModel.LABEL));
    kpiVertex.setProperty(IKeyPerformanceIndicator.FREQUENCY,
        kpiMap.get(ISaveKeyPerformanceIndexModel.FREQUENCY));
    kpiVertex.setProperty(IKeyPerformanceIndicator.PHYSICAL_CATALOG_IDS,
        kpiMap.get(IKeyPerformanceIndicator.PHYSICAL_CATALOG_IDS));
    handleAddedDeletedTypes(kpiVertex, kpiMap);
    handleModifiedGovernanceRuleBlocks(kpiMap);
    handleAddedDeletedModifiedRules(kpiMap);
    handleAddedModifiedRoles(kpiVertex, kpiMap);
    handleAddedDeletedModifiedKPITags(kpiVertex, kpiMap);
    handleADMforDrillDown(kpiVertex, kpiMap);
    TabUtils.manageAddedDashboardTabId(kpiMap, kpiVertex);
    TabUtils.manageDeletedDashboardTabId(kpiMap, kpiVertex);
    handleOrganizaitions(kpiMap, kpiVertex);
    handleEndpoints(kpiMap, kpiVertex);
  }
  
  private static void handleEndpoints(Map<String, Object> kpiMap, Vertex kpiVertex) throws Exception
  {
    manageAddedEndpointIds(kpiVertex, kpiMap);
    manageDeletedEndpointIds(kpiVertex, kpiMap);
  }
  
  private static void manageDeletedEndpointIds(Vertex kpiVertex, Map<String, Object> kpiMap)
  {
    List<String> deletedEndpointIds = (List<String>) kpiMap
        .remove(ISaveDataRuleModel.DELETED_ENDPOINTS);
    
    if (deletedEndpointIds == null || deletedEndpointIds.isEmpty()) {
      return;
    }
    List<Edge> edgesToRemove = new ArrayList<>();
    Iterable<Edge> availableOrganizationEdges = kpiVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.KPI_ENDPOINT_LINK);
    for (Edge availableOrganizationEdge : availableOrganizationEdges) {
      Vertex OrganizationVertex = availableOrganizationEdge.getVertex(Direction.IN);
      String organizationId = UtilClass.getCodeNew(OrganizationVertex);
      if (deletedEndpointIds.contains(organizationId)) {
        edgesToRemove.add(availableOrganizationEdge);
        deletedEndpointIds.remove(organizationId);
      }
      if (deletedEndpointIds.isEmpty()) {
        break;
      }
    }
    for (Edge edgeToRemove : edgesToRemove) {
      edgeToRemove.remove();
    }
  }
  
  private static void manageAddedEndpointIds(Vertex kpiVertex, Map<String, Object> kpiMap)
      throws Exception
  {
    List<String> addedEndpoints = (List<String>) kpiMap.remove(ISaveDataRuleModel.ADDED_ENDPOINTS);
    List<String> alreadyExisting = new ArrayList<>();
    Iterable<Vertex> existingEndpoints = kpiVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.KPI_ENDPOINT_LINK);
    if (addedEndpoints != null && !addedEndpoints.isEmpty()) {
      for (Vertex vertex : existingEndpoints) {
        alreadyExisting.add(UtilClass.getCodeNew(vertex));
      }
      for (String addedEndpoint : addedEndpoints) {
        if (alreadyExisting.contains(addedEndpoint)) {
          continue;
        }
        else {
          Vertex endpoint = UtilClass.getVertexById(addedEndpoint, VertexLabelConstants.ENDPOINT);
          kpiVertex.addEdge(RelationshipLabelConstants.KPI_ENDPOINT_LINK, endpoint);
        }
      }
    }
  }
  
  private static void handleOrganizaitions(Map<String, Object> kpiMap, Vertex kpiVertex)
      throws Exception
  {
    manageAddedOrganizations(kpiMap, kpiVertex);
    manageDeletedOrganizations(kpiMap, kpiVertex);
  }
  
  private static void manageDeletedOrganizations(Map<String, Object> kpiMap, Vertex kpiVertex)
  {
    List<String> deletedOrganizationIds = (List<String>) kpiMap
        .remove(ISaveKeyPerformanceIndexModel.DELETED_ORGANIZATION_IDS);
    
    if (deletedOrganizationIds == null || deletedOrganizationIds.isEmpty()) {
      return;
    }
    List<Edge> edgesToRemove = new ArrayList<>();
    Iterable<Edge> availableOrganizationEdges = kpiVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.KPI_ORGANISATION_LINK);
    for (Edge availableOrganizationEdge : availableOrganizationEdges) {
      Vertex OrganizationVertex = availableOrganizationEdge.getVertex(Direction.IN);
      String organizationId = UtilClass.getCodeNew(OrganizationVertex);
      if (deletedOrganizationIds.contains(organizationId)) {
        edgesToRemove.add(availableOrganizationEdge);
        deletedOrganizationIds.remove(organizationId);
      }
      if (deletedOrganizationIds.isEmpty()) {
        break;
      }
    }
    for (Edge edgeToRemove : edgesToRemove) {
      edgeToRemove.remove();
    }
  }
  
  private static void manageAddedOrganizations(Map<String, Object> kpiMap, Vertex kpiVertex)
      throws Exception
  {
    List<String> addedOrganizationIds = (List<String>) kpiMap
        .remove(ISaveKeyPerformanceIndexModel.ADDED_ORGANIZATION_IDS);
    
    if (addedOrganizationIds == null || addedOrganizationIds.isEmpty()) {
      return;
    }
    List<String> alreadyExisting = new ArrayList<>();
    Iterable<Vertex> existingOrg = kpiVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.KPI_ORGANISATION_LINK);
    for (Vertex vertex : existingOrg) {
      alreadyExisting.add(UtilClass.getCodeNew(vertex));
    }
    for (String addedOrganizationId : addedOrganizationIds) {
      if (alreadyExisting.contains(addedOrganizationId)) {
        continue;
      }
      Vertex organizationVertex = UtilClass.getVertexByIndexedId(addedOrganizationId,
          VertexLabelConstants.ORGANIZATION);
      kpiVertex.addEdge(RelationshipLabelConstants.KPI_ORGANISATION_LINK, organizationVertex);
    }
  }
  
  public static void handleAddedDeletedTypes(Vertex entityVertex, Map<String, Object> entityMap)
      throws Exception
  {
    Map<String, Object> modifiedTargetFilters = (Map<String, Object>) entityMap
        .get(ISaveKeyPerformanceIndexModel.MODIFIED_TARGET_FILTERS);
    List<String> addedKlassIds = (List<String>) modifiedTargetFilters
        .get(IModifiedTargetFiltersModel.ADDED_KLASS_IDS);
    List<String> deletedKlassIds = (List<String>) modifiedTargetFilters
        .get(IModifiedTargetFiltersModel.DELETED_KLASS_IDS);
    List<String> addedTaxonomyIds = (List<String>) modifiedTargetFilters
        .get(IModifiedTargetFiltersModel.ADDED_TAXONOMY_IDS);
    List<String> deletedTaxonomyIds = (List<String>) modifiedTargetFilters
        .get(IModifiedTargetFiltersModel.DELETED_TAXONOMY_IDS);
    Iterable<Vertex> klassVertices = UtilClass.getVerticesByIds(addedKlassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Iterator<Vertex> linkedKlassVerticeIterator = entityVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.LINK_KLASS)
        .iterator();
    List<Vertex> linkedKlassVertices = IteratorUtils.toList(linkedKlassVerticeIterator);
    for (Vertex klass : klassVertices) {
      if (linkedKlassVertices.contains(klass)) {
        continue;
      }
      entityVertex.addEdge(RelationshipLabelConstants.LINK_KLASS, klass);
    }
    List<Edge> edgesToRemove = new ArrayList<>();
    Iterable<Edge> linkKlassEdges = entityVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.LINK_KLASS);
    for (Edge linkKlassEdge : linkKlassEdges) {
      String klassId = UtilClass.getCodeNew(linkKlassEdge.getVertex(Direction.IN));
      if (deletedKlassIds.contains(klassId)) {
        edgesToRemove.add(linkKlassEdge);
      }
    }
    
    Iterable<Vertex> taxonomyVertices = UtilClass.getVerticesByIds(addedTaxonomyIds,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    Iterator<Vertex> linkedTaxonomyVerticeIterator = entityVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.LINK_TAXONOMY)
        .iterator();
    List<Vertex> linkedTaxonomyVertices = IteratorUtils.toList(linkedTaxonomyVerticeIterator);
    for (Vertex taxonomy : taxonomyVertices) {
      if (linkedTaxonomyVertices.contains(taxonomy)) {
        continue;
      }
      entityVertex.addEdge(RelationshipLabelConstants.LINK_TAXONOMY, taxonomy);
    }
    Iterable<Edge> linkTaxonomyEdges = entityVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.LINK_TAXONOMY);
    for (Edge linkTaxonomyEdge : linkTaxonomyEdges) {
      String taxonomyId = UtilClass.getCodeNew(linkTaxonomyEdge.getVertex(Direction.IN));
      if (deletedTaxonomyIds.contains(taxonomyId)) {
        edgesToRemove.add(linkTaxonomyEdge);
      }
    }
    
    for (Edge edge : edgesToRemove) {
      edge.remove();
    }
  }
  
  public static void handleModifiedGovernanceRuleBlocks(Map<String, Object> kpiMap) throws Exception
  {
    List<Map<String, Object>> modifiedGovernanceRuleBlocks = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.MODIFIED_GOVERNANCE_RULE_BLOCKS);
    for (Map<String, Object> modifiedGovernanceRuleBlock : modifiedGovernanceRuleBlocks) {
      String governanceRuleBlockId = (String) modifiedGovernanceRuleBlock
          .get(IGovernanceRuleBlock.ID);
      Vertex ruleBlockVertex = UtilClass.getVertexById(governanceRuleBlockId,
          VertexLabelConstants.GOVERNANCE_RULE_BLOCK);
      // ruleBlockVertex.setProperty(IGovernanceRuleBlock.LABEL,
      // modifiedGovernanceRuleBlock.get(IGovernanceRuleBlock.LABEL));
      // ruleBlockVertex.setProperty(IGovernanceRuleBlock.TYPE,
      // modifiedGovernanceRuleBlock.get(IGovernanceRuleBlock.TYPE));
      ruleBlockVertex.setProperty(IGovernanceRuleBlock.UNIT,
          modifiedGovernanceRuleBlock.get(IGovernanceRuleBlock.UNIT));
      ruleBlockVertex.setProperty(IGovernanceRuleBlock.THRESHOLD,
          modifiedGovernanceRuleBlock.get(IGovernanceRuleBlock.THRESHOLD));
      
      String taskId = (String) modifiedGovernanceRuleBlock.get(IGovernanceRuleBlock.TASK);
      Iterator<Edge> edges = ruleBlockVertex
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TASK)
          .iterator();
      if (edges.hasNext()) {
        Edge edge = edges.next();
        edge.remove();
      }
      if (taskId != null && !taskId.isEmpty()) {
        Vertex taskVertex = null;
        try {
          taskVertex = UtilClass.getVertexById(taskId, VertexLabelConstants.GOVERNANCE_RULE_TASK);
        }
        catch (NotFoundException e) {
          throw new TaskNotFoundException();
        }
        ruleBlockVertex.addEdge(RelationshipLabelConstants.HAS_TASK, taskVertex);
      }
    }
  }
  
  public static Vertex getGovernanceRuleBlockByType(String kpiId, String type) throws Exception
  {
    Vertex ruleBlockToReturn = null;
    Vertex kpiVertex = UtilClass.getVertexById(kpiId, VertexLabelConstants.GOVERNANCE_RULE_KPI);
    Iterable<Vertex> ruleBlockVertices = kpiVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KPI);
    for (Vertex ruleBlock : ruleBlockVertices) {
      if (ruleBlock.getProperty(IGovernanceRuleBlock.TYPE)
          .equals(type)) {
        ruleBlockToReturn = ruleBlock;
        break;
      }
    }
    return ruleBlockToReturn;
  }
  
  public static void handleAddedDeletedModifiedRules(Map<String, Object> kpiMap) throws Exception
  {
    String kpiId = (String) kpiMap.get(ISaveKeyPerformanceIndexModel.ID);
    List<Map<String, Object>> addedRules = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.ADDED_RULES);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULES, CommonConstants.CODE_PROPERTY);
    List<String> fieldsToIgnore = Arrays.asList(IGovernanceRule.ATTRIBUTES, IGovernanceRule.TAGS,
        IGovernanceRule.RELATIONSHIPS, IGovernanceRule.ROLES, IGovernanceRule.KLASS_IDS,
        IGovernanceRule.TAXONOMY_IDS);
    for (Map<String, Object> addedRule : addedRules) {
      
      String ruleType = (String) addedRule.get(IGovernanceRule.TYPE);
      String blockType = ruleType + "Block";
      Vertex governanceRuleNode = UtilClass.createNode(addedRule, vertexType, fieldsToIgnore);
      Vertex governanceRuleBlock = getGovernanceRuleBlockByType(kpiId, blockType);
      governanceRuleBlock.addEdge(RelationshipLabelConstants.HAS_GOVERNANCE_RULE,
          governanceRuleNode);
      List<Map<String, Object>> attributeRules = (List<Map<String, Object>>) addedRule
          .get(IGovernanceRule.ATTRIBUTES);
      handleAddedAttributeRules(governanceRuleNode, attributeRules);
      List<Map<String, Object>> tagRules = (List<Map<String, Object>>) addedRule
          .get(IGovernanceRule.TAGS);
      handleAddedTagRules(governanceRuleNode, tagRules);
      List<Map<String, Object>> roleRules = (List<Map<String, Object>>) addedRule
          .get(IGovernanceRule.ROLES);
      handleAddedRoleGovernanceRules(governanceRuleNode, roleRules);
    }
    
    List<String> deletedRules = (List<String>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.DELETED_RULES);
    for (String id : deletedRules) {
      Vertex governanceRuleNode = UtilClass.getVertexById(id,
          VertexLabelConstants.GOVERNANCE_RULES);
      GovernanceRuleUtil.deleteLinkedNodes(governanceRuleNode);
      governanceRuleNode.remove();
    }
    
    List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.MODIFIED_RULES);
    for (Map<String, Object> modifiedRule : modifiedRules) {
      String governanceRuleId = (String) modifiedRule.get(IModifiedKPIRuleModel.ID);
      Vertex governanceRuleNode = UtilClass.getVertexById(governanceRuleId,
          VertexLabelConstants.GOVERNANCE_RULES);
      governanceRuleNode.setProperty(EntityUtil.getLanguageConvertedField(IGovernanceRule.LABEL),
          modifiedRule.get(IModifiedKPIRuleModel.LABEL));
      governanceRuleNode.setProperty(IGovernanceRule.TYPE,
          modifiedRule.get(IModifiedKPIRuleModel.TYPE));
      handleSavedAttributeGovernanceRules(governanceRuleNode, modifiedRule);
      handleSavedTagGovernanceRules(governanceRuleNode, modifiedRule);
      handleSavedRoleGovernanceRules(governanceRuleNode, modifiedRule);
      // handleAddedDeletedTypes(governanceRuleNode,modifiedRule);
    }
  }
  
  public static void handleAddedModifiedRoles(Vertex kpiVertex, Map<String, Object> kpiMap)
      throws Exception
  {
    String kpiId = UtilClass.getCodeNew(kpiVertex);
    List<Map<String, Object>> addedRoles = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.ADDED_ROLES);
    for (Map<String, Object> addedRole : addedRoles) {
      String roleId = (String) addedRole.get(IAddedKPIRoleModel.ROLE_ID);
      Vertex roleVertex = null;
      try {
        roleVertex = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      }
      catch (Exception e) {
        throw new RoleNotFoundException();
      }
      kpiVertex.addEdge(RelationshipLabelConstants.HAS_RACIVS_ROLE, roleVertex);
      List<String> candidates = (List<String>) addedRole.get(IAddedKPIRoleModel.CANDIDATES);
      for (String candidate : candidates) {
        Vertex userVertex = UtilClass.getVertexById(candidate,
            VertexLabelConstants.ENTITY_TYPE_USER);
        Edge hasCandidateEdge = roleVertex.addEdge(RelationshipLabelConstants.HAS_CANDIDATE,
            userVertex);
        hasCandidateEdge.setProperty("kpiId", kpiId);
      }
    }
    
    List<Map<String, Object>> modifiedRoles = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.MODIFIED_ROLES);
    for (Map<String, Object> modifiedRole : modifiedRoles) {
      String roleId = (String) modifiedRole.get(IModifiedKPIRoleModel.ROLE_ID);
      Vertex roleVertex = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      
      List<String> addedCandidates = (List<String>) modifiedRole
          .get(IModifiedKPIRoleModel.ADDED_CANDIDATES);
      Iterable<Vertex> userVertices = UtilClass.getVerticesByIds(addedCandidates,
          VertexLabelConstants.ENTITY_TYPE_USER);
      // Iterator<Vertex> linkedUserVerticeIterator =
      // roleVertex.getVertices(Direction.OUT,
      // RelationshipLabelConstants.HAS_CANDIDATE).iterator();
      // List<Vertex> linkedUserVertices =
      // IteratorUtils.toList(linkedUserVerticeIterator);
      for (Vertex user : userVertices) {
        /* if (linkedUserVertices.contains(user)) {
          continue;
        }*/
        Edge hasCandidateEdge = roleVertex.addEdge(RelationshipLabelConstants.HAS_CANDIDATE, user);
        hasCandidateEdge.setProperty("kpiId", kpiId);
      }
      
      List<String> deletedCandidates = (List<String>) modifiedRole
          .get(IModifiedKPIRoleModel.DELETED_CANDIDATES);
      List<Edge> edgesToRemove = new ArrayList<>();
      Iterable<Edge> linkCandidateEdges = roleVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_CANDIDATE);
      for (Edge linkCandidateEdge : linkCandidateEdges) {
        String userId = UtilClass.getCodeNew(linkCandidateEdge.getVertex(Direction.IN));
        if (deletedCandidates.contains(userId) && linkCandidateEdge.getProperty("kpiId")
            .equals(kpiId)) {
          edgesToRemove.add(linkCandidateEdge);
        }
      }
      
      for (Edge edge : edgesToRemove) {
        edge.remove();
      }
    }
  }
  
  public static void handleAddedDeletedModifiedKPITags(Vertex kpiVertex, Map<String, Object> kpiMap)
      throws Exception
  {
    List<Map<String, Object>> addedTags = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.ADDED_TAGS);
    OrientVertexType tagVertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.KPI_TAG,
        CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> addedTag : addedTags) {
      String tagId = (String) addedTag.get(IAddedVariantContextTagsModel.TAG_ID);
      List<String> tagValueIds = (List<String>) addedTag
          .get(IAddedVariantContextTagsModel.TAG_VALUE_IDS);
      Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      Vertex kpiTag = UtilClass.createNode(new HashMap<>(), tagVertexType, new ArrayList<>());
      Edge kpiTagNodeEdge = kpiVertex.addEdge(RelationshipLabelConstants.HAS_KPI_TAG, kpiTag);
      kpiTagNodeEdge.setProperty(IVariantContextTagModel.TAG_ID, UtilClass.getCodeNew(tagNode));
      kpiTag.addEdge(RelationshipLabelConstants.HAS_KPI_TAG, tagNode);
      for (String tagValueId : tagValueIds) {
        Vertex tagValueNode = UtilClass.getVertexById(tagValueId, VertexLabelConstants.ENTITY_TAG);
        Edge kpiTagValueNodeEdge = kpiTag.addEdge(RelationshipLabelConstants.HAS_KPI_TAG_VALUE,
            tagValueNode);
        kpiTagValueNodeEdge.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID,
            UtilClass.getCodeNew(tagValueNode));
      }
    }
    
    List<Map<String, Object>> modifiedTags = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.MODIFIED_TAGS);
    Map<String, Object> addedTagValuesMap = new HashMap<>();
    Map<String, Object> deletedTagValuesMap = new HashMap<>();
    
    for (Map<String, Object> modifiedTag : modifiedTags) {
      String tagId = (String) modifiedTag.get(IVariantContextModifiedTagsModel.TAG_ID);
      List<String> addedTagValuesList = (List<String>) modifiedTag
          .get(IVariantContextModifiedTagsModel.ADDED_TAG_VALUE_IDS);
      if (addedTagValuesList.size() > 0) {
        addedTagValuesMap.put(tagId, addedTagValuesList);
      }
      List<String> deletedTagValuesList = (List<String>) modifiedTag
          .get(IVariantContextModifiedTagsModel.DELETED_TAG_VALUE_IDS);
      
      if (deletedTagValuesList.size() > 0) {
        deletedTagValuesMap.put(tagId, deletedTagValuesList);
      }
    }
    handleModifiedTags(kpiVertex, addedTagValuesMap, deletedTagValuesMap);
    
    List<String> deletedTagIds = (List<String>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.DELETED_TAGS);
    if (deletedTagIds.size() > 0) {
      Iterable<Edge> kpiNodeEdges = kpiVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_KPI_TAG);
      for (Edge kpiNodeEdge : kpiNodeEdges) {
        String tagId = kpiNodeEdge.getProperty(IVariantContextTagModel.TAG_ID);
        if (deletedTagIds.contains(tagId)) {
          Vertex kpiTagNode = kpiNodeEdge.getVertex(Direction.IN);
          kpiTagNode.remove();
        }
      }
    }
  }
  
  private static void handleModifiedTags(Vertex kpiNode, Map<String, Object> addedTagValues,
      Map<String, Object> deletedTagValues) throws Exception
  {
    Iterable<Edge> kpiNodeEdges = kpiNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KPI_TAG);
    for (Edge kpiNodeEdge : kpiNodeEdges) {
      String tagId = kpiNodeEdge.getProperty(IVariantContextTagModel.TAG_ID);
      List<String> addedTagValueIds = (List<String>) addedTagValues.get(tagId);
      if (addedTagValueIds != null) {
        Vertex kpiTagNode = kpiNodeEdge.getVertex(Direction.IN);
        for (String tagValueId : addedTagValueIds) {
          try {
            Vertex tagValueNode = UtilClass.getVertexByIndexedId(tagValueId,
                VertexLabelConstants.ENTITY_TAG);
            Edge kpiTagValueNodeEdge = kpiTagNode
                .addEdge(RelationshipLabelConstants.HAS_KPI_TAG_VALUE, tagValueNode);
            kpiTagValueNodeEdge.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID,
                UtilClass.getCodeNew(tagValueNode));
          }
          catch (NotFoundException e) {
            throw new TagNotFoundException();
          }
        }
      }
      
      List<String> deletedTagValueIds = (List<String>) deletedTagValues.get(tagId);
      if (deletedTagValueIds == null) {
        continue;
      }
      
      for (String tagValueId : deletedTagValueIds) {
        try {
          Vertex tagValueNode = UtilClass.getVertexByIndexedId(tagValueId,
              VertexLabelConstants.ENTITY_TAG);
          Iterable<Edge> tagValueEdges = tagValueNode.getEdges(Direction.IN,
              RelationshipLabelConstants.HAS_KPI_TAG_VALUE);
          for (Edge tagValueEdge : tagValueEdges) {
            if (tagValueEdge.getProperty(IVariantContextTagValuesModel.TAG_VALUE_ID)
                .equals(tagValueId)) {
              tagValueEdge.remove();
            }
          }
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
      }
    }
  }
  
  private static void handleADMforDrillDown(Vertex kpiVertex, Map<String, Object> kpiMap)
      throws Exception
  {
    Iterator<Vertex> drillDownIterator = kpiVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DRILL_DOWN)
        .iterator();
    if (!drillDownIterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex drillDownNode = drillDownIterator.next();
    OrientVertexType drillDownLevelVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.DRILL_DOWN_LEVEL, CommonConstants.CODE_PROPERTY);
    
    List<Map<String, Object>> addedDrillDowns = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.ADDED_DRILL_DOWNS);
    for (Map<String, Object> addedDrillDown : addedDrillDowns) {
      String type = (String) addedDrillDown.get(IDrillDown.TYPE);
      Vertex drillDownLevelNode = UtilClass.createNode(addedDrillDown, drillDownLevelVertexType,
          Arrays.asList(IDrillDown.TYPE_IDS));
      drillDownNode.addEdge(RelationshipLabelConstants.HAS_DRILL_DOWN_LEVEL, drillDownLevelNode);
      if (type.equals("tag")) {
        List<String> typeIds = (List<String>) addedDrillDown.get(IDrillDown.TYPE_IDS);
        for (String typeId : typeIds) {
          Vertex tagVertex = UtilClass.getVertexById(typeId, VertexLabelConstants.ENTITY_TAG);
          Edge edge = drillDownLevelNode.addEdge(RelationshipLabelConstants.HAS_LEVEL_TAG,
              tagVertex);
          edge.setProperty("tagId", typeId);
        }
      }
    }
    
    List<String> deletedDrillDownLevels = (List<String>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.DELETED_DRILL_DOWNS);
    for (String levelId : deletedDrillDownLevels) {
      Vertex levelVertex = UtilClass.getVertexById(levelId, VertexLabelConstants.DRILL_DOWN_LEVEL);
      levelVertex.remove();
    }
    
    List<Map<String, Object>> modifiedDrillDowns = (List<Map<String, Object>>) kpiMap
        .get(ISaveKeyPerformanceIndexModel.MODIFIED_DRILL_DOWNS);
    for (Map<String, Object> modifiedDrillDown : modifiedDrillDowns) {
      List<Edge> edgesToRemove = new ArrayList<>();
      String levelId = (String) modifiedDrillDown.get(IModifiedDrillDownModel.ID);
      Vertex levelVertex = UtilClass.getVertexById(levelId, VertexLabelConstants.DRILL_DOWN_LEVEL);
      String levelType = (String) modifiedDrillDown.get(IModifiedDrillDownModel.TYPE);
      levelVertex.setProperty(IDrillDown.TYPE, levelType);
      List<String> deletedTypes = (List<String>) modifiedDrillDown
          .get(IModifiedDrillDownModel.DELETED_TYPES);
      Iterable<Edge> hasLevelTagEdges = levelVertex.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_LEVEL_TAG);
      for (Edge hasLevelTagEdge : hasLevelTagEdges) {
        String tagId = hasLevelTagEdge.getProperty("tagId");
        if (deletedTypes.contains(tagId)) {
          edgesToRemove.add(hasLevelTagEdge);
        }
      }
      for (Edge edge : edgesToRemove) {
        edge.remove();
      }
      if (levelType.equals("tag")) {
        List<String> addedTypes = (List<String>) modifiedDrillDown
            .get(IModifiedDrillDownModel.ADDED_TYPES);
        for (String addedtype : addedTypes) {
          Vertex tagVertex = UtilClass.getVertexById(addedtype, VertexLabelConstants.ENTITY_TAG);
          Edge edge = levelVertex.addEdge(RelationshipLabelConstants.HAS_LEVEL_TAG, tagVertex);
          edge.setProperty("tagId", addedtype);
        }
      }
    }
  }
}

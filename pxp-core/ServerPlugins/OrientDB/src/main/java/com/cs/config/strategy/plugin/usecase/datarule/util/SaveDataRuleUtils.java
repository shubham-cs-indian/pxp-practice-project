package com.cs.config.strategy.plugin.usecase.datarule.util;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.cs.core.config.interactor.entity.datarule.IFindReplaceNormalizatiom;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.entity.datarule.ISubStringNormalization;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.datarule.IDataRuleEntityRule;
import com.cs.core.config.interactor.model.datarule.ISaveDataRuleModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveDataRuleUtils {
  
  public static void execute(Map<String, Object> ruleMap) throws Exception
  {
    String label = (String) ruleMap.get(CommonConstants.LABEL_PROPERTY);
    Vertex dataRule = UtilClass.getVertexById((String) ruleMap.get(CommonConstants.ID_PROPERTY),
        VertexLabelConstants.DATA_RULE);
    dataRule.setProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY),
        label);
    dataRule.setProperty(IDataRule.PHYSICAL_CATALOG_IDS,
        ruleMap.get(IDataRule.PHYSICAL_CATALOG_IDS));
    handleSavedAttributeDataRules(dataRule, ruleMap);
    handleSavedRuleViolation(dataRule, ruleMap);
    handleSavedRoleDataRules(dataRule, ruleMap);
    handleSavedTypes(dataRule, ruleMap);
    handleSavedTaxonomies(dataRule, ruleMap);
    // handleSavedTypeDataRules(dataRule, ruleMap);
    handleSavedRelationshipDataRules(dataRule, ruleMap);
    handleSavedTagDataRules(dataRule, ruleMap);
    handleSavedNormalizations(dataRule, ruleMap);
    handleSaveOrganizations(dataRule, ruleMap);
    handleSaveEndpoints(dataRule, ruleMap);
    handleSaveLanguages(dataRule, ruleMap);
  }
  
  private static void handleSaveEndpoints(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    manageAddedEndpointIds(dataRule, ruleMap);
    manageDeletedEndpointIds(dataRule, ruleMap);
  }
  
  private static void handleSaveLanguages(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<String> addedLanguages = (List<String>) ruleMap.get(ISaveDataRuleModel.ADDED_LANGUAGES);
    List<String> deletedLanguages = (List<String>) ruleMap
        .remove(ISaveDataRuleModel.DELETED_LANGUAGES);
    
    DataRuleUtils.addLanguages(dataRule, addedLanguages);
    DataRuleUtils.manageDeletedLanguages(dataRule, deletedLanguages);
  }
  
  private static void manageDeletedEndpointIds(Vertex dataRule, Map<String, Object> ruleMap)
  {
    List<String> deletedEndpoints = (List<String>) ruleMap
        .remove(ISaveDataRuleModel.DELETED_ENDPOINTS);
    if (deletedEndpoints != null && !deletedEndpoints.isEmpty()) {
      Iterator<Edge> existingEdges = (Iterator<Edge>) dataRule.getEdges(Direction.OUT,
          RelationshipLabelConstants.RULE_ENDPOINT_LINK);
      while (existingEdges.hasNext()) {
        Edge existingEdge = existingEdges.next();
        if (deletedEndpoints.contains(UtilClass.getCodeNew(existingEdge.getVertex(Direction.IN)))) {
          existingEdge.remove();
        }
      }
    }
  }
  
  private static void manageAddedEndpointIds(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<String> addedEndpoints = (List<String>) ruleMap.remove(ISaveDataRuleModel.ADDED_ENDPOINTS);
    List<String> alreadyExisting = new ArrayList<>();
    Iterator<Vertex> existingEndpoints = (Iterator<Vertex>) dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.RULE_ENDPOINT_LINK);
    if (addedEndpoints != null && !addedEndpoints.isEmpty()) {
      while (existingEndpoints.hasNext()) {
        Vertex node = existingEndpoints.next();
        alreadyExisting.add(UtilClass.getCodeNew(node));
      }
      for (String addedEndpoint : addedEndpoints) {
        if (alreadyExisting != null && alreadyExisting.contains(addedEndpoint)) {
          continue;
        }
        else {
          Vertex endpoint = UtilClass.getVertexById(addedEndpoint, VertexLabelConstants.ENDPOINT);
          dataRule.addEdge(RelationshipLabelConstants.RULE_ENDPOINT_LINK, endpoint);
        }
      }
    }
  }
  
  private static void handleSaveOrganizations(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    manageAddedOrganizationIds(dataRule, ruleMap);
    manageDeletedOrganizationIds(dataRule, ruleMap);
  }
  
  private static void manageDeletedOrganizationIds(Vertex dataRule, Map<String, Object> ruleMap)
  {
    List<String> deletedOrganizationIds = (List<String>) ruleMap
        .remove(ISaveDataRuleModel.DELETED_ORGANIZATION_IDS);
    deleteOrganizations(dataRule, deletedOrganizationIds);
  }

  public static void deleteOrganizations(Vertex dataRule, List<String> deletedOrganizationIds)
  {
    List<Edge> edgesToRemove = new ArrayList<>();
    List<String> deletedOrganizationIdsClone = new ArrayList<>(deletedOrganizationIds);
    Iterable<Edge> availableOrganizationEdges = dataRule.getEdges(Direction.OUT,
        RelationshipLabelConstants.ORGANISATION_RULE_LINK);
    for (Edge availableOrganizationEdge : availableOrganizationEdges) {
      Vertex OrganizationVertex = availableOrganizationEdge.getVertex(Direction.IN);
      String organizationId = UtilClass.getCodeNew(OrganizationVertex);
      if (deletedOrganizationIdsClone.contains(organizationId)) {
        edgesToRemove.add(availableOrganizationEdge);
        deletedOrganizationIdsClone.remove(organizationId);
      }
      if (deletedOrganizationIdsClone.isEmpty()) {
        break;
      }
    }
    for (Edge edgeToRemove : edgesToRemove) {
      edgeToRemove.remove();
    }
  }

  public static void manageAddedOrganizationIds(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<String> addedOrganizationIds = (List<String>) ruleMap
        .remove(ISaveDataRuleModel.ADDED_ORGANIZATION_IDS);

    addOrganizations(dataRule, addedOrganizationIds);
  }

  public static void addOrganizations(Vertex dataRule, List<String> addedOrganizationIds) throws Exception
  {
    if (addedOrganizationIds == null || addedOrganizationIds.isEmpty()) {
      return;
    }
    for (String addedOrganizationId : addedOrganizationIds) {
      Vertex dashboardTabVertex = UtilClass.getVertexByIndexedId(addedOrganizationId,
          VertexLabelConstants.ORGANIZATION);
      dataRule.addEdge(RelationshipLabelConstants.ORGANISATION_RULE_LINK, dashboardTabVertex);
    }
  }

  public static void handleSavedAttributeDataRules(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<Map<String, Object>> addedAttributeRules = (List<Map<String, Object>>) ruleMap
        .get("addedAttributeRules");
    List<String> deletedAttributeRuleIds = (List<String>) ruleMap.get("deletedAttributeRules");
    List<Map<String, Object>> modifiedAttributeRules = (List<Map<String, Object>>) ruleMap
        .get("modifiedAttributeRules");
    handleAddedAttributeRules(dataRule, addedAttributeRules);
    handleDeletedAttributeRules(dataRule, deletedAttributeRuleIds);
    handleModifiedAttributeRules(dataRule, modifiedAttributeRules);
  }
  
  public static void handleAddedAttributeRules(Vertex dataRule,
      List<Map<String, Object>> addedAttributeRules) throws Exception
  {
    DataRuleUtils.addAttributeRules(dataRule, addedAttributeRules);
  }
  
  public static void handleDeletedAttributeRules(Vertex dataRule,
      List<String> deletedAttributeRuleIds) throws Exception
  {
    for (String id : deletedAttributeRuleIds) {
      try {
        Vertex attributeIntermediate = UtilClass.getVertexById(id,
            VertexLabelConstants.ATTRIBUTE_RULE_INTERMEDIATE);
        DataRuleUtils.deleteIntermediateWithRuleNodes(attributeIntermediate);
      }
      catch (NotFoundException e) {
        // node already deleted. Do nothing
      }
    }
  }
  
  public static void handleModifiedAttributeRules(Vertex dataRule,
      List<Map<String, Object>> modifiedAttributeRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedAttribute : modifiedAttributeRules) {
      
      String id = (String) modifiedAttribute.get(CommonConstants.ID_PROPERTY);
      Vertex attibuteIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.ATTRIBUTE_RULE_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedAttribute
          .get("addedRules");
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedAttribute
          .get("modifiedRules");
      List<String> deletedRules = (List<String>) modifiedAttribute.get("deletedRules");
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        String ruleListLinkId = (String) addedRule.get(CommonConstants.RULE_LIST_LINK_ID_PROPERTY);
        String attributeLinkId = (String) addedRule.get(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        List<String> klassLinkIds = (List<String>) addedRule
            .get(IDataRuleEntityRule.KLASS_LINK_IDS);
        addedRule.remove(CommonConstants.RULE_LIST_LINK_ID_PROPERTY);
        addedRule.remove(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType, List.of(CommonConstants.ENTITYTYPE));
        attibuteIntermediate.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
        if (ruleListLinkId != null && !ruleListLinkId.isEmpty()) {
          Vertex ruleList = UtilClass.getVertexById(ruleListLinkId, VertexLabelConstants.RULE_LIST);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_RULE_LIST, ruleList);
        }
        if (attributeLinkId != null && !attributeLinkId.isEmpty()) {
          Vertex attributeToLink = UtilClass.getVertexById(attributeLinkId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_LINK, attributeToLink);
        }
        if (klassLinkIds != null && klassLinkIds.size() > 0) {
          for (String klassLinkId : klassLinkIds) {
            Vertex klassToLink = UtilClass.getVertexById(klassLinkId,
                VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
            ruleNode.addEdge(RelationshipLabelConstants.HAS_KLASS_LINK, klassToLink);
          }
        }
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId, VertexLabelConstants.RULE_NODE);
        String to = (String) modifiedRule.get(CommonConstants.TO_PROPERTY);
        String from = (String) modifiedRule.get(CommonConstants.FROM_PROPERTY);
        String type = (String) modifiedRule.get(CommonConstants.TYPE_PROPERTY);
        List<String> values = (List<String>) modifiedRule.get(CommonConstants.VALUES_PROPERTY);
        Boolean shouldCompareWithSystemDate = (Boolean) modifiedRule
            .get(IDataRuleEntityRule.SHOULD_COMPARE_WITH_SYSTEM_DATE);
        String ruleListLinkId = (String) modifiedRule
            .get(CommonConstants.RULE_LIST_LINK_ID_PROPERTY);
        String attributeLinkId = (String) modifiedRule
            .get(CommonConstants.ATTRIBUTE_LINK_ID_PROPERTY);
        List<String> klassLinkIds = (List<String>) modifiedRule
            .get(IDataRuleEntityRule.KLASS_LINK_IDS);
        if (to != null)
          ruleNode.setProperty(CommonConstants.TO_PROPERTY, to);
        if (from != null)
          ruleNode.setProperty(CommonConstants.FROM_PROPERTY, from);
        if (type != null)
          ruleNode.setProperty(CommonConstants.TYPE_PROPERTY, type);
        if (values != null)
          ruleNode.setProperty(CommonConstants.VALUES_PROPERTY, values);
        if (shouldCompareWithSystemDate != null) {
          ruleNode.setProperty(IDataRuleEntityRule.SHOULD_COMPARE_WITH_SYSTEM_DATE,
              shouldCompareWithSystemDate);
        }
        Iterable<Edge> hasRuleListEdge = ruleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_RULE_LIST);
        Iterable<Edge> hasAttributeLinkEdge = ruleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_ATTRIBUTE_LINK);
        Iterable<Edge> hasKlassLinkEdge = ruleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_KLASS_LINK);
        for (Edge ruleList : hasRuleListEdge) {
          ruleList.remove();
        }
        for (Edge attributeLink : hasAttributeLinkEdge) {
          attributeLink.remove();
        }
        for (Edge klassLink : hasKlassLinkEdge) {
          klassLink.remove();
        }
        if (ruleListLinkId != null && !ruleListLinkId.isEmpty()) {
          Vertex ruleList = UtilClass.getVertexById(ruleListLinkId, VertexLabelConstants.RULE_LIST);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_RULE_LIST, ruleList);
        }
        if (attributeLinkId != null && !attributeLinkId.isEmpty()) {
          Vertex attributeToLink = UtilClass.getVertexById(attributeLinkId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          ruleNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_LINK, attributeToLink);
        }
        if (klassLinkIds != null && klassLinkIds.size() > 0) {
          for (String klassLinkId : klassLinkIds) {
            Vertex klassToLink = UtilClass.getVertexById(klassLinkId,
                VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
            ruleNode.addEdge(RelationshipLabelConstants.HAS_KLASS_LINK, klassToLink);
          }
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId, VertexLabelConstants.RULE_NODE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleSavedRuleViolation(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<Map<String, Object>> addedRuleViolations = (List<Map<String, Object>>) ruleMap
        .get("addedRuleViolations");
    List<String> deletedRuleViolationIds = (List<String>) ruleMap.get("deletedRuleViolations");
    List<Map<String, Object>> modifiedRuleViolation = (List<Map<String, Object>>) ruleMap
        .get("modifiedRuleViolations");
    handleAddedRuleViolations(dataRule, addedRuleViolations);
    handleDeletedRuleViolations(deletedRuleViolationIds);
    handleModifiedRuleViolations(dataRule, modifiedRuleViolation);
  }
  
  public static void handleAddedRuleViolations(Vertex dataRule,
      List<Map<String, Object>> addedRuleViolations) throws Exception
  {
    DataRuleUtils.addRuleViolations(dataRule, addedRuleViolations);
  }
  
  public static void handleDeletedRuleViolations(List<String> deletedRuleViolationIds)
      throws Exception
  {
    for (String deletedIds : deletedRuleViolationIds) {
      Vertex ruleViolationIds = UtilClass.getVertexById(deletedIds,
          VertexLabelConstants.RULE_VIOLATION);
      ruleViolationIds.remove();
    }
  }
  
  public static void handleModifiedRuleViolations(Vertex dataRule,
      List<Map<String, Object>> modifiedRuleViolations) throws Exception
  {
    for (Map<String, Object> modifiedRuleViolation : modifiedRuleViolations) {
      String modifiedRuleViolationId = (String) modifiedRuleViolation
          .get(CommonConstants.ID_PROPERTY);
      Vertex ruleViolationNode = UtilClass.getVertexById(modifiedRuleViolationId,
          VertexLabelConstants.RULE_VIOLATION);
      String description = (String) modifiedRuleViolation.get(CommonConstants.DESCRIPTION_PROPERTY);
      String color = (String) modifiedRuleViolation.get(CommonConstants.COLOR_PROPERTY);
      ruleViolationNode.setProperty(
          EntityUtil.getLanguageConvertedField(CommonConstants.DESCRIPTION_PROPERTY), description);
      ruleViolationNode.setProperty(CommonConstants.COLOR_PROPERTY, color);
    }
  }
  
  public static void handleSavedRoleDataRules(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<Map<String, Object>> addedRoleDataRules = (List<Map<String, Object>>) ruleMap
        .get("addedRoleRules");
    List<String> deletedRoleDataRules = (List<String>) ruleMap.get("deletedRoleRules");
    List<Map<String, Object>> modifiedRoleDataRules = (List<Map<String, Object>>) ruleMap
        .get("modifiedRoleRules");
    handleAddedRoleDataRules(dataRule, addedRoleDataRules);
    handleDeletedRoleDataRules(dataRule, deletedRoleDataRules);
    handleModifiedRoleDataRules(dataRule, modifiedRoleDataRules);
  }
  
  public static void handleAddedRoleDataRules(Vertex dataRule,
      List<Map<String, Object>> addedRoleDataRules) throws Exception
  {
    DataRuleUtils.addRoleRules(dataRule, addedRoleDataRules);
  }
  
  public static void handleDeletedRoleDataRules(Vertex dataRule,
      List<String> deletedRoleDataRuleIds) throws Exception
  {
    for (String id : deletedRoleDataRuleIds) {
      Vertex roleIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.ROLE_RULE_INTERMEDIATE);
      DataRuleUtils.deleteIntermediateWithRuleNodes(roleIntermediate);
    }
  }
  
  public static void handleModifiedRoleDataRules(Vertex dataRule,
      List<Map<String, Object>> modifiedRoleDataRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedRoleDataRule : modifiedRoleDataRules) {
      String modifiedRoleDataRuleId = (String) modifiedRoleDataRule
          .get(CommonConstants.ID_PROPERTY);
      Vertex roleIntermediate = UtilClass.getVertexById(modifiedRoleDataRuleId,
          VertexLabelConstants.ROLE_RULE_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedRoleDataRule
          .get("addedRules");
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedRoleDataRule
          .get("modifiedRules");
      List<String> deletedRules = (List<String>) modifiedRoleDataRule.get("deletedRules");
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType);
        roleIntermediate.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
        List<String> values = (List<String>) addedRule.get(CommonConstants.VALUES_PROPERTY);
        for (String userId : values) {
          Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          ruleNode.addEdge(RelationshipLabelConstants.RULE_USER_LINK, user);
        }
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId, VertexLabelConstants.RULE_NODE);
        String to = (String) modifiedRule.get(CommonConstants.TO_PROPERTY);
        String from = (String) modifiedRule.get(CommonConstants.FROM_PROPERTY);
        String type = (String) modifiedRule.get(CommonConstants.TYPE_PROPERTY);
        List<String> values = (List<String>) modifiedRule.get(CommonConstants.VALUES_PROPERTY);
        ruleNode.setProperty(CommonConstants.TO_PROPERTY, to);
        ruleNode.setProperty(CommonConstants.FROM_PROPERTY, from);
        ruleNode.setProperty(CommonConstants.TYPE_PROPERTY, type);
        ruleNode.setProperty(CommonConstants.VALUES_PROPERTY, values);
        Iterable<Edge> ruleUserEdges = ruleNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.RULE_USER_LINK);
        for (Edge ruleUserEdge : ruleUserEdges) {
          ruleUserEdge.remove();
        }
        for (String userId : values) {
          Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          ruleNode.addEdge(RelationshipLabelConstants.RULE_USER_LINK, user);
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId, VertexLabelConstants.RULE_NODE);
        ruleNode.remove();
      }
    }
  }
  
  /* public static void handleSavedTypeDataRules(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<Map<String, Object>> addedTypeRules = (List<Map<String, Object>>) ruleMap
        .get("addedTypeRules");
    List<String> deletedTypeRules = (List<String>) ruleMap.get("deletedTypeRules");
    List<Map<String, Object>> modifiedTypeRules = (List<Map<String, Object>>) ruleMap
        .get("modifiedTypeRules");
    handleAddedTypeRules(dataRule, addedTypeRules);
    handleDeletedTypeRules(dataRule, deletedTypeRules);
    handleModifiedTypeRules(dataRule, modifiedTypeRules);
  }*/
  
  public static void handleAddedTypeRules(Vertex dataRule, List<Map<String, Object>> addedTypeRules)
      throws Exception
  {
    DataRuleUtils.addTypeRules(dataRule, addedTypeRules);
  }
  
  public static void handleDeletedTypeRules(Vertex dataRule, List<String> deletedTypeRuleIds)
      throws Exception
  {
    for (String id : deletedTypeRuleIds) {
      Vertex klassIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.KLASS_RULE_INTERMEDIATE);
      DataRuleUtils.deleteIntermediateWithRuleNodes(klassIntermediate);
    }
  }
  
  public static void handleModifiedTypeRules(Vertex dataRule,
      List<Map<String, Object>> modifiedTypeRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedTypeRule : modifiedTypeRules) {
      String modifiedTypeRuleId = (String) modifiedTypeRule.get(CommonConstants.ID_PROPERTY);
      Vertex klassIntermediate = UtilClass.getVertexById(modifiedTypeRuleId,
          VertexLabelConstants.KLASS_RULE_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedTypeRule
          .get("addedRules");
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedTypeRule
          .get("modifiedRules");
      List<String> deletedRules = (List<String>) modifiedTypeRule.get("deletedRules");
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType);
        klassIntermediate.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId, VertexLabelConstants.RULE_NODE);
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
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId, VertexLabelConstants.RULE_NODE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleSavedRelationshipDataRules(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<Map<String, Object>> addedRelationshipRules = (List<Map<String, Object>>) ruleMap
        .get("addedRelationshipRules");
    List<String> deletedRelationshipRuleIds = (List<String>) ruleMap
        .get("deletedRelationshipRules");
    List<Map<String, Object>> modifiedRelationshipRules = (List<Map<String, Object>>) ruleMap
        .get("modifiedRelationshipRules");
    handleAddedRelationshipRules(dataRule, addedRelationshipRules);
    handleDeletedRelationshipRules(dataRule, deletedRelationshipRuleIds);
    handleModifiedRelationshipRules(dataRule, modifiedRelationshipRules);
  }
  
  public static void handleAddedRelationshipRules(Vertex dataRule,
      List<Map<String, Object>> addedRelationshipRules) throws Exception
  {
    DataRuleUtils.addRelationshipRules(dataRule, addedRelationshipRules);
  }
  
  public static void handleDeletedRelationshipRules(Vertex dataRule,
      List<String> deletedRelationshipRuleIds) throws Exception
  {
    for (String id : deletedRelationshipRuleIds) {
      Vertex relationshipIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.RELATIONSHIP_RULE_INTERMEDIATE);
      DataRuleUtils.deleteIntermediateWithRuleNodes(relationshipIntermediate);
    }
  }
  
  public static void handleModifiedRelationshipRules(Vertex dataRule,
      List<Map<String, Object>> modifiedRelationshipRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedRelationshipRule : modifiedRelationshipRules) {
      String modifiedRelationshipRuleId = (String) modifiedRelationshipRule
          .get(CommonConstants.ID_PROPERTY);
      Vertex relationshipIntermediate = UtilClass.getVertexById(modifiedRelationshipRuleId,
          VertexLabelConstants.RELATIONSHIP_RULE_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedRelationshipRule
          .get("addedRules");
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedRelationshipRule
          .get("modifiedRules");
      List<String> deletedRules = (List<String>) modifiedRelationshipRule.get("deletedRules");
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType);
        relationshipIntermediate.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId, VertexLabelConstants.RULE_NODE);
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
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId, VertexLabelConstants.RULE_NODE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleSavedTagDataRules(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<Map<String, Object>> addedTagRules = (List<Map<String, Object>>) ruleMap
        .get("addedTagRules");
    List<String> deletedTagRuleIds = (List<String>) ruleMap.get("deletedTagRules");
    List<Map<String, Object>> modifiedTagRules = (List<Map<String, Object>>) ruleMap
        .get("modifiedTagRules");
    handleAddedTagRules(dataRule, addedTagRules);
    handleDeletedTagRules(dataRule, deletedTagRuleIds);
    handleModifiedTagRules(dataRule, modifiedTagRules);
  }
  
  public static void handleAddedTagRules(Vertex dataRule, List<Map<String, Object>> addedTagRules)
      throws Exception
  {
    DataRuleUtils.addTagRules(dataRule, addedTagRules);
  }
  
  public static void handleDeletedTagRules(Vertex dataRule, List<String> deletedTagRuleIds)
      throws Exception
  {
    for (String id : deletedTagRuleIds) {
      Vertex tagIntermediate = UtilClass.getVertexById(id,
          VertexLabelConstants.TAG_RULE_INTERMEDIATE);
      DataRuleUtils.deleteIntermediateWithRuleNodes(tagIntermediate);
    }
  }
  
  public static void handleModifiedTagRules(Vertex dataRule,
      List<Map<String, Object>> modifiedTagRules) throws Exception
  {
    OrientVertexType ruleNodeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.RULE_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> modifiedTagRule : modifiedTagRules) {
      String modifiedTagRuleId = (String) modifiedTagRule.get(CommonConstants.ID_PROPERTY);
      Vertex tagIntermediate = UtilClass.getVertexById(modifiedTagRuleId,
          VertexLabelConstants.TAG_RULE_INTERMEDIATE);
      List<Map<String, Object>> addedRules = (List<Map<String, Object>>) modifiedTagRule
          .get("addedRules");
      List<Map<String, Object>> modifiedRules = (List<Map<String, Object>>) modifiedTagRule
          .get("modifiedRules");
      List<String> deletedRules = (List<String>) modifiedTagRule.get("deletedRules");
      
      // add rule nodes
      for (Map<String, Object> addedRule : addedRules) {
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) addedRule
            .get(CommonConstants.TAG_VALUES);
        addedRule.remove(CommonConstants.TAG_VALUES);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.createNode(addedRule, ruleNodeVertexType);
        tagIntermediate.addEdge(RelationshipLabelConstants.RULE_LINK, ruleNode);
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
          int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
          String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
          Vertex tagValueNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          ruleNode.addEdge(RelationshipLabelConstants.RULE_TAG_VALUE_LINK, tagValueNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(CommonConstants.TO_PROPERTY, to);
          propertyMap.put(CommonConstants.FROM_PROPERTY, from);
          idPropertyMap.put(id, propertyMap);
        }
        DataRuleUtils.setTagValueEdgeProperty(idPropertyMap, ruleNode,
            RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
      }
      
      // modify rule nodes
      for (Map<String, Object> modifiedRule : modifiedRules) {
        String modifiedRuleId = (String) modifiedRule.get(CommonConstants.ID_PROPERTY);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        Vertex ruleNode = UtilClass.getVertexById(modifiedRuleId, VertexLabelConstants.RULE_NODE);
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
            DataRuleUtils.createRuleTagValueEdges(ruleNode, tagValues);
          }
        }
        if (!newType.equals(CommonConstants.NOT_EMPTY_PROPERTY)
            && !newType.equals(CommonConstants.EMPTY_PROPERTY)) {
          DataRuleUtils.setTagValueEdgeProperty(idPropertyMap, ruleNode,
              RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
        }
      }
      
      // delete rule nodes
      for (String deletedRuleId : deletedRules) {
        Vertex ruleNode = UtilClass.getVertexById(deletedRuleId, VertexLabelConstants.RULE_NODE);
        ruleNode.remove();
      }
    }
  }
  
  public static void handleSavedNormalizations(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<Map<String, Object>> addedNormalizations = (List<Map<String, Object>>) ruleMap
        .get("addedNormalizations");
    List<String> deletedNormalizationIds = (List<String>) ruleMap.get("deletedNormalizations");
    List<Map<String, Object>> modifiedNormalizations = (List<Map<String, Object>>) ruleMap
        .get("modifiedNormalizations");
    if (addedNormalizations != null) {
      handleAddedNormalizations(dataRule, addedNormalizations);
    }
    if (deletedNormalizationIds != null) {
      handleDeletedNormalizations(deletedNormalizationIds);
    }
    if (modifiedNormalizations != null) {
      handleModifiedNormalizations(dataRule, modifiedNormalizations);
    }
  }
  
  public static void handleDeletedNormalizations(List<String> deletedNormalizationIds)
      throws Exception
  {
    for (String id : deletedNormalizationIds) {
      Vertex normalization = UtilClass.getVertexById(id, VertexLabelConstants.NORMALIZATION);
      String transformationType = normalization.getProperty(INormalization.TRANSFORMATION_TYPE);
      if (transformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
        deleteNormalizationConcatenatedNodes(normalization);
      }
      normalization.remove();
    }
  }

  public static void handleAddedNormalizations(Vertex dataRule,
      List<Map<String, Object>> addedNormalizations) throws Exception
  {
    DataRuleUtils.addNormalizations(dataRule, addedNormalizations);
  }

  public static void deleteNormalizationConcatenatedNodes(Vertex normalization)
  {
    Iterable<Vertex> concatenatedNodes = normalization.getVertices(Direction.OUT,
        new String[] { RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK,
            RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK,
            RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK });
    for (Vertex concatenatedNode : concatenatedNodes) {
      concatenatedNode.remove();
    }
  }
  
  public static void handleModifiedNormalizations(Vertex dataRule, List<Map<String, Object>> modifiedNormalizations) throws Exception
  {
    for (Map<String, Object> modifiedNormalization : modifiedNormalizations) {
      String modifiedNormalizationId = (String) modifiedNormalization
          .get(CommonConstants.ID_PROPERTY);
      Vertex normalizationNode = UtilClass.getVertexByIndexedId(modifiedNormalizationId,
          VertexLabelConstants.NORMALIZATION);
      String type = (String) modifiedNormalization.get(CommonConstants.TYPE_PROPERTY);
      if (type.equals("attribute")) {
        List<String> values = (List<String>) modifiedNormalization.get(CommonConstants.VALUES_PROPERTY);
        String valueAsHTML = (String) modifiedNormalization.get(INormalization.VALUE_AS_HTML);
        
        String baseType = (String) modifiedNormalization.get(INormalization.BASE_TYPE);
        if (baseType.equals(CommonConstants.NORMALIZATION_BASE_TYPE)
            || baseType.equals(CommonConstants.ATTRIBUTE_VALUE_NORMALIZATION_BASE_TYPE)
            || baseType.equals(CommonConstants.CONCATENATED_NORMALIZATION_BASE_TYPE)) {
          
          if (values != null) {
            normalizationNode.setProperty(CommonConstants.VALUES_PROPERTY, values);
          }
          
          // Changes for HTML type attributes
          if (valueAsHTML != null) {
            normalizationNode.setProperty(INormalization.VALUE_AS_HTML, valueAsHTML);
          }
          
          String transformationType = normalizationNode
              .getProperty(INormalization.TRANSFORMATION_TYPE);
          if (transformationType.equals(CommonConstants.ATTRIBUTE_VALUE_TRANFORMATION_TYPE)) {
            Iterable<Edge> edges = normalizationNode.getEdges(Direction.OUT,
                RelationshipLabelConstants.NORMALIZATION_ATTRIBUTE_VALUE_LINK);
            for (Edge edge : edges) {
              edge.remove();
            }
          }
          else if (transformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
            deleteNormalizationConcatenatedNodes(normalizationNode);
          }
          
          String modifiedTransformationType = (String) modifiedNormalization
              .get(INormalization.TRANSFORMATION_TYPE);
          if (modifiedTransformationType
              .equals(CommonConstants.ATTRIBUTE_VALUE_TRANFORMATION_TYPE)) {
            DataRuleUtils.addAttributeToNormalization(normalizationNode, modifiedNormalization);
          }
          else if (modifiedTransformationType.equals(CommonConstants.CONCAT_TRANSFORMATION_TYPE)) {
            DataRuleUtils.addConcatenationToNormalization(normalizationNode, modifiedNormalization);
          }
          
          if (normalizationNode.getProperty(IFindReplaceNormalizatiom.FIND_TEXT) != null) {
            normalizationNode.removeProperty(IFindReplaceNormalizatiom.FIND_TEXT);
          }
          if (normalizationNode.getProperty(IFindReplaceNormalizatiom.REPLACE_TEXT) != null) {
            normalizationNode.removeProperty(IFindReplaceNormalizatiom.REPLACE_TEXT);
          }
          if (normalizationNode.getProperty(ISubStringNormalization.END_INDEX) != null) {
            normalizationNode.removeProperty(ISubStringNormalization.END_INDEX);
          }
          if (normalizationNode.getProperty(ISubStringNormalization.START_INDEX) != null) {
            normalizationNode.removeProperty(ISubStringNormalization.START_INDEX);
          }
        }
        Object attrOperatorList = modifiedNormalization.get(INormalization.ATTRIBUTE_OPERATOR_LIST);
        if(attrOperatorList != null){
          normalizationNode.setProperty(INormalization.ATTRIBUTE_OPERATOR_LIST,
              (List<Map<String, Object>>) attrOperatorList);
        }

        String calculatedAttributeUnit = (String) modifiedNormalization
            .get(INormalization.CALCULATED_ATTRIBUTE_UNIT);
        if (calculatedAttributeUnit == null) {
          calculatedAttributeUnit = "";
        }
        normalizationNode.setProperty(INormalization.CALCULATED_ATTRIBUTE_UNIT,
            calculatedAttributeUnit);
        String calculatedAttributeUnitAsHTML = (String) modifiedNormalization
            .get(INormalization.CALCULATED_ATTRIBUTE_UNIT_AS_HTML);
        if (calculatedAttributeUnitAsHTML == null) {
          calculatedAttributeUnitAsHTML = "";
        }
        normalizationNode.setProperty(INormalization.CALCULATED_ATTRIBUTE_UNIT_AS_HTML,
            calculatedAttributeUnitAsHTML);
        
        String transformationType = (String) modifiedNormalization
            .get(INormalization.TRANSFORMATION_TYPE);
        normalizationNode.setProperty(INormalization.TRANSFORMATION_TYPE, transformationType);
        normalizationNode.setProperty(INormalization.BASE_TYPE, baseType);
        if (baseType.equals(CommonConstants.SUB_STRING_NORMALIZATION_BASE_TYPE)) {
          Integer startIndex = (Integer) modifiedNormalization
              .get(ISubStringNormalization.START_INDEX);
          Integer endIndex = (Integer) modifiedNormalization.get(ISubStringNormalization.END_INDEX);
          normalizationNode.setProperty(ISubStringNormalization.START_INDEX, startIndex);
          normalizationNode.setProperty(ISubStringNormalization.END_INDEX, endIndex);
          if (normalizationNode.getProperty(IFindReplaceNormalizatiom.FIND_TEXT) != null) {
            normalizationNode.removeProperty(IFindReplaceNormalizatiom.FIND_TEXT);
          }
          if (normalizationNode.getProperty(IFindReplaceNormalizatiom.REPLACE_TEXT) != null) {
            normalizationNode.removeProperty(IFindReplaceNormalizatiom.REPLACE_TEXT);
          }
          if (normalizationNode.getProperty(INormalization.VALUES) != null) {
            normalizationNode.removeProperty(INormalization.VALUES);
          }
        }
        else if (baseType.equals(CommonConstants.FIND_REPLACE_NORMALIZATION_BASE_TYPE)) {
          String findText = (String) modifiedNormalization.get(IFindReplaceNormalizatiom.FIND_TEXT);
          String replaceText = (String) modifiedNormalization
              .get(IFindReplaceNormalizatiom.REPLACE_TEXT);
          normalizationNode.setProperty(IFindReplaceNormalizatiom.FIND_TEXT, findText);
          normalizationNode.setProperty(IFindReplaceNormalizatiom.REPLACE_TEXT, replaceText);
          if (normalizationNode.getProperty(ISubStringNormalization.START_INDEX) != null) {
            normalizationNode.removeProperty(ISubStringNormalization.START_INDEX);
          }
          if (normalizationNode.getProperty(ISubStringNormalization.END_INDEX) != null) {
            normalizationNode.removeProperty(ISubStringNormalization.END_INDEX);
          }
          if (normalizationNode.getProperty(INormalization.VALUES) != null) {
            normalizationNode.removeProperty(INormalization.VALUES);
          }
        }
      }
      else if (type.equals("role")) {
        Iterable<Edge> normalizationUserEdges = normalizationNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_USER_LINK);
        for (Edge edge : normalizationUserEdges) {
          edge.remove();
        }
        List<String> values = new ArrayList<>();
        values = (List<String>) modifiedNormalization.get(CommonConstants.VALUES_PROPERTY);
        for (String userId : values) {
          Vertex user = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
          normalizationNode.addEdge(RelationshipLabelConstants.NORMALIZATION_USER_LINK, user);
        }
      }
      else if (type.equals("tag")) {
        normalizationNode.setProperty(INormalization.TRANSFORMATION_TYPE,
            (String) modifiedNormalization.get(INormalization.TRANSFORMATION_TYPE));
        
        List<Map<String, Object>> tagValues = new ArrayList<>();
        tagValues = (List<Map<String, Object>>) modifiedNormalization.get(CommonConstants.TAG_VALUES);
        Map<String, Map<String, Object>> idPropertyMap = new HashMap<>();
        for (Map<String, Object> tagValue : tagValues) {
          int to = (int) tagValue.get(CommonConstants.TO_PROPERTY);
          int from = (int) tagValue.get(CommonConstants.FROM_PROPERTY);
          String id = (String) tagValue.get(CommonConstants.ID_PROPERTY);
          String innerTagId = (String) tagValue.get(IDataRuleTagValues.INNER_TAG_ID);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(CommonConstants.TO_PROPERTY, to);
          propertyMap.put(CommonConstants.FROM_PROPERTY, from);
          propertyMap.put(CommonConstants.ID_PROPERTY, id);
          idPropertyMap.put(innerTagId, propertyMap);
        }
        DataRuleUtils.setTagValueEdgePropertyForNormalization(idPropertyMap, normalizationNode,
            RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
      }
      else if (type.equals("type")) {
        Iterable<Edge> normalizationKlassEdges = normalizationNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        for (Edge edge : normalizationKlassEdges) {
          edge.remove();
        }
        List<String> klassIds = (List<String>) modifiedNormalization
            .get(CommonConstants.VALUES_PROPERTY);
        for (String klassId : klassIds) {
          Vertex klassVertex = UtilClass.getVertexById(klassId,
              VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
          normalizationNode.addEdge(RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK,
              klassVertex);
        }
      }
      else if (type.equals("taxonomy")) {
        Iterable<Edge> normalizationTaxonomyEdges = normalizationNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
        for (Edge edge : normalizationTaxonomyEdges) {
          edge.remove();
        }
        List<String> taxonomyIds = (List<String>) modifiedNormalization
            .get(CommonConstants.VALUES_PROPERTY);
        for (String taxonomyId : taxonomyIds) {
          Vertex taxonomyVertex = UtilClass.getVertexById(taxonomyId,
              VertexLabelConstants.ROOT_KLASS_TAXONOMY);
          normalizationNode.addEdge(RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK,
              taxonomyVertex);
        }
      }
    }
  }
  
  public static void handleSavedTaxonomies(Vertex dataRule, Map<String, Object> ruleMap)
      throws Exception
  {
    List<String> addedTaxonomies = (List<String>) ruleMap.get("addedTaxonomies");
    List<String> deletedTaxonomies = (List<String>) ruleMap.get("deletedTaxonomies");
    DataRuleUtils.addTaxonomyRules(dataRule, addedTaxonomies);
    Iterable<Edge> edgesIterator = dataRule.getEdges(Direction.IN,
        RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
    for (Edge edge : edgesIterator) {
      Vertex taxonomyVertex = edge.getVertex(Direction.OUT);
      if (deletedTaxonomies.contains(UtilClass.getCodeNew(taxonomyVertex))) {
        edge.remove();
      }
    }
    /*  Vertex intermediateNode = null;
    Iterator<Vertex> intermediateIterator = dataRule.getVertices(Direction.OUT,
        RelationshipLabelConstants.TAXONOMY_DATA_RULE).iterator();
    if(intermediateIterator.hasNext()){
      intermediateNode = intermediateIterator.next();
    }
    else if(addedTaxonomies.size() > 0) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.TAXONOMY_RULE_INTERMEDIATE
          , CommonConstants.CODE_PROPERTY);
      intermediateNode = UtilClass.createNode(new HashMap<>(), vertexType, new ArrayList<>());
      dataRule.addEdge(RelationshipLabelConstants.TAXONOMY_DATA_RULE, intermediateNode);
    }
    for (String taxonomyId : addedTaxonomies) {
      Vertex klassVertex = UtilClass.getVertexById(taxonomyId, VertexLabelConstants.KLASS_TAXONOMY);
      intermediateNode.addEdge(RelationshipLabelConstants.HAS_TAXONOMY_LINK, klassVertex);
    
      if(dataRule.getProperty(IDataRule.TYPE).equals("classification")) {
        Edge edge = getEdgeBetweenTwoEntity(dataRule.getProperty(CommonConstants.CODE_PROPERTY), taxonomyId, RelationshipLabelConstants.DATA_RULES);
        if(edge == null) {
          klassVertex.addEdge(RelationshipLabelConstants.DATA_RULES, dataRule);
        }
      }
    }
    
    if(deletedTaxonomies.size() > 0){
      Iterable<Edge> edgesIterator = intermediateNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_TAXONOMY_LINK);
      for (Edge edge : edgesIterator) {
        Vertex klassVertex = edge.getVertex(Direction.IN);
        if(deletedTaxonomies.contains(UtilClass.getCode(klassVertex))){
          edge.remove();
        }
      }
    }*/
  }
  
  public static void handleSavedTypes(Vertex dataRule, Map<String, Object> ruleMap) throws Exception
  {
    List<String> addedTypes = (List<String>) ruleMap.get("addedTypes");
    DataRuleUtils.addKlassRules(dataRule, addedTypes);
    List<String> deletedTypes = (List<String>) ruleMap.get("deletedTypes");
    Iterable<Edge> edgesIterator = dataRule.getEdges(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
    for (Edge edge : edgesIterator) {
      Vertex klassVertex = edge.getVertex(Direction.OUT);
      if (deletedTypes.contains(UtilClass.getCodeNew(klassVertex))) {
        edge.remove();
      }
    }
    /*Vertex intermediateNode = null;
    Iterator<Vertex> intermediateIterator = dataRule.getVertices(Direction.OUT, RelationshipLabelConstants.TYPE_DATA_RULE).iterator();
    if(intermediateIterator.hasNext()){
      intermediateNode = intermediateIterator.next();
    }
    else if(addedTypes.size() > 0) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.KLASS_RULE_INTERMEDIATE, CommonConstants.CODE_PROPERTY);
      intermediateNode = UtilClass.createNode(new HashMap<>(), vertexType, new ArrayList<>());
      dataRule.addEdge(RelationshipLabelConstants.TYPE_DATA_RULE, intermediateNode);
    }
    for (String klassId : addedTypes) {
      Vertex klassVertex = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      intermediateNode.addEdge(RelationshipLabelConstants.HAS_KLASS_LINK, klassVertex);
    
      if(dataRule.getProperty(IDataRule.TYPE).equals("classification")) {
        Edge edge = getEdgeBetweenTwoEntity(dataRule.getProperty(CommonConstants.CODE_PROPERTY), klassId, RelationshipLabelConstants.DATA_RULES);
        if(edge == null) {
          klassVertex.addEdge(RelationshipLabelConstants.DATA_RULES, dataRule);
        }
      }
    }
    
    if(deletedTypes.size() > 0){
      Iterable<Edge> edgesIterator = intermediateNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_LINK);
      for (Edge edge : edgesIterator) {
        Vertex klassVertex = edge.getVertex(Direction.IN);
        if(deletedTypes.contains(UtilClass.getCode(klassVertex))){
         edge.remove();
        }
      }
     }*/
  }
  
  public static Edge getEdgeBetweenTwoEntity(String inId, String outId, String relationshipLabel)
  {
    String query = "SELECT FROM " + relationshipLabel + " where in.code = '" + inId
        + "' and out.code = '" + outId + "'";
    Iterable<Edge> iterator = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Edge klassPropertyEdge = null;
    for (Edge vertex : iterator) {
      klassPropertyEdge = vertex;
    }
    return klassPropertyEdge;
  }
}

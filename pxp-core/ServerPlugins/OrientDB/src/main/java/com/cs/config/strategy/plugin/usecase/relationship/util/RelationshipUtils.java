package com.cs.config.strategy.plugin.usecase.relationship.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.CRC32;

import javax.management.relation.RelationNotFoundException;

import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.goldenrecord.GoldenRecordRuleUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.ISaveRelationshipSide;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.klass.IAddedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.ParentChildRelationshipException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedNatureRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.entity.relationshipinstance.IReferencedRelationshipInheritanceModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipSidePropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class RelationshipUtils {
  
  public static final String KLASS_CODE   = "classCode";
  public static final String CONTEXT_CODE = "contextCode";
  public static final String COUPLINGS    = "couplings";
  
  public static Vertex createRelationship(Map<String, Object> relationMap,
      List<String> fieldsToExclude) throws Exception
  {
    Map<String, Object> side1 = (Map<String, Object>) relationMap.get(IRelationship.SIDE1);
    
    updateDefaultLabelForSide(side1);
    TranslationsUtils.updateRelationshipSideLabel(side1);
    Map<String, Object> side2 = (Map<String, Object>) relationMap.get(IRelationship.SIDE2);
    updateDefaultLabelForSide(side2);
    TranslationsUtils.updateRelationshipSideLabel(side2);
    String side1KlassId = (String) side1.get(IRelationshipSide.KLASS_ID);
    String side2KlassId = (String) side2.get(IRelationshipSide.KLASS_ID);
    List<String> klassIds = Arrays.asList(side1KlassId, side2KlassId);
    checkParentChildRelationship(side1KlassId, side2KlassId, klassIds);
    relationMap.put(IRelationship.IS_NATURE, false);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP, CommonConstants.CODE_PROPERTY);
    side1.remove(COUPLINGS);
    side2.remove(COUPLINGS);
    Vertex vertex = UtilClass.createNode(relationMap, vertexType, fieldsToExclude);
    return vertex;
  }

  public static String getSide2KlassIdFromNatureRelationshipId(String natureRelationshipId)
      throws Exception
  {
    Vertex natureRelationshipNode = UtilClass.getVertexByIndexedId(natureRelationshipId,
        VertexLabelConstants.NATURE_RELATIONSHIP);
    Map<String, Object> relationshipMap = UtilClass.getMapFromNode(natureRelationshipNode);
    Map<String, Object> side2Map = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    String side2KlassId = (String) side2Map.get(IRelationshipSide.KLASS_ID);
    return side2KlassId;
  }
  
  public static Iterable<Vertex> getAllNatureKlassNodesFromKlassId(String klassId)
  {
    List<String> natureTypesToExclude = Arrays.asList(Constants.STANDARD_IDENTIFIER,
        Constants.EMBEDDED);
    Iterable<Vertex> klassNodes = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from (select from RootKlass where code = '" + klassId
                + "') strategy BREADTH_FIRST)" + " where natureType not in "
                + EntityUtil.quoteIt(natureTypesToExclude) + " and " + IKlass.IS_NATURE
                + " = true and " + IKlass.IS_ABSTRACT + " = false"))
        .execute();
    return klassNodes;
  }
  
  public static void checkParentChildRelationship(String side1KlassId, String side2KlassId,
      List<String> klassIds) throws Exception, ParentChildRelationshipException
  {
    if (side1KlassId.equals(side2KlassId)) {
      return;
    }
    
    int sidesIndex = 1;
    for (String klassId : klassIds) {
      Vertex klass = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      String query = "Select count(*) from (traverse in('Child_Of') from " + klass.getId()
          + " strategy BREADTH_FIRST) where code in ['" + klassIds.get(sidesIndex) + "']";
      Long count = EntityUtil.executeCountQueryToGetTotalCount(query);
      if (count == 1) {
        throw new ParentChildRelationshipException();
      }
      sidesIndex--;
    }
  }
  
  public static void manageADMOfProperties(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Map<String, Object>> sideInfoForDataTransfer)
      throws Exception
  {
    manageAddedAttributes(relationshipNode, relationshipMap, sideInfoForDataTransfer);
    manageModifiedAttributes(relationshipNode, relationshipMap, sideInfoForDataTransfer);
    manageDeletedAttributes(relationshipNode, relationshipMap, sideInfoForDataTransfer);
    manageAddedTags(relationshipNode, relationshipMap, sideInfoForDataTransfer);
    manageModifiedTags(relationshipNode, relationshipMap, sideInfoForDataTransfer);
    manageDeletedTags(relationshipNode, relationshipMap, sideInfoForDataTransfer);
  }
  
  public static void manageAddedAttributes(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Map<String, Object>> sideInfoForDataTransfer)
      throws Exception
  {
    List<Map<String, Object>> addedAttributes = (List<Map<String, Object>>) relationshipMap
        .get(ISaveRelationshipModel.ADDED_ATTRIBUTES);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      String krId = UtilClass.getCodeNew(kRNode);
      
      for (Map<String, Object> addedAttribute : addedAttributes) {
        String side = (String) addedAttribute.get(IModifiedRelationshipPropertyModel.SIDE);
        if (!KRSide.equals(side)) {
          continue;
        }
        String id = (String) addedAttribute.get(IModifiedRelationshipPropertyModel.ID);
        String couplingType = (String) addedAttribute
            .get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
        
        Map<String, Object> addedAttributeElement = new HashMap<>();
        addedAttributeElement.put(IReferencedRelationshipProperty.ID, id);
        addedAttributeElement.put(IReferencedRelationshipProperty.CODE, id);
        addedAttributeElement.put(IReferencedRelationshipProperty.COUPLING_TYPE, couplingType);
        
        Vertex attributeNode = null;
        try {
          attributeNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException();
        }
        
        Iterator<Edge> hasRelationshipAttributeEdges = ((OrientVertex) kRNode)
            .getEdges((OrientVertex) attributeNode, Direction.OUT,
                RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE)
            .iterator();
        if (hasRelationshipAttributeEdges.hasNext()) {
          throw new MultipleLinkFoundException();
        }
        
        Edge hasRelationshipAttribute = kRNode
            .addEdge(RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE, attributeNode);
        hasRelationshipAttribute.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE,
            couplingType);
        
        if (!couplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
          
          Map<String, Object> sideInfoForDataTransferMap = getSideMap(sideInfoForDataTransfer, krId,
              UtilClass.getCodeNew(relationshipNode));
          
          if ((Boolean) attributeNode.getProperty(IAttribute.IS_TRANSLATABLE)) {
            List<Map<String, Object>> modifiedDependentAttributeList = getModifiedDependentAttributeList(
                sideInfoForDataTransferMap);
            modifiedDependentAttributeList.add(addedAttributeElement);
          }
          else {
            List<Map<String, Object>> modifiedDependentAttributeList = getModifiedInDependentAttributeList(
                sideInfoForDataTransferMap);
            modifiedDependentAttributeList.add(addedAttributeElement);
          }
        }
      }
    }
  }
  
  private static List<Map<String, Object>> getModifiedInDependentAttributeList(
      Map<String, Object> sideInfoForDataTransferMap)
  {
    List<Map<String, Object>> modifiedDependentAttributeList = (List<Map<String, Object>>) sideInfoForDataTransferMap
        .get(ISideInfoForRelationshipDataTransferModel.MODIFIED_INDEPENDENT_ATTRIBUTES);
    if (modifiedDependentAttributeList == null) {
      modifiedDependentAttributeList = new ArrayList<>();
      sideInfoForDataTransferMap.put(
          ISideInfoForRelationshipDataTransferModel.MODIFIED_INDEPENDENT_ATTRIBUTES,
          modifiedDependentAttributeList);
    }
    return modifiedDependentAttributeList;
  }
  
  private static List<String> getDeletedDependentAttributeList(
      Map<String, Object> sideInfoForDataTransferMap)
  {
    List<String> deletedInDependentAttributeList = (List<String>) sideInfoForDataTransferMap
        .get(ISideInfoForRelationshipDataTransferModel.DELETED_DEPENDENT_ATTRIBUTES);
    if (deletedInDependentAttributeList == null) {
      deletedInDependentAttributeList = new ArrayList<>();
      sideInfoForDataTransferMap.put(
          ISideInfoForRelationshipDataTransferModel.DELETED_DEPENDENT_ATTRIBUTES,
          deletedInDependentAttributeList);
    }
    return deletedInDependentAttributeList;
  }
  
  private static List<Map<String, Object>> getModifiedDependentAttributeList(
      Map<String, Object> sideInfoForDataTransferMap)
  {
    List<Map<String, Object>> modifiedDependentAttributeList = (List<Map<String, Object>>) sideInfoForDataTransferMap
        .get(ISideInfoForRelationshipDataTransferModel.MODIFIED_DEPENDENT_ATTRIBUTES);
    if (modifiedDependentAttributeList == null) {
      modifiedDependentAttributeList = new ArrayList<>();
      sideInfoForDataTransferMap.put(
          ISideInfoForRelationshipDataTransferModel.MODIFIED_DEPENDENT_ATTRIBUTES,
          modifiedDependentAttributeList);
    }
    return modifiedDependentAttributeList;
  }
  
  private static List<String> getDeletedInDependentAttributeList(
      Map<String, Object> sideInfoForDataTransferMap)
  {
    List<String> modifiedInDependentAttributeList = (List<String>) sideInfoForDataTransferMap
        .get(ISideInfoForRelationshipDataTransferModel.DELETED_INDEPENDENT_ATTRIBUTES);
    if (modifiedInDependentAttributeList == null) {
      modifiedInDependentAttributeList = new ArrayList<>();
      sideInfoForDataTransferMap.put(
          ISideInfoForRelationshipDataTransferModel.DELETED_INDEPENDENT_ATTRIBUTES,
          modifiedInDependentAttributeList);
    }
    return modifiedInDependentAttributeList;
  }
  
  private static List<Map<String, Object>> getModifiedTagList(
      Map<String, Object> sideInfoForDataTransferMap)
  {
    List<Map<String, Object>> modifiedTagList = (List<Map<String, Object>>) sideInfoForDataTransferMap
        .get(ISideInfoForRelationshipDataTransferModel.MODIFIED_TAGS);
    if (modifiedTagList == null) {
      modifiedTagList = new ArrayList<>();
      sideInfoForDataTransferMap.put(ISideInfoForRelationshipDataTransferModel.MODIFIED_TAGS,
          modifiedTagList);
    }
    return modifiedTagList;
  }
  
  private static List<String> getDeletedTagList(Map<String, Object> sideInfoForDataTransferMap)
  {
    List<String> deletedTagList = (List<String>) sideInfoForDataTransferMap
        .get(ISideInfoForRelationshipDataTransferModel.DELETED_TAGS);
    if (deletedTagList == null) {
      deletedTagList = new ArrayList<>();
      sideInfoForDataTransferMap.put(ISideInfoForRelationshipDataTransferModel.DELETED_TAGS,
          deletedTagList);
    }
    return deletedTagList;
  }
  
  private static Map<String, Object> getSideMap(
      Map<String, Map<String, Object>> sideInfoForDataTransfer, String krId, String relationshipId)
  {
    Map<String, Object> sideInfoForDataTransferMap = sideInfoForDataTransfer.get(krId);
    if (sideInfoForDataTransferMap == null) {
      sideInfoForDataTransferMap = new HashMap<>();
      sideInfoForDataTransferMap.put(ISideInfoForRelationshipDataTransferModel.SIDE_ID, krId);
      sideInfoForDataTransferMap.put(ISideInfoForRelationshipDataTransferModel.RELATIONSHIP_ID,
          relationshipId);
      sideInfoForDataTransfer.put(krId, sideInfoForDataTransferMap);
    }
    return sideInfoForDataTransferMap;
  }
  
  public static List<Vertex> getKlassRelationshipNodesFromRelationshipNode(Vertex relationshipNode)
  {
    List<Vertex> returnList = new ArrayList<>();
    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex krNode : kRNodes) {
      returnList.add(krNode);
    }
    
    return returnList;
  }
  
  private static void manageModifiedAttributes(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Map<String, Object>> sideInfoForDataTransfer)
      throws Exception
  {
    List<Map<String, Object>> modifiedAttributes = (List<Map<String, Object>>) relationshipMap
        .get(ISaveRelationshipModel.MODIFIED_ATTRIBUTES);
    
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      String krId = UtilClass.getCodeNew(kRNode);
      
      for (Map<String, Object> modifiedAttribute : modifiedAttributes) {
        String side = (String) modifiedAttribute.get(IModifiedRelationshipPropertyModel.SIDE);
        if (!KRSide.equals(side)) {
          continue;
        }
        String id = (String) modifiedAttribute.get(IModifiedRelationshipPropertyModel.ID);
        String couplingType = (String) modifiedAttribute
            .get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
        
        Map<String, Object> modifiedAttributeElement = new HashMap<>();
        modifiedAttributeElement.put(IReferencedRelationshipProperty.ID, id);
        modifiedAttributeElement.put(IReferencedRelationshipProperty.CODE, id);
        modifiedAttributeElement.put(IDefaultValueChangeModel.COUPLING_TYPE, couplingType);
        
        Vertex attributeNode = null;
        try {
          attributeNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException();
        }
        
        Iterator<Edge> hasRelationshipAttributeEdges = ((OrientVertex) kRNode)
            .getEdges((OrientVertex) attributeNode, Direction.OUT,
                RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE)
            .iterator();
        if (hasRelationshipAttributeEdges.hasNext()) {
          Edge hasRelationshipAttribute = hasRelationshipAttributeEdges.next();
          hasRelationshipAttribute.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE,
              couplingType);
          if (hasRelationshipAttributeEdges.hasNext()) {
            throw new MultipleLinkFoundException();
          }
        }
        
        if (!couplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
          
          Map<String, Object> sideInfoForDataTransferMap = getSideMap(sideInfoForDataTransfer, krId,
              UtilClass.getCodeNew(relationshipNode));
          
          if ((Boolean) attributeNode.getProperty(IAttribute.IS_TRANSLATABLE)) {
            List<Map<String, Object>> modifiedDependentAttributeList = getModifiedDependentAttributeList(
                sideInfoForDataTransferMap);
            modifiedDependentAttributeList.add(modifiedAttributeElement);
          }
          else {
            List<Map<String, Object>> modifiedDependentAttributeList = getModifiedInDependentAttributeList(
                sideInfoForDataTransferMap);
            modifiedDependentAttributeList.add(modifiedAttributeElement);
          }
        }
      }
    }
  }
  
  private static void manageDeletedAttributes(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Map<String, Object>> sideInfoForDataTransfer)
      throws Exception
  {
    List<Map<String, Object>> deletedAttributes = (List<Map<String, Object>>) relationshipMap
        .get(ISaveRelationshipModel.DELETED_ATTRIBUTES);
    List<String> deletedAttributeElements = new ArrayList<>();
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      String krId = UtilClass.getCodeNew(kRNode);
      for (Map<String, Object> deletedAttribute : deletedAttributes) {
        String id = (String) deletedAttribute.get(IModifiedRelationshipPropertyModel.ID);
        String side = (String) deletedAttribute.get(IModifiedRelationshipPropertyModel.SIDE);
        if (!KRSide.equals(side)) {
          continue;
        }
        Vertex attributeNode = null;
        try {
          attributeNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException();
        }
        
        Iterator<Edge> hasRelationshipAttributeEdges = ((OrientVertex) kRNode)
            .getEdges((OrientVertex) attributeNode, Direction.OUT,
                RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE)
            .iterator();
        if (hasRelationshipAttributeEdges.hasNext()) {
          Edge hasRelationshipAttr = hasRelationshipAttributeEdges.next();
          hasRelationshipAttr.remove();
        }
        deletedAttributeElements.add(id);
        Map<String, Object> sideInfoForDataTransferMap = getSideMap(sideInfoForDataTransfer, krId,
            UtilClass.getCodeNew(relationshipNode));
        
        if ((Boolean) attributeNode.getProperty(IAttribute.IS_TRANSLATABLE)) {
          List<String> deletedDependentAttributeList = getDeletedDependentAttributeList(
              sideInfoForDataTransferMap);
          deletedDependentAttributeList.add(id);
        }
        else {
          List<String> modifiedDependentAttributeList = getDeletedInDependentAttributeList(
              sideInfoForDataTransferMap);
          modifiedDependentAttributeList.add(id);
        }
      }
    }
  }
  
  public static void manageAddedTags(Vertex relationshipNode, Map<String, Object> relationshipMap,
      Map<String, Map<String, Object>> sideInfoForDataTransfer) throws Exception
  {
    List<Map<String, Object>> addedTags = (List<Map<String, Object>>) relationshipMap
        .get(ISaveRelationshipModel.ADDED_TAGS);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      String krId = UtilClass.getCodeNew(kRNode);
      
      for (Map<String, Object> addedTag : addedTags) {
        String side = (String) addedTag.get(IModifiedRelationshipPropertyModel.SIDE);
        if (!KRSide.equals(side)) {
          continue;
        }
        String id = (String) addedTag.get(IModifiedRelationshipPropertyModel.ID);
        String couplingType = (String) addedTag
            .get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
        
        Map<String, Object> addedTagElement = new HashMap<>();
        addedTagElement.put(IReferencedRelationshipPropertiesModel.ID, id);
        addedTagElement.put(IReferencedRelationshipPropertiesModel.CODE, id);
        addedTagElement.put(IDefaultValueChangeModel.COUPLING_TYPE, couplingType);
        
        Vertex tagNode = null;
        try {
          tagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
        
        Iterator<Edge> hasRelationshipTagEdges = ((OrientVertex) kRNode)
            .getEdges((OrientVertex) tagNode, Direction.OUT,
                RelationshipLabelConstants.HAS_RELATIONSHIP_TAG)
            .iterator();
        if (hasRelationshipTagEdges.hasNext()) {
          throw new MultipleLinkFoundException();
        }
        
        Edge hasRelationshipTag = kRNode.addEdge(RelationshipLabelConstants.HAS_RELATIONSHIP_TAG,
            tagNode);
        hasRelationshipTag.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE,
            couplingType);
        if (!couplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
          Map<String, Object> sideInfoForDataTransferMap = getSideMap(sideInfoForDataTransfer, krId,
              UtilClass.getCodeNew(relationshipNode));
          List<Map<String, Object>> modifiedTagAttributeList = getModifiedTagList(
              sideInfoForDataTransferMap);
          modifiedTagAttributeList.add(addedTagElement);
        }
      }
    }
    /*for (Map<String, Object> addedTag : addedTags) {
      String id = (String) addedTag.get(IModifiedRelationshipPropertyModel.ID);
      String couplingType = (String) addedTag.get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
      Map<String, Object> addedTagElement = new HashMap<>();
      addedTagElement.put(IDefaultValueChangeModel.ENTITY_ID, id);
      addedTagElement.put(IDefaultValueChangeModel.TYPE, CommonConstants.TAG);
      addedTagElement.put(IDefaultValueChangeModel.COUPLING_TYPE, couplingType);
      String side = (String) addedTag.get(IModifiedRelationshipPropertyModel.SIDE);
      List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
      for (Vertex kRNode : krNodes) {
        String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
        if (KRSide.equals(side)) {
          Vertex tagNode = null;
          try {
            tagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          }
          catch (NotFoundException e) {
            throw new TagNotFoundException();
          }
    
          Iterator<Edge> hasRelationshipTagEdges = ((OrientVertex) kRNode)
              .getEdges((OrientVertex) tagNode, Direction.OUT,
                  RelationshipLabelConstants.HAS_RELATIONSHIP_TAG)
              .iterator();
          if (hasRelationshipTagEdges.hasNext()) {
            throw new MultipleLinkFoundException();
          }
    
          Edge hasRelationshipTag = kRNode.addEdge(RelationshipLabelConstants.HAS_RELATIONSHIP_TAG,
              tagNode);
          hasRelationshipTag.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE,
              couplingType);
    
          Iterable<Vertex> typeNodes = kRNode.getVertices(Direction.IN,
              RelationshipLabelConstants.HAS_KLASS_PROPERTY);
          List<String> klassAndChildrenIds = new ArrayList<>();
          for (Vertex typeNode : typeNodes) {
            klassAndChildrenIds.add(UtilClass.getCode(typeNode));
          }
          Iterable<Vertex> typeNodesForKNR = kRNode.getVertices(Direction.IN,
              RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
          for (Vertex typeNode : typeNodesForKNR) {
            klassAndChildrenIds.add(UtilClass.getCode(typeNode));
          }
    
          addedTagElement.put(IDefaultValueChangeModel.KLASS_AND_CHILDRENIDS, klassAndChildrenIds);
        }
      }
      if (!couplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
        addedTagElements.add(addedTagElement);
      }
    }
    if (!addedTags.isEmpty()) {
      sideInfoForDataTransfer.put(IRelationshipPropertiesToInheritModel.TAGS, addedTagElements);
      sideInfoForDataTransfer.put(IRelationshipPropertiesToInheritModel.RELATIONSHIPID, relationshipId);
    }*/
  }
  
  private static void manageModifiedTags(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Map<String, Object>> sideInfoForDataTransfer)
      throws Exception
  {
    List<Map<String, Object>> modifiedTags = (List<Map<String, Object>>) relationshipMap
        .get(ISaveRelationshipModel.MODIFIED_TAGS);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      String krId = UtilClass.getCodeNew(kRNode);
      
      for (Map<String, Object> modifiedTag : modifiedTags) {
        String side = (String) modifiedTag.get(IModifiedRelationshipPropertyModel.SIDE);
        if (!KRSide.equals(side)) {
          continue;
        }
        String id = (String) modifiedTag.get(IModifiedRelationshipPropertyModel.ID);
        String couplingType = (String) modifiedTag
            .get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
        
        Map<String, Object> addedTagElement = new HashMap<>();
        addedTagElement.put(IReferencedRelationshipPropertiesModel.ID, id);
        addedTagElement.put(IReferencedRelationshipPropertiesModel.CODE, id);
        addedTagElement.put(IDefaultValueChangeModel.COUPLING_TYPE, couplingType);
        
        Vertex tagNode = null;
        try {
          tagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
        
        Iterator<Edge> hasRelationshipTagEdges = ((OrientVertex) kRNode)
            .getEdges((OrientVertex) tagNode, Direction.OUT,
                RelationshipLabelConstants.HAS_RELATIONSHIP_TAG)
            .iterator();
        if (hasRelationshipTagEdges.hasNext()) {
          Edge hasRelationshipAttribute = hasRelationshipTagEdges.next();
          hasRelationshipAttribute.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE,
              couplingType);
          if (hasRelationshipTagEdges.hasNext()) {
            throw new MultipleLinkFoundException();
          }
        }
        
        if (!couplingType.equals(CommonConstants.LOOSELY_COUPLED)) {
          Map<String, Object> sideInfoForDataTransferMap = getSideMap(sideInfoForDataTransfer, krId,
              UtilClass.getCodeNew(relationshipNode));
          List<Map<String, Object>> modifiedTagAttributeList = getModifiedTagList(
              sideInfoForDataTransferMap);
          modifiedTagAttributeList.add(addedTagElement);
        }
      }
    }
    /*for (Map<String, Object> modifiedTag : modifiedTags) {
      String id = (String) modifiedTag.get(IModifiedRelationshipPropertyModel.ID);
      String couplingType = (String) modifiedTag.get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
      Map<String, Object> modifiedTagElement = new HashMap<>();
      modifiedTagElement.put(IDefaultValueChangeModel.ENTITY_ID, id);
      modifiedTagElement.put(IDefaultValueChangeModel.TYPE, CommonConstants.TAG);
      modifiedTagElement.put(IDefaultValueChangeModel.COUPLING_TYPE, couplingType);
      String side = (String) modifiedTag.get(IModifiedRelationshipPropertyModel.SIDE);
      List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
      for (Vertex kRNode : krNodes) {
        String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
        if (KRSide.equals(side)) {
    
          Vertex tagNode = null;
          try {
            tagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
          }
          catch (NotFoundException e) {
            throw new AttributeNotFoundException();
          }
    
          Iterator<Edge> hasRelationshipTagEdges = ((OrientVertex) kRNode)
              .getEdges((OrientVertex) tagNode, Direction.OUT,
                  RelationshipLabelConstants.HAS_RELATIONSHIP_TAG)
              .iterator();
          if (hasRelationshipTagEdges.hasNext()) {
            Edge hasRelationshipAttribute = hasRelationshipTagEdges.next();
            hasRelationshipAttribute.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE,
                couplingType);
            if (hasRelationshipTagEdges.hasNext()) {
              throw new MultipleLinkFoundException();
            }
          }
        }
        else {
          Iterable<Vertex> typeNodes = kRNode.getVertices(Direction.IN,
              RelationshipLabelConstants.HAS_KLASS_PROPERTY);
          List<String> klassAndChildrenIds = new ArrayList<>();
          for (Vertex typeNode : typeNodes) {
            klassAndChildrenIds.add(UtilClass.getCode(typeNode));
          }
          Iterable<Vertex> typeNodesForKNR = kRNode.getVertices(Direction.IN,
              RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
          for (Vertex typeNode : typeNodesForKNR) {
            klassAndChildrenIds.add(UtilClass.getCode(typeNode));
          }
    
          modifiedTagElement.put(IDefaultValueChangeModel.KLASS_AND_CHILDRENIDS,
              klassAndChildrenIds);
        }
      }
        modifiedTagElements.add(modifiedTagElement);
    }
    if (!modifiedTags.isEmpty()) {
      sideInfoForDataTransfer.put(IRelationshipPropertiesToInheritModel.TAGS, modifiedTagElements);
      sideInfoForDataTransfer.put(IRelationshipPropertiesToInheritModel.RELATIONSHIPID, relationshipId);
    }*/
  }
  
  private static void manageDeletedTags(Vertex relationshipNode,
      Map<String, Object> relationshipMap, Map<String, Map<String, Object>> sideInfoForDataTransfer)
      throws Exception
  {
    List<Map<String, Object>> deletedTags = (List<Map<String, Object>>) relationshipMap
        .get(ISaveRelationshipModel.DELETED_TAGS);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      String krId = UtilClass.getCodeNew(kRNode);
      for (Map<String, Object> deletedTag : deletedTags) {
        String id = (String) deletedTag.get(IModifiedRelationshipPropertyModel.ID);
        String side = (String) deletedTag.get(IModifiedRelationshipPropertyModel.SIDE);
        if (!KRSide.equals(side)) {
          continue;
        }
        Vertex attributeNode = null;
        try {
          attributeNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException();
        }
        
        Iterator<Edge> hasRelationshipAttributeEdges = ((OrientVertex) kRNode)
            .getEdges((OrientVertex) attributeNode, Direction.OUT,
                RelationshipLabelConstants.HAS_RELATIONSHIP_TAG)
            .iterator();
        if (hasRelationshipAttributeEdges.hasNext()) {
          Edge hasRelationshipAttr = hasRelationshipAttributeEdges.next();
          hasRelationshipAttr.remove();
        }
        Map<String, Object> sideInfoForDataTransferMap = getSideMap(sideInfoForDataTransfer, krId,
            UtilClass.getCodeNew(relationshipNode));
        List<String> modifiedTagAttributeList = getDeletedTagList(sideInfoForDataTransferMap);
        modifiedTagAttributeList.add(id);
      }
    }
  }
  
  public static Vertex createNatureRelationship(Map<String, Object> relationMap,
      List<String> fieldsToExclude) throws Exception
  {
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.NATURE_RELATIONSHIP, CommonConstants.CODE_PROPERTY);
    relationMap.put(IRelationship.IS_NATURE, true);
    Vertex vertex = UtilClass.createNode(relationMap, vertexType, fieldsToExclude);
    String relationshipType = (String) relationMap.get(IKlassNatureRelationship.RELATIONSHIP_TYPE);
    Map<String, Object> tabMap = (Map<String, Object>) relationMap
        .get(IAddedNatureRelationshipModel.TAB);
    if (relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
      TabUtils.linkAddedOrDefaultTab(vertex, tabMap, CommonConstants.NATURE_RELATIONSHIP);
    }
    else {
      TabUtils.linkAddedOrDefaultTab(vertex, tabMap, CommonConstants.RELATIONSHIP);
    }
    return vertex;
  }
  
  public static HashMap<String, Object> getRelationshipMapWithContext(Vertex relationshipNode)
      throws Exception
  {
    HashMap<String, Object> relationshipMap = new HashMap<>();
    HashMap<String, Object> mapFromNode = UtilClass.getMapFromNode(relationshipNode);
    
    GetRelationshipUtils.prepareRelationshipSidesTranslations(mapFromNode);
    
    relationshipMap.putAll(mapFromNode);
    fillContextInfo(relationshipNode, relationshipMap);
    Iterator<Edge> iterator = relationshipNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF)
        .iterator();
    Edge validatorOfRelationship = null;
    while (iterator.hasNext()) {
      validatorOfRelationship = iterator.next();
      HashMap<String, Object> validatorMap = new HashMap<>();
      Vertex validatorNode = validatorOfRelationship
          .getVertex(com.tinkerpop.blueprints.Direction.OUT);
      validatorMap.putAll(UtilClass.getMapFromNode(validatorNode));
      relationshipMap.put(CommonConstants.VALIDATOR_PROPERTY, validatorMap);
    }
    return relationshipMap;
  }
  
  public static HashMap<String, Object> getNatureRelationshipMap(Vertex natureRelationshipNode)
      throws Exception
  {
    HashMap<String, Object> relationshipMap = new HashMap<>();
    HashMap<String, Object> mapFromNode = UtilClass.getMapFromNode(natureRelationshipNode);
    
    GetRelationshipUtils.prepareRelationshipSidesTranslations(mapFromNode);
    
    relationshipMap.putAll(mapFromNode);
    fillContextInfo(natureRelationshipNode, relationshipMap);
    Iterator<Edge> iterator = natureRelationshipNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF)
        .iterator();
    Edge validatorOfRelationship = null;
    while (iterator.hasNext()) {
      validatorOfRelationship = iterator.next();
      HashMap<String, Object> validatorMap = new HashMap<>();
      Vertex validatorNode = validatorOfRelationship
          .getVertex(com.tinkerpop.blueprints.Direction.OUT);
      validatorMap.putAll(UtilClass.getMapFromNode(validatorNode));
      relationshipMap.put(CommonConstants.VALIDATOR_PROPERTY, validatorMap);
    }
    return relationshipMap;
  }
  
  public static String getTargetSideId(List<HashMap<String, Object>> sections,
      String relationshipId)
  {
    for (HashMap<String, Object> section : sections) {
      List<Map<String, Object>> elements = (List<Map<String, Object>>) section
          .get(ISection.ELEMENTS);
      for (Map<String, Object> element : elements) {
        Map<String, Object> relationship = (Map<String, Object>) element
            .get(CommonConstants.RELATIONSHIP);
        if (relationship != null && relationship.get(CommonConstants.CODE_PROPERTY)
            .equals(relationshipId)) {
          
          return (String) ((HashMap<String, Object>) element
              .get(CommonConstants.RELATIONSHIP_SIDE_PROPERTY))
                  .get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
        }
      }
    }
    
    return null;
  }
  
  public static String getTargetSideId(Vertex klassNode, String relationshipId) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Vertex entityNode = UtilClass.getVertexById(relationshipId,
        VertexLabelConstants.ROOT_RELATIONSHIP);
    String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    
    // get Klass Property Node by klassId and relationshipNode
    String query = "select from (select expand(in('has_property')) from " + entityNode.getId()
        + ") where in('has_klass_property') contains (code='" + klassId + "')";
    
    Iterable<Vertex> iterator = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : iterator) {
      Map<String, Object> klassPropertyNode = UtilClass.getMapFromVertex(new ArrayList<>(), vertex);
      Map<String, Object> relationshipSide = (Map<String, Object>) klassPropertyNode
          .get(CommonConstants.RELATIONSHIP_SIDE_PROPERTY);
      
      return (String) relationshipSide.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
    }
    return null;
  }
  
  public static void updateSideLabelsContextAndVisibility(OrientGraph graph,
      Map<String, Object> relationship, Vertex relationshipVertex) throws Exception
  {
    HashMap<String, Object> rSide1 = (HashMap<String, Object>) relationship
        .get(CommonConstants.RELATIONSHIP_SIDE_1);
    String rSide1Label = (String) rSide1
        .get(EntityUtil.getLanguageConvertedField(IRelationshipSide.LABEL));
    String rSide1AddedContextId = (String) rSide1.get(ISaveRelationshipSide.ADDED_CONTEXT);
    String rSide1DeletedContextId = (String) rSide1.get(ISaveRelationshipSide.DELETED_CONTEXT);
    
    Boolean rSide1IsVisible = (Boolean) rSide1.get(IRelationshipSide.IS_VISIBLE);
    HashMap<String, Object> rSide2 = (HashMap<String, Object>) relationship
        .get(CommonConstants.RELATIONSHIP_SIDE_2);
    String rSide2Label = (String) rSide2
        .get(EntityUtil.getLanguageConvertedField(IRelationshipSide.LABEL));
    Boolean rSide2IsVisible = (Boolean) rSide2.get(IRelationshipSide.IS_VISIBLE);
    String rSide2AddedContextId = (String) rSide2.get(ISaveRelationshipSide.ADDED_CONTEXT);
    String rSide2DeletedContextId = (String) rSide2.get(ISaveRelationshipSide.DELETED_CONTEXT);
    
    Iterable<Vertex> klassRelationships = relationshipVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex klassRelationship : klassRelationships) {
      String side = klassRelationship.getProperty(CommonConstants.SIDE_PROPERTY);
      Map<String, Object> relationshipSide = klassRelationship
          .getProperty(CommonConstants.RELATIONSHIP_SIDE);
      if (side.equals(CommonConstants.RELATIONSHIP_SIDE_2)) {
        relationshipSide.put(EntityUtil.getLanguageConvertedField(IKlassRelationshipSide.LABEL),
            rSide1Label);
        relationshipSide.put(IKlassRelationshipSide.IS_VISIBLE, rSide1IsVisible);
        relationshipSide.put(IKlassRelationshipSide.IS_OPPOSITE_VISIBLE, rSide2IsVisible);
        // as KR side stores info of other side
        manageContext(klassRelationship, rSide2AddedContextId, rSide2DeletedContextId);
        
      }
      else {
        relationshipSide.put(EntityUtil.getLanguageConvertedField(IKlassRelationshipSide.LABEL),
            rSide2Label);
        relationshipSide.put(IKlassRelationshipSide.IS_VISIBLE, rSide2IsVisible);
        relationshipSide.put(IKlassRelationshipSide.IS_OPPOSITE_VISIBLE, rSide1IsVisible);
        // as KR side stores info of other side
        manageContext(klassRelationship, rSide1AddedContextId, rSide1DeletedContextId);
      }
      klassRelationship.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide);
    }
  }
  
  @SuppressWarnings({ "rawtypes" })
  public static Map<String, Object> bulkCreateRelationships(HashMap<String, Object> relationship)
      throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    OrientGraph graph = UtilClass.getGraph();
    
    Map side1 = (Map) relationship.get(CommonConstants.RELATIONSHIP_SIDE_1);
    String side1Id = UtilClass.getUniqueSequenceId(null);
    side1.put(CommonConstants.ID_PROPERTY, side1Id);
    side1.put(CommonConstants.CODE_PROPERTY, side1Id);
    Map side2 = (Map) relationship.get(CommonConstants.RELATIONSHIP_SIDE_2);
    String side2Id = UtilClass.getUniqueSequenceId(null);
    side2.put(CommonConstants.ID_PROPERTY, side2Id);
    side2.put(CommonConstants.CODE_PROPERTY, side2Id);
    
    Vertex vertex = RelationshipUtils.createRelationship(relationship,
        Arrays.asList(IRelationshipModel.SECTIONS, IRelationshipModel.ADDED_ATTRIBUTES,
            IRelationshipModel.ADDED_TAGS));
    returnMap = new HashMap<String, Object>();
    returnMap.putAll(UtilClass.getMapFromNode(vertex));
    
    GetRelationshipUtils.prepareRelationshipSidesTranslations(returnMap);
    
    RelationshipUtils.addSectionElement(graph, returnMap, vertex);
    
    // tab
    Map<String, Object> tabMap = (Map<String, Object>) relationship
        .get(ICreateRelationshipModel.TAB);
    TabUtils.linkAddedOrDefaultTab(vertex, tabMap, CommonConstants.RELATIONSHIP);
    
    graph.commit();
    
    return returnMap;
  }
  
  // Changes related to section element model should be made here also.
  public static void addSectionElement(OrientGraph graph, Map<String, Object> relationship,
      Vertex relationhshipVertex) throws Exception
  {
    List<String> klassIds = new ArrayList<>();
    Vertex klassNode = null;
    Map<String, Object> side1 = (HashMap<String, Object>) relationship
        .get(CommonConstants.RELATIONSHIP_SIDE_1);
    Map<String, Object> side2 = (HashMap<String, Object>) relationship
        .get(CommonConstants.RELATIONSHIP_SIDE_2);
    String side2KlassId = (String) side2.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
    String side1KlassId = (String) side1.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
    HashMap<Integer, Object> klassPropertyIds = new HashMap<>();
    if (side1.containsKey(IRelationshipSide.ELEMENT_ID)
        && side1.get(IRelationshipSide.ELEMENT_ID) != null
        && !side1.get(IRelationshipSide.ELEMENT_ID)
            .toString()
            .isEmpty()) {
      klassPropertyIds.put(0, side1.get(IRelationshipSide.ELEMENT_ID));
    }
    else {
      klassPropertyIds.put(0, generateNewUniqueId(side2KlassId,
          (String) relationship.get(CommonConstants.CODE_PROPERTY), CommonConstants.SIDE_2_ID));
    }
    if (side2.containsKey(IRelationshipSide.ELEMENT_ID)
        && side2.get(IRelationshipSide.ELEMENT_ID) != null
        && !side2.get(IRelationshipSide.ELEMENT_ID)
            .toString()
            .isEmpty()) {
      klassPropertyIds.put(1, side2.get(IRelationshipSide.ELEMENT_ID));
    }
    else {
      klassPropertyIds.put(1, generateNewUniqueId(side1KlassId,
          (String) relationship.get(CommonConstants.CODE_PROPERTY), CommonConstants.SIDE_1_ID));
    }
    Map<String, Object> riSide1 = new HashMap<>();
    Map<String, Object> riSide2 = new HashMap<>();
    Map<Integer, Object> sides = new HashMap<>();
    int directions = 0;
    
    String side1ContextId = (String) side1.get(IRelationshipSide.CONTEXT_ID);
    String side2ContextId = (String) side2.get(IRelationshipSide.CONTEXT_ID);
    
    klassIds.add((String) side1KlassId);
    riSide1 = side2;
    riSide2 = side1;
    riSide1.put(CommonConstants.TARGET_RELATIONSHIP_MAPPING_ID_PROPERTY, klassPropertyIds.get(1));
    riSide1.put((String) CommonConstants.SOURCE_CARDINALITY,
        side1.get(CommonConstants.CARDINALITY));
    riSide1.put(CommonConstants.RELATIONSHIP_OPPOSITE_SIDE_ISVISIBLE,
        riSide2.get(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE));
    riSide1.put(IRelationshipSide.CONTEXT_ID, side1ContextId);
    
    sides.put(directions, riSide1);
    directions++;
    
    klassIds.add(side2KlassId);
    riSide2.put(CommonConstants.TARGET_RELATIONSHIP_MAPPING_ID_PROPERTY, klassPropertyIds.get(0));
    riSide2.put((String) CommonConstants.SOURCE_CARDINALITY,
        side2.get(CommonConstants.CARDINALITY));
    riSide2.put(CommonConstants.RELATIONSHIP_OPPOSITE_SIDE_ISVISIBLE,
        riSide1.get(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE));
    riSide2.put(IRelationshipSide.CONTEXT_ID, side2ContextId);
    sides.put(directions, riSide2);
    int sideCount = 0;
    for (String klassId : klassIds) {
      try {
        klassNode = UtilClass.getVertexByIndexedId(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        Map<String, Object> rSide = (Map<String, Object>) sides.get(sideCount);
        rSide.put(IKlassRelationshipSide.TARGET_TYPE,
            getTargetKlassType((String) rSide.get(IKlassRelationshipSide.KLASS_ID)));
        String relationshipId = (String) relationship.get(CommonConstants.CODE_PROPERTY);
        Vertex relationshipNode = UtilClass.getVertexById(relationshipId,
            VertexLabelConstants.ROOT_RELATIONSHIP);
        Map<String, Object> klassPropertyMap = new HashMap<>();
        String klassPropertyVertexId = (String) klassPropertyIds.get(sideCount);
        klassPropertyMap.put(CommonConstants.CODE_PROPERTY, klassPropertyVertexId);
        klassPropertyMap.put(CommonConstants.ID_PROPERTY, klassPropertyVertexId);
        if (sideCount == 0) {
          klassPropertyMap.put(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_1);
          Map<String, Object> relationshipSide1 = relationhshipVertex
              .getProperty(IRelationship.SIDE1);
          relationshipSide1.put(IRelationshipSide.ELEMENT_ID, klassPropertyVertexId);
          relationhshipVertex.setProperty(IRelationship.SIDE1, relationshipSide1);
        }
        else if (sideCount == 1) {
          klassPropertyMap.put(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_2);
          Map<String, Object> relationshipSide2 = relationhshipVertex
              .getProperty(IRelationship.SIDE2);
          relationshipSide2.put(IRelationshipSide.ELEMENT_ID, klassPropertyVertexId);
          relationhshipVertex.setProperty(IRelationship.SIDE2, relationshipSide2);
        }
        klassPropertyMap.put(ISectionRelationship.IS_NATURE, false);
        Vertex klassRelationshipNode = KlassUtils.createNewKlassPropertyNode(
            CommonConstants.RELATIONSHIP, klassNode, relationshipNode, null, rSide,
            klassPropertyMap, new ArrayList<>());
        
        String addedContextId = (String) rSide.get(IRelationshipSide.CONTEXT_ID);
        
        manageContext(klassRelationshipNode, addedContextId, null);
        
        Boolean isLite = relationhshipVertex.getProperty(IRelationship.IS_LITE);
        if (isLite == null) {
          isLite = false;
        }
        inheritKlassRelationshipNodesInChildKlasses(klassNode, klassRelationshipNode, isLite);
        
        sideCount++;
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
  }
  
  public static void addNatureSectionElement(OrientGraph graph,
      HashMap<String, Object> relationship, Vertex natureNode, String natureType) throws Exception
  {
    Vertex klassNode = null;
    Map<String, Object> side1 = (HashMap<String, Object>) relationship
        .get(CommonConstants.RELATIONSHIP_SIDE_1);
    Map<String, Object> side2 = (HashMap<String, Object>) relationship
        .get(CommonConstants.RELATIONSHIP_SIDE_2);
    
    String side2KlassId = (String) side2.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
    String side1KlassId = (String) side1.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID);
    List<String> klassIds = Arrays.asList(side1KlassId, side2KlassId);
    checkParentChildRelationship(side1KlassId, side2KlassId, klassIds);
    
    TranslationsUtils.updateRelationshipSideLabel(side1);
    TranslationsUtils.updateRelationshipSideLabel(side2);
    
    klassIds = new ArrayList<>();
    HashMap<Integer, Object> klassPropertyIds = new HashMap<>();
    klassPropertyIds.put(0,
        generateNewUniqueId(side2KlassId, (String) relationship.get(CommonConstants.CODE_PROPERTY),
            CommonConstants.RELATIONSHIP_SIDE_1));
    klassPropertyIds.put(1,
        generateNewUniqueId(side1KlassId, (String) relationship.get(CommonConstants.CODE_PROPERTY),
            CommonConstants.RELATIONSHIP_SIDE_2));
    Map<String, Object> riSide1 = new HashMap<>();
    Map<String, Object> riSide2 = new HashMap<>();
    Map<Integer, Object> sides = new HashMap<>();
    int directions = 0;
    klassIds.add((String) side1KlassId);
    // if ((Boolean) side.get(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE)) {
    riSide1 = side2;
    riSide2 = side1;
    riSide1.put(CommonConstants.TARGET_RELATIONSHIP_MAPPING_ID_PROPERTY, klassPropertyIds.get(1));
    riSide1.put((String) CommonConstants.SOURCE_CARDINALITY,
        side1.get(CommonConstants.CARDINALITY));
    riSide1.put(CommonConstants.RELATIONSHIP_OPPOSITE_SIDE_ISVISIBLE,
        riSide2.get(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE));
    sides.put(directions, riSide1);
    directions++;
    // }
    // if (!klassIds.contains(side2KlassId)) {
    klassIds.add(side2KlassId);
    // if ((Boolean) side.get(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE)) {
    riSide2.put(CommonConstants.TARGET_RELATIONSHIP_MAPPING_ID_PROPERTY, klassPropertyIds.get(0));
    riSide2.put((String) CommonConstants.SOURCE_CARDINALITY,
        side2.get(CommonConstants.CARDINALITY));
    riSide2.put(CommonConstants.RELATIONSHIP_OPPOSITE_SIDE_ISVISIBLE,
        riSide1.get(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE));
    sides.put(directions, riSide2);
    // }
    // }
    
    int sideCount = 0;
    for (String klassId : klassIds) {
      try {
        klassNode = UtilClass.getVertexByIndexedId(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        /*String rid = klassNode.getId().toString();
        Iterable<Vertex> i = graph.command(new OCommandSQL(
          "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
          .execute();
        List<Vertex> klassAndChildNodes = new ArrayList<>();
        for (Vertex node : i) {
        klassAndChildNodes.add(node);
        }*/
        
        Map<String, Object> rSide = (Map<String, Object>) sides.get(sideCount);
        rSide.put("targetType", getTargetKlassType((String) rSide.get("klassId")));
        String relationshipId = (String) relationship.get(CommonConstants.CODE_PROPERTY);
        Vertex relationshipNode = UtilClass.getVertexById(relationshipId,
            VertexLabelConstants.ROOT_RELATIONSHIP);
        String krId = (String) klassPropertyIds.get(sideCount);
        if (sideCount == 0) {
          KlassUtils.updateKlassNaturePropertyNode(klassNode, relationshipNode, rSide, natureNode,
              natureType, krId);
          Map<String, Object> relationshipSide1 = relationshipNode.getProperty(IRelationship.SIDE1);
          relationshipSide1.put(IRelationshipSide.ELEMENT_ID, krId);
          relationshipNode.setProperty(IRelationship.SIDE1, relationshipSide1);
        }
        else {
          Map<String, Object> klassPropertyMap = new HashMap<>();
          klassPropertyMap.put(CommonConstants.CODE_PROPERTY, krId);
          klassPropertyMap.put(CommonConstants.ID_PROPERTY, krId);
          klassPropertyMap.put(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_2);
          klassPropertyMap.put(ISectionRelationship.IS_NATURE, false);
          Vertex klassPropertyNode = KlassUtils.createNewKlassPropertyNode(
              CommonConstants.RELATIONSHIP, klassNode, relationshipNode, null, rSide,
              klassPropertyMap, new ArrayList<>());
          inheritKRNodeInChildKlasses(klassNode, klassPropertyNode);
          Map<String, Object> relationshipSide2 = relationshipNode.getProperty(IRelationship.SIDE2);
          relationshipSide2.put(IRelationshipSide.ELEMENT_ID, krId);
          relationshipNode.setProperty(IRelationship.SIDE2, relationshipSide2);
        }
        sideCount++;
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
  }
  
  public static void inheritKRNodeInChildKlasses(Vertex klassNode, Vertex klassPropertyNode)
      throws Exception
  {
    Iterable<Vertex> childNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex child : childNodes) {
      
      // Create edge with parentKlassProperty node and set utilizing section ids
      Edge duplicatedHasKlassPropertyEdge = child
          .addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY, klassPropertyNode);
      duplicatedHasKlassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      duplicatedHasKlassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
          new ArrayList<>());
      
      inheritKRNodeInChildKlasses(child, klassPropertyNode);
    }
  }
  
  private static String getTargetKlassType(String klassId) throws Exception
  {
    try {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      return klassNode.getProperty(CommonConstants.TYPE_PROPERTY);
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
  }
  
  private static String generateNewUniqueId(String... ids)
  {
    StringBuilder sb = new StringBuilder();
    for (String id : ids) {
      CRC32 crc = new CRC32();
      crc.update(id.getBytes());
      String str = Long.toHexString(crc.getValue());
      sb.append(str);
    }
    return UUID.nameUUIDFromBytes(sb.toString()
        .getBytes())
        .toString();
  }
  
  public static Map<String, Object> getRelationshipSideForNatureRelationship(Vertex klassNode,
      String relationshipId) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Vertex entityNode = UtilClass.getVertexById(relationshipId,
        VertexLabelConstants.ROOT_RELATIONSHIP);
    String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "select from (select expand(in("
        + EntityUtil.quoteIt(RelationshipLabelConstants.HAS_PROPERTY) + ")) from "
        + entityNode.getId() + ") where in('klass_nature_relationship_of') contains (code='"
        + klassId + "')";
    Iterable<Vertex> iterator = graph.command(new OCommandSQL(query))
        .execute();
    Map<String, Object> klassPropertyNode = new HashMap<>();
    for (Vertex vertex : iterator) {
      klassPropertyNode = UtilClass.getMapFromVertex(new ArrayList<>(), vertex);
    }
    return (Map<String, Object>) klassPropertyNode.get(CommonConstants.RELATIONSHIP_SIDE_PROPERTY);
  }
  
  public static void inheritKlassRelationshipNodesInChildKlasses(Vertex klassNode,
      Vertex klassPropertyNode, Boolean isLite)
  {
    Iterable<Vertex> childNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : childNodes) {
      inheritKlassRelationshipNodesInChildKlasses(childNode, klassPropertyNode, isLite);
     
      Boolean isNature = childNode.getProperty(IKlass.IS_NATURE);
      if ((isNature == null || !isNature) && isLite) {
        continue;
      }
      Edge klassPropertyEdge = childNode.addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY,
          klassPropertyNode);
      klassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      klassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
          new ArrayList<String>());
    }
  }
  
  // Delete Relationship Utils
  
  public static List<String> deleteRelationships(Iterable<Vertex> relationshipNodes,
      List<String> deletedNatureRelationshipIds, List<String> deletedRelationshipIds,
      List<Map<String, Object>> auditInfoList) throws Exception
  {
    List<String> deletedIds = new ArrayList<>();
    for (Vertex relationshipNode : relationshipNodes) {
      if(ValidationUtils.vaildateIfStandardEntity(relationshipNode))
        continue;
      String relationshipCid = UtilClass.getCodeNew(relationshipNode);
      if (deletedIds.contains(relationshipCid)) {
        // there is a possibilty of geting same Vertex multiple time in self
        // nature relationship ex.
        // in promotional Collection
        continue;
      }
      deletedIds.add(relationshipCid);
      
      Boolean isNature = relationshipNode.getProperty(IRelationship.IS_NATURE);
      String relationshipId = UtilClass.getCodeNew(relationshipNode);
      
      if (isNature) {
        deletedNatureRelationshipIds.add(relationshipId);
      }
      else {
        deletedRelationshipIds.add(relationshipId);
      }
      
      Iterator<Edge> iterator = relationshipNode
          .getEdges(com.tinkerpop.blueprints.Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF)
          .iterator();
      Edge validatorOfRelationship = null;
      while (iterator.hasNext()) {
        validatorOfRelationship = iterator.next();
        
        if (validatorOfRelationship != null) {
          Vertex validatorNode = validatorOfRelationship
              .getVertex(com.tinkerpop.blueprints.Direction.OUT);
          validatorOfRelationship.remove();
          validatorNode.remove();
        }
      }
      
      // AttributionTaxonomyUtil.getLinkedAttributionTaxonomyRelationshipNodes(relationshipNode,
      // CommonConstants.RELATIONSHIP);
      deleteSectionElementNodesAttached(relationshipNode);
      deletePermissionNodesAttached(relationshipNode);
      deleteEntryFromSequenceNode(relationshipNode);
      
      Iterable<Edge> relationshipLinkRelationships = relationshipNode.getEdges(
          com.tinkerpop.blueprints.Direction.IN,
          RelationshipLabelConstants.STRUCTURE_RELATIONSHIP_LINK);
      
      // iterate over all relationship link relationships
      for (Edge relationshipLinkRelation : relationshipLinkRelationships) {
        Vertex structureNode = relationshipLinkRelation.getVertex(Direction.OUT);
        deleteStructureNode(structureNode);
        // relationshipLinkRelation.remove();
      }
      DataRuleUtils.deleteIntermediateVerticesWithInDirection(relationshipNode,
          RelationshipLabelConstants.RELATIONSHIP_DATA_RULE_LINK);
      DataRuleUtils.deleteVerticesWithInDirection(relationshipNode,
          RelationshipLabelConstants.ENTITY_RULE_VIOLATION_LINK);
      // GovernanceRuleUtil.deleteIntermediateVerticesWithInDirection(relationshipNode,
      // RelationshipLabelConstants.GOVERNANCE_RULE_REL_LINK);
      deleteTemplatePermissionNodesByRelationshipNode(relationshipNode);
      TabUtils.updateTabOnEntityDelete(relationshipNode);
      GoldenRecordRuleUtil.deleteGoldenRecordMergeEffectTypeNode(relationshipNode);
      
      AuditLogUtils.fillAuditLoginfo(auditInfoList, relationshipNode,
          Entities.RELATIONSHIPS, Elements.RELATIONSHIPS);
      relationshipNode.remove();
    }
    return deletedIds;
  }
  
  public static void deletePermissionNodesAttached(Vertex attributeNode)
  {
    Iterable<Vertex> iterable = attributeNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
    for (Vertex permissionNode : iterable) {
      permissionNode.remove();
    }
  }
  
  private static void deleteStructureNode(Vertex deletedStructureNode)
  {
    // TODO Auto-generated method stub
    Set<Vertex> nodesToDelete = new HashSet<>();
    Set<Edge> relationshipsToDelete = new HashSet<>();
    Set<Vertex> positionNodes = new HashSet<>();
    
    // get all position nodes...
    Iterable<Edge> structureChildAtRelationships = deletedStructureNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.STRUCTURE_CHILD_AT);
    for (Edge structureChildAtRelation : structureChildAtRelationships) {
      Vertex positionNode = structureChildAtRelation.getVertex(Direction.IN);
      positionNodes.add(positionNode);
      relationshipsToDelete.add(structureChildAtRelation);
    }
    
    Iterator<Edge> iterator = deletedStructureNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF)
        .iterator();
    Edge validatorOfRelation = null;
    while (iterator.hasNext()) {
      validatorOfRelation = iterator.next();
    }
    if (validatorOfRelation != null) {
      Vertex validatorNode = validatorOfRelation.getVertex(Direction.OUT);
      relationshipsToDelete.add(validatorOfRelation);
      nodesToDelete.add(validatorNode);
    }
    
    Iterable<Edge> viewSettingsOfRelationships = deletedStructureNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_SETTING_OF);
    
    for (Edge viewSettingRelation : viewSettingsOfRelationships) {
      Vertex viewSettingNode = viewSettingRelation.getVertex(Direction.OUT);
      
      Iterator<Edge> edgeIterator = viewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF)
          .iterator();
      Edge roleOfRelationship = null;
      while (edgeIterator.hasNext()) {
        roleOfRelationship = iterator.next();
        relationshipsToDelete.add(roleOfRelationship);
      }
      
      edgeIterator = viewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TREE_SETTING_OF)
          .iterator();
      Edge treeSettingOfRelationship = null;
      while (edgeIterator.hasNext()) {
        treeSettingOfRelationship = iterator.next();
        Vertex treeSettingNode = treeSettingOfRelationship.getVertex(Direction.OUT);
        relationshipsToDelete.add(treeSettingOfRelationship);
        nodesToDelete.add(treeSettingNode);
      }
      edgeIterator = viewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_EDITOR_SETTING_OF)
          .iterator();
      Edge editorSettingOfRelationship = null;
      while (edgeIterator.hasNext()) {
        editorSettingOfRelationship = edgeIterator.next();
        Vertex editorSettingNode = editorSettingOfRelationship.getVertex(Direction.OUT);
        relationshipsToDelete.add(editorSettingOfRelationship);
        nodesToDelete.add(editorSettingNode);
      }
      
      relationshipsToDelete.add(viewSettingRelation);
      nodesToDelete.add(viewSettingNode);
    }
    
    Iterable<Edge> roleViewSettingsOfRelationships = deletedStructureNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_VIEW_SETTING_OF);
    
    for (Edge roleViewSettingRelation : roleViewSettingsOfRelationships) {
      Vertex roleViewSettingNode = roleViewSettingRelation.getVertex(Direction.OUT);
      Iterator<Edge> edgeIterator = roleViewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_VIEW_OF)
          .iterator();
      
      Edge viewOfRelationship = null;
      while (edgeIterator.hasNext()) {
        viewOfRelationship = edgeIterator.next();
        relationshipsToDelete.add(viewOfRelationship);
      }
      
      edgeIterator = roleViewSettingNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF)
          .iterator();
      Edge roleOfRelationship = null;
      while (edgeIterator.hasNext()) {
        roleOfRelationship = edgeIterator.next();
        relationshipsToDelete.add(roleOfRelationship);
      }
      relationshipsToDelete.add(roleViewSettingRelation);
      nodesToDelete.add(roleViewSettingNode);
    }
    
    nodesToDelete.add(deletedStructureNode);
    
    for (Vertex positionNode : positionNodes) {
      Iterable<Edge> structureChildrenRelationships = positionNode.getEdges(Direction.IN,
          RelationshipLabelConstants.STRUCTURE_CHILD_AT);
      
      boolean isEmptyPosition = true;
      for (Edge relationship : structureChildrenRelationships) {
        isEmptyPosition = false;
        break;
      }
      
      if (isEmptyPosition) {
        Iterable<Edge> structurePositionOfRelationships = positionNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.KLASS_STRUCTURE_CHILD_POSITION_OF);
        
        for (Edge structureChildPositionRelationship : structurePositionOfRelationships) {
          relationshipsToDelete.add(structureChildPositionRelationship);
        }
        nodesToDelete.add(positionNode);
      }
    }
    
    for (Edge relationship : relationshipsToDelete) {
      if (relationship != null) {
        relationship.remove();
      }
    }
    for (Vertex node : nodesToDelete) {
      node.remove();
    }
  }
  
  private static void deleteSectionElementNodesAttached(Vertex relationshipNode) throws Exception
  {
    Iterator<Vertex> iterator = relationshipNode
        .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    
    while (iterator.hasNext()) {
      Vertex klassPropertyNode = iterator.next();
      klassPropertyNode.remove();
    }
  }
  
  public static Map<String, Object> getRelationshipsAndReferencesForKlassIds(
      String vertexLabelConstant, List<String> ids) throws Exception
  {
    Set<String> klassIds = new HashSet<>();
    Iterable<Vertex> klassVertices = UtilClass.getVerticesByIndexedIds(ids, vertexLabelConstant);
    for (Vertex vertex : klassVertices) {
      klassIds.add(vertex.getId()
          .toString());
    }
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> childKlassVertices = graph
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from " + klassIds + "  strategy BREADTH_FIRST)"))
        .execute();
    
    for (Vertex vertex : childKlassVertices) {
      klassIds.add(vertex.getId()
          .toString());
    }
    
    Iterable<Vertex> relationshipNodesToDelete = RelationshipDBUtil
        .getNonInheritedRelationshipVerticesFromTypes(new ArrayList<>(klassIds));
    
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("relationshipsToDelete", relationshipNodesToDelete);
    
    return returnMap;
  }
  
  public static List<String> getRelationshipIdsNotLinkedWithKlass(List<String> klassIds,
      Map<String, Object> relationshipsMap, Map<String, Object> referencedElementMap,
      Map<String, String> referencedRelationshipsMapping) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> connectedRelationship = new ArrayList<>();
    List<String> notConnectedRelationships = new ArrayList<>();
    
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      // Get klass Property Node for all relationships of klass
      String query = "select from (select expand(in('has_property')) from "
          + VertexLabelConstants.ROOT_RELATIONSHIP
          + ") where in('has_klass_property') contains (code='" + klassId + "') "; // Error in Publication template
      Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex kPVertex : iterable) {
        Boolean isEntityNotLinkedToKlass = true;
        // Get edge betn klass Relationship(KP)Node and klass Node
        query = "SELECT FROM " + RelationshipLabelConstants.HAS_KLASS_PROPERTY + " where out = "
            + klassNode.getId() + " and in = " + kPVertex.getId();
        Iterable<Edge> edgeIterable = graph.command(new OCommandSQL(query))
            .execute();
        Iterator<Edge> iterator = edgeIterable.iterator();
        Edge edge = iterator.next();
        List<String> utilizingSectionIds = edge
            .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
        if (utilizingSectionIds == null) {
          utilizingSectionIds = new ArrayList<String>();
        }
        for (String sectionId : utilizingSectionIds) {
          isEntityNotLinkedToKlass = false;
          /*Vertex sectionNode = UtilClass.getVertexById(sectionId,
          VertexLabelConstants.ENTITY_TYPE_KLASS_SECTION);*/
          query = "SELECT FROM " + RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF
              + " where in = " + klassNode.getId() + " and out IN (SELECT FROM "
              + VertexLabelConstants.ENTITY_TYPE_KLASS_SECTION + " where code = " + "'" + sectionId
              + "')";
          Iterable<Edge> sectionIterable = graph.command(new OCommandSQL(query))
              .execute();
          Iterator<Edge> sectionIterator = sectionIterable.iterator();
          if (!sectionIterator.hasNext()) {
            isEntityNotLinkedToKlass = true;
            break;
          }
        }
        // FIXME : remove above for loop and update following condition as
        // utilizingSectionIds.isEmpty()
        
        Iterator<Vertex> relationshipIterator = kPVertex
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        while (relationshipIterator.hasNext()) {
          Vertex relationshipVertex = relationshipIterator.next();
          String relationshipId = relationshipVertex.getProperty(CommonConstants.CODE_PROPERTY);
          if (isEntityNotLinkedToKlass) {
            if (notConnectedRelationships.contains(relationshipId)) {
              continue;
            }
            notConnectedRelationships.add(relationshipId);
            
            Map<String, Object> relationshipMap = RelationshipUtils
                .getRelationshipMapWithContext(relationshipVertex);
            relationshipsMap.put((String) relationshipMap.get(IRelationship.ID), relationshipMap);
            
            Map<String, Object> referencedElement = UtilClass.getMapFromVertex(new ArrayList<>(),
                kPVertex);
            referencedElement.remove(IReferencedNatureRelationshipModel.NATURE_TYPE);
            referencedElement.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
            referencedElement.put(IReferencedSectionRelationshipModel.ID, relationshipId);
            referencedElementMap.put(relationshipId, referencedElement);
            
            Map<String, Object> side = (Map<String, Object>) referencedElement
                .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
            if (side != null) {
              side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY,
                  UtilClass.getCodeNew(kPVertex));
            }
            
            referencedRelationshipsMapping.put(relationshipId, klassId);
          }
          else {
            connectedRelationship.add(relationshipId);
          }
        }
      }
    }
    connectedRelationship.retainAll(notConnectedRelationships);
    notConnectedRelationships.removeAll(connectedRelationship);
    for (String relationshipId : connectedRelationship) {
      relationshipsMap.remove(relationshipId);
      referencedElementMap.remove(relationshipId);
      referencedRelationshipsMapping.remove(relationshipId);
    }
    return notConnectedRelationships;
  }
  
  public static List<String> getAllAssetRelationshipIdsForKlasses(List<String> klassIds)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    List<String> relationshipIds = new ArrayList<>();
    
    for (String klassId : klassIds) {
      
      // Get klass Property Node for all relationships of klass
      String query = "select from (select expand(in('has_property')) from "
          + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP
          + ") where in('has_klass_property') contains (code='" + klassId + "')";
      Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex kPVertex : iterable) {
        Map<String, Object> relationshipSide = kPVertex
            .getProperty(CommonConstants.RELATIONSHIP_SIDE_PROPERTY);
        String targetType = (String) relationshipSide.get(CommonConstants.TARGET_TYPE);
        
        // If target klass baseType is not Asset continue
        if (targetType == null || !targetType.equalsIgnoreCase(CommonConstants.ASSET_KLASS_TYPE)) {
          continue;
        }
        
        // If target type is asset, add relationshipId to arraylist to return
        Iterator<Vertex> relationshipIterator = kPVertex
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        while (relationshipIterator.hasNext()) {
          Vertex relationshipVertex = relationshipIterator.next();
          String relationshipId = relationshipVertex.getProperty(CommonConstants.CODE_PROPERTY);
          relationshipIds.add(relationshipId);
        }
      }
    }
    return relationshipIds;
  }
  
  private static void deleteEntryFromSequenceNode(Vertex relationshipNode)
  {
    Iterable<Vertex> templateNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_TEMPLATE_RELATIONSHIP);
    for (Vertex template : templateNodes) {
      Iterator<Vertex> sequenceIterator = template
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_SEQUENCE)
          .iterator();
      if (sequenceIterator.hasNext()) {
        Vertex sequenceNode = sequenceIterator.next();
        List<String> sequenceList = sequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
        sequenceList.remove(UtilClass.getCodeNew(relationshipNode));
        sequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, sequenceList);
      }
    }
  }
  
  /**
   * @author Lokesh
   * @param relationshipNode
   */
  public static void deleteTemplatePermissionNodesByRelationshipNode(Vertex relationshipNode)
  {
    Iterable<Vertex> permissionIterable = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.IS_RELATIONSHIP_PERMISSION_OF);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  }
  
  public static void populatePropetiesInfo(Vertex relationshipNode, Map<String, Object> returnMap)
      throws KlassNotFoundException, Exception
  {
    Map<String, Object> relationshipSide1 = (Map<String, Object>) returnMap
        .get(IRelationshipModel.SIDE1);
    Map<String, Object> relationshipSide2 = (Map<String, Object>) returnMap
        .get(IRelationshipModel.SIDE2);
    
    populateAttributesForRelationshipSide(relationshipSide1, CommonConstants.RELATIONSHIP_SIDE_1,
        relationshipNode);
    populateAttributesForRelationshipSide(relationshipSide2, CommonConstants.RELATIONSHIP_SIDE_2,
        relationshipNode);
    
    populateTagsForRelationshipSide(relationshipSide1, CommonConstants.RELATIONSHIP_SIDE_1,
        relationshipNode);
    populateTagsForRelationshipSide(relationshipSide2, CommonConstants.RELATIONSHIP_SIDE_2,
        relationshipNode);
    
    populateInheritanceRelationshipsForRelationshipSide(relationshipSide1,
        CommonConstants.RELATIONSHIP_SIDE_1, relationshipNode);
    populateInheritanceRelationshipsForRelationshipSide(relationshipSide2,
        CommonConstants.RELATIONSHIP_SIDE_1, relationshipNode);
  }
  
  public static void populateAttributesForRelationshipSide(Map<String, Object> relationshipSide,
      String side, Vertex relationshipNode) throws Exception
  {
    List<Map<String, Object>> propertyMapList = new ArrayList<>();
    relationshipSide.put(IRelationshipSide.ATTRIBUTES, propertyMapList);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    for (Vertex kRNode : krNodes) {
      
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      if (KRSide.equals(side)) {
        Iterable<Edge> iterableAttributes = kRNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE);
        for (Edge hasRelationshipAttr : iterableAttributes) {
          Vertex attributeNode = hasRelationshipAttr.getVertex(Direction.IN);
          String couplingType = hasRelationshipAttr
              .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
          String attributeId = UtilClass.getCodeNew(attributeNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(IReferencedRelationshipProperty.ID, attributeId);
          propertyMap.put(IReferencedRelationshipProperty.COUPLING_TYPE, couplingType);
          propertyMap.put(IReferencedRelationshipProperty.TYPE, CommonConstants.ATTRIBUTE);
          propertyMapList.add(propertyMap);
        }
      }
    }
  }
  
  private static void populateTagsForRelationshipSide(Map<String, Object> relationshipSide,
      String side, Vertex relationshipNode) throws Exception
  {
    List<Map<String, Object>> propertyMapList = new ArrayList<>();
    relationshipSide.put(IRelationshipSide.TAGS, propertyMapList);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      if (KRSide.equals(side)) {
        
        Iterable<Edge> iterableTags = kRNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_RELATIONSHIP_TAG);
        for (Edge hasRelationshipTag : iterableTags) {
          Vertex attributeNode = hasRelationshipTag.getVertex(Direction.IN);
          String couplingType = hasRelationshipTag
              .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
          String tagId = UtilClass.getCodeNew(attributeNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(IReferencedRelationshipProperty.ID, tagId);
          propertyMap.put(IReferencedRelationshipProperty.COUPLING_TYPE, couplingType);
          propertyMap.put(IReferencedRelationshipProperty.TYPE, CommonConstants.TAG);
          propertyMapList.add(propertyMap);
        }
      }
    }
  }
  
  private static void populateInheritanceRelationshipsForRelationshipSide(
      Map<String, Object> relationshipSide, String side, Vertex relationshipNode)
  {
    List<Map<String, Object>> propertyMapList = new ArrayList<>();
    relationshipSide.put(IRelationshipSide.RELATIONSHIPS, propertyMapList);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    for (Vertex kRNode : krNodes) {
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      if (KRSide.equals(side)) {
        Iterable<Edge> inheritanceRelationships = kRNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP);
        for (Edge inheritanceRelationship : inheritanceRelationships) {
          Vertex relationshipToBeInheritedNode = inheritanceRelationship.getVertex(Direction.IN);
          String couplingType = inheritanceRelationship
              .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
          String relationshipId = UtilClass.getCodeNew(relationshipToBeInheritedNode);
          Map<String, Object> propertyMap = new HashMap<>();
          propertyMap.put(IReferencedRelationshipProperty.ID, relationshipId);
          propertyMap.put(IReferencedRelationshipProperty.COUPLING_TYPE, couplingType);
          propertyMap.put(IReferencedRelationshipProperty.CODE, relationshipId);
          propertyMap.put(IReferencedRelationshipProperty.TYPE, CommonConstants.RELATIONSHIP);
          propertyMapList.add(propertyMap);
        }
      }
    }
  }
  
  /**
   * link addedContext and remove link from deletedContext to KlassRelationship
   *
   * @author Lokesh
   * @param klassRelationshipNode
   * @param addedContextId
   * @param deletedContextIds
   * @throws Exception
   */
  private static void manageContext(Vertex klassRelationshipNode, String addedContextId,
      String deletedContextId) throws Exception
  {
    if (deletedContextId != null && !deletedContextId.equals("")) {
      String query = "Select From " + RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF
          + " Where in = " + klassRelationshipNode.getId() + "and out.code = '" + deletedContextId
          + "'";
      Iterable<Edge> edges = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      Iterator<Edge> iterator = edges.iterator();
      if (iterator.hasNext()) {
        Edge relationshipContextOf = iterator.next();
        relationshipContextOf.remove();
      }
    }
    if (addedContextId != null && !addedContextId.equals("")) {
      String query = "Select From " + RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF
          + " Where in = " + klassRelationshipNode.getId();
      Iterable<Edge> edges = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      Iterator<Edge> iterator = edges.iterator();
      // remove any existing context
      if (iterator.hasNext()) {
        Edge relationshipContextOf = iterator.next();
        relationshipContextOf.remove();
      }
      Vertex contextNode = UtilClass.getVertexById(addedContextId,
          VertexLabelConstants.VARIANT_CONTEXT);
      contextNode.addEdge(RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF,
          klassRelationshipNode);
    }
  }
  
  /**
   * fill Context(id) in sides of relationship(returnMap)
   *
   * @author Lokesh
   * @param relationshipNode
   * @param entityMap
   * @throws Exception
   */
  public static void fillContextInfo(Vertex relationshipNode, Map<String, Object> entityMap)
      throws Exception
  {
    String relationshipType = relationshipNode
        .getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
    if (relationshipType != null) {
      return;
    }
    Map<String, Object> side1 = (Map<String, Object>) entityMap.get(IRelationship.SIDE1);
    Map<String, Object> side2 = (Map<String, Object>) entityMap.get(IRelationship.SIDE2);
    
    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex kRNode : kRNodes) {
      String context = getContextId(kRNode);
      String KRSide = kRNode.getProperty(CommonConstants.SIDE_PROPERTY);
      if (KRSide.equals(CommonConstants.RELATIONSHIP_SIDE_2)) {
        side2.put(IRelationshipSide.CONTEXT_ID, context);
      }
      else {
        side1.put(IRelationshipSide.CONTEXT_ID, context);
      }
    }
  }
  
  /**
   * get contextId linked to klassRelationship
   *
   * @author Lokesh
   * @param klassRelationship
   * @return
   * @throws Exception
   */
  public static String getContextId(Vertex klassRelationship) throws Exception
  {
    
    Iterable<Vertex> vertices = klassRelationship.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIP_CONTEXT_OF);
    for (Vertex contextNode : vertices) {
      String id = contextNode.getProperty(CommonConstants.CODE_PROPERTY);
      return id;
    }
    return null;
  }
  
  public static String getContextIdForNatureRelationship(Vertex klassNatureRelationship)
      throws Exception
  {
    
    Iterable<Vertex> vertices = klassNatureRelationship.getVertices(Direction.OUT,
        RelationshipLabelConstants.VARIANT_CONTEXT_OF);
    for (Vertex contextNode : vertices) {
      String id = UtilClass.getCodeNew(contextNode);
      return id;
    }
    return null;
  }
  
  /**
   * Description :
   *
   * @author Ajit
   * @param relationshipSide
   * @param relationshipNode
   * @param relationshipSidePropertiesMap
   * @throws Exception
   */
  public static void populatePropertiesForRelationshipSide(Vertex kRNode,
      Map<String, Object> relationshipSidePropertiesMap) throws Exception
  {
    List<Map<String, Object>> attributeMapList = new ArrayList<>();
    List<Map<String, Object>> tagMapList = new ArrayList<>();
    List<Map<String, Object>> independentAttributeList = new ArrayList<>();
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.ATTRIBUTES,
        attributeMapList);
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.TAGS, tagMapList);
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.DEPENDENT_ATTRIBUTES,
        independentAttributeList);
    
    List<String> klassIds = new ArrayList<>();
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.KLASS_IDS, klassIds);
    Map<String, Object> relationshipSide = (Map<String, Object>) kRNode
        .getProperty(CommonConstants.RELATIONSHIP_SIDE);
    String targetType = (String) relationshipSide.get(CommonConstants.TARGET_TYPE);
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.TARGET_TYPE, targetType);
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.SIDE_ID,
        UtilClass.getCodeNew(kRNode));
    Iterable<Vertex> hasKPs = kRNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassNode : hasKPs) {
      klassIds.add(UtilClass.getCodeNew(klassNode));
    }
    Iterable<Vertex> hasNatureKPs = kRNode.getVertices(Direction.IN,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    for (Vertex klassNode : hasNatureKPs) {
      klassIds.add(UtilClass.getCodeNew(klassNode));
    }
    
    Iterable<Edge> iterableAttributes = kRNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE);
    for (Edge hasRelationshipAttr : iterableAttributes) {
      Vertex attributeNode = hasRelationshipAttr.getVertex(Direction.IN);
      String couplingType = hasRelationshipAttr
          .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
      if (couplingType.equals(CommonConstants.LOOSELY_COUPLED)
          || couplingType.equals(CommonConstants.READ_ONLY_COUPLED)) {
        continue;
      }
      String attributeId = UtilClass.getCodeNew(attributeNode);
      Map<String, Object> propertyMap = new HashMap<>();
      propertyMap.put(IReferencedRelationshipProperty.ID, attributeId);
      propertyMap.put(IReferencedRelationshipProperty.COUPLING_TYPE, couplingType);
      propertyMap.put(IReferencedRelationshipProperty.CODE, UtilClass.getCode(attributeNode));
      propertyMap.put(IReferencedRelationshipProperty.TYPE,
          attributeNode.getProperty(IAttribute.TYPE));
      propertyMap.put(IReferencedRelationshipProperty.PROPERTY_IID,
          UtilClass.getPropertyIID(attributeNode));
      Boolean isTranslatable = attributeNode.getProperty(IAttribute.IS_TRANSLATABLE);
      if (isTranslatable) {
        independentAttributeList.add(propertyMap);
      }
      else {
        attributeMapList.add(propertyMap);
      }
    }
    
    Iterable<Edge> iterableTags = kRNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_TAG);
    for (Edge hasRelationshipTag : iterableTags) {
      Vertex tagNode = hasRelationshipTag.getVertex(Direction.IN);
      String couplingType = hasRelationshipTag
          .getProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
      if (couplingType.equals(CommonConstants.LOOSELY_COUPLED)
          || couplingType.equals(CommonConstants.READ_ONLY_COUPLED)) {
        continue;
      }
      String tagId = UtilClass.getCodeNew(tagNode);
      
      Map<String, Object> propertyMap = new HashMap<>();
      propertyMap.put(IReferencedRelationshipProperty.ID, tagId);
      propertyMap.put(IReferencedRelationshipProperty.COUPLING_TYPE, couplingType);
      propertyMap.put(IReferencedRelationshipProperty.CODE, UtilClass.getCode(tagNode));
      propertyMap.put(IReferencedRelationshipProperty.TYPE, tagNode.getProperty(IAttribute.TYPE));
      propertyMap.put(IReferencedRelationshipProperty.PROPERTY_IID,
          UtilClass.getPropertyIID(tagNode));
      tagMapList.add(propertyMap);
    }
  }
  
  public static void populatePropertiesForRelationshipSideForGoldenRecord(Vertex kRNode,
      Map<String, Object> relationshipSidePropertiesMap) throws Exception
  {
    
    List<String> klassIds = new ArrayList<>();
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.KLASS_IDS, klassIds);
    Map<String, Object> relationshipSide = (Map<String, Object>) kRNode
        .getProperty(CommonConstants.RELATIONSHIP_SIDE);
    String targetType = (String) relationshipSide.get(CommonConstants.TARGET_TYPE);
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.TARGET_TYPE, targetType);
    relationshipSidePropertiesMap.put(IRelationshipSidePropertiesModel.SIDE_ID,
        UtilClass.getCodeNew(kRNode));
    Iterable<Vertex> hasKPs = kRNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassNode : hasKPs) {
      klassIds.add(UtilClass.getCodeNew(klassNode));
    }
    Iterable<Vertex> hasNatureKPs = kRNode.getVertices(Direction.IN,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    for (Vertex klassNode : hasNatureKPs) {
      klassIds.add(UtilClass.getCodeNew(klassNode));
    }
  }
  
  /**
   * Fixme: Ajit name for this API
   *
   * @param relationshipNode
   * @param relationshipPropertiesMap
   * @throws KlassNotFoundException
   * @throws Exception
   */
  public static void populatePropetiesInfoNew(Vertex relationshipNode,
      Map<String, Object> relationshipPropertiesMap) throws KlassNotFoundException, Exception
  {
    Map<String, Object> side1 = new HashMap<>();
    Map<String, Object> side2 = new HashMap<>();
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.SIDE1, side1);
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.SIDE2, side2);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    int count = 0;
    for (Vertex kRNode : krNodes) {
      if (count == 0) {
        populatePropertiesForRelationshipSide(kRNode, side1);
        count++;
      }
      else if (count == 1) {
        populatePropertiesForRelationshipSide(kRNode, side2);
      }
    }
  }
  
  public static void populatePropetiesInfoForGoldenRecord(Vertex relationshipNode,
      Map<String, Object> relationshipPropertiesMap) throws KlassNotFoundException, Exception
  {
    Map<String, Object> side1 = new HashMap<>();
    Map<String, Object> side2 = new HashMap<>();
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.SIDE1, side1);
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.SIDE2, side2);
    List<Vertex> krNodes = getKlassRelationshipNodesFromRelationshipNode(relationshipNode);
    int count = 0;
    for (Vertex kRNode : krNodes) {
      if (count == 0) {
        populatePropertiesForRelationshipSideForGoldenRecord(kRNode, side1);
        count++;
      }
      else if (count == 1) {
        populatePropertiesForRelationshipSideForGoldenRecord(kRNode, side2);
      }
    }
  }
  
  /**
   * Splits the list of properties into two separate lists, one for Side 1 &
   * other for Side 2
   *
   * @param properties
   * @return Map with keys 'side1' & 'side2'
   * @author Kshitij
   */
  private static Map<String, Object> splitListBasedOnSide(List<Map<String, Object>> properties)
  {
    List<Map<String, Object>> side1Properties = new ArrayList<>();
    List<Map<String, Object>> side2Properties = new ArrayList<>();
    for (Map<String, Object> property : properties) {
      String side = (String) property.get(IModifiedRelationshipPropertyModel.SIDE);
      if (side.equalsIgnoreCase(CommonConstants.RELATIONSHIP_SIDE_1)) {
        side1Properties.add(property);
      }
      else {
        side2Properties.add(property);
      }
    }
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(CommonConstants.RELATIONSHIP_SIDE_1, side1Properties);
    mapToReturn.put(CommonConstants.RELATIONSHIP_SIDE_2, side2Properties);
    return mapToReturn;
  }
  
  public static Vertex getNatureRelationshipNodeFromKNR(Vertex natureRelationshipKlassNode)
      throws RelationNotFoundException, MultipleLinkFoundException
  {
    Iterator<Vertex> natureRelationshipNodes = natureRelationshipKlassNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    if (!natureRelationshipNodes.hasNext()) {
      throw new RelationNotFoundException();
    }
    Vertex natureRelationship = natureRelationshipNodes.next();
    if (natureRelationshipNodes.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    return natureRelationship;
  }
  
  public static Map<String, Object> getRelationshipEntityMap(Vertex relationshipNode)
      throws Exception
  {
    Map<String, Object> relationshipEntityMap = UtilClass.getMapFromNode(relationshipNode);
    GetRelationshipUtils.prepareRelationshipSidesTranslations(relationshipEntityMap);
    RelationshipUtils.fillContextInfo(relationshipNode, relationshipEntityMap);
    RelationshipUtils.populatePropetiesInfo(relationshipNode, relationshipEntityMap);
    
    return relationshipEntityMap;
  }
  
  /**
   * @author Aayush
   * @param relationshipNode
   * @return relationship Map from Relationship Node
   * @throws Exception
   */
  public static HashMap<String, Object> getRelationshipMap(Vertex relationshipNode) throws Exception
  {
    HashMap<String, Object> relationshipMap = new HashMap<>();
    HashMap<String, Object> mapFromNode = UtilClass.getMapFromNode(relationshipNode);
    
    GetRelationshipUtils.prepareRelationshipSidesTranslations(mapFromNode);
    relationshipMap.putAll(mapFromNode);
    
    return relationshipMap;
  }
  
  /**
   * get all the relationship ids for klass or taxonomy ids
   *
   * @author Janak.Gurme
   * @param klassTaxonomyVertex
   * @return relationship ids to delete
   */
  public static List<String> getNonInheritedRelationshipIdsForTypes(List<String> typeIds)
  {
    List<String> relationshipIds = new ArrayList<>();
    
    Iterable<Vertex> iterable = RelationshipDBUtil
        .getNonInheritedRelationshipVerticesFromTypes(typeIds);
    for (Vertex vertex : iterable) {
      relationshipIds.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return relationshipIds;
  }
  
  /**
   * get All nature relationship ids for klass ids
   *
   * @author Janak.Gurme
   * @param klassVertex
   * @return
   */
  public static List<String> getNatureRelationshipIdsForTypes(List<String> typeIds)
  {
    List<String> natureRelationshipIds = new ArrayList<>();
    Iterable<Vertex> iterable = RelationshipDBUtil.getNatureRelationshipVerticesFromTypes(typeIds);
    for (Vertex vertex : iterable) {
      natureRelationshipIds.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    return natureRelationshipIds;
  }
  
  public static List<String> deleteRelationships(List<String> relationshipInstanceIds, List<Map<String, Object>> auditInfoList)
      throws Exception
  {
    Iterable<Vertex> relationshipVertices = UtilClass
        .getVerticesByIndexedIds(relationshipInstanceIds, VertexLabelConstants.ROOT_RELATIONSHIP);
    return deleteRelationships(relationshipVertices, new ArrayList<>(), new ArrayList<>(), auditInfoList);
  }
  
  public static List<String> deleteRelationships(Iterable<Vertex> relationshipNodes)
      throws Exception
  {
    return deleteRelationships(relationshipNodes, new ArrayList<>(), new ArrayList<>(), new ArrayList<Map<String,Object>>());
  }
  
  public static void getNatureRelationshipMapForRelationshipInheritance(
      Map<String, Object> referencedNatureRelationshipsMap, Vertex natureRelationshipNode)
      throws Exception
  {
    Set<String> klass2SideIds = new HashSet<String>();
    HashMap<String, Object> natureRelationshipMap = RelationshipUtils
        .getNatureRelationshipMap(natureRelationshipNode);
    Iterable<Vertex> side2KlassNodes = RelationshipRepository
        .getSide2KlassNodesOfNatureRelationship(natureRelationshipNode);
    
    for (Vertex side2KlassNode : side2KlassNodes) {
      klass2SideIds.add(UtilClass.getCodeNew(side2KlassNode));
    }
    Map<String, Object> side1 = (Map<String, Object>) natureRelationshipMap
        .get(IReferencedNatureRelationshipInheritanceModel.SIDE1);
    String klassId = (String) side1.get(IRelationshipSide.KLASS_ID);
    natureRelationshipMap.put(IReferencedNatureRelationshipInheritanceModel.SIDE1_KLASS_IDS,
        Arrays.asList(klassId));
    natureRelationshipMap.put(IReferencedNatureRelationshipInheritanceModel.SIDE2_KLASS_IDS,
        klass2SideIds);
    
    referencedNatureRelationshipsMap.put(
        (String) natureRelationshipMap.get(IReferencedNatureRelationshipInheritanceModel.ID),
        natureRelationshipMap);
  }
  
  public static void getNatureRelationshipSideKlassForRelationshipInheritance(
      Map<String, Object> referencedNatureRelationshipsMap, Vertex natureRelationshipNode)
      throws Exception
  {
    Set<String> klass1SideIds = new HashSet<>();
    Set<String> klass2SideIds = new HashSet<String>();
    HashMap<String, Object> natureRelationshipMap = new HashMap<>();
    
    /*    Iterable<Vertex> side1KlassNodes = RelationshipRepository.getSide1KlassNodesOfNatureRelationship(natureRelationshipNode);
    Iterable<Vertex> side2KlassNodes = RelationshipRepository.getSide2KlassNodesOfNatureRelationship(natureRelationshipNode);
    
    for (Vertex side1KlassNode : side1KlassNodes) {
      klass1SideIds.add(UtilClass.getCode(side1KlassNode));
    }
    
    for (Vertex side2KlassNode : side2KlassNodes) {
      klass2SideIds.add(UtilClass.getCode(side2KlassNode));
    }
    
    natureRelationshipMap.put(IReferencedNatureRelationshipInheritanceModel.SIDE1_KLASS_IDS, klass1SideIds);
    natureRelationshipMap.put(IReferencedNatureRelationshipInheritanceModel.SIDE2_KLASS_IDS, klass2SideIds);*/
    
    referencedNatureRelationshipsMap.put(UtilClass.getCodeNew(natureRelationshipNode),
        natureRelationshipMap);
  }
  
  public static void getRelationshipMapForRelationshipInheritance(
      Map<String, Object> referencedRelationshipsMap, String relationshipId) throws Exception
  {
    Vertex relationshipVertex = UtilClass.getVertexByIndexedId(relationshipId,
        VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    getRelationshipMapForRelationshipInheritance(relationshipVertex, referencedRelationshipsMap);
  }
  
  public static void getRelationshipMapForRelationshipInheritance(Vertex relationshipVertex,
      Map<String, Object> referencedRelationshipsMap) throws Exception
  {
    Map<String, Object> relationshipEntityMap = RelationshipUtils
        .getRelationshipEntityMap(relationshipVertex);
    
    Iterable<Vertex> KRVertices = relationshipVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex KRVertex : KRVertices) {
      List<String> sideKlassIds = new ArrayList<>();
      List<String> sideTaxonomyIds = new ArrayList<>();
      String side = KRVertex.getProperty(CommonConstants.SIDE_PROPERTY);
      Iterable<Vertex> klassVertices = KRVertex.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassVertex : klassVertices) {
        String entityId = klassVertex.getProperty(CommonConstants.CODE_PROPERTY);
        if (klassVertex.getProperty("@class")
            .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
          sideTaxonomyIds.add(entityId);
        }
        else {
          sideKlassIds.add(entityId);
        }
      }
      if (side.equals(CommonConstants.RELATIONSHIP_SIDE_1)) {
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE1_KLASS_IDS,
            sideKlassIds);
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE1_TAXONOMY_IDS,
            sideTaxonomyIds);
      }
      else {
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE2_KLASS_IDS,
            sideKlassIds);
        relationshipEntityMap.put(IReferencedRelationshipInheritanceModel.SIDE2_TAXONOMY_IDS,
            sideTaxonomyIds);
      }
    }
    referencedRelationshipsMap.put(UtilClass.getCodeNew(relationshipVertex), relationshipEntityMap);
  }
  
  public static void prepareRelationshipSideInfoForExport(Map<String, Object> relationshipMap)
  {
    
    Map<String, Object> old_side1 = (Map<String, Object>) relationshipMap
        .get(CommonConstants.RELATIONSHIP_SIDE_1);
    Map<String, Object> old_side2 = (Map<String, Object>) relationshipMap
        .get(CommonConstants.RELATIONSHIP_SIDE_2);
    
    relationshipMap.put(CommonConstants.RELATIONSHIP_SIDE_1, prepareSideInfoForExport(old_side1));
    relationshipMap.put(CommonConstants.RELATIONSHIP_SIDE_2, prepareSideInfoForExport(old_side2));
  }
  
  private static Map<String, Object> prepareSideInfoForExport(Map<String, Object> old_side)
  {
    
    Map<String, Object> new_side = new HashMap<String, Object>();
    
    new_side.put(CommonConstants.CODE_PROPERTY, old_side.get(CommonConstants.CODE_PROPERTY));
    new_side.put(CommonConstants.LABEL_PROPERTY, old_side.get(CommonConstants.LABEL_PROPERTY));
    new_side.put(KLASS_CODE, old_side.get(CommonConstants.RELATIONSHIP_SIDE_KLASSID));
    new_side.put(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE,
        old_side.get(CommonConstants.RELATIONSHIP_SIDE_ISVISIBLE));
    new_side.put(CommonConstants.CARDINALITY, old_side.get(CommonConstants.CARDINALITY));
    new_side.put(CONTEXT_CODE, old_side.get(CommonConstants.CONTEXT_ID));
    new_side.put(COUPLINGS, getCouplingFromSide(old_side));
    
    return new_side;
  }
  
  public static List<Map<String, Object>> getCouplingFromSide(Map<String, Object> side)
  {
    
    List<Map<String, Object>> couplingList = new ArrayList<Map<String, Object>>();
    
    List<Map<String, Object>> attributeList = (List<Map<String, Object>>) side
        .get(CommonConstants.ATTRIBUTES);
    List<Map<String, Object>> tagList = (List<Map<String, Object>>) side.get(CommonConstants.TAGS);
    List<Map<String, Object>> relationshipList = (List<Map<String, Object>>) side
        .get(CommonConstants.RELATIONSHIPS);
    
    attributeList = attributeList == null ? new ArrayList<>() : attributeList;
    tagList = tagList == null ? new ArrayList<>() : tagList;
    relationshipList = relationshipList == null ? new ArrayList<>() : relationshipList;
    
    couplingList.addAll(attributeList);
    couplingList.addAll(tagList);
    couplingList.addAll(relationshipList);
    
    return couplingList;
  }
  
  /**
   * Default Label handling for relationship side.
   * @param side
   */
  private static void updateDefaultLabelForSide(Map<String, Object> side)
  {
    String sideLabel = (String) side.get(IRelationshipSide.LABEL);
    if (sideLabel != null) {
      side.put("defaultLabel", sideLabel);
    }
  }
}

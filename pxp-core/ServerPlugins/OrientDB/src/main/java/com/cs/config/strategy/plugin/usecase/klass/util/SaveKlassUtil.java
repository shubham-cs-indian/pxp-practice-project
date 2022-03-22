package com.cs.config.strategy.plugin.usecase.klass.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.mutable.MutableBoolean;

import com.cs.config.strategy.plugin.usecase.relationship.AbstractSaveRelationship;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.IAddedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IAttributeDefaultValueCouplingTypeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IModifiedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementPermissionModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionPermissionModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionTagModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.config.interactor.model.relationship.ISaveRelationshipModel;
import com.cs.core.config.interactor.model.relationship.ISideInfoForRelationshipInheritanceModel;
import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IModifiedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagValuesModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.CoreConstant;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class SaveKlassUtil {
  
  private static List<String> getUtilizingKlassIdsForNewlyCreatedSectionPermissionNode(
      List<String> klassAndChildIds, Vertex modifiedSectionElement, String roleId)
  {
    List<String> allKlassIdsAttachedToModifiedSection = new ArrayList<>();
    List<String> copyOfKlassAndChildIds = new ArrayList<>();
    copyOfKlassAndChildIds.addAll(klassAndChildIds);
    Iterable<Vertex> klassNodes = modifiedSectionElement.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Vertex klassNode : klassNodes) {
      allKlassIdsAttachedToModifiedSection
          .add(klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    copyOfKlassAndChildIds.retainAll(allKlassIdsAttachedToModifiedSection);
    
    Iterable<Edge> permissionForEdges = modifiedSectionElement.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
    for (Edge permissionForEdge : permissionForEdges) {
      if (roleId.equals(permissionForEdge.getProperty(CommonConstants.ROLE_ID_PROPERY))) {
        copyOfKlassAndChildIds
            .removeAll(permissionForEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY));
      }
    }
    return copyOfKlassAndChildIds;
  }
  
  private static void createSectionPermissionNodeIfDoesntExists(List<String> klassAndChildIds,
      Vertex modifiedSection, String klassId, Map<String, Object> modifiedPermission,
      Vertex roleNode) throws Exception
  {
    String roleId = roleNode.getProperty(CommonConstants.CODE_PROPERTY);
    List<String> newklassAndChildIds = getUtilizingKlassIdsForNewlyCreatedSectionPermissionNode(
        klassAndChildIds, modifiedSection, roleId);
    // create section permission node
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_SECTION_PERMISSION, CommonConstants.CODE_PROPERTY);
    List<String> fieldsToExclude = Arrays.asList(IModifiedSectionPermissionModel.ROLE_ID);
    Vertex sectionPermissionNode = UtilClass.createNode(modifiedPermission, vertexType,
        fieldsToExclude);
    
    // add edge and set utilizingklassIds
    Edge newPermissionEdge = sectionPermissionNode
        .addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR, modifiedSection);
    newPermissionEdge.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
    newPermissionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
        newklassAndChildIds);
    newPermissionEdge.setProperty(CommonConstants.ROLE_ID_PROPERY, roleId);
    
    // link to role
    roleNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF, sectionPermissionNode);
  }
  
  private static void manageModifiedSectionPermission(List<String> klassAndChildIds,
      HashMap<String, Object> modifiedSectionMap, Vertex modifiedSection, String klassId)
      throws Exception
  {
    List<Map<String, Object>> modifiedPermissions = (List<Map<String, Object>>) modifiedSectionMap
        .get(IModifiedSectionModel.MODIFIED_PERMISSIONS);
    
    String owningKlassId = null;
    
    for (Map<String, Object> modifiedPermission : modifiedPermissions) {
      String roleId = (String) modifiedPermission
          .get(IModifiedSectionElementPermissionModel.ROLE_ID);
      Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      
      Edge permissionFor = KlassUtils
          .getPermissionForEdgeFromSectionRoleIdAndKlassId(modifiedSection, klassId, roleId);
      if (permissionFor == null) {
        createSectionPermissionNodeIfDoesntExists(klassAndChildIds, modifiedSection, klassId,
            modifiedPermission, roleNode);
        continue;
      }
      
      List<String> utilizingKlassIds = permissionFor
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      List<String> copyOfUtilizingKlassIds = new ArrayList<>();
      copyOfUtilizingKlassIds.addAll(utilizingKlassIds);
      owningKlassId = permissionFor.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      if (owningKlassId.equals(klassId)) {
        Vertex sectionPermissionNode = permissionFor.getVertex(Direction.OUT);
        List<String> fieldsToExclude = Arrays.asList(IModifiedSectionPermissionModel.ROLE_ID);
        UtilClass.saveNode(modifiedPermission, sectionPermissionNode, fieldsToExclude);
        sectionPermissionNode.setProperty(CommonConstants.ROLE_ID_PROPERY, roleId);
      }
      else {
        // cut off
        
        // create new node
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.ENTITY_TYPE_SECTION_PERMISSION, CommonConstants.CODE_PROPERTY);
        List<String> fieldsToExclude = Arrays.asList(IModifiedSectionPermissionModel.ROLE_ID);
        Vertex sectionPermissionNode = UtilClass.createNode(modifiedPermission, vertexType,
            fieldsToExclude);
        
        // create edge, set edge properties and link to the klass peoprrty node
        Edge newPermissionEdge = sectionPermissionNode
            .addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR, modifiedSection);
        newPermissionEdge.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
        
        // FIXME:
        copyOfUtilizingKlassIds.retainAll(klassAndChildIds);
        newPermissionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
            copyOfUtilizingKlassIds);
        newPermissionEdge.setProperty(CommonConstants.ROLE_ID_PROPERY, roleId);
        
        // update old utilizing klass ids
        utilizingKlassIds.removeAll(copyOfUtilizingKlassIds);
        
        permissionFor.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, utilizingKlassIds);
        
        // link with role
        roleNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF,
            sectionPermissionNode);
      }
    }
  }
  
  public static void manageModifiedSections(List<String> klassAndChildIds,
      List<HashMap<String, Object>> modifiedSections, Vertex klassNode, String vertexLabel)
      throws Exception
  {
    for (HashMap<String, Object> modifiedSectionMap : modifiedSections) {
      String modifiedSectionId = (String) modifiedSectionMap.get(ISection.ID);
      Vertex sectionNode = UtilClass.getVertexById(modifiedSectionId,
          VertexLabelConstants.ENTITY_TYPE_KLASS_SECTION);
      
      if ((Boolean) modifiedSectionMap.get(IModifiedSectionModel.IS_MODIFIED)) {
        
        Boolean isSkipped = (Boolean) modifiedSectionMap.get(ISection.IS_SKIPPED);
        Edge sectionOf = getConnectingSectionOfEdge(klassNode, sectionNode);
        sectionOf.setProperty(ISection.IS_SKIPPED, isSkipped);
      }
      manageModifiedSectionPermission(klassAndChildIds, modifiedSectionMap, sectionNode,
          klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static Edge getConnectingSectionOfEdge(Vertex klassNode, Vertex sectionNode)
  {
    String sectionId = UtilClass.getCodeNew(sectionNode);
    Iterable<Edge> edges = klassNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    Iterator<Edge> iterator = edges.iterator();
    while (iterator.hasNext()) {
      Edge edge = iterator.next();
      Vertex otherSectionNode = edge.getVertex(Direction.OUT);
      if (otherSectionNode.getProperty(CommonConstants.CODE_PROPERTY)
          .equals(sectionId)) {
        return edge;
      }
    }
    return null;
  }
  
  /*private static void shiftSAnySectionNodeToZeroPosition(Vertex firstSectionNode, Vertex sectionNode, String klassId,List<String> klassAndChildIds)
  {
    Iterable<Edge> nextSectionEdgesOfFirstSectionNode = firstSectionNode.getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION);
    for (Edge nextSectionEdgeOfFirstSectionNode : nextSectionEdgesOfFirstSectionNode) {
      List<String> nextUtilizingKlassIdsOfFirstSectionNode = nextSectionEdgeOfFirstSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if(nextUtilizingKlassIdsOfFirstSectionNode.contains(klassId)) {
        List<String> actualUtilizingKlassIds = new ArrayList<>(nextUtilizingKlassIdsOfFirstSectionNode);
        actualUtilizingKlassIds.retainAll(klassAndChildIds);
        Vertex previousSectionNodeOfCurrentSectionNode = null;
        Iterable<Edge> previousSectionEdgesOfSectionNode = sectionNode.getEdges(Direction.OUT, RelationshipLabelConstants.PREVIOUS_SECTION);
        for (Edge previousSectionEdgeOfSectionNode : previousSectionEdgesOfSectionNode) {
          List<String> previousUtilizingKlassIdsOfSectionNode = previousSectionEdgeOfSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
          if(previousUtilizingKlassIdsOfSectionNode.contains(klassId)){
            previousSectionNodeOfCurrentSectionNode = previousSectionEdgeOfSectionNode.getVertex(Direction.IN);
            List<String> copyOfPreviousUtilizingKlassIds = new ArrayList<String>(previousUtilizingKlassIdsOfSectionNode);
            copyOfPreviousUtilizingKlassIds.removeAll(actualUtilizingKlassIds);
            if(copyOfPreviousUtilizingKlassIds.size() == 0){
              previousSectionEdgeOfSectionNode.remove();
            }
            else {
              previousSectionEdgeOfSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, copyOfPreviousUtilizingKlassIds);
            }
          }
        }
        Vertex nextSectionNodeOfCurrentSectionNode = null;
        Iterable<Edge> nextSectionEdgesOfSectionNode = sectionNode.getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION);
        for (Edge nextSectionEdgeOfSectionNode : nextSectionEdgesOfSectionNode) {
          List<String> nextUtilizingKlassIdsOfSectionNode = nextSectionEdgeOfSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
          if(nextUtilizingKlassIdsOfSectionNode.contains(klassId)){
            nextSectionNodeOfCurrentSectionNode = nextSectionEdgeOfSectionNode.getVertex(Direction.OUT);
            List<String> copyOfNextUtilizingKlassIds = new ArrayList<String>(nextUtilizingKlassIdsOfSectionNode);
            copyOfNextUtilizingKlassIds.removeAll(actualUtilizingKlassIds);
            if(copyOfNextUtilizingKlassIds.size() == 0){
              nextSectionEdgeOfSectionNode.remove();
            }
            else {
              nextSectionEdgeOfSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, copyOfNextUtilizingKlassIds);
            }
          }
          else {
            for (String actualKlassId : actualUtilizingKlassIds) {
              if(nextUtilizingKlassIdsOfSectionNode.contains(actualKlassId)){
                Vertex nextSectionNodeOfCurrentSectionForChildren = nextSectionEdgeOfSectionNode.getVertex(Direction.OUT);
                Edge newEdgeForChildren =nextSectionNodeOfCurrentSectionForChildren.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION, firstSectionNode);
                newEdgeForChildren.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, nextUtilizingKlassIdsOfSectionNode);
                newEdgeForChildren.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, actualKlassId);
                nextSectionEdgeOfSectionNode.remove();
                break;
              }
            }
          }
        }
        if (actualUtilizingKlassIds.size() != 0 && !firstSectionNode.equals(sectionNode)) {
          Edge newEdgeBetweenFirstNodeAndSectionNode = firstSectionNode.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION, sectionNode);
          newEdgeBetweenFirstNodeAndSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, actualUtilizingKlassIds);
          newEdgeBetweenFirstNodeAndSectionNode.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
          if (nextSectionNodeOfCurrentSectionNode != null && previousSectionNodeOfCurrentSectionNode != null && !previousSectionNodeOfCurrentSectionNode.equals(nextSectionNodeOfCurrentSectionNode)) {
            Edge newEdgeBetweenNextSectionNodeAndPreviousSectionNode = nextSectionNodeOfCurrentSectionNode.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION,previousSectionNodeOfCurrentSectionNode);
            newEdgeBetweenNextSectionNodeAndPreviousSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, actualUtilizingKlassIds);
            newEdgeBetweenNextSectionNodeAndPreviousSectionNode.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
          }
        }
      }
    }
  }*/
  
  /*private static void manageSequenceChangeOfKlass(String klassId, Integer sequence,
      Vertex firstSectionNode, int currentSectionPostion, Vertex sectionNode,List<String> klassAndChildIds, String vertexLabel) throws Exception
  {
    if(sequence == 0) {
      shiftSAnySectionNodeToZeroPosition(firstSectionNode, sectionNode, klassId, klassAndChildIds);
    }
    else{
      Vertex destinedPreviousNode = null;
      Vertex destinedNextNode = null;
      destinedPreviousNode = KlassUtils.getPreviousSectionNode(sequence, destinedPreviousNode, firstSectionNode, klassId);
      destinedNextNode = KlassUtils.getNextSectionNode(klassId, destinedPreviousNode, destinedNextNode);
      if(destinedNextNode != null){
      shiftAnySectionNodeToAnyOtherPositionOtherThenZeroPoistion(destinedPreviousNode, destinedNextNode, sectionNode, klassId, klassAndChildIds, vertexLabel);
      }
    }
  }*/
  
  /*  private static List<String> updatePreviousAndGetDestinedIncomingUtilizingKlassId(Vertex destinedNextNode,
      Vertex sectionNode, String klassId, List<String> klassAndChildIds, String vertexLabel) throws Exception
  {
    Iterable <Edge> sectionEdgesIncomingIntoDestinedNextNode = destinedNextNode.getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION);
    List<String> utilizingKlassIdsFromSectionNodeToDestinedNextNode = new ArrayList<>(); // this variable is used to decide weather to delete edge between nextNode and its previousNode where the edge contains klassId
    for (Edge sectionEdgeIncomingIntoDestinedNextNode : sectionEdgesIncomingIntoDestinedNextNode) {
      List<String> copyOfKlassAndChildIds = new ArrayList<>(klassAndChildIds);
      copyOfKlassAndChildIds.remove(klassId);
      String owningKlassId = sectionEdgeIncomingIntoDestinedNextNode.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      if(copyOfKlassAndChildIds.contains(owningKlassId)){
        Vertex otherSectionNode = sectionEdgeIncomingIntoDestinedNextNode.getVertex(Direction.OUT);
        if(!otherSectionNode.equals(sectionNode)){
          List<String> utilizingKlassIds = sectionEdgeIncomingIntoDestinedNextNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
          utilizingKlassIdsFromSectionNodeToDestinedNextNode.addAll(utilizingKlassIds);
        }
      }
    }
    Iterable<Edge> sectionEdgesBetweenDestinedNodes = destinedNextNode.getEdges(Direction.OUT, RelationshipLabelConstants.PREVIOUS_SECTION);
    List<String> destinedIncomingUtilizingKlassIds = new ArrayList<>();
    for (Edge sectionEdgeBetweenDestinedNodes : sectionEdgesBetweenDestinedNodes) { // iterating over all out going edges to find the edge where utilizing klassId contains klassId
      List<String> utilizingKlassIdsOfPreviousSection = sectionEdgeBetweenDestinedNodes.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if(utilizingKlassIdsOfPreviousSection.contains(klassId)) {
        destinedIncomingUtilizingKlassIds.addAll(utilizingKlassIdsOfPreviousSection); // add all utilizing klassIds to actualPreviousUtilizingKlassIds
        destinedIncomingUtilizingKlassIds.retainAll(klassAndChildIds); // only retain klass And child ids , eg utilizingKlassIds = k1,k2,k3,k4 klassId = k2 and klassAndChildIds = k2,k3,k3,k4
  
        //get all out going PreviousSection edges from SectionNode
        Iterable<Edge> sourcePreviousSectionEdgesFromSectionNode = sectionNode.getEdges(Direction.OUT, RelationshipLabelConstants.PREVIOUS_SECTION);
        for (Edge sourcePreviousSectionEdge : sourcePreviousSectionEdgesFromSectionNode) {
          String tempOwningKlassId = sourcePreviousSectionEdge.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
          if(klassAndChildIds.contains(tempOwningKlassId) && !tempOwningKlassId.equals(klassId)){ // check if edges out from section node have sections that are only in child and not in parent
            List<String> tempUtilizingKlassIds = sourcePreviousSectionEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
            utilizingKlassIdsFromSectionNodeToDestinedNextNode.addAll(tempUtilizingKlassIds);
          }
          if(klassAndChildIds.contains(tempOwningKlassId) && tempOwningKlassId.equals(klassId)){
            List<String> tempUtilizingKlassIds = sourcePreviousSectionEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
            HashSet<String> copyUtilizingKlassIdsOfPreviousSection = new HashSet<>(utilizingKlassIdsOfPreviousSection);
            copyUtilizingKlassIdsOfPreviousSection.removeAll(tempUtilizingKlassIds);
            utilizingKlassIdsFromSectionNodeToDestinedNextNode.addAll(copyUtilizingKlassIdsOfPreviousSection);
          }
        }
        Set<String> copyOfKlassAndChildIds = new HashSet<>(klassAndChildIds);
        copyOfKlassAndChildIds.removeAll(utilizingKlassIdsFromSectionNodeToDestinedNextNode); // remove all klass Ids from klass And childIds where child have different edge
        utilizingKlassIdsOfPreviousSection.removeAll(copyOfKlassAndChildIds); // remove only those klassIds that are actually inheriting
        if(utilizingKlassIdsOfPreviousSection.size() == 0) {
          sectionEdgeBetweenDestinedNodes.remove();
        }
        else{
          String newOwningKlassId = getOwningKlassIdsUnderAmbigousSceanario(utilizingKlassIdsOfPreviousSection, vertexLabel); //check for ambigousSceanario if there are two klasses left in utilizingKlassId which one to be owning klassId
          if(newOwningKlassId == null && utilizingKlassIdsOfPreviousSection.size() == 1){
            newOwningKlassId = utilizingKlassIdsOfPreviousSection.get(0); //if size of utilizingKlassIds = 1 then same become the owning klassId
          }
          if(!utilizingKlassIdsOfPreviousSection.contains(klassId) && newOwningKlassId != null){
            destinedIncomingUtilizingKlassIds.removeAll(utilizingKlassIdsOfPreviousSection); //remove utilizing klassIds from actualKlassIds
            sectionEdgeBetweenDestinedNodes.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, newOwningKlassId);
          }
          sectionEdgeBetweenDestinedNodes.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, utilizingKlassIdsOfPreviousSection);
        }
      }
    }
    return destinedIncomingUtilizingKlassIds;
  }*/
  
  /*  private static String getOwningKlassIdsUnderAmbigousSceanario(
      List<String> utilizingKlassIdsOfPreviousSection, String vertexLabel) throws Exception
  {
    String returnOwningKlassId = null;
    List<String> copyOfUtilizingKlassIds = new ArrayList<>(utilizingKlassIdsOfPreviousSection);
    for (String utilizingKlassId : utilizingKlassIdsOfPreviousSection) {
      Vertex klassNode = UtilClass.getVertexById(utilizingKlassId,
          vertexLabel);
      Iterable<Edge> childOfEdges = klassNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      for (Edge childOfEdge : childOfEdges) {
        Vertex parentNode = childOfEdge.getVertex(Direction.IN);
        String parentId = parentNode.getProperty(CommonConstants.CODE_PROPERTY);
        if(!copyOfUtilizingKlassIds.contains(parentId)){
          returnOwningKlassId = utilizingKlassId;
        }
      }
    }
    return returnOwningKlassId;
  }*/
  
  /*  private static List<String> getDestinedOutgoingUtilizingKlassIds(Vertex destinedNextNode,
      String klassId, List<String> klassAndChildIds)
  {
    Iterable<Edge> nextSectionEdgesOfNextNode = destinedNextNode.getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION);
    List<String> destinedOutgoingUtilizingKlassIds = new ArrayList<>();
    for (Edge nextSectionEdgeOfSectionNode : nextSectionEdgesOfNextNode) {
      List<String> utilizingKlassIdsOfNextSection = nextSectionEdgeOfSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if(utilizingKlassIdsOfNextSection.contains(klassId)) {
        destinedOutgoingUtilizingKlassIds.addAll(utilizingKlassIdsOfNextSection);
        destinedOutgoingUtilizingKlassIds.retainAll(klassAndChildIds);
        utilizingKlassIdsOfNextSection.removeAll(klassAndChildIds);
        if(utilizingKlassIdsOfNextSection.size() == 0){
          nextSectionEdgeOfSectionNode.remove();
        }
        else{
          nextSectionEdgeOfSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, utilizingKlassIdsOfNextSection);
        }
      }
    }
    return destinedOutgoingUtilizingKlassIds;
  }*/
  
  /*  private static List<String> getKlassIdsToRemoveFromEdgeBetweenNextOfSectionNodeAndDestinedNextNode(
      Vertex sectionNode, String klassId, List<String> klassAndChildIds,
      Iterable<Edge> nextSectionEdgesOfSectionNode)
  {
    List<String> copyOfKlassAndChildIdsWithHavingOwningKlassIds = new ArrayList<>(klassAndChildIds);
    copyOfKlassAndChildIdsWithHavingOwningKlassIds.remove(klassId);
    List<String> utilizingKlassIdsToRemove = new ArrayList<>();
    //Iterate over all nextSectionEdgesFromSectionNode and to calculate utilizingKlassIdsToRemove when creating edge between nextOfSectionNode and nextNode
    for (Edge nextSectionEdgeOfSectionNode : nextSectionEdgesOfSectionNode) {
      String owningKlassIdOfNextSectionNode = nextSectionEdgeOfSectionNode.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      if(copyOfKlassAndChildIdsWithHavingOwningKlassIds.contains(owningKlassIdOfNextSectionNode)){
        List<String> utilizingKlassIdsOfNextSectionNode = nextSectionEdgeOfSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        utilizingKlassIdsToRemove.addAll(utilizingKlassIdsOfNextSectionNode);
      }
    }
    // remove all utilizingKlassIdsThat are Common in previousSectionEdges
    Iterable<Edge> previousSectionEdgesOfSectionNode = sectionNode.getEdges(Direction.OUT, RelationshipLabelConstants.PREVIOUS_SECTION);
    for (Edge previousSectionEdgeOfSectionNode : previousSectionEdgesOfSectionNode) {
      String owningKlassIdOfPreviousSectioNode = previousSectionEdgeOfSectionNode.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      if(copyOfKlassAndChildIdsWithHavingOwningKlassIds.contains(owningKlassIdOfPreviousSectioNode)){
        List<String> utilizingKlassIdsOfPreviousSectionNode = previousSectionEdgeOfSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        utilizingKlassIdsToRemove.removeAll(utilizingKlassIdsOfPreviousSectionNode);
      }
    }
    return utilizingKlassIdsToRemove;
  }*/
  
  /*private static void shiftAnySectionNodeToAnyOtherPositionOtherThenZeroPoistion(
      Vertex destinedPreviousNode, Vertex destinedNextNode, Vertex sectionNode, String klassId,
      List<String> klassAndChildIds, String vertexLabel) throws Exception
  {
    List<String> destinedIncomingUtilizinKlassIds = updatePreviousAndGetDestinedIncomingUtilizingKlassId(
        destinedNextNode, sectionNode, klassId, klassAndChildIds, vertexLabel);
  
    List<String> destinedOutgoingUtilizingKlassIds = getDestinedOutgoingUtilizingKlassIds(
        destinedNextNode, klassId, klassAndChildIds);
  
    Iterable<Edge> nextSectionEdgesOfSectionNode = sectionNode.getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION);
  
    List<String> utilizingKlassIdsToRemove = getKlassIdsToRemoveFromEdgeBetweenNextOfSectionNodeAndDestinedNextNode(
        sectionNode, klassId, klassAndChildIds, nextSectionEdgesOfSectionNode);
  
    //Logic for swaping sections and creating new Edges
    for (Edge nextSectionEdgeOfSectionNode : nextSectionEdgesOfSectionNode) {
      List<String> utilizingKlassIdsOfNextSectionNode = nextSectionEdgeOfSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      String owningKlassIdOfNextSectionNode = nextSectionEdgeOfSectionNode.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      if(utilizingKlassIdsOfNextSectionNode.contains(klassId)){
        utilizingKlassIdsOfNextSectionNode.removeAll(destinedOutgoingUtilizingKlassIds);
        if(utilizingKlassIdsOfNextSectionNode.size() == 0) {
          nextSectionEdgeOfSectionNode.remove();
        }
        else {
          nextSectionEdgeOfSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, utilizingKlassIdsOfNextSectionNode);
        }
        Vertex nextOfSectionNode = nextSectionEdgeOfSectionNode.getVertex(Direction.OUT);
        if (destinedOutgoingUtilizingKlassIds.size() != 0 && !nextOfSectionNode.equals(destinedNextNode)) {
          List<String> copyOfactualNextUtilizingKlassIds = new ArrayList<>(destinedOutgoingUtilizingKlassIds);
          copyOfactualNextUtilizingKlassIds.removeAll(utilizingKlassIdsToRemove);
          Edge newEdgeBetweenNextNodeAndNextSectionOfSectionNode = nextOfSectionNode.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION, destinedNextNode);
          newEdgeBetweenNextNodeAndNextSectionOfSectionNode.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
          newEdgeBetweenNextNodeAndNextSectionOfSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, copyOfactualNextUtilizingKlassIds);
        }
      }
      Vertex nextOfSectionNode = nextSectionEdgeOfSectionNode.getVertex(Direction.OUT);
      if(utilizingKlassIdsToRemove.contains(owningKlassIdOfNextSectionNode) && !nextOfSectionNode.equals(destinedNextNode)){
        Edge newEdgeBetweenNextNodeAndNextSectionOfSectionNode = nextOfSectionNode.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION, destinedNextNode);
        newEdgeBetweenNextNodeAndNextSectionOfSectionNode.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, nextSectionEdgeOfSectionNode.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY));
        newEdgeBetweenNextNodeAndNextSectionOfSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, nextSectionEdgeOfSectionNode.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY));
        nextSectionEdgeOfSectionNode.remove();
      }
    }
    if(destinedIncomingUtilizinKlassIds.size() != 0 && !destinedNextNode.equals(sectionNode)){
      Edge newEdgeBetweenNextAndSectionNode = destinedNextNode.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION, sectionNode);
      newEdgeBetweenNextAndSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, destinedIncomingUtilizinKlassIds);
      newEdgeBetweenNextAndSectionNode.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
    }
    if(destinedOutgoingUtilizingKlassIds.size() != 0 && !sectionNode.equals(destinedPreviousNode)) {
      Edge newEdgeBetweenPreviousAndSectionNode = sectionNode.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION, destinedPreviousNode);
      newEdgeBetweenPreviousAndSectionNode.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, destinedOutgoingUtilizingKlassIds);
      newEdgeBetweenPreviousAndSectionNode.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
    }
  }*/
  
  public static void manageTaxonomyInKlass(Vertex klassNode, Map<String, Object> klassADM)
  {
    Map<String, Object> parentKlassMap = (Map<String, Object>) klassADM
        .get(CommonConstants.PARENT_PROPERTY);
    
    if ((parentKlassMap != null)) {
      String parentId = (String) parentKlassMap.get(CommonConstants.ID_PROPERTY);
      
      if (parentId.equals("-1")) {
        Iterator<Edge> edgeIterator = klassNode
            .getEdges(Direction.IN,
                RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF)
            .iterator();
        Vertex taxonomyNode = null;
        Edge taxonomyOf = null;
        while (edgeIterator.hasNext()) {
          taxonomyOf = edgeIterator.next();
          taxonomyNode = taxonomyOf.getVertex(Direction.OUT);
        }
        
        boolean isEnforcedTaxonomy = (boolean) klassADM
            .get(CommonConstants.IS_ENFORCED_TAXONOMY_PROPERTY);
        taxonomyNode.setProperty(CommonConstants.IS_ENFORCED_TAXONOMY_PROPERTY, isEnforcedTaxonomy);
      }
    }
  }
  
  public static void manageDeletedSections(Vertex klassNode, List<String> deletedSectionIds,
      List<String> klassAndChildIds, String vertexLabel, List<Vertex> klassAndChildNodes,
      Map<String, List<String>> deletedPropertyMap, Map<String, Object> propertiesADMMap)
      throws Exception
  {
    Set<Vertex> nodesToDelete = new HashSet<>();
    Set<Edge> relationshipsToDelete = new HashSet<>();
    for (String deletedSectionId : deletedSectionIds) {
      Vertex existingSectionNode = UtilClass.getVertexById(deletedSectionId,
          VertexLabelConstants.ENTITY_TYPE_KLASS_SECTION);
      Edge sectionOfRelationship = KlassUtils.getSectionOfEdgeFromKlassAndSection(klassNode,
          existingSectionNode);
      String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
      KlassUtils.deleteSectionFromKlass(sectionOfRelationship, existingSectionNode,
          klassAndChildIds, klassId, nodesToDelete, relationshipsToDelete, false, vertexLabel,
          deletedPropertyMap);
      
      Iterator<Vertex> iterator = existingSectionNode
          .getVertices(Direction.IN, RelationshipLabelConstants.PROPERTY_COLLECTION_OF)
          .iterator();
      if (!iterator.hasNext()) {
        throw new PropertyCollectionNotFoundException();
      }
      Vertex propertyCollectionNode = iterator.next();
      /*
      Vertex templateNode = TemplateUtils.getTemplateFromKlass(klassNode);
      Edge hasPropertyCollection = TemplateUtils.getEdgeBetweenTemplateAndPropertyCollection(templateNode, propertyCollectionNode);
      hasPropertyCollection.remove();
      KlassUtils.updateTemplateSequenceNode(templateNode, UtilClass.getCode(propertyCollectionNode), childNodes, -2, RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE);
      */
      // increment version of child Nodes
      List<Vertex> childNodes = KlassUtils.getChildKlassNodesConnectedToSection(existingSectionNode,
          klassNode, klassAndChildNodes);
      for (Vertex vertex : childNodes) {
        UtilClass.addNodesForVersionIncrement(vertex);
      }
    }
    
    for (Edge relationship : relationshipsToDelete) {
      relationship.remove();
    }
    for (Vertex node : nodesToDelete) {
      checkIdentifierAttributeNodeInDelete(propertiesADMMap, node);
      node.remove();
    }
  }
  
  private static void checkIdentifierAttributeNodeInDelete(Map<String, Object> propertiesADMMap,
      Vertex node)
  {
    if (!node.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
        .equals(VertexLabelConstants.KLASS_ATTRIBUTE)) {
      return;
    }
    Boolean isIdentifier = (Boolean) node.getProperty(ISectionAttribute.IS_IDENTIFIER);
    if (isIdentifier == null || !isIdentifier) {
      return;
    }
    propertiesADMMap
        .put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, true);
  }
  
  public static void manageSectionsInKlass(Vertex klassNode, Map<String, Object> klassADMMap,
      List<Vertex> klassAndChildNodes, String vertexLabel,
      List<Map<String, Object>> defaultValueChangeList,
      Map<String, List<String>> deletedPropertyMap, Map<String, Object> propertiesADMMap,
      Map<String, Object> removedAttributeVariantContexts, List<Long> mandatoryPropertyUpdatedIIDs,
      List<Long> propertyIIDsToEvaluateProductIdentifier, List<Long> propertyIIDsToRemoveProductIdentifier,
      List<String> addedCalculatedAttributeIds)
      throws Exception
  {
    List<String> addedSectionIds = new ArrayList<String>();
    
    List<HashMap<String, Object>> modifiedSections = (List<HashMap<String, Object>>) klassADMMap.remove(IKlassSaveModel.MODIFIED_SECTIONS);
    List<String> deletedSectionIds = (List<String>) klassADMMap.remove(IKlassSaveModel.DELETED_SECTIONS);
    List<HashMap<String, Object>> addedSections = (List<HashMap<String, Object>>) klassADMMap.remove(IKlassSaveModel.ADDED_SECTIONS);
    
    List<String> klassAndChildIds = new ArrayList<>();
    for (Vertex inheritanceNode : klassAndChildNodes) {
      klassAndChildIds.add((String) inheritanceNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    List<HashMap<String, Object>> modifiedElements = (List<HashMap<String, Object>>) klassADMMap
        .remove(IKlassSaveModel.MODIFIED_ELEMENTS);
    
    SaveKlassUtil.manageDeletedSections(klassNode, deletedSectionIds, klassAndChildIds, vertexLabel,
        klassAndChildNodes, deletedPropertyMap, propertiesADMMap);
    SaveKlassUtil.manageModifiedSections(klassAndChildIds, modifiedSections, klassNode,
        vertexLabel);
    SaveKlassUtil.manageModifiedSectionElements(klassNode, modifiedElements, klassAndChildIds,
        defaultValueChangeList, vertexLabel, propertiesADMMap, removedAttributeVariantContexts, mandatoryPropertyUpdatedIIDs,
        propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier);
    
    // Added Sections
    KlassUtils.createAndLinkSectionsToKlass(klassNode, addedSections, klassAndChildNodes,
        addedSectionIds, defaultValueChangeList, vertexLabel, addedCalculatedAttributeIds);
    
    Set<Vertex> nodesForVersionIncrement = UtilClass.getNodesForVersionIncrement();
    if (nodesForVersionIncrement != null) {
      for (Vertex vertex : nodesForVersionIncrement) {
        UtilClass.incrementVersionIdOfNode(vertex);
      }
    }
  }
  
  private static void manageKlassTaxanomyForNewlyCreatedKlassProperty(Vertex modifiedSectionElement,
      Vertex duplicateSectionElementNode)
  {
    Iterable<Edge> filterableTagSectionElementLinks = modifiedSectionElement.getEdges(Direction.IN,
        RelationshipLabelConstants.FILTERABLE_TAG_SECTION_ELEMENT_LINK);
    for (Edge filterableTagSectionElementLink : filterableTagSectionElementLinks) {
      Vertex filterableTagNode = filterableTagSectionElementLink.getVertex(Direction.OUT);
      filterableTagNode.addEdge(RelationshipLabelConstants.FILTERABLE_TAG_SECTION_ELEMENT_LINK,
          duplicateSectionElementNode);
    }
    
    Iterable<Edge> filterableAttributeSectionElementLinks = modifiedSectionElement.getEdges(
        Direction.IN, RelationshipLabelConstants.FILTERABLE_ATTRIBUTE_SECTION_ELEMENT_LINK);
    for (Edge filterableAttributeSectionElementLink : filterableAttributeSectionElementLinks) {
      Vertex filterableAttributeNode = filterableAttributeSectionElementLink
          .getVertex(Direction.OUT);
      filterableAttributeNode.addEdge(
          RelationshipLabelConstants.FILTERABLE_ATTRIBUTE_SECTION_ELEMENT_LINK,
          duplicateSectionElementNode);
    }
    
    Iterable<Edge> sortableAttributeSectionElementLinks = modifiedSectionElement
        .getEdges(Direction.IN, RelationshipLabelConstants.SORTABLE_ATTRIBUTE_SECTION_ELEMENT_LINK);
    for (Edge SortableAttributeSectionElementLink : sortableAttributeSectionElementLinks) {
      Vertex sortableAttributeNode = SortableAttributeSectionElementLink.getVertex(Direction.OUT);
      sortableAttributeNode.addEdge(
          RelationshipLabelConstants.SORTABLE_ATTRIBUTE_SECTION_ELEMENT_LINK,
          duplicateSectionElementNode);
    }
    
    Iterable<Edge> defaultFilterSectionElementLinks = modifiedSectionElement.getEdges(Direction.IN,
        RelationshipLabelConstants.DEFAULT_FILTER_SECTION_ELEMENT_LINK);
    for (Edge defaultFilterSectionElementLink : defaultFilterSectionElementLinks) {
      Vertex defaultFilterNode = defaultFilterSectionElementLink.getVertex(Direction.OUT);
      defaultFilterNode.addEdge(RelationshipLabelConstants.DEFAULT_FILTER_SECTION_ELEMENT_LINK,
          duplicateSectionElementNode);
    }
  }
  
  public static Vertex manageModifiedKlassProperty(Vertex klassNode, List<String> klassAndChildIds,
      HashMap<String, Object> modifiedElement, Vertex modifiedSectionElement, String klassId,
      List<Map<String, Object>> defaultValueChangeList, String vertexLabel, Map<String, Object> removedAttributeVariantContexts,
      List<Long> mandatoryPropertyUpdatedIIDs, List<Long> propertyIIDsToEvaluateProductIdentifier,
      List<Long> propertyIIDsToRemoveProductIdentifier) throws Exception
  {
    Edge hasKlassPropertyEdge = KlassUtils
        .getHasKlassPropertyEdgeFromKlassPropertyAndKLass(modifiedSectionElement, klassId);
    Boolean isInherited = hasKlassPropertyEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
    List<String> fieldsToExclude = Arrays.asList(IModifiedSectionElementModel.MODIFIED_PERMISSION,
        IModifiedSectionElementModel.IS_MODIFIED,
        IModifiedSectionElementModel.MODIFIED_NOTIFICATION,
        IModifiedSectionTagModel.ADDED_DEFAULT_VALUES,
        IModifiedSectionTagModel.MODIFIED_DEFAULT_VALUES,
        IModifiedSectionTagModel.DELETED_DEFAULT_VALUES, IModifiedSectionAttributeModel.CONTEXT,
        IModifiedSectionAttributeModel.ATTRIBUTE_VARIANT_CONTEXT,
        IModifiedSectionTagModel.ADDED_SELECTED_TAG_VALUES,
        IModifiedSectionTagModel.DELETED_SELECTED_TAG_VALUES);
    Vertex sectionElementToReturn;
    
    // check if default value is being changed
    if (defaultValueChangeList != null) {
      checkDefaultValueChanged(modifiedElement, modifiedSectionElement, defaultValueChangeList,
          vertexLabel, klassAndChildIds, mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier, 
          propertyIIDsToRemoveProductIdentifier);
    }
    
    if (!isInherited) {
      updateDefaultTagValueNodes(modifiedSectionElement, modifiedElement);
      UtilClass.saveNode(modifiedElement, modifiedSectionElement, fieldsToExclude);
      
      // add or delete tag in attributes
      updateTagsInAttributes(modifiedElement, modifiedSectionElement);
      updateAttributeVariantContextAssociatedWithSectionElement(modifiedElement,
          modifiedSectionElement, klassNode, removedAttributeVariantContexts);
      manageSelectedTagValuesInKPNode(modifiedSectionElement, modifiedElement);
      sectionElementToReturn = modifiedSectionElement;
    }
    else {
      // cut off section element and inherit the new section element in child
      Vertex duplicateSectionElementNode = UtilClass.createDuplicateNode(modifiedSectionElement);
      Iterator<Edge> hasPropertyEdges = modifiedSectionElement
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex entityNode = hasPropertyEdges.next()
          .getVertex(Direction.IN);
      String entityId = entityNode.getProperty(CommonConstants.CODE_PROPERTY);
      duplicateSectionElementNode.setProperty(CommonConstants.CODE_PROPERTY,
          UtilClass.generateUniqueId(klassId, entityId));
      String type = duplicateSectionElementNode.getProperty(ISectionElement.TYPE);
      Vertex propertyNode = KlassUtils.getPropertyNodeFromKlassProperty(modifiedSectionElement);
      String attributeType = propertyNode.getProperty(IAttribute.TYPE);
      if (type.equals(CommonConstants.ATTRIBUTE)
          && attributeType.equals(CommonConstants.HTML_TYPE_ATTRIBUTE)) {
        
        String valueAsHtmlFromProperty = propertyNode
            .getProperty(IModifiedSectionAttributeModel.VALUE_AS_HTML);
        String parentValueAsHtml = modifiedSectionElement
            .getProperty(IModifiedSectionAttributeModel.VALUE_AS_HTML);
        String childValueAsHtml = (String) modifiedElement
            .get(IModifiedSectionAttributeModel.VALUE_AS_HTML);
        if ((parentValueAsHtml == null || parentValueAsHtml.isEmpty())
            && valueAsHtmlFromProperty.equals(childValueAsHtml)) {
          modifiedElement.remove(IModifiedSectionAttributeModel.VALUE_AS_HTML);
        }
      }
      
      //This is done because the id passed from UI should not be passed to the element as 
      //code is autogenerated through sequence.
       ArrayList<String> fieldsToExcludeWithCodeAndId = new ArrayList<>(fieldsToExclude);
       fieldsToExcludeWithCodeAndId.addAll(Arrays.asList(IModifiedSectionAttributeModel.CODE, IModifiedSectionAttributeModel.ID));
      UtilClass.saveNode(modifiedElement, duplicateSectionElementNode, fieldsToExcludeWithCodeAndId);
      duplicateSectionElementNode.setProperty(ISectionElement.IS_CUT_OFF, true);
      List<String> utilizingSectionIds = hasKlassPropertyEdge
          .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
      
      // link klass property node with property node
      duplicateSectionElementNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY, propertyNode);
      
      copyDefaultTagValuesToNewKlassPropertyNode(modifiedSectionElement,
          duplicateSectionElementNode);
      updateDefaultTagValueNodes(duplicateSectionElementNode, modifiedElement);
      copySelectedTagValuesToNewKlassPropertyNode(modifiedSectionElement,
          duplicateSectionElementNode);
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        // copy existing tags in attributes
        copyExistingAttributeContext(modifiedSectionElement, duplicateSectionElementNode);
        // add or delete tag in attributes
        updateTagsInAttributes(modifiedElement, duplicateSectionElementNode);
        updateAttributeVariantContextAssociatedWithSectionElement(modifiedElement,
            duplicateSectionElementNode, klassNode, removedAttributeVariantContexts);
      }
      
      // manage taxanomy settings for newly create klass property node
      manageKlassTaxanomyForNewlyCreatedKlassProperty(modifiedSectionElement,
          duplicateSectionElementNode);
      
      manageSelectedTagValuesInKPNode(duplicateSectionElementNode, modifiedElement);
      
      // inherit section element in child klasses and remove previous inherited
      // links
      Iterable<Edge> inheritedHasKlassPropertyEdges = modifiedSectionElement.getEdges(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      Iterator<Edge> iterator = inheritedHasKlassPropertyEdges.iterator();
      while (iterator.hasNext()) {
        Edge inheritedHasKlassPropertyEdge = iterator.next();
        Vertex otherKlassNode = inheritedHasKlassPropertyEdge.getVertex(Direction.OUT);
        String otherKlassId = otherKlassNode.getProperty(CommonConstants.CODE_PROPERTY);
        
        if (klassAndChildIds.contains(otherKlassId)) {
          inheritedHasKlassPropertyEdge.remove();
          if (!otherKlassId.equals(klassId)) {
            Edge newInheritedHasKlassPropertyEdge = otherKlassNode.addEdge(
                RelationshipLabelConstants.HAS_KLASS_PROPERTY, duplicateSectionElementNode);
            newInheritedHasKlassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY,
                true);
            newInheritedHasKlassPropertyEdge
                .setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY, utilizingSectionIds);
          }
        }
        
        // link klass node with new section element
        if (otherKlassId.equals(klassId)) {
          Edge newHasKlassPropertyEdge = klassNode
              .addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY, duplicateSectionElementNode);
          newHasKlassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
          // utilizingSectionIds.add(duplicateSectionElementNode.getProperty(CommonConstants.CODE_PROPERTY));
          newHasKlassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
              utilizingSectionIds);
        }
        // Manage section permission for new klassPropertyNode made after cutoff
        Iterable<Edge> permissionForEdges = modifiedSectionElement.getEdges(Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
        for (Edge permissionFor : permissionForEdges) {
          List<String> utilizingKlassIds = permissionFor
              .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
          List<String> copyOfUtilizingKlassIds = new ArrayList<>();
          copyOfUtilizingKlassIds.addAll(utilizingKlassIds);
          if (utilizingKlassIds.contains(klassId)) {
            Vertex sectionElementPermission = permissionFor.getVertex(Direction.OUT);
            Vertex duplicateSectionElementPermission = UtilClass
                .createDuplicateNode(sectionElementPermission);
            Edge newPermissionEdge = duplicateSectionElementPermission.addEdge(
                RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR,
                duplicateSectionElementNode);
            String roleId = permissionFor.getProperty(CommonConstants.ROLE_ID_PROPERY);
            newPermissionEdge.setProperty(CommonConstants.ROLE_ID_PROPERY, roleId);
            copyOfUtilizingKlassIds.retainAll(klassAndChildIds);
            newPermissionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
                copyOfUtilizingKlassIds);
            newPermissionEdge.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
            Vertex roleNode = UtilClass.getVertexById(roleId,
                VertexLabelConstants.ENTITY_TYPE_ROLE);
            roleNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF,
                duplicateSectionElementPermission);
            
            // update old utillizing klass ids
            utilizingKlassIds.removeAll(klassAndChildIds);
            permissionFor.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
                utilizingKlassIds);
          }
        }
      }
      sectionElementToReturn = duplicateSectionElementNode;
    }
    sectionElementToReturn.getProperty("code");
    return sectionElementToReturn;
  }
  
  private static void updateAttributeVariantContextAssociatedWithSectionElement(
      HashMap<String, Object> modifiedElement, Vertex modifiedSectionElement, Vertex klassNode,
      Map<String, Object> removedAttributeVariantContexts) throws Exception
  {
    Map<String, List<String>> removedAttributeIdVsContextIds = (Map<String, List<String>>) removedAttributeVariantContexts
        .get(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS);
    
    String attributeVariantContext = (String) modifiedElement
        .get(IModifiedSectionElementModel.ATTRIBUTE_VARIANT_CONTEXT);
    Iterator<Edge> variantContextOfIterator = modifiedSectionElement
        .getEdges(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    
    List<Long> changedClassifiersForAttributeContext = (List<Long>) removedAttributeVariantContexts.get(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS);
    
    Iterator<Vertex> klassAndTaxonomyVertices = modifiedSectionElement.getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY).iterator();
    while (klassAndTaxonomyVertices.hasNext()) {
      Vertex klassOrTaxonomyVertex = klassAndTaxonomyVertices.next();
      changedClassifiersForAttributeContext.add(klassOrTaxonomyVertex.getProperty(IKlass.CLASSIFIER_IID));
    }
    
    if ((attributeVariantContext == null || attributeVariantContext.isEmpty()) && variantContextOfIterator.hasNext()) {
      Edge variantContextOfEdge = variantContextOfIterator.next();
      Vertex attributeVariantContextNode = variantContextOfEdge.getVertex(Direction.OUT);
      removedAttributeIdVsContextIds.put((String) modifiedElement.get(ISectionElement.PROPERTY_ID), Arrays.asList(UtilClass.getCodeNew(attributeVariantContextNode)));
      variantContextOfEdge.remove();
    }
    else if (attributeVariantContext != null && !attributeVariantContext.isEmpty() && variantContextOfIterator.hasNext()) {
      Edge variantContextOfEdge = variantContextOfIterator.next();
      Vertex attributeVariantContextNode = variantContextOfEdge.getVertex(Direction.OUT);
      if (!UtilClass.getCodeNew(attributeVariantContextNode).equals(attributeVariantContext)) {
        removedAttributeIdVsContextIds.put((String) modifiedElement.get(ISectionElement.PROPERTY_ID), Arrays.asList(UtilClass.getCodeNew(attributeVariantContextNode)));
        variantContextOfEdge.remove();
        Vertex updatedAttributeVariantContextNode = UtilClass.getVertexById(attributeVariantContext, VertexLabelConstants.VARIANT_CONTEXT);
        updatedAttributeVariantContextNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, modifiedSectionElement);
      }
    }
    else if (attributeVariantContext != null && !attributeVariantContext.isEmpty() && !variantContextOfIterator.hasNext()) {
      Vertex updatedAttributeVariantContextNode = UtilClass.getVertexById(attributeVariantContext,
          VertexLabelConstants.VARIANT_CONTEXT);
      updatedAttributeVariantContextNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF,
          modifiedSectionElement);
    }
  }
  
  private static Vertex getContextTagNode(Vertex attributeContext, String tagId)
  {
    Iterable<Vertex> vertices = attributeContext.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    for (Vertex contextTag : vertices) {
      Vertex tagNode = contextTag
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY)
          .iterator()
          .next();
      String tagNodeId = UtilClass.getCodeNew(tagNode);
      if (tagNodeId.equals(tagId)) {
        return contextTag;
      }
    }
    
    return null;
  }
  
  private static void copyDefaultTagValuesToNewKlassPropertyNode(Vertex modifiedSectionElement,
      Vertex duplicateSectionElementNode)
  {
    Iterable<Edge> hasDefaultTagValues = modifiedSectionElement.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE);
    for (Edge edge : hasDefaultTagValues) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      Integer relevance = edge.getProperty(CommonConstants.SORT_FIELD_RELEVANCE);
      if (relevance == null) {
        relevance = 100;
      }
      Edge duplicateEdge = duplicateSectionElementNode
          .addEdge(RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE, tagValueNode);
      duplicateEdge.setProperty(CommonConstants.SORT_FIELD_RELEVANCE, relevance);
    }
  }
  
  private static void updateDefaultTagValueNodes(Vertex modifiedSectionElement,
      HashMap<String, Object> modifiedElement) throws Exception
  {
    String type = modifiedSectionElement.getProperty(ISectionElement.TYPE);
    if (type.equals(CommonConstants.TAG_PROPERTY)) {
      
      List<Map<String, Object>> addedDefaultValues = (List<Map<String, Object>>) modifiedElement
          .get(IModifiedSectionTagModel.ADDED_DEFAULT_VALUES);
      List<Map<String, Object>> modifiedDfaultValues = (List<Map<String, Object>>) modifiedElement
          .get(IModifiedSectionTagModel.MODIFIED_DEFAULT_VALUES);
      List<String> deletedDefaultValues = (List<String>) modifiedElement
          .get(IModifiedSectionTagModel.DELETED_DEFAULT_VALUES);
      
      for (Map<String, Object> modifiedDefaultValue : modifiedDfaultValues) {
        String tagValueId = (String) modifiedDefaultValue.get(IIdRelevance.TAGID);
        Integer relevance = (Integer) modifiedDefaultValue.get(IIdRelevance.RELEVANCE);
        Vertex tagValueNode = UtilClass.getVertexById(tagValueId, VertexLabelConstants.ENTITY_TAG);
        Edge hasDefaultTagValue = getHasDefaultTagValueEdge(modifiedSectionElement, tagValueNode);
        if (hasDefaultTagValue == null) {
          hasDefaultTagValue = modifiedSectionElement
              .addEdge(RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE, tagValueNode);
        }
        hasDefaultTagValue.setProperty(CommonConstants.SORT_FIELD_RELEVANCE, relevance);
      }
      
      for (Map<String, Object> addedDefaultValue : addedDefaultValues) {
        String tagValueId = (String) addedDefaultValue.get(IIdRelevance.TAGID);
        Integer relevance = (Integer) addedDefaultValue.get(IIdRelevance.RELEVANCE);
        Vertex tagValueNode = UtilClass.getVertexById(tagValueId, VertexLabelConstants.ENTITY_TAG);
        Edge hasDefaultTagValue = modifiedSectionElement
            .addEdge(RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE, tagValueNode);
        hasDefaultTagValue.setProperty(CommonConstants.SORT_FIELD_RELEVANCE, relevance);
      }
      
      for (String deletedTagValue : deletedDefaultValues) {
        Vertex tagValueNode = UtilClass.getVertexById(deletedTagValue,
            VertexLabelConstants.ENTITY_TAG);
        Edge hasDefaultTagValue = getHasDefaultTagValueEdge(modifiedSectionElement, tagValueNode);
        if (hasDefaultTagValue != null) {
          hasDefaultTagValue.remove();
        }
      }
    }
  }
  
  private static Edge getHasDefaultTagValueEdge(Vertex modifiedSectionElement, Vertex tagValueNode)
  {
    Iterable<Edge> iterable = modifiedSectionElement.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE);
    String tagValueId = UtilClass.getCodeNew(tagValueNode);
    Edge edgeToReturn = null;
    for (Edge edge : iterable) {
      Vertex tagValueNodeToCompare = edge.getVertex(Direction.IN);
      String tagValueIdToCompare = UtilClass.getCodeNew(tagValueNodeToCompare);
      if (tagValueId.equals(tagValueIdToCompare)) {
        edgeToReturn = edge;
        break;
      }
    }
    return edgeToReturn;
  }
  
  private static void checkDefaultValueChanged(HashMap<String, Object> modifiedElement,
      Vertex modifiedSectionElement, List<Map<String, Object>> defaultValueChangeList,
      String vertexLabel, List<String> klassAndChildIds, List<Long> mandatoryPropertyUpdatedIIDs,
      List<Long> propertyIIDsToEvaluateProductIdentifier, List<Long> propertyIIDsToRemoveProductIdentifier)
  {
    String type = modifiedSectionElement.getProperty(ISectionElement.TYPE);
    Iterable<Vertex> iterable = modifiedSectionElement.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    List<String> childrenList = new ArrayList<>();
    for (Vertex klassNode : iterable) {
      String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
      childrenList.add(klassId);
    }
    childrenList.retainAll(klassAndChildIds);
    Iterator<Vertex> iterator = modifiedSectionElement
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    Vertex entityNode = null;
    while (iterator.hasNext()) {
      entityNode = iterator.next();
    }
    String entityId = entityNode.getProperty(CommonConstants.CODE_PROPERTY);
    Boolean isDependent = entityNode.getProperty(IAttribute.IS_TRANSLATABLE);
    String oldCouplingType = modifiedSectionElement.getProperty(ISectionElement.COUPLING_TYPE);
    String newCouplingType = (String) modifiedElement.get(ISectionElement.COUPLING_TYPE);
    Boolean oldIsMandatory = modifiedSectionElement
        .getProperty(ISectionElement.IS_MANDATORY) == null ? false
            : modifiedSectionElement.getProperty(ISectionElement.IS_MANDATORY);
    Boolean newIsMandatory = (Boolean) modifiedElement.get(ISectionElement.IS_MANDATORY);
    Boolean oldIsShould = modifiedSectionElement.getProperty(ISectionElement.IS_SHOULD) == null
        ? false
        : modifiedSectionElement.getProperty(ISectionElement.IS_SHOULD);
    Boolean newIsShould = (Boolean) modifiedElement.get(ISectionElement.IS_SHOULD);
    Boolean oldIsSkipped = modifiedSectionElement.getProperty(ISectionElement.IS_SKIPPED);
    Boolean newIsSkipped = (Boolean) modifiedElement.get(ISectionElement.IS_SKIPPED);
    Boolean oldIsIdentifier = modifiedSectionElement
        .getProperty(ISectionAttribute.IS_IDENTIFIER) == null ? false
            : modifiedSectionElement.getProperty(ISectionAttribute.IS_IDENTIFIER);
    Boolean newIsIdentifier = (Boolean) modifiedElement.get(ISectionAttribute.IS_IDENTIFIER) == null
        ? false
        : (Boolean) modifiedElement.get(ISectionAttribute.IS_IDENTIFIER);
    
    Map<String, Object> valueChangeMap = new HashMap<>();
    Boolean isValueChanged = false;
    boolean isCouplingChanged = !oldCouplingType.equals(newCouplingType);
    if (type.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
      
      String oldDefaultValue = modifiedSectionElement.getProperty(ISectionAttribute.DEFAULT_VALUE);
      String newDefaultValue = (String) modifiedElement.get(ISectionAttribute.DEFAULT_VALUE);
      String oldDefaultValueAsHtml = modifiedSectionElement
          .getProperty(ISectionAttribute.VALUE_AS_HTML);
      String newDefaultValueAsHtml = (String) modifiedElement.get(ISectionAttribute.VALUE_AS_HTML);
      
      if (isCouplingChanged || !oldIsMandatory.equals(newIsMandatory)
          || !oldIsSkipped.equals(newIsSkipped) || !oldIsShould.equals(newIsShould)
          || !oldIsIdentifier.equals(newIsIdentifier)) {
        isValueChanged = true;
      }
      
      Boolean isAttributeValueChanged = isAttributeValueChange(newDefaultValue, newDefaultValueAsHtml, oldDefaultValue,
          oldDefaultValueAsHtml);
      if (!isValueChanged && !newCouplingType.equals(CommonConstants.LOOSELY_COUPLED)
          && isAttributeValueChanged) {
        isValueChanged = true;
      }
      if (isValueChanged) {
        Vertex entityVertex = modifiedSectionElement.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY).iterator().next();
        String propertytype = entityVertex.getProperty(CommonConstants.TYPE_PROPERTY);
        
        if (CoreConstant.NUMERIC_TYPE.contains(propertytype)) {
          if(newDefaultValue != null && !newDefaultValue.isEmpty()) {
            valueChangeMap.put(IAttributeDefaultValueCouplingTypeModel.VALUE_AS_NUMBER, Double.parseDouble(newDefaultValue));
          }
          String unitSymbol = (String) modifiedElement.get(ISectionAttribute.DEFAULT_UNIT);
          if (unitSymbol == null) {
            unitSymbol = modifiedSectionElement.getProperty(ISectionAttribute.DEFAULT_UNIT);
          }
          valueChangeMap.put(IAttributeDefaultValueCouplingTypeModel.UNIT_SYMBOL, unitSymbol);
          
        }
        
        valueChangeMap.put(IDefaultValueChangeModel.VALUE, newDefaultValue);
        valueChangeMap.put(IAttributeDefaultValueCouplingTypeModel.VALUE_AS_HTML, newDefaultValueAsHtml);
      }
      valueChangeMap.put(IDefaultValueChangeModel.IS_VALUE_CHANGED, isAttributeValueChanged);
    }
    else if (type.equals(CommonConstants.TAG_PROPERTY)) {
      List<Map<String, Object>> addedDefaultValues = (List<Map<String, Object>>) modifiedElement
          .get(IModifiedSectionTagModel.ADDED_DEFAULT_VALUES);
      List<Map<String, Object>> modifiedDfaultValues = (List<Map<String, Object>>) modifiedElement
          .get(IModifiedSectionTagModel.MODIFIED_DEFAULT_VALUES);
      List<String> deletedDefaultValues = (List<String>) modifiedElement
          .get(IModifiedSectionTagModel.DELETED_DEFAULT_VALUES);
      
      List<Map<String, Object>> existingDefaultTagValues = KlassUtils
          .getDefaultTagValuesOfKlassPropertyNode(modifiedSectionElement);
      List<Map<String, Object>> updatedDefaultTagValuesList = updateDefaultTagValuesList(
          existingDefaultTagValues, modifiedElement);
      if (isCouplingChanged || !oldIsMandatory.equals(newIsMandatory)
          || !oldIsSkipped.equals(newIsSkipped) || !oldIsShould.equals(newIsShould)) {
        isValueChanged = true;
      }
      
      boolean isTagValueChanged = (addedDefaultValues.size() > 0 || modifiedDfaultValues.size() > 0
          || deletedDefaultValues.size() > 0);
      if (!isValueChanged && !newCouplingType.equals(CommonConstants.LOOSELY_COUPLED)
          && isTagValueChanged) {
        isValueChanged = true;
      }
      if (isValueChanged) {
        valueChangeMap.put(IDefaultValueChangeModel.VALUE, updatedDefaultTagValuesList);
      }
      valueChangeMap.put(IDefaultValueChangeModel.IS_VALUE_CHANGED, isTagValueChanged);
    }
    
    
    if (isValueChanged) {
      String sourceType = KlassUtils.getSourceTypeBasedUponNodeLabel(vertexLabel);
      Long propertyiid = Long.valueOf(entityNode.getProperty(IAttribute.PROPERTY_IID).toString());
      valueChangeMap.put(IDefaultValueChangeModel.TYPE, type);
      valueChangeMap.put(IDefaultValueChangeModel.ENTITY_ID, entityId);
      valueChangeMap.put(IDefaultValueChangeModel.COUPLING_TYPE, newCouplingType);
      valueChangeMap.put(IDefaultValueChangeModel.IS_MANDATORY, newIsMandatory);
      valueChangeMap.put(IDefaultValueChangeModel.IS_SHOULD, newIsShould);
      valueChangeMap.put(IDefaultValueChangeModel.IS_SKIPPED, newIsSkipped);
      valueChangeMap.put(IDefaultValueChangeModel.KLASS_AND_CHILDRENIDS, childrenList);
      valueChangeMap.put(IDefaultValueChangeModel.SOURCE_TYPE, sourceType);
      valueChangeMap.put(IDefaultValueChangeModel.PROPERTY_IID, propertyiid);
      valueChangeMap.put(IDefaultValueChangeModel.IS_COUPLING_TYPE_CHANGED, isCouplingChanged);
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        valueChangeMap.put(IAttributeDefaultValueCouplingTypeModel.IS_DEPENDENT, isDependent);
      }
      if(!oldIsMandatory.equals(newIsMandatory) || !oldIsShould.equals(newIsShould)) {
        mandatoryPropertyUpdatedIIDs.add(propertyiid);
      }
      if (!oldIsIdentifier.equals(newIsIdentifier)) {
        if (newIsIdentifier) {
          propertyIIDsToEvaluateProductIdentifier.add(propertyiid);
        }
        else {
          propertyIIDsToRemoveProductIdentifier.add(propertyiid);
        }
      }
      defaultValueChangeList.add(valueChangeMap);
    }
  }
  
  public static Boolean isAttributeValueChange(String newValue, String newValueAsHtml, String value,
      String valueAsHtml)
  {
    newValue = newValue == null ? "" : newValue;
    newValueAsHtml = newValueAsHtml == null ? "" : newValueAsHtml;
    value = value == null ? "" : value;
    valueAsHtml = valueAsHtml == null ? "" : valueAsHtml;
    
    if (!value.equals(newValue) || !valueAsHtml.equals(newValueAsHtml)) {
      return true;
    }
    
    return false;
  }
  
  private static List<Map<String, Object>> updateDefaultTagValuesList(
      List<Map<String, Object>> existingDefaultTagValues, HashMap<String, Object> modifiedElement)
  {
    List<Map<String, Object>> addedDefaultValues = (List<Map<String, Object>>) modifiedElement
        .get(IModifiedSectionTagModel.ADDED_DEFAULT_VALUES);
    List<Map<String, Object>> modifiedDfaultValues = (List<Map<String, Object>>) modifiedElement
        .get(IModifiedSectionTagModel.MODIFIED_DEFAULT_VALUES);
    List<String> deletedDefaultValues = (List<String>) modifiedElement
        .get(IModifiedSectionTagModel.DELETED_DEFAULT_VALUES);
    
    for (Map<String, Object> addedDefaultValue : addedDefaultValues) {
      existingDefaultTagValues.add(addedDefaultValue);
    }
    
    List<Map<String, Object>> valuesToRemove = new ArrayList<>();
    for (String deletedValue : deletedDefaultValues) {
      for (Map<String, Object> existingValue : existingDefaultTagValues) {
        String tagId = (String) existingValue.get(IIdRelevance.TAGID);
        if (tagId.equals(deletedValue)) {
          valuesToRemove.add(existingValue);
        }
      }
    }
    existingDefaultTagValues.removeAll(valuesToRemove);
    
    for (Map<String, Object> modfiedValue : modifiedDfaultValues) {
      String modifiedTagId = (String) modfiedValue.get(IIdRelevance.TAGID);
      Integer relevance = (Integer) modfiedValue.get(IIdRelevance.RELEVANCE);
      for (Map<String, Object> existingValue : existingDefaultTagValues) {
        String tagId = (String) existingValue.get(IIdRelevance.TAGID);
        if (modifiedTagId.equals(tagId)) {
          existingValue.put(IIdRelevance.RELEVANCE, relevance);
        }
      }
    }
    return existingDefaultTagValues;
  }
  
  public static void manageModifiedSectionElements(Vertex klassNode,
      List<HashMap<String, Object>> modifiedElements, List<String> klassAndChildIds,
      List<Map<String, Object>> defaultValueChangeList, String vertexLabel,
      Map<String, Object> propertiesADMMap, Map<String, Object> removedAttributeVariantContexts,
      List<Long> mandatoryPropertyUpdatedIIDs, List<Long> propertyIIDsToEvaluateProductIdentifier,
      List<Long> propertyIIDsToRemoveProductIdentifier) throws Exception
  {
    List<Vertex> modifiedElementsNodes = new ArrayList<Vertex>();
    for (HashMap<String, Object> modifiedElement : modifiedElements) {
      String modifiedElementId = (String) modifiedElement.get(CommonConstants.ID_PROPERTY);
      Vertex modifiedSectionElement = UtilClass.getVertexById(modifiedElementId,
          VertexLabelConstants.ENTITY_KLASS_PROPERTY);
      String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
      
      if ((Boolean) modifiedElement.get(IModifiedSectionElementModel.IS_MODIFIED)) {
        // Check if identifier attributechanged
        Boolean modifiedIsIdentifier = (Boolean) modifiedElement
            .get(IModifiedSectionAttributeModel.IS_IDENTIFIER);
        Boolean isIndentifier = modifiedSectionElement.getProperty(ISectionAttribute.IS_IDENTIFIER);
        if (modifiedIsIdentifier != null && modifiedIsIdentifier != isIndentifier) {
          propertiesADMMap.put(
              IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, true);
        }
        
        modifiedSectionElement = manageModifiedKlassProperty(klassNode, klassAndChildIds,
            modifiedElement, modifiedSectionElement, klassId, defaultValueChangeList, vertexLabel, removedAttributeVariantContexts,
            mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier);
        modifiedElementsNodes.add(modifiedSectionElement);
      }
    }
    incrementVersionIdsofAllKlassNodeInheritingKp(modifiedElementsNodes);
  }
  
  public static void manageLifeCycleStatusTag(Map<String, Object> klassMap, Vertex klassNode)
      throws Exception
  {
    Iterable<Edge> klassLifeCycleTagLinks = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);
    Map<String, Edge> tagIdWithLifeCycleEdges = new HashMap<String, Edge>();
    for (Edge edge : klassLifeCycleTagLinks) {
      Vertex linkedTag = edge.getVertex(Direction.IN);
      String linkedTagId = linkedTag.getProperty(CommonConstants.CODE_PROPERTY);
      tagIdWithLifeCycleEdges.put(linkedTagId, edge);
    }
    // add link between klass node & tags of type 'Lifecycle Status'
    List<String> addedLifecycleStatusTags = (List<String>) klassMap
        .get(IKlassSaveModel.ADDED_LIFECYCLE_STATUS_TAGS);
    for (String id : addedLifecycleStatusTags) {
      if (!tagIdWithLifeCycleEdges.containsKey(id)) {
        Vertex tagToLink = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        Edge newEdge = klassNode.addEdge(RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK, tagToLink);
        tagIdWithLifeCycleEdges.put(id, newEdge);
      }
    }
    
    // remove link between klass node & tags of type 'Lifecycle Status'
    List<String> deletedLifecycleStatusTags = (List<String>) klassMap
        .get(IKlassSaveModel.DELETED_LIFECYCLE_STATUS_TAGS);
    for (String id : deletedLifecycleStatusTags) {
      if (tagIdWithLifeCycleEdges.containsKey(id)) {
        Edge edgeToDelete = tagIdWithLifeCycleEdges.get(id);
        edgeToDelete.remove();
      }
    }
  }
  
  public static void addLifeCycleStatusTag(List<String> statusTagIds, Vertex klassNode)
      throws Exception
  {
    for (String id : statusTagIds) {
      Vertex tagToLink = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
      klassNode.addEdge(RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK, tagToLink);
    }
  }
  
  public static void manageTreeTypeOption(Map<String, Object> klassMap, Vertex klassNode,
      List<Vertex> klassAndChildNodes) throws Exception
  {
    Iterable<Vertex> vertices = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.TREE_TYPE_OPTION_LINK);
    Iterator<Vertex> vertexIterator = vertices.iterator();
    if (!vertexIterator.hasNext()) {
      throw new NotFoundException();
    }
    
    Vertex treeTypeNode = vertexIterator.next();
    String treeTypeOption = treeTypeNode.getProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
    Object newTreeTypeOption = klassMap.get(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
    if (!treeTypeOption.equals(newTreeTypeOption)) { // if tree type option is
      // modified
      
      Iterable<Edge> edges = klassNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.TREE_TYPE_OPTION_LINK);
      Iterator<Edge> edgeIterator = edges.iterator();
      if (!edgeIterator.hasNext()) {
        throw new NotFoundException();
      }
      
      Edge treeTypeOptionLink = edgeIterator.next();
      Boolean isInherited = treeTypeOptionLink.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if (isInherited) {
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.TREE_TYPE, CommonConstants.CODE_PROPERTY);
        Map<String, Object> treeTypeMap = new HashMap<String, Object>();
        treeTypeMap.put(CommonConstants.TREE_TYPE_OPTION_PROPERTY, newTreeTypeOption);
        
        cuttOfTreeTypeNode(klassNode);
        
        treeTypeNode = UtilClass.createNode(treeTypeMap, vertexType);
        Edge newTreeTypeOptionLink = klassNode
            .addEdge(RelationshipLabelConstants.TREE_TYPE_OPTION_LINK, treeTypeNode);
        newTreeTypeOptionLink.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
        
        inheritTreeTypeOptionInChildKlasses(klassNode, treeTypeNode);
        
      }
      else {
        treeTypeNode.setProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY, newTreeTypeOption);
      }
    }
  }
  
  private static void inheritTreeTypeOptionInChildKlasses(Vertex klassNode, Vertex treeTypeNode)
      throws Exception
  {
    Iterable<Vertex> childNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : childNodes) {
      Iterable<Edge> edges = childNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.TREE_TYPE_OPTION_LINK);
      Iterator<Edge> iterator = edges.iterator();
      if (!iterator.hasNext()) {
        throw new NotFoundException();
      }
      
      Edge edge = iterator.next();
      Boolean isInherited = edge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if (!isInherited) {
        continue;
      }
      
      cuttOfTreeTypeNode(childNode);
      
      Edge treeTypeOptionLinkEdge = childNode
          .addEdge(RelationshipLabelConstants.TREE_TYPE_OPTION_LINK, treeTypeNode);
      treeTypeOptionLinkEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      UtilClass.addNodesForVersionIncrement(childNode);
      inheritTreeTypeOptionInChildKlasses(childNode, treeTypeNode);
    }
  }
  
  private static void cuttOfTreeTypeNode(Vertex klassNode)
  {
    Iterable<Edge> edgesToRemove = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.TREE_TYPE_OPTION_LINK);
    for (Edge edge : edgesToRemove) {
      edge.remove();
    }
  }
  
  public static List<Map<String, Object>> createSaveKlassData(Map<String, Object> klass,
      OrientGraph graph, String entityType) throws Exception
  {
    List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
    String klassId = (String) klass.get(CommonConstants.ID_PROPERTY);
    try {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId, entityType);
      String rid = (String) klassNode.getId()
          .toString();
      Iterable<Vertex> klassAndChildNodes = graph
          .command(new OCommandSQL(
              "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
          .execute();
      
      List<String> klassAndChildIds = new ArrayList<>();
      for (Vertex inheritanceNode : klassAndChildNodes) {
        klassAndChildIds
            .add((String) inheritanceNode.getProperty(CommonConstants.CODE_PROPERTY));
      }
      
      List<HashMap<String, Object>> modifiedElements = (List<HashMap<String, Object>>) klass
          .get(IKlassSaveModel.MODIFIED_ELEMENTS);
      
      for (HashMap<String, Object> modifiedElement : modifiedElements) {
        Vertex entityNode = getEntityNode(modifiedElement.get(ISectionElement.ID)
            .toString(), graph);
        Vertex modifiedSectionElement = KlassUtils.getRespectiveKlassPropertyNode(klassNode,
            entityNode);
        modifiedElement.put(ISectionElement.ID,
            modifiedSectionElement.getProperty(CommonConstants.CODE_PROPERTY));
        if ((Boolean) modifiedElement.get(IModifiedSectionElementModel.IS_MODIFIED)) {
          modifiedSectionElement = manageModifiedKlassProperty(klassNode, klassAndChildIds,
              modifiedElement, modifiedSectionElement, klassId, null, entityType, null, new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>());
        }
        graph.commit();
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put(CommonConstants.KLASSID,
            klassNode.getProperty(CommonConstants.CODE_PROPERTY));
        responseMap.put("klassLabel",
            (String) UtilClass.getValueByLanguage(klassNode, CommonConstants.LABEL_PROPERTY));
        responseMap.put("propertyId", entityNode.getProperty(CommonConstants.ID_PROPERTY));
        responseMap.put(ISectionAttribute.DEFAULT_VALUE,
            modifiedSectionElement.getProperty(CommonConstants.DEFAULT_VALUE_PROPERTY));
        responseList.add(responseMap);
      }
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    return responseList;
  }
  
  private static Vertex getEntityNode(String id, OrientGraph graph) throws KlassNotFoundException
  {
    Vertex entity = null;
    Iterator<Vertex> vertices = graph
        .getVertices("attribute", new String[] { CommonConstants.CODE_PROPERTY },
            new Object[] { id })
        .iterator();
    if (!vertices.hasNext()) {
      vertices = graph
          .getVertices("tag", new String[] { CommonConstants.CODE_PROPERTY },
              new Object[] { id })
          .iterator();
      if (!vertices.hasNext()) {
        vertices = graph
            .getVertices("role", new String[] { CommonConstants.CODE_PROPERTY },
                new Object[] { id })
            .iterator();
        if (!vertices.hasNext()) {
          throw new KlassNotFoundException();
        }
      }
    }
    entity = vertices.next();
    return entity;
  }
  
  public static Map<String, Map<String, Object>> manageKlassNatureInKlass(Vertex klassNode,
      Map<String, Object> klassADM, List<String> addedProductVariantContexts,
      List<String> deletedProductVariantContexts,
      Map<String, Map<String, Object>> sideInfoForDataTransfer,
      Map<String, Map<String, Object>> relationshipInheritance,
      MutableBoolean isRemoveTaxonomyConflictsRequired) throws Exception
  {
    Vertex natureNode = null;
    String natureType = (String) klassADM.get(IKlass.NATURE_TYPE);
    
    List<String> deletedRelationships = (List<String>) klassADM
        .get(IKlassSaveModel.DELETED_RELATIONSHIPS);

    if (deletedRelationships != null && deletedRelationships.size() > 0) {
      for (String relationshipId : deletedRelationships) {
        // natureNode is KNR node
        natureNode = KlassUtils.getKlassNatureRelationshipPropertyNode(klassNode, relationshipId);
        natureNode.setProperty(IKlass.NATURE_TYPE, natureType);
      }
      RelationshipUtils.deleteRelationships(deletedRelationships, new ArrayList<>());
    }
    
    List<HashMap<String, Object>> addedRelationships = (List<HashMap<String, Object>>) klassADM
        .remove(IKlassSaveModel.ADDED_RELATIONSHIPS);
    if (addedRelationships != null && addedRelationships.size() > 0) {
      for (HashMap<String, Object> relationMap : addedRelationships) {
        String relationshipType = (String) relationMap
            .get(IKlassNatureRelationship.RELATIONSHIP_TYPE);
        natureNode = getOrCreateKlassRelationshipNatureNode(klassNode, relationshipType,
            natureType);
        
        String propertyCollectionId = (String) relationMap
            .remove(IAddedNatureRelationshipModel.ADDED_PROPERTY_COLLECTION);
        Vertex natureRelationshipNode = RelationshipUtils.createNatureRelationship(relationMap,
            AbstractSaveRelationship.fieldsToExcludeForCreate);
        
        RelationshipUtils.addNatureSectionElement(UtilClass.getGraph(), relationMap, natureNode,
            natureType);
        
        // Connect nature relationshipNode to template node..
        /*
         * TODO
        CreateRelationshiputils.manageRelationshiplinkToTemplates(natureRelationshipNode);
        CreateRelationshiputils.manageNatureRelationshiplinkToTemplates(natureRelationshipNode);
        */
        if (propertyCollectionId != null) {
          linkPropertyCollectionToNatureNode(natureNode, propertyCollectionId);
        }
        if (relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
          for (String contextId : addedProductVariantContexts) {
            KlassUtils.addedVariantContexts(natureNode, VertexLabelConstants.VARIANT_CONTEXT,
                contextId);
          }
        }
        List<Map<String, Object>> addedAttributes = (List<Map<String, Object>>) relationMap
                .get(ISaveRelationshipModel.ADDED_ATTRIBUTES);
        if (addedAttributes != null) { 
          RelationshipUtils.manageAddedAttributes(natureRelationshipNode, relationMap,
            sideInfoForDataTransfer);
        }
        
        List<Map<String, Object>> addedTags = (List<Map<String, Object>>) relationMap.get(ISaveRelationshipModel.ADDED_TAGS);
        if (addedTags != null) {
          RelationshipUtils.manageAddedTags(natureRelationshipNode, relationMap, sideInfoForDataTransfer);
        }
        
        List<Map<String, Object>> addedRelationship = (List<Map<String, Object>>) relationMap.get(IModifiedNatureRelationshipModel.ADDED_RELATIONSHIP_INHERITANCE);
        if (addedRelationship != null) {
           handleAddedRelationshipsInheritance(new ArrayList<>(),
              addedRelationship, natureNode, new ArrayList<>());
        }
      }
    }
    else {
      if (addedProductVariantContexts.size() > 0) {
        natureNode = getProductVariantNatureNode(klassNode);
      }
      for (String contextId : addedProductVariantContexts) {
        KlassUtils.addedVariantContexts(natureNode, VertexLabelConstants.VARIANT_CONTEXT,
            contextId);
      }
    }
    
    Map<String, Object> deletedElements = new HashMap<>();
    List<HashMap<String, Object>> modifiedRelationships = (List<HashMap<String, Object>>) klassADM
        .remove(IKlassSaveModel.MODIFIED_RELATIONSHIPS);
    if (modifiedRelationships != null && modifiedRelationships.size() > 0) {
      for (HashMap<String, Object> modifiedRelationship : modifiedRelationships) {
        String relationshipId = (String) modifiedRelationship
            .get(IModifiedNatureRelationshipModel.ID);
        String relationshipType = (String) modifiedRelationship
            .get(IKlassNatureRelationship.RELATIONSHIP_TYPE);
        natureNode = getOrCreateKlassRelationshipNatureNode(klassNode, relationshipType,
            natureType);
        String propertyCollectionId = (String) modifiedRelationship
            .remove(IModifiedNatureRelationshipModel.DELETED_PROPERTY_COLLECTION);
        if (propertyCollectionId != null) {
          unlinkPropertyCollectionAndContextsFromNatureNode(natureNode, false);
        }
        propertyCollectionId = (String) modifiedRelationship
            .remove(IModifiedNatureRelationshipModel.ADDED_PROPERTY_COLLECTION);
        if (propertyCollectionId != null) {
          linkPropertyCollectionToNatureNode(natureNode, propertyCollectionId);
        }
        Vertex vertex = UtilClass.getVertexById((String) modifiedRelationship.get(IRelationship.ID),
            VertexLabelConstants.NATURE_RELATIONSHIP);
        vertex.setProperty(EntityUtil.getLanguageConvertedField(IRelationship.LABEL),
            (String) modifiedRelationship.get(IRelationship.LABEL));
        vertex.setProperty(IKlassNatureRelationship.MAX_NO_OF_ITEMS,
            modifiedRelationship.get(IKlassNatureRelationship.MAX_NO_OF_ITEMS));
        String oldTaxonomyInheritanceSetting = vertex.getProperty(IKlassNatureRelationship.TAXONOMY_INHERITANCE_SETTING);
        String newTaxonomyInheritanceSetting = (String) modifiedRelationship.get(IKlassNatureRelationship.TAXONOMY_INHERITANCE_SETTING);
        if (!oldTaxonomyInheritanceSetting.equals("Off")
            && !newTaxonomyInheritanceSetting.equals(oldTaxonomyInheritanceSetting)
            && relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
          isRemoveTaxonomyConflictsRequired.setValue(true);
        }
        vertex.setProperty(IKlassNatureRelationship.TAXONOMY_INHERITANCE_SETTING,
            newTaxonomyInheritanceSetting);
        vertex.setProperty(IKlassNatureRelationship.ENABLE_AFTER_SAVE,
            modifiedRelationship.get(IKlassNatureRelationship.ENABLE_AFTER_SAVE));
        
        String tabIdToDelete = (String) modifiedRelationship
            .get(IModifiedNatureRelationshipModel.DELETED_TAB);
        Map<String, Object> tabMap = (Map<String, Object>) modifiedRelationship
            .get(IModifiedNatureRelationshipModel.ADDED_TAB);
        TabUtils.manageAddedAndDeletedTab(vertex, tabMap, tabIdToDelete,
            CommonConstants.RELATIONSHIP);
        
        Boolean autoCreateSettings = (Boolean) modifiedRelationship
            .get(IModifiedNatureRelationshipModel.AUTO_CREATE_SETTINGS);
        if (autoCreateSettings != null) {
          vertex.setProperty(IKlassNatureRelationship.AUTO_CREATE_SETTINGS, autoCreateSettings);
          String rhythm = autoCreateSettings
              ? (String) modifiedRelationship.get(IModifiedNatureRelationshipModel.RHYTHM)
              : null;
          if (rhythm != null) {
            vertex.setProperty(IKlassNatureRelationship.RHYTHM, rhythm);
          }
          else {
            vertex.removeProperty(IKlassNatureRelationship.RHYTHM);
          }
        }
        
        List<String> deletedContextIds = (List<String>) modifiedRelationship
            .get(IModifiedNatureRelationshipModel.DELETED_CONTEXT_TAGS);
        if (deletedContextIds != null && !deletedContextIds.isEmpty()) {
          Iterable<Edge> edgesToTagGroups = vertex.getEdges(Direction.OUT,
              RelationshipLabelConstants.HAS_RELATIONSHIP_CONTEXT_TAG);
          for (Edge edge : edgesToTagGroups) {
            Vertex tagGroup = edge.getVertex(Direction.IN);
            String tagGroupId = tagGroup.getProperty(CommonConstants.CODE_PROPERTY);
            if (deletedContextIds.contains(tagGroupId)) {
              edge.remove();
            }
          }
        }
        
        List<String> addedContextIds = (List<String>) modifiedRelationship
            .get(IModifiedNatureRelationshipModel.ADDED_CONTEXT_TAGS);
        if (addedContextIds != null && !addedContextIds.isEmpty()) {
          Iterable<Vertex> tagGroupVertices = UtilClass.getVerticesByIds(addedContextIds,
              VertexLabelConstants.ENTITY_TAG);
          for (Vertex tagGroupVertex : tagGroupVertices) {
            vertex.addEdge(RelationshipLabelConstants.HAS_RELATIONSHIP_CONTEXT_TAG, tagGroupVertex);
          }
        }
        
        if (relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
          for (String contextId : addedProductVariantContexts) {
            KlassUtils.addedVariantContexts(natureNode, VertexLabelConstants.VARIANT_CONTEXT,
                contextId);
          }
          for (String contextId : deletedProductVariantContexts) {
            KlassUtils.deletedVariantContexts(natureNode, contextId);
          }
        }
        
        RelationshipUtils.manageADMOfProperties(vertex, modifiedRelationship,
            sideInfoForDataTransfer);
        handleADMOfRelationshipInheritance(modifiedRelationship, klassNode,
            relationshipInheritance);
      }
    }
    else {
      if (deletedProductVariantContexts.size() > 0) {
        natureNode = getProductVariantNatureNode(klassNode);
      }
      if (natureNode != null) {
        for (String contextId : deletedProductVariantContexts) {
          KlassUtils.deletedVariantContexts(natureNode, contextId);
        }
      }
    }
    
    Map<String, Map<String, Object>> returnMap = new HashMap<>();
    returnMap.put(IGetKlassWithGlobalPermissionModel.DELETED_ELEMENTS, deletedElements);
    return returnMap;
  }
  
  private static void handleADMOfRelationshipInheritance(Map<String, Object> modifiedRelationship,
      Vertex klassNode, Map<String, Map<String, Object>> relationshipInheritance) throws Exception
  {
    List<String> existingInheritedRelationships = new ArrayList<>();
    String relationshipId = (String) modifiedRelationship.get(IModifiedNatureRelationshipModel.ID);
    List<Map<String, Object>> addedRelationshipsInheritance = (List<Map<String, Object>>) modifiedRelationship
        .get(IModifiedNatureRelationshipModel.ADDED_RELATIONSHIP_INHERITANCE);
    List<Map<String, Object>> modifiedRelationshipsInheritance = (List<Map<String, Object>>) modifiedRelationship
        .get(IModifiedNatureRelationshipModel.MODIFIED_RELATIONSHIP_INHERITANCE);
    List<String> deletedRelationshipsInheritance = (List<String>) modifiedRelationship
        .get(IModifiedNatureRelationshipModel.DELETED_RELATIONSHIP_INHERITANCE);
    
    if (addedRelationshipsInheritance.isEmpty() && modifiedRelationshipsInheritance.isEmpty()
        && deletedRelationshipsInheritance.isEmpty()) {
      return;
    }
    
    Vertex KRVertex = RelationshipRepository
        .getKRNodeBetweenNatureKlassNodeAndRelationshipId(klassNode, relationshipId);
    Iterable<Edge> inheritedRelationshipEdges = KRVertex.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP);
    
    List<Map<String, Object>> modifiedRelationships = new ArrayList<>();
    List<String> deletedRelationships = new ArrayList<>();
    
    handleDeletedRelationshipsInheritanceAndFillExisting(existingInheritedRelationships,
        deletedRelationshipsInheritance, inheritedRelationshipEdges, deletedRelationships);
    handleAddedRelationshipsInheritance(existingInheritedRelationships,
        addedRelationshipsInheritance, KRVertex, modifiedRelationships);
    handleModifiedRelationshipsInheritance(existingInheritedRelationships,
        modifiedRelationshipsInheritance, KRVertex, modifiedRelationships);
    
    if (!modifiedRelationships.isEmpty() || !deletedRelationships.isEmpty()) {
      fillInheritanceMap(UtilClass.getCodeNew(KRVertex), relationshipInheritance, relationshipId,
          modifiedRelationships, deletedRelationships);
    }
  }
  
  private static void fillInheritanceMap(String sideId,
      Map<String, Map<String, Object>> relationshipInheritance, String relationshipId,
      List<Map<String, Object>> modifiedRelationships, List<String> deletedRelationships)
  {
    Map<String, Object> changeReltionshipModel = new HashMap<>();
    changeReltionshipModel.put(ISideInfoForRelationshipInheritanceModel.RELATIONSHIP_ID,
        relationshipId);
    changeReltionshipModel.put(ISideInfoForRelationshipInheritanceModel.SIDE_ID, sideId);
    changeReltionshipModel.put(ISideInfoForRelationshipInheritanceModel.MODIFIED_RELATIONSHIPS,
        modifiedRelationships);
    changeReltionshipModel.put(ISideInfoForRelationshipInheritanceModel.DELETED_RELATIONSHIPS,
        deletedRelationships);
    relationshipInheritance.put(relationshipId, changeReltionshipModel);
  }
  
  private static void handleModifiedRelationshipsInheritance(
      List<String> existingInheritedRelationships,
      List<Map<String, Object>> modifiedRelationshipsInheritance, Vertex KRVertex,
      List<Map<String, Object>> modifiedRelationships) throws Exception
  {
    for (Map<String, Object> modifiedRelationshipInheritance : modifiedRelationshipsInheritance) {
      String relationshipId = (String) modifiedRelationshipInheritance
          .get(IModifiedRelationshipPropertyModel.ID);
      if (!existingInheritedRelationships.contains(relationshipId)) {
        continue;
      }
      String couplingType = (String) modifiedRelationshipInheritance
          .get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
      Vertex relationshipVertex = UtilClass.getVertexByIndexedId(relationshipId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      Edge relationshipInheritanceEdge = RelationshipRepository
          .getRelationshipInheritanceEdgeBetweenKRAndRelationshipVertex(
              RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP, relationshipVertex,
              KRVertex);
      relationshipInheritanceEdge.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE,
          couplingType);
      
      modifiedRelationships.add(getModifiedRelationshipModel(relationshipId, couplingType));
    }
  }
  
  private static void handleAddedRelationshipsInheritance(
      List<String> existingInheritedRelationships,
      List<Map<String, Object>> addedRelationshipsInheritance, Vertex KRVertex,
      List<Map<String, Object>> modifiedRelationships) throws Exception
  {
    for (Map<String, Object> addedRelationshipInheritance : addedRelationshipsInheritance) {
      String relationshipId = (String) addedRelationshipInheritance
          .get(IModifiedRelationshipPropertyModel.ID);
      if (existingInheritedRelationships.contains(relationshipId)) {
        continue;
      }
      String couplingType = (String) addedRelationshipInheritance
          .get(IModifiedRelationshipPropertyModel.COUPLING_TYPE);
      Vertex relationshipVertex = UtilClass.getVertexByIndexedId(relationshipId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
      Edge edge = KRVertex.addEdge(RelationshipLabelConstants.HAS_INHERITANCE_RELATIONSHIP,
          relationshipVertex);
      edge.setProperty(IModifiedRelationshipPropertyModel.COUPLING_TYPE, couplingType);
      
      modifiedRelationships.add(getModifiedRelationshipModel(relationshipId, couplingType));
    }
  }
  
  private static Map<String, Object> getModifiedRelationshipModel(String relationshipId,
      String couplingType)
  {
    Map<String, Object> modifiedRelationshipModel = new HashMap<>();
    modifiedRelationshipModel.put(IModifiedRelationshipPropertyModel.COUPLING_TYPE, couplingType);
    modifiedRelationshipModel.put(IModifiedRelationshipPropertyModel.ID, relationshipId);
    return modifiedRelationshipModel;
  }
  
  private static void handleDeletedRelationshipsInheritanceAndFillExisting(
      List<String> existingInheritedRelationships, List<String> deletedRelationshipsInheritance,
      Iterable<Edge> inheritedRelationshipEdges, List<String> deletedRelationships)
  {
    for (Edge inheritedRelationshipEdge : inheritedRelationshipEdges) {
      Vertex inheritedRelationship = inheritedRelationshipEdge.getVertex(Direction.IN);
      String relationshipId = inheritedRelationship.getProperty(CommonConstants.CODE_PROPERTY);
      if (deletedRelationshipsInheritance.contains(relationshipId)) {
        inheritedRelationshipEdge.remove();
        deletedRelationships.add(relationshipId);
        continue;
      }
      existingInheritedRelationships.add(relationshipId);
    }
  }

  private static void linkPropertyCollectionToNatureNode(Vertex natureNode,
      String propertyCollectionId) throws Exception
  {
    Vertex propertyCollection = UtilClass.getVertexById(propertyCollectionId,
        VertexLabelConstants.PROPERTY_COLLECTION);
    natureNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION, propertyCollection);
  }
  
  private static void unlinkPropertyCollectionAndContextsFromNatureNode(Vertex natureNode,
      Boolean isTypeChanged) throws Exception
  {
    Iterable<Edge> relationshipEdges = natureNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_COLLECTION);
    for (Edge propertyCollectionEdge : relationshipEdges) {
      propertyCollectionEdge.remove();
    }
    
    if (isTypeChanged != null && isTypeChanged) {
      Iterable<Edge> contextEdges = natureNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      for (Edge contextEdge : contextEdges) {
        contextEdge.remove();
      }
    }
  }
  
  private static Vertex getOrCreateKlassRelationshipNatureNode(Vertex klassNode,
      String relationshipType, String natureType) throws Exception
  {
    Vertex natureNode = null;
    Iterator<Vertex> klassNatureVertices = klassNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF)
        .iterator();
    Boolean isNatureNodeExist = false;
    while (klassNatureVertices.hasNext()) {
      
      natureNode = klassNatureVertices.next();
      Object existingRelationshipType = natureNode
          .getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
      
      if (existingRelationshipType.equals(relationshipType)) {
        natureNode.setProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE, relationshipType);
        isNatureNodeExist = true;
        break;
      }
    }
    if (!isNatureNodeExist) {
      if (relationshipType == null) {
        relationshipType = "";
      }
      natureNode = KlassUtils.createKlassNatureNode(klassNode, relationshipType, natureType);
    }
    
    return natureNode;
  }
  
  private static Vertex getProductVariantNatureNode(Vertex klassNode) throws Exception
  {
    Vertex natureNode = null;
    Iterator<Vertex> klassNatureVertices = klassNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF)
        .iterator();
    while (klassNatureVertices.hasNext()) {
      natureNode = klassNatureVertices.next();
      if (natureNode.getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE)
          .equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
        break;
      }
    }
    
    return natureNode;
  }
  
  public static void manageTasks(Map<String, Object> klassADM, Vertex klassNode) throws Exception
  {
    List<String> addedTasks = (List<String>) klassADM.get(IKlassSaveModel.ADDED_TASKS);
    for (String addedTask : addedTasks) {
      Vertex taskNode = UtilClass.getVertexById(addedTask, VertexLabelConstants.ENTITY_TYPE_TASK);
      try {
        getEdgeBetweenKlassAndTaskNode(klassNode, taskNode);
      }
      catch (NotFoundException e) {
        klassNode.addEdge(RelationshipLabelConstants.HAS_TASK, taskNode);
      }
    }
    
    List<String> deletedTasks = (List<String>) klassADM.get(IKlassSaveModel.DELETED_TASKS);
    for (String deletedTask : deletedTasks) {
      Vertex taskNode = UtilClass.getVertexById(deletedTask, VertexLabelConstants.ENTITY_TYPE_TASK);
      Edge hasTask;
      try {
        hasTask = getEdgeBetweenKlassAndTaskNode(klassNode, taskNode);
      }
      catch (NotFoundException e) {
        continue;
      }
      hasTask.remove();
    }
  }
  
  private static Edge getEdgeBetweenKlassAndTaskNode(Vertex klassNode, Vertex taskNode)
      throws Exception
  {
    String query = "SELECT FROM " + RelationshipLabelConstants.HAS_TASK + " where in = "
        + taskNode.getId() + " and out = " + klassNode.getId();
    
    Iterable<Edge> edges = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    if (!edges.iterator()
        .hasNext()) {
      throw new NotFoundException();
    }
    Edge hasTask = edges.iterator()
        .next();
    if (edges.iterator()
        .hasNext()) {
      throw new MultipleLinkFoundException();
    }
    return hasTask;
  }
  
  /** @param klassPropertyNodes */
  private static void incrementVersionIdsofAllKlassNodeInheritingKp(List<Vertex> klassPropertyNodes)
  {
    for (Vertex klassPropertyNode : klassPropertyNodes) {
      
      Iterable<Edge> hasKlassPropertyEdges = klassPropertyNode.getEdges(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Edge hasKlassProperty : hasKlassPropertyEdges) {
        Boolean isInherited = hasKlassProperty.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
        // update only for children hence having isInherited = true
        if (isInherited) {
          Vertex klassNode = hasKlassProperty.getVertex(Direction.OUT);
          UtilClass.addNodesForVersionIncrement(klassNode);
        }
      }
    }
  }
  
  /**
   * This function simply checks if klass label is updated and template label is
   * equals to oldKlassLabel and if this is true then it update template label
   *
   * @author Lokesh
   * @param klassADM
   * @param klassNode
   * @throws Exception
   */
  public static void updateTemplateLabelIfKlassLabelChanged(Map<String, Object> klassADM,
      Vertex klassNode) throws Exception
  {
    String oldKlassLabel = (String) UtilClass.getValueByLanguage(klassNode, IKlass.LABEL);
    String newKlassLabel = (String) klassADM.get(IKlass.LABEL);
    if (newKlassLabel.equals(oldKlassLabel)) {
      return;
    }
    updateTemplateLabelOnKlassLabelChanged(klassNode, oldKlassLabel, newKlassLabel);
    updateContextLabelOnKlassLabelChanged(klassNode, oldKlassLabel, newKlassLabel);
  }
  
  public static void updateTemplateLabelOnKlassLabelChanged(Vertex klassNode, String oldKlassLabel,
      String newKlassLabel)
  {
    Vertex templateNode = null;
    Iterator<Edge> edgesIterator = klassNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE)
        .iterator();
    if (!edgesIterator.hasNext()) {
      return;
    }
    
    Edge hasTemplateEdge = edgesIterator.next();
    Boolean isInherited = hasTemplateEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
    if (isInherited != null && isInherited) {
      return;
    }
    
    templateNode = hasTemplateEdge.getVertex(Direction.IN);
    String templateLabel = (String) UtilClass.getValueByLanguage(templateNode, ITemplate.LABEL);
    if (!templateLabel.equals(oldKlassLabel)) {
      return;
    }
    templateNode.setProperty(EntityUtil.getLanguageConvertedField(ITemplate.LABEL), newKlassLabel);
  }
  
  /*
  public static void deleteTemplateIfSectionsDontExist(Vertex klassTaxonomy, List<Vertex> taxonomyAndChildNodes) throws Exception
  {
    // Scenario for following code
    // Create parent child A-B
    // add PC X to B
    // add PC X, Y to A
    // remove X from B, and after that remove Y from A
    // expected output: template for B should get deleted
    Iterable<Vertex> immediateChilds = klassTaxonomy.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex immediateChild : immediateChilds) {
      Iterator<Edge> parentKlassSectionRelationships = immediateChild
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF).iterator();
      Iterator<Vertex> parentContextKlassIterable = immediateChild.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS).iterator();
  
      Iterator<Vertex> parentRelationshipsIterable = checkTaxonomyHasRelationships(immediateChild);
  
      if (parentKlassSectionRelationships.hasNext() || parentContextKlassIterable.hasNext()
          || parentRelationshipsIterable.hasNext()) {
        continue;
      }
      Vertex template = TemplateUtils.getTemplateFromKlassIfExist(immediateChild);
      if(template != null) {
        DeleteTemplateUtils.deleteTemplateNode(template);
      }
    }
  
    Iterator<Edge> klassTaxonomySectionRelationships = klassTaxonomy
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF).iterator();
    Iterator<Vertex> contextKlassIterable = klassTaxonomy.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS).iterator();
  
    Iterator<Vertex> relationshipsIterable = checkTaxonomyHasRelationships(klassTaxonomy);
  
    if (klassTaxonomySectionRelationships.hasNext() || contextKlassIterable.hasNext()
        || relationshipsIterable.hasNext()) {
      return;
    }
    Iterator<Edge> edgesIterator = klassTaxonomy
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE).iterator();
    if (!edgesIterator.hasNext()) {
      return;
    }
  
    Edge hasTemplateEdge = edgesIterator.next();
    Boolean isInherited = (Boolean) hasTemplateEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
  
    if (isInherited != null && isInherited) { //if klassTaxonomy is not the owner of template unlink it
      List<Edge> edgesToRemove = new ArrayList<>();
      Vertex templateNode = hasTemplateEdge.getVertex(Direction.IN);
      Iterable<Edge> edges = templateNode.getEdges(Direction.IN, RelationshipLabelConstants.HAS_TEMPLATE);
      for (Edge edge : edges) {
        Vertex taxonomyConnectedToTemplate = edge.getVertex(Direction.IN);
        if(taxonomyAndChildNodes.contains(taxonomyConnectedToTemplate)) {
          edgesToRemove.add(edge);
        }
      }
  
      for (Edge edge : edgesToRemove) {
        edge.remove();
      }
    }
  
    if (isInherited == null || !isInherited) { //if klassTaxonomy is the owner of template delete it
      Vertex templateNode = hasTemplateEdge.getVertex(Direction.IN);
      DeleteTemplateUtils.deleteTemplateNode(templateNode);
    }
  
    // Scenario for following code
    // Create parent child A-B
    // add PC X to B
    // add PC X, Y to A
    // remove X from B, and after that remove X,Y from A
    // expected output: template for B should get deleted
    for (Vertex immediateChild : immediateChilds) {
      Iterator<Edge> parentKlassSectionRelationships = immediateChild
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF).iterator();
      Iterator<Vertex> parentContextKlassIterable = immediateChild.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS).iterator();
  
      Iterator<Vertex> parentRelationshipsIterable = checkTaxonomyHasRelationships(immediateChild);
  
      if (parentKlassSectionRelationships.hasNext() || parentContextKlassIterable.hasNext()
          || parentRelationshipsIterable.hasNext()) {
        continue;
      }
      Vertex template = TemplateUtils.getTemplateFromKlassIfExist(immediateChild);
      if(template != null) {
        DeleteTemplateUtils.deleteTemplateNode(template);
      }
    }
  }
  */
  /**
   * add, modify or delete tags in attributes through attribute context to KP
   *
   * @author Lokesh
   * @param modifiedElement
   * @param klassPropertyNode
   * @throws Exception
   */
  private static void updateTagsInAttributes(HashMap<String, Object> modifiedElement,
      Vertex klassPropertyNode) throws Exception
  {
    String type = klassPropertyNode.getProperty(ISectionElement.TYPE);
    
    Map<String, Object> unitContextMap = (Map<String, Object>) modifiedElement
        .get(IModifiedSectionAttributeModel.CONTEXT);
    
    if (!type.equals(CommonConstants.ATTRIBUTE) || unitContextMap == null) {
      return;
    }
    List<Map<String, Object>> addedTags = (List<Map<String, Object>>) unitContextMap
        .get(IModifiedContextModel.ADDED_TAGS);
    List<Map<String, Object>> modifiedTags = (List<Map<String, Object>>) unitContextMap
        .get(IModifiedContextModel.MODIFIED_TAGS);
    List<String> deletedTag = (List<String>) unitContextMap.get(IModifiedContextModel.DELETED_TAGS);
    
    Vertex attributeContext = KlassUtils.getAttributeVariant(klassPropertyNode);
    
    List<String> fieldsToExclude = Arrays.asList(IModifiedContextModel.ADDED_ENTITIES,
        IModifiedContextModel.DELETED_ENTITIES, IModifiedContextModel.ADDED_TAGS,
        IModifiedContextModel.DELETED_TAGS, IModifiedContextModel.MODIFIED_TAGS);
    
    if (attributeContext == null) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
      attributeContext = UtilClass.createNode(unitContextMap, vertexType, fieldsToExclude);
      klassPropertyNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, attributeContext);
    }
    else {
      UtilClass.saveNode(unitContextMap, attributeContext, fieldsToExclude);
    }
    
    VariantContextUtils.manageEntities(attributeContext, unitContextMap);
    
    manageAddedTagInAttribute(addedTags, attributeContext);
    
    VariantContextUtils.manageModifiedContextTag(attributeContext, modifiedTags);
    
    manageDeletedTagFromAttribute(deletedTag, attributeContext);
  }
  
  /**
   * add tags in attributes through attribute context
   *
   * @author Lokesh
   * @param addedTags
   * @param attributeContext
   * @throws Exception
   */
  private static void manageAddedTagInAttribute(List<Map<String, Object>> addedTags,
      Vertex attributeContext) throws Exception
  {
    if (addedTags.isEmpty()) {
      return;
    }
    
    for (Map<String, Object> addedTag : addedTags) {
      
      String tagId = (String) addedTag.get(IAddedVariantContextTagsModel.TAG_ID);
      Vertex contextTagNode = getContextTagNode(attributeContext, tagId);
      
      if (contextTagNode != null) {
        // It means tag is already added in that attribute
        continue;
      }
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.VARIANT_CONTEXT_TAG, CommonConstants.CODE_PROPERTY);
      
      Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      contextTagNode = UtilClass.createNode(new HashMap<String, Object>(), vertexType,
          new ArrayList<>());
      Edge contextTagNodeEdge = attributeContext.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG,
          contextTagNode);
      contextTagNodeEdge.setProperty(IVariantContextTagModel.TAG_ID, UtilClass.getCodeNew(tagNode));
      
      contextTagNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY, tagNode);
      
      List<String> tagValues = (List<String>) addedTag
          .get(IAddedVariantContextTagsModel.TAG_VALUE_IDS);
      
      // connect all tagValues of that perticular tag to context
      Iterable<Vertex> tagValuesVertices = tagNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      for (Vertex tagValueNode : tagValuesVertices) {
        
        String tagValueId = UtilClass.getCodeNew(tagValueNode);
        if (!tagValues.contains(tagValueId)) {
          continue;
        }
        Edge contextTagValueNodeEdge = contextTagNode
            .addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE, tagValueNode);
        contextTagValueNodeEdge.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID, tagValueId);
      }
    }
  }
  
  /**
   * delete tags in attributes from attribute context. And if no tag is
   * connected to attribute context then delete it
   *
   * @author Lokesh
   * @param deletedTags
   * @param attributeContext
   * @throws Exception
   */
  private static void manageDeletedTagFromAttribute(List<String> deletedTags,
      Vertex attributeContext)
  {
    if (attributeContext == null || deletedTags.isEmpty()) {
      return;
    }
    
    // delete context tag
    for (String deletedTagId : deletedTags) {
      Vertex contextTagNode = getContextTagNode(attributeContext, deletedTagId);
      
      if (contextTagNode == null) {
        // It means that this tag is no longer connected to that attribute hence
        // remove
        continue;
      }
      contextTagNode.remove();
    }
    /*
    // if no tag in connected to context then delete that context
    Iterator<Vertex> vertices = attributeContext.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG).iterator();
    if (!vertices.hasNext()) {
      attributeContext.remove();
    }*/
  }
  
  /**
   * @author Lokesh
   * @param oldKlassPropertyNode
   * @param newKlassPropertyNode
   */
  private static void copyExistingAttributeContext(Vertex oldKlassPropertyNode,
      Vertex newKlassPropertyNode)
  {
    Vertex oldAttributeContextNode = KlassUtils.getAttributeVariant(oldKlassPropertyNode);
    
    if (oldAttributeContextNode == null) {
      return;
    }
    Vertex attributeContext = UtilClass.createDuplicateNode(oldAttributeContextNode);
    
    copyInheritedContextProperty(oldAttributeContextNode, attributeContext);
    newKlassPropertyNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, attributeContext);
  }
  
  /**
   * inherit All context specific Nodes
   *
   * @author Lokesh
   * @param inHeritedAttributeContext
   * @param newAttributeContext
   */
  private static void copyInheritedContextProperty(Vertex inHeritedAttributeContext,
      Vertex newAttributeContext)
  {
    Iterable<Vertex> contextTagVertices = inHeritedAttributeContext.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    for (Vertex contextTagNode : contextTagVertices) {
      
      // copy contextTagNode
      Vertex duplicateContextTagNode = UtilClass.createDuplicateNode(contextTagNode);
      newAttributeContext.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG,
          duplicateContextTagNode);
      
      // copy tag Node
      Vertex tagNode = contextTagNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY)
          .iterator()
          .next();
      Edge hasContextTagProperty = duplicateContextTagNode
          .addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY, tagNode);
      hasContextTagProperty.setProperty(IVariantContextTagModel.TAG_ID,
          UtilClass.getCodeNew(tagNode));
      
      // copy tagValue Nodes
      Iterable<Vertex> contextTagValues = contextTagNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE);
      for (Vertex tagValueNode : contextTagValues) {
        Edge hasContextTagValues = duplicateContextTagNode
            .addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE, tagValueNode);
        hasContextTagValues.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID,
            UtilClass.getCodeNew(tagValueNode));
      }
    }
  }
  
  /**
   * @author Aayush
   * @param klassNode
   * @param oldKlassLabel
   * @param newKlassLabel
   * @throws MultipleVertexFoundException
   */
  public static void updateContextLabelOnKlassLabelChanged(Vertex klassNode, String oldKlassLabel,
      String newKlassLabel) throws MultipleVertexFoundException
  {
    String natureType = (String) klassNode.getProperty(IKlass.NATURE_TYPE);
    if (natureType == null || natureType.isEmpty()) {
      return;
    }
    if (CommonConstants.CONTEXTUAL_KLASS_TYPES.contains(natureType)) {
      Vertex context = KlassUtils.getContextForKlass(klassNode);
      
      String contextLabel = (String) UtilClass.getValueByLanguage(context, ITemplate.LABEL);
      if (!contextLabel.equals(oldKlassLabel)) {
        return;
      }
      context.setProperty(EntityUtil.getLanguageConvertedField(IVariantContext.LABEL),
          newKlassLabel);
    }
  }
  
  public static Iterator<Vertex> checkTaxonomyHasRelationships(Vertex attributionTaxonomyNode)
      throws Exception
  {
    String query = "select from " + VertexLabelConstants.KLASS_RELATIONSHIP
        + " where in('has_klass_property') contains (code='"
        + UtilClass.getCodeNew(attributionTaxonomyNode) + "')";
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    return vertices.iterator();
  }
  
  private static void manageSelectedTagValuesInKPNode(Vertex modifiedSectionElement,
      HashMap<String, Object> modifiedElement) throws Exception
  {
    String type = modifiedSectionElement.getProperty(ISectionElement.TYPE);
    if (type.equals(CommonConstants.TAG_PROPERTY)) {
      List<String> addedselectedTagValues = (List<String>) modifiedElement
          .get(IModifiedSectionTagModel.ADDED_SELECTED_TAG_VALUES);
      List<String> deletedselectedTagValues = (List<String>) modifiedElement
          .get(IModifiedSectionTagModel.DELETED_SELECTED_TAG_VALUES);
      
      for (String addedTagValueId : addedselectedTagValues) {
        Vertex tagValueNode = UtilClass.getVertexById(addedTagValueId,
            VertexLabelConstants.ENTITY_TAG);
        modifiedSectionElement.addEdge(RelationshipLabelConstants.HAS_KLASS_TAG_VALUE,
            tagValueNode);
      }
      
      for (String deletedTagValueId : deletedselectedTagValues) {
        Vertex tagValueNode = UtilClass.getVertexById(deletedTagValueId,
            VertexLabelConstants.ENTITY_TAG);
        Edge hasSelectedTagValue = getHasSelectedTagValueEdge(modifiedSectionElement, tagValueNode);
        if (hasSelectedTagValue != null) {
          hasSelectedTagValue.remove();
        }
      }
    }
  }
  
  private static Edge getHasSelectedTagValueEdge(Vertex modifiedSectionElement, Vertex tagValueNode)
  {
    Iterable<Edge> iterable = modifiedSectionElement.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_TAG_VALUE);
    String tagValueId = UtilClass.getCodeNew(tagValueNode);
    Edge edgeToReturn = null;
    for (Edge edge : iterable) {
      Vertex tagValueNodeToCompare = edge.getVertex(Direction.IN);
      String tagValueIdToCompare = UtilClass.getCodeNew(tagValueNodeToCompare);
      if (tagValueId.equals(tagValueIdToCompare)) {
        edgeToReturn = edge;
        break;
      }
    }
    
    return edgeToReturn;
  }
  
  private static void copySelectedTagValuesToNewKlassPropertyNode(Vertex modifiedSectionElement,
      Vertex duplicateSectionElementNode)
  {
    Iterable<Edge> hasDefaultTagValues = modifiedSectionElement.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_TAG_VALUE);
    for (Edge edge : hasDefaultTagValues) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      duplicateSectionElementNode.addEdge(RelationshipLabelConstants.HAS_KLASS_TAG_VALUE,
          tagValueNode);
    }
  }
  
  /**
   * @author Lokesh
   * @param klassADM
   * @param klassNode
   */
  public static void updateNatureRelationshipSideLabel(Map<String, Object> klassADM,
      Vertex klassNode)
  {
    String klassId = (String) klassADM.get(IKlassSaveModel.ID);
    String newLabel = (String) klassADM.get(IKlassSaveModel.LABEL);
    String oldLabel = (String) UtilClass.getValueByLanguage(klassNode, IKlass.LABEL);
    if (newLabel.equals(oldLabel)) {
      return;
    }
    
    String query = "select from " + VertexLabelConstants.KLASS_RELATIONSHIP + " Where " + "in('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').code = '" + klassId + "'"
        + "and out('" + RelationshipLabelConstants.HAS_PROPERTY + "').@class contains '"
        + VertexLabelConstants.NATURE_RELATIONSHIP + "'";
    
    Iterable<Vertex> kRVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Vertex kR : kRVertices) {
      Vertex natureRelarionshipNode = kR
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator()
          .next();
      Map<String, Object> side2 = natureRelarionshipNode.getProperty(IRelationship.SIDE2);
      side2.put(IRelationshipSide.LABEL, newLabel);
      natureRelarionshipNode.setProperty(IRelationship.SIDE2, side2);
      Iterator<Vertex> kRIterator = natureRelarionshipNode
          .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex kNR = kRIterator.next();
      if (kNR.equals(kR)) {
        kNR = kRIterator.next();
      }
      Map<String, Object> relationshipSide = kNR.getProperty(CommonConstants.RELATIONSHIP_SIDE);
      relationshipSide.put(IRelationshipSide.LABEL, newLabel);
      kNR.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide);
    }
    
    Iterable<Vertex> kNRVertices = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    
    for (Vertex kNR : kNRVertices) {
      Vertex natureRelarionshipNode = kNR
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator()
          .next();
      Map<String, Object> side1 = natureRelarionshipNode.getProperty(IRelationship.SIDE1);
      side1.put(IRelationshipSide.LABEL, newLabel);
      natureRelarionshipNode.setProperty(IRelationship.SIDE1, side1);
      Iterator<Vertex> kRIterator = natureRelarionshipNode
          .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      if (!kRIterator.hasNext()) {
        continue;
      }
      Vertex kR = kRIterator.next();
      if (kR.equals(kNR)) {
        kR = kRIterator.next();
      }
      Map<String, Object> relationshipSide = kR.getProperty(CommonConstants.RELATIONSHIP_SIDE);
      relationshipSide.put(IRelationshipSide.LABEL, newLabel);
      kR.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide);
    }
  }
  
  /**
   * used for task import and also handle the task code which are not present
   * @param klassADM
   * @param klassNode
   * @param failure 
   * @throws Exception
   */
  public static void manageTasksImport(Map<String, Object> klassADM, Vertex klassNode, IExceptionModel failure) throws Exception
  {
    List<String> addedTasks = (List<String>) klassADM.get(IKlassSaveModel.ADDED_TASKS);
    for (String addedTask : addedTasks) {
      try {
        Vertex taskNode = UtilClass.getVertexById(addedTask, VertexLabelConstants.ENTITY_TYPE_TASK);
        try {
          getEdgeBetweenKlassAndTaskNode(klassNode, taskNode);
        }
        catch (NotFoundException e) {
          klassNode.addEdge(RelationshipLabelConstants.HAS_TASK, taskNode);
        }
      }catch(Exception n) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, n, null, (String) klassNode.getProperty(IKlass.CODE));
      }
    }
  }
}

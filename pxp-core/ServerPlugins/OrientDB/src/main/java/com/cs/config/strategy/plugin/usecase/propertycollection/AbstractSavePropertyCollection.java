package com.cs.config.strategy.plugin.usecase.propertycollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * @author tauseef
 */
public abstract class AbstractSavePropertyCollection extends AbstractOrientPlugin {

  public AbstractSavePropertyCollection(OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  @SuppressWarnings("unchecked")
  protected void manageDeletedElements(Map<String, Object> propertyCollection,
      Vertex propertyCollectionNode, Map<String, List<String>> deletedPropertyMap, List<String> propertySequenceIds) throws Exception
  {
    List<String> attributeIds = propertyCollectionNode
        .getProperty(IPropertyCollection.ATTRIBUTE_IDS);
    List<String> tagIds = propertyCollectionNode.getProperty(IPropertyCollection.TAG_IDS);
    List<Map<String, Object>> deletedElements = (List<Map<String, Object>>) propertyCollection
        .get(ISavePropertyCollectionModel.DELETED_ELEMENTS);
    for (Map<String, Object> deletedElement : deletedElements) {
      String elementId = (String) deletedElement.get(IPropertyCollectionElement.ID);
      String elementType = (String) deletedElement.get(IPropertyCollectionElement.TYPE);
      
      Vertex elementNode = PropertyCollectionUtils.getElementVertexFromIdAndType(elementId,
          elementType);
      String query = "DELETE EDGE FROM " + elementNode.getId() + " to "
          + propertyCollectionNode.getId();
      UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      // delete section element..
      Iterable<Edge> propertyCollectionOfEdges = propertyCollectionNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.PROPERTY_COLLECTION_OF);
      for (Edge propertyCollectionOf : propertyCollectionOfEdges) {
        Vertex sectionNode = propertyCollectionOf.getVertex(Direction.IN);
        String sectionId = sectionNode.getProperty(CommonConstants.CODE_PROPERTY);
        Iterable<Vertex> klassNodes = sectionNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
        for (Vertex klassNode : klassNodes) {
          // increment versionIds for all klass,taxonomy and cntext using PC
          UtilClass.addNodesForVersionIncrement(klassNode);
          Vertex klassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(klassNode,
              elementNode);
          if (klassPropertyNode == null) {
            continue;
          }
          
          Edge hasKlassProperty = PropertyCollectionUtils
              .getLinkBetweenKlassAndKlassProperty(klassNode, klassPropertyNode);
          List<String> utilizingSectionIds = hasKlassProperty
              .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
          utilizingSectionIds.remove(sectionId);
          
          if (utilizingSectionIds.size() != 0 || elementType.equals(CommonConstants.RELATIONSHIP)) {
            hasKlassProperty.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
                utilizingSectionIds);
            continue;
          }
          String propertyId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
          Set<Vertex> nodesToDelete = new HashSet<Vertex>();
          KlassUtils.deleteKlassPropertyNode(nodesToDelete, false, klassPropertyNode,
              deletedPropertyMap);
          GlobalPermissionUtils
              .deletePropertyPermissionNode(new HashSet<>(Arrays.asList(klassNode)), propertyId);
          for (Vertex nodeToDelete : nodesToDelete) {
            nodeToDelete.remove();
          }
        }
      }
      
      if (elementType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
        attributeIds.remove(elementId);
      }
      else if (elementType.equals(CommonConstants.TAG_PROPERTY)) {
        tagIds.remove(elementId);
      }
      propertySequenceIds.remove(elementId);
    }
  }
  
  @SuppressWarnings("unchecked")
  protected void manageModifiedElements(Map<String, Object> propertyCollection,
      Vertex propertyCollectionNode, List<String> propertySequenceIds) throws Exception, NotFoundException, MultipleLinkFoundException
  {
    List<Map<String, Object>> modifiedElements = (List<Map<String, Object>>) propertyCollection
        .get(ISavePropertyCollectionModel.MODIFIED_ELEMENTS);
    for (Map<String, Object> modifeidElement : modifiedElements) {
      String elementId = (String) modifeidElement.get(IPropertyCollectionElement.ID);
      Integer elementIndex = (Integer) modifeidElement.get(IPropertyCollectionElement.INDEX);
      propertySequenceIds.remove(elementId);
      propertySequenceIds.add(elementIndex - 1, elementId);
    }
  }

  @SuppressWarnings("unchecked")
  protected void manageAddedElements(Map<String, Object> propertyCollection, Vertex propertyCollectionNode,
      List<Map<String, Object>> defaultValueChangeList, List<String> propertySequenceIds) throws Exception
  {
    List<Map<String, Object>> addedElements = (List<Map<String, Object>>) propertyCollection
        .get(ISavePropertyCollectionModel.ADDED_ELEMENTS);
    List<String> attributeIds = propertyCollectionNode.getProperty(IPropertyCollection.ATTRIBUTE_IDS);
    List<String> tagIds = propertyCollectionNode.getProperty(IPropertyCollection.TAG_IDS);
    
    for (Map<String, Object> addedElement : addedElements) {
      String elementId = (String) addedElement.get(IPropertyCollectionElement.ID);
      String elementType = (String) addedElement.get(IPropertyCollectionElement.TYPE);
      Integer elementIndex = (Integer) addedElement.get(IPropertyCollectionElement.INDEX);
      //Don't add duplicate element
      if (!propertySequenceIds.contains(elementId)) {
        Vertex elementNode = PropertyCollectionUtils.getElementVertexFromIdAndType(elementId, elementType);
        Edge entityTo = elementNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO, propertyCollectionNode);
        entityTo.setProperty(CommonConstants.TYPE_PROPERTY, elementType);
        
        // add element to the klass sections...
        Iterable<Vertex> sections = propertyCollectionNode.getVertices(Direction.OUT, RelationshipLabelConstants.PROPERTY_COLLECTION_OF);
        for (Vertex section : sections) {
          Iterable<Edge> sectionOfRelations = section.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
          // get klassNode for the section where isInherited is false
          Vertex klassNode = null;
          for (Edge sectionOf : sectionOfRelations) {
            Boolean isInherited = (Boolean) sectionOf.getProperty("isInherited");
            Vertex connectedKlassNode = sectionOf.getVertex(Direction.IN);
            ;
            if (isInherited == false) {
              klassNode = connectedKlassNode;
            }
            // increment versionIds for all klass,taxonomy and context using PC
            UtilClass.addNodesForVersionIncrement(connectedKlassNode);
          }
          String vertexLabel = klassNode.getProperty("@Class");
          Vertex klassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(klassNode, elementNode);
          if (klassPropertyNode == null && !elementType.equals(CommonConstants.RELATIONSHIP)) {
            List<String> klassAndChildIdsForProperty = new ArrayList<>();
            klassAndChildIdsForProperty.add(UtilClass.getCodeNew(klassNode));
            klassPropertyNode = KlassUtils.createNewKlassPropertyNode(elementType, klassNode, elementNode,
                (String) section.getProperty(CommonConstants.CODE_PROPERTY), null, new HashMap<>(), klassAndChildIdsForProperty);
            Map<String, Object> addedPropertyMap = new HashMap<>();
            KlassUtils.fillAddedPropertyMap(addedPropertyMap, klassPropertyNode, vertexLabel);
            if (!addedPropertyMap.isEmpty()) {
              addedPropertyMap.put(IDefaultValueChangeModel.KLASS_AND_CHILDRENIDS, klassAndChildIdsForProperty);
              defaultValueChangeList.add(addedPropertyMap);
            }
          }
          else if (elementType.equals(CommonConstants.RELATIONSHIP) && klassPropertyNode == null) {
            updateUtilizingSectionIdsForHasKPOfChildNodes(elementNode, section, klassNode);
          }
          else {
            Iterator<Edge> hasKlassPropertyIterator = klassPropertyNode
                .getEdges(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY).iterator();
            while (hasKlassPropertyIterator.hasNext()) {
              Edge hasKlassProperty = hasKlassPropertyIterator.next();
              Vertex klassVertex = hasKlassProperty.getVertex(Direction.OUT);
              if (!KlassUtils.checkIfSectionIsLinkedToTheKlass(UtilClass.getCodeNew(section), klassVertex)) {
                continue;
              }
              List<String> utilizingSectionIds = hasKlassProperty.getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
              if (utilizingSectionIds == null) {
                utilizingSectionIds = new ArrayList<String>();
              }
              utilizingSectionIds.add(section.getProperty(CommonConstants.CODE_PROPERTY));
              hasKlassProperty.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY, utilizingSectionIds);
            }
            updateUtilizingSectionIdsForHasKPOfChildNodes(elementNode, section, klassNode);
          }
        }
        
        if (elementType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
          attributeIds.add(elementId);
        }
        else if (elementType.equals(CommonConstants.TAG_PROPERTY)) {
          tagIds.add(elementId);
        }
        propertySequenceIds.add(elementIndex - 1, elementId);
      }
    }
  }
  
  /**
   * Update UtilizingSectionIds For child Nodes of paramentered KlassNode if
   * that elementNode is linked with child via KP
   *
   * @param elementNode
   * @param sectionNode
   * @param klassNode
   * @throws Exception
   */
  private void updateUtilizingSectionIdsForHasKPOfChildNodes(Vertex elementNode, Vertex sectionNode,
      Vertex klassNode) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = (String) klassNode.getId()
        .toString();
    Iterable<Vertex> i = graph
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
        .execute();
    List<Vertex> klassAndChildNodes = new ArrayList<>();
    for (Vertex node : i) {
      klassAndChildNodes.add(node);
    }
    klassAndChildNodes.remove(klassNode);
    for (Vertex childNode : klassAndChildNodes) {
      Iterator<Vertex> childSectionNodes = childNode
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
          .iterator();
      while (childSectionNodes.hasNext()) {
        Vertex tempSectionNode = childSectionNodes.next();
        if (tempSectionNode.equals(sectionNode)) {
          Vertex childKlassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(childNode,
              elementNode);
          if (childKlassPropertyNode != null) {
            Edge hasKlassPropertyOfChild = KlassUtils
                .getHasKlassPropertyEdgeFromKlassPropertyAndKLass(childKlassPropertyNode,
                    UtilClass.getCodeNew(childNode));
            List<String> utilizingSectionIds = hasKlassPropertyOfChild
                .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
            if (utilizingSectionIds == null) {
              utilizingSectionIds = new ArrayList<String>();
            }
            if (!utilizingSectionIds.contains(UtilClass.getCodeNew(sectionNode))) {
              utilizingSectionIds.add(sectionNode.getProperty(CommonConstants.CODE_PROPERTY));
            }
            hasKlassPropertyOfChild.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
                utilizingSectionIds);
          }
        }
      }
    }
  }

}

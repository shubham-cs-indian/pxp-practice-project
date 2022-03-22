package com.cs.config.strategy.plugin.usecase.propertycollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.propertycollection.IBulkDeletePropertyCollectionReturnModel;
import com.cs.core.config.interactor.model.propertycollection.IBulkDeleteSuccessPropertyCollectionModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;

public class DeletePropertyCollection extends AbstractOrientPlugin {
  
  public DeletePropertyCollection(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeletePropertyCollection/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> ids = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    List<String> deletedIds = new ArrayList<>();
    Map<String, List<String>> associatedTypeIds = new HashMap<>();
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    for (String propertyCollectionId : ids) {
      Vertex propertyCollectionNode = null;
      try {
        propertyCollectionNode = UtilClass.getVertexById(propertyCollectionId,
            VertexLabelConstants.PROPERTY_COLLECTION);
        if(ValidationUtils.vaildateIfStandardEntity(propertyCollectionNode))
          continue;
        deleteAttachedSectionAndKlassPropertyNodes(propertyCollectionNode, associatedTypeIds);
        deleteEntryFromSequenceNode(propertyCollectionNode);
        // delete property collection permissions..
        Iterable<Vertex> iterable = propertyCollectionNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION);
        for (Vertex permissionNode : iterable) {
          permissionNode.remove();
        }
        
        deleteTemplatePermissionNodesByPropertyCollectionNode(propertyCollectionNode);
        
        TabUtils.updateTabOnEntityDelete(propertyCollectionNode);
        
        propertyCollectionNode.remove();
        deletedIds.add(propertyCollectionId);
        AuditLogUtils.fillAuditLoginfo(auditInfoList, propertyCollectionNode,
            Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE, Elements.PROPERTY_COLLECTION);
      }
      catch (PluginException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
        
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null, null);
      }
    }
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IBulkDeleteSuccessPropertyCollectionModel.ASSOCIATED_TYPE_IDS,
        associatedTypeIds);
    successMap.put(IBulkDeleteSuccessPropertyCollectionModel.SUCCESS, deletedIds);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkDeleteReturnModel.SUCCESS, successMap);
    responseMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    responseMap.put(IBulkDeletePropertyCollectionReturnModel.AUDIT_LOG_INFO, auditInfoList);
    return responseMap;
  }
  
  @SuppressWarnings("unchecked")
  private void deleteAttachedSectionAndKlassPropertyNodes(Vertex propertyCollectionNode,
      Map<String, List<String>> associatedTypeIds) throws Exception
  {
    Iterable<Vertex> iterable = propertyCollectionNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.PROPERTY_COLLECTION_OF);
    Set<Vertex> verticesToRemove = new HashSet<>();
    Set<Vertex> klassesLinkedToThePC = new HashSet<>();
    
    for (Vertex sectionNode : iterable) {
      Iterator<Vertex> klassesIterator = sectionNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
          .iterator();
      Set<Vertex> klassVertices = new HashSet<>(IteratorUtils.toList(klassesIterator));
      klassesLinkedToThePC.addAll(klassVertices);
      String sectionId = sectionNode.getProperty(CommonConstants.CODE_PROPERTY);
      String query = "SELECT FROM " + RelationshipLabelConstants.HAS_KLASS_PROPERTY + " WHERE '"
          + sectionId + "' in utilizingSectionIds";
      Iterable<Edge> iterator = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      for (Edge edge : iterator) {
        List<String> utilizingSectiionIds = edge
            .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
        utilizingSectiionIds.remove(sectionId);
        if (utilizingSectiionIds.size() == 0) {
          Vertex klassPropertyNode = edge.getVertex(Direction.IN);
          Iterator<Vertex> propertyNodeAssociatedWithKlassPropertyNode = klassPropertyNode
              .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
              .iterator();
          if (!propertyNodeAssociatedWithKlassPropertyNode.hasNext()) {
            throw new NotFoundException();
          }
          Vertex propertyNode = propertyNodeAssociatedWithKlassPropertyNode.next();
          if (propertyNodeAssociatedWithKlassPropertyNode.hasNext()) {
            throw new MultipleLinkFoundException();
          }
          String propertyId = UtilClass.getCodeNew(propertyNode);
          Iterable<Vertex> typeNodesAssociatedWithKlassPropertyNode = klassPropertyNode
              .getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
          List<String> associateTypeIdsList = new ArrayList<>();
          typeNodesAssociatedWithKlassPropertyNode.forEach(typeNode -> {
            associateTypeIdsList.add(UtilClass.getCodeNew(typeNode));
          });
          associatedTypeIds.put(propertyId, associateTypeIdsList);
          verticesToRemove.add(klassPropertyNode);
          GlobalPermissionUtils.deletePropertyPermissionNode(klassVertices, propertyId);
        }
        else {
          edge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY, utilizingSectiionIds);
        }
      }
      verticesToRemove.add(sectionNode);
    }
    for (Vertex vertex : verticesToRemove) {
      vertex.remove();
    }
    
    for (Vertex klass : klassesLinkedToThePC) {
      deleteTemplateIfSectionsDontExist(klass);
    }
  }
  
  private void deleteEntryFromSequenceNode(Vertex propertyCollectionNode)
  {
    Iterable<Vertex> templateNodes = propertyCollectionNode.getVertices(Direction.IN,
        RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION);
    for (Vertex template : templateNodes) {
      Iterator<Vertex> sequenceIterator = template
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE)
          .iterator();
      if (sequenceIterator.hasNext()) {
        Vertex sequenceNode = sequenceIterator.next();
        List<String> sequenceList = sequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
        sequenceList.remove(UtilClass.getCodeNew(propertyCollectionNode));
        sequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, sequenceList);
      }
    }
  }
  
  /**
   * @author Lokesh
   * @param pCNode
   */
  private void deleteTemplatePermissionNodesByPropertyCollectionNode(Vertex pCNode)
  {
    Iterable<Vertex> permissionIterable = pCNode.getVertices(Direction.IN,
        RelationshipLabelConstants.IS_PROPERTY_COLLECTION_PERMISSION_OF);
    for (Vertex permissionVertex : permissionIterable) {
      permissionVertex.remove();
    }
  }
  
  private void deleteTemplateIfSectionsDontExist(Vertex klassTaxonomy) throws Exception
  {
    List<String> superClasses = UtilClass.getSuperClasses(klassTaxonomy);
    String vertexLabel = ((OrientVertex) klassTaxonomy).getType()
        .getName();
    if (superClasses.contains(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS)
        && !vertexLabel.equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
      return;
    }
    
    Iterator<Vertex> parentRelationshipsIterable = SaveKlassUtil
        .checkTaxonomyHasRelationships(klassTaxonomy);
    Iterator<Vertex> parentContextKlassIterable = klassTaxonomy
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS)
        .iterator();
    
    Iterator<Edge> klassTaxonomySectionRelationships = klassTaxonomy
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
        .iterator();
    if (klassTaxonomySectionRelationships.hasNext() || parentContextKlassIterable.hasNext()
        || parentRelationshipsIterable.hasNext()) {
      return;
    }
    Iterator<Edge> edgesIterator = klassTaxonomy
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE)
        .iterator();
    if (!edgesIterator.hasNext()) {
      return;
    }
    Edge hasTemplateEdge = edgesIterator.next();
    Boolean isInherited = (Boolean) hasTemplateEdge
        .getProperty(CommonConstants.IS_INHERITED_PROPERTY);
    if (isInherited != null && !isInherited) {
      Vertex templateNode = hasTemplateEdge.getVertex(Direction.IN);
      
      templateNode.remove();
    }
  }
}

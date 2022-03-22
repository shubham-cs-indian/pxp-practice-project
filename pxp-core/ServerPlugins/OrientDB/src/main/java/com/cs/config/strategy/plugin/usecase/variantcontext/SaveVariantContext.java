package com.cs.config.strategy.plugin.usecase.variantcontext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;

import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.validationontype.InvalidContextTypeException;
import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IUniqueSelectorModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagValuesModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.variants.UniqueSelectorAlreadyExistsException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class SaveVariantContext extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToExclude = Arrays.asList(
      ISaveVariantContextModel.ADDED_TAGS, ISaveVariantContextModel.MODIFIED_TAGS,
      ISaveVariantContextModel.DELETED_TAGS, ISaveVariantContextModel.ADDED_SECTIONS,
      ISaveVariantContextModel.MODIFIED_SECTIONS, ISaveVariantContextModel.DELETED_SECTIONS,
      ISaveVariantContextModel.ADDED_ELEMENTS, ISaveVariantContextModel.MODIFIED_ELEMENTS,
      ISaveVariantContextModel.DELETED_ELEMENTS, ISaveVariantContextModel.ADDED_STATUS_TAGS,
      ISaveVariantContextModel.DELETED_STATUS_TAGS, ISaveVariantContextModel.ADDED_ENTITIES,
      ISaveVariantContextModel.DELETED_ENTITIES, ISaveVariantContextModel.ADDED_UNIQUE_SELECTIONS,
      ISaveVariantContextModel.DELETED_UNIQUE_SELECTIONS, ISaveVariantContextModel.ADDED_TAB,
      ISaveVariantContextModel.DELETED_TAB, ISaveVariantContextModel.ADDED_SUB_CONTEXTS,
      ISaveVariantContextModel.DELETED_SUB_CONTEXTS);
  
  public SaveVariantContext(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveVariantContext/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> contextMap = (HashMap<String, Object>) requestMap.get("variantContext");
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    Vertex contextNode = null;
    try {
      contextNode = UtilClass.getVertexByIndexedId((String) contextMap.get(IVariantContext.ID),
          VertexLabelConstants.VARIANT_CONTEXT);
    }
    catch (NotFoundException e) {
      throw new ContextNotFoundException();
    }
    
    String type = (String) contextMap.get(ISaveVariantContextModel.TYPE);
    if (!type.equals(contextNode.getProperty(IVariantContext.TYPE))) {
      throw new InvalidContextTypeException();
    }
    
    String defaultView = (String) contextMap.get(ISaveVariantContextModel.DEFAULT_VIEW);
    if (defaultView != null && !type.equals(CommonConstants.CONTEXTUAL_VARIANT)
        && !type.equals(CommonConstants.LANGUAGE_VARIANT)) {
      contextMap.put(ISaveVariantContextModel.DEFAULT_VIEW, CommonConstants.THUMBNAIL_VIEW);
    }
    
    saveContext(contextNode, contextMap);
    SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(contextMap, contextNode);
    String tabIdToDelete = (String) contextMap.get(ISaveVariantContextModel.DELETED_TAB);
    Map<String, Object> tabMap = (Map<String, Object>) contextMap
        .get(ISaveVariantContextModel.ADDED_TAB);
    TabUtils.manageAddedAndDeletedTab(contextNode, tabMap, tabIdToDelete, CommonConstants.CONTEXT);
    UtilClass.saveNode(contextMap, contextNode, fieldsToExclude);
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> returnMap = VariantContextUtils.getContextMapToReturn(contextNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, contextNode, Entities.CONTEXT, Elements.UNDEFINED);
    return returnMap;
  }
  
  private static void saveContext(Vertex contextNode, Map<String, Object> contextMap)
      throws Exception
  {
    List<String> addedStatusTypeTags = (List<String>) contextMap
        .get(ISaveVariantContextModel.ADDED_STATUS_TAGS);
    for (String addedStatusTypeTag : addedStatusTypeTags) {
      Vertex tag = UtilClass.getVertexById(addedStatusTypeTag, VertexLabelConstants.ENTITY_TAG);
      contextNode.addEdge(RelationshipLabelConstants.STATUS_TAG_TYPE_LINK, tag);
    }
    
    List<String> deletedStatusTypeTags = (List<String>) contextMap
        .get(ISaveVariantContextModel.DELETED_STATUS_TAGS);
    Iterable<Edge> edges = contextNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.STATUS_TAG_TYPE_LINK);
    for (Edge edge : edges) {
      Vertex tagNode = edge.getVertex(Direction.IN);
      String id = tagNode.getProperty(CommonConstants.CODE_PROPERTY);
      if (deletedStatusTypeTags.contains(id)) {
        edge.remove();
      }
    }
    
    List<Map<String, Object>> addedTags = (List<Map<String, Object>>) contextMap
        .get(ISaveVariantContextModel.ADDED_TAGS);
    Map<String, Object> addedTagsMap = new HashMap<>();
    for (Map<String, Object> addedTag : addedTags) {
      addedTagsMap.put((String) addedTag.get(IAddedVariantContextTagsModel.TAG_ID),
          addedTag.get(IAddedVariantContextTagsModel.TAG_VALUE_IDS));
    }
    
    VariantContextUtils.handleAddedTags(contextNode, addedTagsMap);
    
    List<Map<String, Object>> modifiedTags = (List<Map<String, Object>>) contextMap
        .get(ISaveVariantContextModel.MODIFIED_TAGS);
    VariantContextUtils.manageModifiedContextTag(contextNode, modifiedTags);
    
    List<String> deletedTagIds = (List<String>) contextMap
        .get(ISaveVariantContextModel.DELETED_TAGS);
    VariantContextUtils.manageDeleteTagIds(contextNode, deletedTagIds);
    
    List<Vertex> klassAndChildNodes = new ArrayList<>();
    klassAndChildNodes.add(contextNode);
    // handleEditableProperties(contextNode, contextMap);
    
    /*
     * Section and subcontext are no longer used.This code create's problem in creating clone of context.
     *
       SaveKlassUtil.manageSectionsInKlass(contextNode, contextMap, klassAndChildNodes,
       VertexLabelConstants.VARIANT_CONTEXT, new ArrayList<>(), new HashMap<>());
        manageSubContexts(contextNode, contextMap);
    */
    VariantContextUtils.manageEntities(contextNode, contextMap);
    manageUniqueSelections(contextNode, contextMap);
  }

  private static void manageUniqueSelections(Vertex contextNode, Map<String, Object> contextMap)
      throws Exception
  {
    Map<String, Object> existingUniqueSelectors = new HashMap<>();
    VariantContextUtils.fillUniqueSelections(contextNode, existingUniqueSelectors);

    List<Map<String, Object>> addedUniqueSelections = (List<Map<String, Object>>) contextMap
        .get(ISaveVariantContextModel.ADDED_UNIQUE_SELECTIONS);
    for (Map<String, Object> uniqueSelection : addedUniqueSelections) {
      String id = (String) uniqueSelection.get(IUniqueSelectorModel.ID);

      // Connect respective tag nodes to unique selector node..
      List<Map<String, Object>> selectionValues = (List<Map<String, Object>>) uniqueSelection
          .get(IUniqueSelectorModel.SELECTION_VALUES);

      Boolean selectorUnique = isSelectorUnique(existingUniqueSelectors, selectionValues);
      if(!selectorUnique){
        throw new UniqueSelectorAlreadyExistsException();
      }
      // Create new Unique selector node..
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.UNIQUE_SELECTOR, CommonConstants.CODE_PROPERTY);
      Map<String, Object> uniqueSelectorMap = new HashMap<>();
      uniqueSelectorMap.put(CommonConstants.ID_PROPERTY, id);
      Vertex uniqueSelectorNode = UtilClass.createNode(uniqueSelectorMap, vertexType,
          new ArrayList<>());
      contextNode.addEdge(RelationshipLabelConstants.HAS_UNIQUE_SELECTOR, uniqueSelectorNode);
      
      for (Map<String, Object> selectionValue : selectionValues) {
        String tagId = (String) selectionValue.get(IVariantContextTagModel.TAG_ID);
        Vertex tagNode = null;
        try {
          tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
        
        vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.UNIQUE_TAG_PROPERTY,
            CommonConstants.CODE_PROPERTY);
        Vertex UniqueTagPropertyNode = UtilClass.createNode(new HashMap<>(), vertexType,
            new ArrayList<>());
        uniqueSelectorNode.addEdge(RelationshipLabelConstants.HAS_UNIQUE_TAG_PROPERTY,
            UniqueTagPropertyNode);
        UniqueTagPropertyNode.addEdge(RelationshipLabelConstants.HAS_UNIQUE_TAG, tagNode);
        // Connect respective tag value nodes to unique selector node..
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) selectionValue
            .get(IVariantContextTagModel.TAG_VALUES);

        for (Map<String, Object> tagValue : tagValues) {
          String tagValueId = (String) tagValue.get(IVariantContextTagValuesModel.TAG_VALUE_ID);
          Vertex tagValueNode = null;
          try {
            tagValueNode = UtilClass.getVertexById(tagValueId, VertexLabelConstants.ENTITY_TAG);
          }
          catch (NotFoundException e) {
            throw new TagNotFoundException();
          }
          UniqueTagPropertyNode.addEdge(RelationshipLabelConstants.HAS_UNIQUE_TAG_VALUE,
              tagValueNode);
        }
      }
    }
    
    List<String> deletedUniqueSelections = (List<String>) contextMap
        .get(ISaveVariantContextModel.DELETED_UNIQUE_SELECTIONS);
    for (String uniqueSelectionId : deletedUniqueSelections) {
      Vertex uniqueSelectorNode = null;
      try {
        uniqueSelectorNode = UtilClass.getVertexById(uniqueSelectionId,
            VertexLabelConstants.UNIQUE_SELECTOR);
        uniqueSelectorNode.remove();
      }
      catch (NotFoundException e) {
        // eat up the exception as user wants to delete the selection..
      }
    }
  }

  /**
   * Check whether the unique selector is already added
   * @param existingUniqueSelectors
   * @param selectionValues
   */
  private static Boolean isSelectorUnique(Map<String, Object> existingUniqueSelectors, List<Map<String, Object>> selectionValues)
  {
    Map<String, List<String>> currentSelector = new HashMap<>();
    for(Map<String, Object> selectionValue : selectionValues){
      String tagId = (String) selectionValue.get(IVariantContextTagModel.TAG_ID);
      List<Map<String, Object>> tagValues = (List<Map<String, Object>>) selectionValue
          .get(IVariantContextTagModel.TAG_VALUES);
      List<String> collect = tagValues.stream().map(x -> (String)x.get(IVariantContextTagValuesModel.TAG_VALUE_ID)).collect(Collectors.toList());
      currentSelector.put(tagId, collect);
    }

    List<Map<String, Object>> uniqueSelectors = (List<Map<String, Object>>) existingUniqueSelectors.get("uniqueSelectors");
    for (Map<String, Object> uniqueSelector : uniqueSelectors) {
      Boolean x = doesSelectorMatch(currentSelector, uniqueSelector);
      if(x){
        return false;
      }
    }
    return true;
  }

  private static Boolean doesSelectorMatch(Map<String, List<String>> currentSelector, Map<String, Object> existingUniqueSelector)
  {
    List<Map<String, Object>> existingValues = (List<Map<String, Object>>) existingUniqueSelector.get(
        IUniqueSelectorModel.SELECTION_VALUES);
    if (existingValues.size() != currentSelector.size()) {
      return false;
    }
    for (Map<String, Object> existingValue : existingValues) {
      String tagId = (String) existingValue.get(IVariantContextTagModel.TAG_ID);
      if (!currentSelector.containsKey(tagId)) {
        return false;
      }
      List<Map<String, Object>> tagValues = (List<Map<String, Object>>) existingValue.get(IVariantContextTagModel.TAG_VALUES);
      List<String> existingTagValues = tagValues.stream()
          .map(x -> (String) x.get(IVariantContextTagValuesModel.TAG_VALUE_ID))
          .collect(Collectors.toList());

      List<String> currentTagValues = currentSelector.get(tagId);
      if (!ListUtils.isEqualList(existingTagValues, currentTagValues)) {
        return false;
      }
    }
    return true;
  }

  /**
   * manage added and deleted sub context. If add sub_context_of edge with added
   * subcontext and remove the same for deleted subcontexts
   *
   * @author Lokesh
   * @param contextNode
   * @param contextMap
   * @throws Exception
   */
  protected static void manageSubContexts(Vertex contextNode, Map<String, Object> contextMap)
      throws Exception
  {
    List<String> addedSubContexts = (List<String>) contextMap
        .remove(ISaveVariantContextModel.ADDED_SUB_CONTEXTS);
    for (String subContextId : addedSubContexts) {
      Vertex subContext = UtilClass.getVertexById(subContextId,
          VertexLabelConstants.VARIANT_CONTEXT);
      subContext.addEdge(RelationshipLabelConstants.SUB_CONTEXT_OF, contextNode);
    }
    
    List<String> deletedSubContexts = (List<String>) contextMap
        .remove(ISaveVariantContextModel.DELETED_SUB_CONTEXTS);
    for (String subContextId : deletedSubContexts) {
      // query to get subContextOf Edge between contextNode and subContextId
      String query = "Select From " + RelationshipLabelConstants.SUB_CONTEXT_OF + " Where in = "
          + contextNode.getId() + "and out.code = '" + subContextId + "'";
      Iterable<Edge> subContextOfEdges = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      Iterator<Edge> iterator = subContextOfEdges.iterator();
      if (iterator.hasNext()) {
        Edge subContextOf = iterator.next();
        subContextOf.remove();
      }
    }
  }
  
  /* @SuppressWarnings("unchecked")
  private static void handleEditableProperties(Vertex contextNode, Map<String, Object> contextMap) throws Exception
  {
    List<String> addedEditableAttributes = (List<String>) contextMap
        .get(ISaveVariantContextModel.ADDED_EDITABLE_ATTRIBUTES);
    List<String> addedEditableTags = (List<String>) contextMap
        .get(ISaveVariantContextModel.ADDED_EDITABLE_TAGS);
    List<String> addedEditableRoles = (List<String>) contextMap
        .get(ISaveVariantContextModel.ADDED_EDITABLE_ROLES);
  
    List<String> deletedEditableAttributes = (List<String>) contextMap
        .get(ISaveVariantContextModel.DELETED_EDITABLE_ATTRIBUTES);
    List<String> deletedEditableTags = (List<String>) contextMap
        .get(ISaveVariantContextModel.DELETED_EDITABLE_TAGS);
    List<String> deletedEditableRoles = (List<String>) contextMap
        .get(ISaveVariantContextModel.DELETED_EDITABLE_ROLES);
  
    manageAddedEditableProperties(contextNode, addedEditableAttributes, EntityConstants.ENTITY_TYPE_ATTRIBUTE);
    manageAddedEditableProperties(contextNode, addedEditableTags, EntityConstants.ENTITY_TAG);
    manageAddedEditableProperties(contextNode, addedEditableRoles, EntityConstants.ENTITY_TYPE_ROLE);
  
    manageDeletedEditableProperties(contextNode, deletedEditableAttributes,EntityConstants.ENTITY_TYPE_ATTRIBUTE);
    manageDeletedEditableProperties(contextNode, deletedEditableTags, EntityConstants.ENTITY_TAG);
    manageDeletedEditableProperties(contextNode,  deletedEditableRoles, EntityConstants.ENTITY_TYPE_ROLE);
  }
  */
  /*private static void manageDeletedEditableProperties(Vertex contextNode,
      List<String> deletedEditableProperties, String vertexType) throws Exception
  {
    for (String id : deletedEditableProperties) {
      Vertex entityNode = null;
      try {
        entityNode = UtilClass.getVertexById(id, vertexType);
      }
      catch (NotFoundException e) {
        //Eat up this exception because anyways user wants to remove that property node from context..
      }
      Edge editablePropertyof = getConnectingEdge(contextNode, entityNode);
      editablePropertyof.remove();
    }
  }*/
  
  /* private static void handleEditableProperties(Vertex contextNode, Map<String, Object> contextMap)
      throws Exception
  {
    Iterable<Edge> oldEdgeBetweenCoxtentNodeAndPropertyNode = contextNode.getEdges(Direction.IN,
        RelationshipLabelConstants.EDITABLE_PROPERTY_OF);
    for (Edge edge : oldEdgeBetweenCoxtentNodeAndPropertyNode) {
      edge.remove();
    }
    List<Map<String, String>> properties = (List<Map<String, String>>) contextMap
        .get(ISaveVariantContextModel.EDITABLE_PROPERTIES);
    int sequence = 0;
    for (Map<String, String> propertyMap : properties) {
      String propertyId = propertyMap.get(IIdParameterModel.ID);
      String type = propertyMap.get(IIdParameterModel.TYPE);
      Vertex propertyNode = getPropertyNode(propertyId, type);
      if(propertyNode != null){
        Edge newEdgeBetweenCoxtentNodeAndPropertyNode = propertyNode.addEdge(RelationshipLabelConstants.EDITABLE_PROPERTY_OF, contextNode);
        newEdgeBetweenCoxtentNodeAndPropertyNode.setProperty(CommonConstants.SEQUENCE, sequence);
        sequence++;
      }
    }
  }*/
  
  /* private static Vertex getPropertyNode(String propertyId, String type)
      throws Exception, AttributeNotFoundException, TagNotFoundException, RoleNotFoundException
  {
    Vertex propertyNode = null;
    switch (type) {
      case CommonConstants.ATTRIBUTE_PROPERTY:
        try {
          propertyNode = UtilClass.getVertexById(propertyId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          break;
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException();
        }
      case CommonConstants.TAG_PROPERTY:
        try {
          propertyNode = UtilClass.getVertexById(propertyId, VertexLabelConstants.ENTITY_TAG);
          break;
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
  
      case CommonConstants.ROLE_PROPERTY:
        try {
          propertyNode = UtilClass.getVertexById(propertyId, VertexLabelConstants.ENTITY_TYPE_ROLE);
          break;
        }
        catch (NotFoundException e) {
          throw new RoleNotFoundException();
        }
    }
    return propertyNode;
  }*/
  
  /*private static Edge getConnectingEdge(Vertex contextNode, Vertex entityNode)
  {
    String query = "SELECT FROM " + RelationshipLabelConstants.EDITABLE_PROPERTY_OF + " where in = "
        + contextNode.getId() + " and out = " + entityNode.getId();
  
    Iterable<Edge> iterator = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Edge editablePropertyOfEdge = null;
    for (Edge vertex : iterator) {
      editablePropertyOfEdge = vertex;
    }
    return editablePropertyOfEdge;
  }*/
  
  /*  private static void manageAddedEditableProperties(Vertex contextNode,
      List<String> addedEditableProperties, String vertexType) throws Exception
  {
    for (String id : addedEditableProperties) {
      Vertex entityNode = null;
      try {
        entityNode = UtilClass.getVertexById(id, vertexType);
      }
      catch (NotFoundException e) {
        switch (vertexType) {
          case EntityConstants.ENTITY_TYPE_ATTRIBUTE:
            throw new AttributeNotFoundException();
          case EntityConstants.ENTITY_TAG:
            throw new TagNotFoundException();
          case EntityConstants.ENTITY_TYPE_ROLE:
            throw new RoleNotFoundException();
        }
      }
      entityNode.addEdge(RelationshipConstants.EDITABLE_PROPERTY_OF, contextNode);
    }
  }*/
  
}

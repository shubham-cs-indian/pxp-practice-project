package com.cs.config.strategy.plugin.usecase.klass.gridview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.GetGridConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetGridConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util.GridEditUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.contentgrid.IGetContentGridKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.gridcontent.IConfigDetailsForContentGridViewModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetKlassConfigRequestModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetReferencedElementRequestModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridInstanceConfigDetailsModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridInstanceReferencedPermission;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForContentGridView extends AbstractConfigDetails {
  
  public static final Integer MAX_ALLOWED_COUNT = 25;
  
  public GetConfigDetailsForContentGridView(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForContentGridView/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> requestKlassInstanceMap = (Map<String, Object>) requestMap
        .get(IGetKlassConfigRequestModel.KLASS_REFERENCED_ELEMENTS);
    Map<String, List<String>> tagIdTagValueIdsMap = (Map<String, List<String>>) requestMap
        .get(IGetKlassConfigRequestModel.TAG_ID_TAG_VALUE_IDS_MAP);
    List<String> selectedPropertyIdsList = (List<String>) requestMap
        .get(IGetKlassConfigRequestModel.SELECTED_PROPERTY_IDS);
    String userId = (String) requestMap.get("userId");
    String roleId = RoleUtils.getRoleIdFromUser(userId);
    
    if (selectedPropertyIdsList.isEmpty()) {
      Vertex propertyListSequenceNode = GridEditUtil.getOrCreateGridEditSequenceNode();
      selectedPropertyIdsList = propertyListSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
      if (selectedPropertyIdsList.isEmpty()) {
        selectedPropertyIdsList = GridEditUtil.fetchPropertyWhenSequencePropertyIsEmpty();
      }
      if (!selectedPropertyIdsList
          .contains(IStandardConfig.StandardProperty.nameattribute.toString())) {
        selectedPropertyIdsList.add(IStandardConfig.StandardProperty.nameattribute.toString());
      }
    }
    
    IGetGridConfigDetailsHelperModel helperModel = new GetGridConfigDetailsHelperModel();
    helperModel.setRoleId(roleId);
    helperModel.setSelectedPropertyIds(new HashSet<>(selectedPropertyIdsList));
    
    Map<String, Object> instanceConfigDetails = new HashMap<>();
    prepareInstanceConfigDetailsAndFillHelperModel(requestKlassInstanceMap, helperModel,
        instanceConfigDetails);
    
    fillInstanceConfigDetails(helperModel, instanceConfigDetails);
    
    String quotedSelectedPropertyIds = EntityUtil.quoteIt(selectedPropertyIdsList);
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedTags = new HashMap<>();
    
    prepareReferencedProperties(referencedAttributes, quotedSelectedPropertyIds,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, null);
    prepareReferencedProperties(referencedTags, quotedSelectedPropertyIds,
        VertexLabelConstants.ENTITY_TAG, tagIdTagValueIdsMap);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IConfigDetailsForContentGridViewModel.INSTNACE_CONFIG_DETAILS,
        instanceConfigDetails);
    responseMap.put(IConfigDetailsForContentGridViewModel.REFERENCED_ATTRIBUTES,
        referencedAttributes);
    responseMap.put(IConfigDetailsForContentGridViewModel.REFERENCED_TAGS, referencedTags);
    responseMap.put(IGetContentGridKlassInstanceResponseModel.GRID_PROPERTY_SEQUENCE_LIST,
        selectedPropertyIdsList);
    
    return responseMap;
  }
  
  private Set<String> getSelectedPropertyIds(Set<String> entityRids)
  {
    // query to get first 10 selectedPropertyIds ids with isGridEditable = true
    String query = "Select distinct(" + ISectionElement.PROPERTY_ID + ") as "
        + ISectionElement.PROPERTY_ID + " from (select expand(out('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) from " + entityRids.toString()
        + ") where " + ISectionElement.IS_SKIPPED + " != true AND " + " out('"
        + RelationshipLabelConstants.HAS_PROPERTY + "')." + IAttribute.IS_GRID_EDITABLE
        + " = true  " + "skip " + 0 + " limit " + MAX_ALLOWED_COUNT;
    
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Set<String> selectedPropertyIds = new HashSet<>();
    for (Vertex kPNode : searchResults) {
      String propertyId = kPNode.getProperty(ISectionElement.PROPERTY_ID);
      selectedPropertyIds.add(propertyId);
    }
    return selectedPropertyIds;
  }
  
  /**
   * This function fills instanceConfigDetails i.e referencedElements and
   * referencedPermissions for all instances
   *
   * @author Lokesh
   * @param selectedPropertyIds
   * @param roleId
   * @param entityVsInstanceIdsMap
   * @param entityNodes
   * @param instanceConfigDetails
   * @throws Exception
   */
  private void fillInstanceConfigDetails(IGetGridConfigDetailsHelperModel helperModel,
      Map<String, Object> instanceConfigDetails) throws Exception
  {
    Set<String> selectedPropertyIds = helperModel.getSelectedPropertyIds();
    Set<Vertex> entityNodes = helperModel.getEntityNodes();
    String roleId = helperModel.getRoleId();
    
    String quotedSelectedPropertyIds = EntityUtil.quoteIt(selectedPropertyIds);
    
    for (Vertex entityNode : entityNodes) {
      Map<String, Object> referencedElementsForKlass = new HashMap<>();
      
      // Query to get all KP from entityNode(klass/taxonomy/collection) of
      // property in
      // selectedPropertyIds
      String query = "Select from (select expand(out('"
          + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) from " + entityNode.getId()
              .toString()
          + ") where " + ISectionElement.PROPERTY_ID + " in " + quotedSelectedPropertyIds + " AND "
          + ISectionElement.IS_SKIPPED + " != true";
      Iterable<Vertex> searchResults = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      // fill entityKPIds i.e. id of klassPropertyNodes for entityNode and
      // Also prepare referencedElementMap(id = propertyId) using KPNode ,
      // maintain it in
      // kPIdvsReferencedElementMap
      for (Vertex kPNode : searchResults) {
        Map<String, Object> referencedElementMap = getReferencedElementMap(kPNode);
        if (referencedElementMap == null) {
          // referencedElementMap is null if it is attributeContext or belong to
          // properties to
          // exclude
          // hence continue
          continue;
        }
        String propertyId = kPNode.getProperty(ISectionElement.PROPERTY_ID);
        referencedElementsForKlass.put(propertyId, referencedElementMap);
      }
      Map<String, String> elementCouplingMap = new HashMap<>();
      // merge referencedElements from relationship coupled element
      String entityVertexLabel = entityNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
      if (!entityVertexLabel.equals(VertexLabelConstants.COLLECTION)) {
        String entityId = UtilClass.getCodeNew(entityNode);
        mergeCouplingTypeFromOfReferencedElementsFromRelationship(entityId, elementCouplingMap);
      }
      
      // permission
      Set<String> visiblePropertyIds = new HashSet<>();
      Set<String> editablePropertyIds = new HashSet<>();
      fillPermissions(selectedPropertyIds, visiblePropertyIds, editablePropertyIds, entityNode,
          roleId);
      
      // fill element and permission in instance
      fillReferencedElementsAndPermissions(instanceConfigDetails, entityNode, visiblePropertyIds,
          editablePropertyIds, referencedElementsForKlass, helperModel, elementCouplingMap);
    }
  }
  
  /**
   * This function fill referenced Elements and referenced Permissions in all
   * instaalces This function didn't calculate permission or
   * referencedElementMap. It uses visiblePropertyIds and editablePropertyIds to
   * fill ReferencedPermission And entityKPIds and kPIdvsReferencedElementMap to
   * fill ReferencedElements
   *
   * @author Lokesh
   * @param instanceConfigDetails
   * @param entityNode
   * @param visiblePropertyIds
   * @param editablePropertyIds
   * @param referencedElementsForKlass
   * @param helperModel
   * @param elementCouplingMap
   * @throws Exception
   */
  private void fillReferencedElementsAndPermissions(Map<String, Object> instanceConfigDetails,
      Vertex entityNode, Set<String> visiblePropertyIds, Set<String> editablePropertyIds,
      Map<String, Object> referencedElementsForKlass, IGetGridConfigDetailsHelperModel helperModel,
      Map<String, String> elementCouplingMap) throws Exception
  {
    Map<String, Map<String, String>> instanceVsElementsCouplings = helperModel
        .getInstanceVsElementsCouplings();
    
    String roleId = helperModel.getRoleId();
    Set<String> allklassIds = helperModel.getKlassIds();
    String entityId = UtilClass.getCodeNew(entityNode);
    
    // consider header permission only for klases
    Boolean isNameVisible = false;
    Boolean isNameEditible = false;
    if (allklassIds.contains(entityId)) {
      Map<String, Object> headerPermission = GlobalPermissionUtils.getHeaderPermission(entityNode,
          roleId);
      isNameVisible = (Boolean) headerPermission.get(IHeaderPermission.VIEW_NAME);
      isNameEditible = (Boolean) headerPermission.get(IHeaderPermission.CAN_EDIT_NAME);
    }
    
    Map<String, List<String>> entityVsInstanceIdsMap = helperModel.getEntityVsInstanceIdsMap();
    List<String> instanceIds = entityVsInstanceIdsMap.get(entityId);
    for (String instanceId : instanceIds) {
      Map<String, Object> instnaceDetailMap = (Map<String, Object>) instanceConfigDetails
          .get(instanceId);
      
      // permissions
      Map<String, Object> referencedPermission = (Map<String, Object>) instnaceDetailMap
          .get(IGridInstanceConfigDetailsModel.REFERENCED_PERMISSIONS);
      Set<String> instanceVisiblePropertyIds = (Set<String>) referencedPermission
          .get(IGridInstanceReferencedPermission.VISIBLE_PROPERTY_IDS);
      instanceVisiblePropertyIds.addAll(visiblePropertyIds);
      Set<String> instanceEditablePropertyIds = (Set<String>) referencedPermission
          .get(IGridInstanceReferencedPermission.EDITABLE_PROPERTY_IDS);
      instanceEditablePropertyIds.addAll(editablePropertyIds);
      
      Boolean isInstanceNameEditable = (Boolean) referencedPermission
          .get(IGridInstanceReferencedPermission.IS_NAME_EDITABLE);
      if (isNameVisible) {
        // if name is not visibe don't need to do any name permisssion check
        referencedPermission.put(IGridInstanceReferencedPermission.IS_NAME_VISIBLE, isNameVisible);
        // or of isInstanceNameEditable + isNameEditible so that even 1 is true
        // result wil be true
        referencedPermission.put(IGridInstanceReferencedPermission.IS_NAME_EDITABLE,
            isInstanceNameEditable || isNameEditible);
      }
      
      Map<String, Object> referencedElements = (Map<String, Object>) instnaceDetailMap
          .get(IGridInstanceConfigDetailsModel.REFERENCED_ELEMENTS);
      Map<String, String> currentElementCouplingMap = instanceVsElementsCouplings.get(instanceId);
      for (String elementId : elementCouplingMap.keySet()) {
        String currentCouplingType = currentElementCouplingMap.get(elementId);
        String latestCouplingType = getMergedCouplingType(currentCouplingType,
            elementCouplingMap.get(elementId));
        currentElementCouplingMap.put(elementId, latestCouplingType);
        if (referencedElements.containsKey(elementId)) {
          Map<String, Object> elementMap = (Map<String, Object>) referencedElements.get(elementId);
          String elementCouplingType = (String) elementMap.get(ISectionElement.COUPLING_TYPE);
          latestCouplingType = getMergedCouplingType(elementCouplingType, latestCouplingType);
          elementMap.put(ISectionElement.COUPLING_TYPE, latestCouplingType);
        }
      }
      
      // referenced Elements
      for (String propertyId : referencedElementsForKlass.keySet()) {
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElementsForKlass
            .get(propertyId);
        
        String propertyType = (String) referencedElementMap.get(CommonConstants.TYPE);
        
        if (referencedElements.containsKey(propertyId)) {
          Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements
              .get(propertyId);
          mergeReferencedElement(referencedElementMap, existingReferencedElement, propertyId,
              propertyType, false);
        }
        else {
          Map<String, Object> cloneReferencedElementMap = new HashMap<>(referencedElementMap);
          
          String elementCouplingType = (String) cloneReferencedElementMap
              .get(ISectionElement.COUPLING_TYPE);
          String latestCouplingType = getMergedCouplingType(elementCouplingMap.get(propertyId),
              elementCouplingType);
          cloneReferencedElementMap.put(ISectionElement.COUPLING_TYPE, latestCouplingType);
          
          referencedElements.put(propertyId, cloneReferencedElementMap);
        }
      }
    }
  }
  
  private void prepareInstanceConfigDetailsAndFillHelperModel(
      Map<String, Object> requestKlassInstanceMap, IGetGridConfigDetailsHelperModel helperModel,
      Map<String, Object> instanceConfigDetails) throws Exception
  {
    Map<String, List<String>> entityVsInstanceIdsMap = helperModel.getEntityVsInstanceIdsMap();
    Set<String> entityRids = helperModel.getEntityRids();
    Set<Vertex> entityNodes = helperModel.getEntityNodes();
    Set<String> allklassIds = helperModel.getKlassIds();
    Map<String, Map<String, String>> instanceVsElementsCouplings = helperModel
        .getInstanceVsElementsCouplings();
    
    for (String klassInstanceId : requestKlassInstanceMap.keySet()) {
      Map<String, Object> instanceReturnMap = getInstanceReturnMap();
      instanceConfigDetails.put(klassInstanceId, instanceReturnMap);
      instanceVsElementsCouplings.put(klassInstanceId, new HashMap<>());
      
      Map<String, Object> requestInstanceMap = (Map<String, Object>) requestKlassInstanceMap
          .get(klassInstanceId);
      List<String> klassIds = (List<String>) requestInstanceMap
          .get(IGetReferencedElementRequestModel.KLASS_IDS);
      allklassIds.addAll(klassIds);
      
      for (String klassId : klassIds) {
        List<String> instanceIds = entityVsInstanceIdsMap.get(klassId);
        if (instanceIds == null) {
          instanceIds = new ArrayList<>();
          entityVsInstanceIdsMap.put(klassId, instanceIds);
          Vertex klassNode = UtilClass.getVertexById(klassId,
              VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
          entityRids.add(klassNode.getId()
              .toString());
          entityNodes.add(klassNode);
        }
        
        instanceIds.add(klassInstanceId);
      }
      
      List<String> taxonomyIds = (List<String>) requestInstanceMap
          .get(IGetReferencedElementRequestModel.TAXONOMY_IDS);
      for (String taxonomyId : taxonomyIds) {
        List<String> instanceIds = entityVsInstanceIdsMap.get(taxonomyId);
        if (instanceIds == null) {
          instanceIds = new ArrayList<>();
          entityVsInstanceIdsMap.put(taxonomyId, instanceIds);
          Vertex taxonomyNode = UtilClass.getVertexById(taxonomyId,
              VertexLabelConstants.ROOT_KLASS_TAXONOMY);
          entityRids.add(taxonomyNode.getId()
              .toString());
          entityNodes.add(taxonomyNode);
        }
        instanceIds.add(klassInstanceId);
      }
    }
  }
  
  private Map<String, Object> getInstanceReturnMap()
  {
    Map<String, Object> instanceReturnMap = new HashMap<>();
    instanceReturnMap.put(IGridInstanceConfigDetailsModel.REFERENCED_ELEMENTS, new HashMap<>());
    HashMap<Object, Object> referencedPermissions = new HashMap<>();
    referencedPermissions.put(IGridInstanceReferencedPermission.VISIBLE_PROPERTY_IDS,
        new HashSet<>());
    referencedPermissions.put(IGridInstanceReferencedPermission.EDITABLE_PROPERTY_IDS,
        new HashSet<>());
    referencedPermissions.put(IGridInstanceReferencedPermission.IS_NAME_EDITABLE, false);
    referencedPermissions.put(IGridInstanceReferencedPermission.IS_NAME_VISIBLE, false);
    
    instanceReturnMap.put(IGridInstanceConfigDetailsModel.REFERENCED_PERMISSIONS,
        referencedPermissions);
    return instanceReturnMap;
  }
  
  /**
   * create referencedElement map using KP node and if klassPropertyNode has
   * attributeContext or its property belongs to propertiesTo exclude then
   * return null
   *
   * @author Lokesh
   * @param klassPropertyNode
   * @return referencedElement map
   * @throws Exception
   */
  private Map<String, Object> getReferencedElementMap(Vertex klassPropertyNode) throws Exception
  {
    String entityId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
    Iterator<Vertex> attributeContextsIterator = klassPropertyNode
        .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (attributeContextsIterator.hasNext()
        || CommonConstants.PROPERTIES_TO_EXCLUDE_FOR_GRID.contains(entityId)) {
      return null;
    }
    
    Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
    String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
    
    referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
    if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAG)) {
      List<Map<String, Object>> defaultTagValues = KlassUtils
          .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
      referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
      
      Vertex entityVertex = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> entity = UtilClass.getMapFromVertex(new ArrayList<>(), entityVertex);
      Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionTag.IS_VERSIONABLE);
      if (isVersionable == null) {
        referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entity.get(ITag.IS_VERSIONABLE));
      }
    }
    else if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE)) {
      Vertex entityVertex = UtilClass.getVertexById(entityId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      Map<String, Object> entity = UtilClass.getMapFromVertex(new ArrayList<>(), entityVertex);
      Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionTag.IS_VERSIONABLE);
      if (isVersionable == null) {
        referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entity.get(ITag.IS_VERSIONABLE));
      }
    }
    
    return referencedElementMap;
  }
  
  /**
   * This metehod is fills only attribute and tags and skips other. And return
   * true if it it filled it into attribute or tags else false for all other
   *
   * @param entityNode
   * @param tagIdTagValueIdsMap
   * @param entityType
   * @param referencedElementMap
   * @param referencedAttributes
   * @param referencedTags
   * @param helperModel
   * @throws Exception
   */
  private void fillReferencedElementInRespectiveMap(Vertex entityNode, String vertexType,
      Map<String, Object> referencedProperties, Map<String, List<String>> tagIdTagValueIdsMap)
      throws Exception
  {
    Map<String, Object> entity;
    String entityId = UtilClass.getCodeNew(entityNode);
    switch (vertexType) {
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
        entity = AttributeUtils.getAttributeMap(entityNode);
        //commenting below code as concatenated/calculated attributes cannot be marked as grid editable
        /*if (entity.get(IAttribute.TYPE)
            .equals(Constants.CALCULATED_ATTRIBUTE_TYPE)
            || entity.get(IAttribute.TYPE)
                .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
          AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(
              referencedProperties, referencedProperties, entity);
        }*/
        if (!referencedProperties.containsKey(entityId)) {
          referencedProperties.put(entityId, entity);
        }
        break;
      case VertexLabelConstants.ENTITY_TAG:
        entity = TagUtils.getTagMap(entityNode, false);
        
        /* List<String> tagTypes =
            Arrays.asList(
                SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
                SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID,
                SystemLevelIds.RANGE_TAG_TYPE_ID);
        if (tagTypes.contains(entityNode.getProperty(ITag.TAG_TYPE))) {
          List<String> tagValueIds = tagIdTagValueIdsMap.get(entityId);
          entity = TagUtils.getTagMapWithSelectTagValues(entityNode, tagValueIds);
        } else {
          entity = TagUtils.getTagMap(entityNode, false);
        }*/
        referencedProperties.put(entityId, entity);
        break;
    }
  }
  
  private void prepareReferencedProperties(Map<String, Object> referencedProperties,
      String quotedVisiblePropertyIds, String vertexType,
      Map<String, List<String>> tagIdTagValueIdsMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String query = "SELECT FROM " + vertexType + " WHERE code in " + quotedVisiblePropertyIds + ""
        + " AND " + IAttribute.IS_GRID_EDITABLE + " = true";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex propertyNode : resultIterable) {
      fillReferencedElementInRespectiveMap(propertyNode, vertexType, referencedProperties,
          tagIdTagValueIdsMap);
    }
  }
  
  private void fillPermissions(Set<String> propertyIds, Set<String> visiblePropertyIds,
      Set<String> editablePropertyIds, Vertex entityNode, String roleId) throws Exception
  {
    String entityId = UtilClass.getCodeNew(entityNode);
    
    Set<String> visiblePropertyCollectionElementIds = new HashSet<>();
    Set<String> editablePropertyCollectionElementIds = new HashSet<>();
    
    // iterate over all section to get all PC nodes for klass
    Iterable<Vertex> sectionNodes = entityNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Vertex sectionNode : sectionNodes) {
      Vertex pCNode = KlassUtils.getPropertyCollectionNodeFromKlassSectionNode(sectionNode);
      List<String> attributeIds = pCNode.getProperty(IPropertyCollection.ATTRIBUTE_IDS);
      List<String> tagIds = pCNode.getProperty(IPropertyCollection.TAG_IDS);
      attributeIds.retainAll(propertyIds);
      tagIds.retainAll(propertyIds);
      if (attributeIds.isEmpty() && tagIds.isEmpty()) {
        // if any propertyIds is not contain in PC then continue
        continue;
      }
      // get permission of PC
      String propertyCollectionId = UtilClass.getCodeNew(pCNode);
      Vertex canReadPermission = GlobalPermissionUtils.getPropertyCollectionPermissionVertexIfExist(
          propertyCollectionId, roleId, entityId,
          VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION);
      Vertex canEditPermission = GlobalPermissionUtils.getPropertyCollectionPermissionVertexIfExist(
          propertyCollectionId, roleId, entityId,
          VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION);
      
      Boolean canEdit = canEditPermission == null ? true : false;
      Boolean canRead = canReadPermission == null ? true : false;
      
      if (!canRead) {
        // if PC don't have read permission continue
        // No need to check edit
        continue;
      }
      // PC has read permission add its attributes and tags retained from
      // propertyIds to
      // visiblePropertyCollectionElementIds
      visiblePropertyCollectionElementIds.addAll(attributeIds);
      visiblePropertyCollectionElementIds.addAll(tagIds);
      if (canEdit) {
        // if PC has edit permission add same in
        // editablePropertyCollectionElementIds
        editablePropertyCollectionElementIds.addAll(attributeIds);
        editablePropertyCollectionElementIds.addAll(tagIds);
      }
    }
    
    // what ever visible Properties we get from PC visiblity now lets this
    // properties own
    // permissions
    String quotedVisiblePropertyIds = EntityUtil.quoteIt(visiblePropertyCollectionElementIds);
    
    // Following query returns propertyPermissionNode (both can Read and can
    // Edit) for
    // visiblePropertyCollectionElementIds
    String query = "Select from (select expand(out('"
        + RelationshipLabelConstants.HAS_PROPERTY_PERMISSION + "')) from " + entityNode.getId()
            .toString()
        + ") where " + IPropertyPermission.PROPERTY_ID + " in " + quotedVisiblePropertyIds + " and "
        + IPropertyPermission.ROLE_ID + " = \"" + roleId + "\"";
    
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    // propertyPermissionNode got means respective property don't have respectve
    // permission
    // (according to node type)
    for (Vertex propertyPermissionNode : searchResults) {
      String propertyId = propertyPermissionNode.getProperty(IPropertyPermission.PROPERTY_ID);
      String vertexType = propertyPermissionNode
          .getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
      if (vertexType.equals(VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION)) {
        // if propertyPermissionNode for canRead permission found that means
        // property don't have
        // visiblity permission hence remove it
        visiblePropertyCollectionElementIds.remove(propertyId);
        editablePropertyCollectionElementIds.remove(propertyId);
      }
      else if (editablePropertyCollectionElementIds.contains(propertyId)) {
        // if propertyPermissionNode for canEdit permission found that means
        // property don't have
        // visiblity permission hence remove it
        editablePropertyCollectionElementIds.remove(propertyId);
      }
    }
    
    // whichever properties remain in PropertyCollectionElementIds have
    // respective permission
    visiblePropertyIds.addAll(visiblePropertyCollectionElementIds);
    editablePropertyIds.addAll(editablePropertyCollectionElementIds);
  }
  
  protected void mergeCouplingTypeFromOfReferencedElementsFromRelationship(String klassId,
      Map<String, String> elementCouplingMap)
      throws RelationshipNotFoundException, MultipleLinkFoundException
  {
    List<Vertex> allAssociatedKRNodes = getAllAssociatedKlassRelationshipsNodes(
        Arrays.asList(klassId));
    for (Vertex klassRelationshipNode : allAssociatedKRNodes) {
      Iterator<Vertex> relationshipNodesIterator = klassRelationshipNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      if (!relationshipNodesIterator.hasNext()) {
        throw new RelationshipNotFoundException();
      }
      Vertex relationshipNode = relationshipNodesIterator.next();
      
      if (relationshipNodesIterator.hasNext()) {
        throw new MultipleLinkFoundException();
      }
      
      Vertex otherSideRelationshipNode = null;
      Iterable<Vertex> klassRelationshipNodes = relationshipNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_PROPERTY);
      for (Vertex klassRelationshipNodeTemp : klassRelationshipNodes) {
        if (!UtilClass.getCodeNew(klassRelationshipNodeTemp)
            .equals(UtilClass.getCodeNew(klassRelationshipNode))) {
          otherSideRelationshipNode = klassRelationshipNodeTemp;
        }
      }
      
      Iterable<Edge> hasRelationshipAttributeEdges = otherSideRelationshipNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE);
      for (Edge hasRelationshipAttributeEdge : hasRelationshipAttributeEdges) {
        Vertex attribute = hasRelationshipAttributeEdge.getVertex(Direction.IN);
        String couplingType = hasRelationshipAttributeEdge
            .getProperty(ISectionElement.COUPLING_TYPE);
        String attributeId = UtilClass.getCodeNew(attribute);
        
        String currentCouplingType = (String) elementCouplingMap.get(attributeId);
        elementCouplingMap.put(attributeId,
            getMergedCouplingType(currentCouplingType, couplingType));
      }
      Iterable<Edge> hasRelationshipTagEdges = otherSideRelationshipNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_RELATIONSHIP_TAG);
      for (Edge hasRelationshipTagEdge : hasRelationshipTagEdges) {
        Vertex tag = hasRelationshipTagEdge.getVertex(Direction.IN);
        String couplingType = hasRelationshipTagEdge.getProperty(ISectionElement.COUPLING_TYPE);
        String tagId = UtilClass.getCodeNew(tag);
        
        String currentCouplingType = (String) elementCouplingMap.get(tagId);
        elementCouplingMap.put(tagId, getMergedCouplingType(currentCouplingType, couplingType));
      }
    }
  }
  
  /**
   * merges 2 coupling types. currentCouplingType can be null but
   * newCouplingType must be present always
   *
   * @author Lokesh
   * @param currentCouplingType
   * @param newCouplingType
   * @return
   */
  private String getMergedCouplingType(String currentCouplingType, String newCouplingType)
  {
    if (currentCouplingType == null || newCouplingType.equals(CommonConstants.DYNAMIC_COUPLED)
        || (currentCouplingType.equals(CommonConstants.LOOSELY_COUPLED))
        || (currentCouplingType.equals(CommonConstants.READ_ONLY_COUPLED)
            && newCouplingType.equals(CommonConstants.TIGHTLY_COUPLED))) {
      return newCouplingType;
    }
    return currentCouplingType;
  }
}

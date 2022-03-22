package com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct;


import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.ListUtils;

import com.cs.config.standard.IStandardConfig.StandardProperty;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttributeOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetTargetToCreateMCRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForRelationshipQuicklistResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.IPaginationInfoSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IReferencedPropertyModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeIconModel;

@SuppressWarnings("unchecked")
public abstract class AbstractConfigDetailsForNewInstanceTree extends AbstractOrientPlugin {
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_SORT_DATA             = Arrays
      .asList( IAttribute.LABEL, IAttribute.CODE);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA = Arrays
      .asList(IAttribute.LABEL, IAttribute.TYPE, IAttribute.CODE, IUnitAttribute.DEFAULT_UNIT);

  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA = Arrays
      .asList(ITag.LABEL, ITag.TAG_TYPE, ITag.CODE);

  public AbstractConfigDetailsForNewInstanceTree(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  protected void managePermission(Vertex roleNode, Map<String, Object> requestMap,
      Map<String, Object> mapToReturn) throws Exception
  {
    List<String> allowedEntities = fillAllowedEntities(roleNode, requestMap);
    Set<String> klassIdsHavingRP = GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode);
    Set<String> taxonomyIdsHavingRP = GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(roleNode);
    List<String> majorTaxonomyIds = GlobalPermissionUtils.getRootMajorTaxonomyIds();
    
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.ALLOWED_ENTITIES, allowedEntities);
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.KLASS_IDS_HAVING_RP, klassIdsHavingRP);
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.TAXONOMY_IDS_HAVING_RP, taxonomyIdsHavingRP);
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.MAJOR_TAXONOMY_IDS, majorTaxonomyIds);
  }
  
  protected List<String> fillAllowedEntities(Vertex roleNode, Map<String, Object> requestMap) throws Exception
  {
    List<String> moduleEntities =  (List<String>) requestMap.get(IGetConfigDetailsForGetNewInstanceTreeRequestModel.ALLOWED_ENTITIES);
    List<String> allowedEntities = (List<String>) roleNode.getProperty(IRole.ENTITIES);
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
      allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
    }
    allowedEntities.retainAll(moduleEntities);
    return allowedEntities;
  }
  
  protected List<String> getTranslatableAttributes(Collection<String> entityTypes) {
    Iterable<Vertex> vertices = getSortableFilterableTranslableSerachableData(IAttribute.IS_TRANSLATABLE, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, 
        entityTypes, new ArrayList<>(), null, null);
    return UtilClass.getCIds(vertices);
  }
  
  protected List<String> getSearchableAttributes(Collection<String> entityTypes) {
    Iterable<Vertex> vertices = getSortableFilterableTranslableSerachableData(IAttribute.IS_SEARCHABLE, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, 
        entityTypes, new ArrayList<>(), null, null);
    return UtilClass.getCIds(vertices);
  }
  
  protected static Iterable<Vertex> getSortableFilterableTranslableSerachableData(String property, String vertexType,
      Collection<String> entityTypes, Collection<String> idsToInclude, Long from, Long size) 
  {
    String query = " select from " + vertexType + " where " + property + " = true AND "
        + "( " + IAttribute.AVAILABILITY + ".size() = 0  Or "
        + IAttribute.AVAILABILITY + " contains " + EntityUtil.quoteIt(entityTypes) + ") OR "
        + CommonConstants.CODE_PROPERTY + " IN " + EntityUtil.quoteIt(idsToInclude)
        + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " "
        + CommonConstants.SORTORDER_ASC;
    if(from != null && size !=null) {
      query = query + " skip " + from + " limit " + size;
    }

    return UtilClass.getGraph().command(new OCommandSQL(query)).execute();
  }
  
  protected void fillReferencedProperties(Map<String, Object> requestMap, Map<String, Object> mapToReturn) throws Exception, AttributeNotFoundException, TagNotFoundException
  {
    List<String> xRayTagIds = (List<String>) requestMap.get(IGetConfigDetailsForGetNewInstanceTreeRequestModel.X_RAY_TAGS);
    List<String> xRayAttributeIds = (List<String>) requestMap.get(IGetConfigDetailsForGetNewInstanceTreeRequestModel.X_RAY_ATTRIBUTES);
    xRayAttributeIds.add(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE);
    xRayAttributeIds.add(SystemLevelIds.NAME_ATTRIBUTE);
    List<String> attributeIds = (List<String>) requestMap.get(IGetConfigDetailsForGetNewInstanceTreeRequestModel.ATTRIBUTE_IDS);
    List<String> tagIds = (List<String>) requestMap.get(IGetConfigDetailsForGetNewInstanceTreeRequestModel.TAG_IDS);
    List<String> attributeIdList = attributeIds != null ? ListUtils.sum(attributeIds, xRayAttributeIds) : xRayAttributeIds;
    List<String> tagIdList = tagIds != null ? ListUtils.sum(tagIds, xRayTagIds) : xRayTagIds;
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedAttributes = new HashMap<>();
    for (String attributeId : attributeIdList) {
      Vertex attributeNode = null;
      try {
        attributeNode = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      }
      catch (NotFoundException e) {
        throw new AttributeNotFoundException();
      }
      Map<String, Object> referencedAttribute = AttributeUtils.getAttributeMap(attributeNode);
      if (referencedAttribute.get(IAttribute.TYPE).equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
        AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(referencedAttributes, referencedTags,
            referencedAttribute);
      }
      referencedAttributes.put(attributeId, referencedAttribute);
    }
    
    for (String tagId : tagIdList) {
      Vertex tagNode;
      try {
        tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
      Map<String, Object> referencedTag = TagUtils.getTagMap(tagNode, true);
      referencedTags.put(tagId, referencedTag);
    }
    
    mapToReturn.put(IXRayConfigDetailsModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    mapToReturn.put(IXRayConfigDetailsModel.REFERENCED_TAGS, referencedTags);
  }

  protected Map<String, Object> getReferencedPropertyMap(Vertex propertyNode, Map<String, Object> requestMap, String filterType)
  {
    List<String> asList = Arrays.asList(IReferencedPropertyModel.ID, IReferencedPropertyModel.CODE, IReferencedPropertyModel.CHILDREN,
        IReferencedPropertyModel.TYPE, ITag.TAG_TYPE, IReferencedPropertyModel.PRECISION, 
        IReferencedPropertyModel.HIDE_SEPARATOR);
    Map<String, Object> referencedProperty = UtilClass.getMapFromVertex(asList, propertyNode);
    String tagType = (String) referencedProperty.remove(ITag.TAG_TYPE);
    if (filterType.equals(CommonConstants.TAG)) {
      referencedProperty.put(IReferencedPropertyModel.TYPE, tagType);
    }
    referencedProperty.put(IReferencedPropertyModel.CHILDREN, getPropertyChildren(propertyNode, requestMap));
    return referencedProperty;
  }

  private List<Map<String, Object>> getPropertyChildren(Vertex propertyNode,Map<String, Object> requestMap)
  {
    String filterType = (String)requestMap.get(IConfigDetailsForGetFilterChildrenRequestModel.FILTER_TYPE);
    String searchText = (String) requestMap.get(IConfigDetailsForGetFilterChildrenRequestModel.SEARCH_TEXT);
    Iterable<Vertex> vertices;
    if(filterType.equals(CommonConstants.TAG) && searchText != null && !searchText.isEmpty()) {
      String query = "SELECT FROM (SELECT EXPAND(in('child_Of')) from " +  propertyNode.getId() + ") where " 
          + EntityUtil.getSearchQuery(searchText, "label");
      vertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    } else {
      vertices = propertyNode.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    }
    List<Map<String, Object>> children = new ArrayList<>();
    for (Vertex childNode : vertices) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(Arrays.asList(IIdLabelCodeIconModel.ID, 
    		  IIdLabelCodeIconModel.CODE, IIdLabelCodeIconModel.LABEL), childNode);
      if((childMap.get(IIdLabelCodeIconModel.LABEL) == null || childMap.get(IIdLabelCodeIconModel.LABEL).equals("")) && CommonConstants.BOOLEAN_TAG_TYPE_ID.equals(propertyNode.getProperty(ITag.TAG_TYPE))) {
        childMap.put(IIdLabelCodeIconModel.LABEL, UtilClass.getValueByLanguage(propertyNode, ITag.LABEL));
      }
      children.add(childMap);
      UtilClass.fetchIconInfo(childNode, childMap);
    } 
    return children; 
  }
  
  protected List<String> getModuleEntitiesAndfillTargetIdsForRelationship(Map<String, Object> requestMap, Map<String, Object> mapToReturn)
      throws Exception
  {
    String targetId = (String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.TARGET_ID);
    Vertex targetNode = UtilClass.getVertexByIndexedId(targetId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    if(targetNode.getProperty("@class").equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
      mapToReturn.put(IConfigDetailsForRelationshipQuicklistResponseModel.IS_TARGET_TAXONOMY, true);
      mapToReturn.put(IConfigDetailsForRelationshipQuicklistResponseModel.TARGET_IDS, Arrays.asList(targetId));
      return CommonConstants.MODULE_ENTITIES;
    }
    
    String sideId = (String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.SIDE_ID);
    List<String> targetIds = new ArrayList<>();
    String relationshipId = (String) requestMap.get(IConfigDetailsForRelationshipQuicklistRequestModel.RELATIONSHIP_ID);
    Vertex relationshipNode = UtilClass.getVertexByIndexedId(relationshipId, VertexLabelConstants.ROOT_RELATIONSHIP);
    Iterable<Vertex> kRNodes = relationshipNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY);
    for (Vertex kRNode : kRNodes) {
      if(sideId!=null && sideId.equals(UtilClass.getCId(kRNode))) {
        continue; //self side
      }
      Boolean foundTarget = false;
      Iterable<Vertex> klassNodes = kRNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassNode : klassNodes) {
        String klassId = UtilClass.getCId(klassNode);
        targetIds.add(klassId);
        if(klassId.equals(targetId)) {
          foundTarget = true;
        }
      }
      if(foundTarget) {
        break;
      }
      targetIds.clear();
    }
    mapToReturn.put(IConfigDetailsForRelationshipQuicklistResponseModel.TARGET_IDS, targetIds);
    
    String klassType = targetNode.getProperty(IKlass.TYPE);
    return EntityUtil.getEntityTypesByKlassTypes(Arrays.asList(klassType));
  }
  
  protected static List<Map<String, Object>> fillSortData(List<String> entityTypes, List<Map<String, Object>> sortData, 
      Map<String, Object> paginatedsortInfo) 
  {
    Long from = (paginatedsortInfo!=null)?Long.valueOf(paginatedsortInfo.get(IPaginationInfoSortModel.FROM).toString()):null;
    Long size = (paginatedsortInfo!=null)?Long.valueOf(paginatedsortInfo.get(IPaginationInfoSortModel.SIZE).toString()):null;
    Iterable<Vertex> attributesToSort = getSortableFilterableTranslableSerachableData(IAttribute.IS_SORTABLE,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, entityTypes, new ArrayList<>(), from, size);
    for (Vertex attribute : attributesToSort) {
      Map<String, Object> attributeMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_SORT_DATA, attribute);
      UtilClass.fetchIconInfo(attribute, attributeMap);
      attributeMap.put(IAppliedSortModel.SORT_FIELD, attributeMap.remove(CommonConstants.ID_PROPERTY));
      sortData.add(attributeMap);
    }
    return sortData;
  }

  protected static List<Map<String, Object>> fillFilterData(List<String> entityTypes, List<Map<String, Object>> filterData) 
  {
    List<String> idsToInclude = Arrays.asList(SystemLevelIds.NAME_ATTRIBUTE);
    Iterable<Vertex> attributesToFilter = getSortableFilterableTranslableSerachableData(IAttribute.IS_FILTERABLE,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, entityTypes, idsToInclude, null, null);
    for (Vertex attribute : attributesToFilter) {
     
      if(attribute.getProperty(CommonConstants.CODE_PROPERTY).equals(StandardProperty.createdonattribute.toString())
         || attribute.getProperty(CommonConstants.CODE_PROPERTY).equals(StandardProperty.lastmodifiedattribute.toString())){
        continue;
      }
      
      Map<String, Object> attributeMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA,
          attribute);
      UtilClass.fetchIconInfo(attribute, attributeMap);
      attributeMap.put(INewApplicableFilterModel.PROPERTY_TYPE, CommonConstants.ATTRIBUTE);
      filterData.add(attributeMap);
    }

    Iterable<Vertex> tagsToFilter = getSortableFilterableTranslableSerachableData(ITag.IS_FILTERABLE,
        VertexLabelConstants.ENTITY_TAG, entityTypes, new ArrayList<>(), null, null);
    for (Vertex tag : tagsToFilter) {
      Map<String, Object> tagMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA, tag);
      UtilClass.fetchIconInfo(tag, tagMap);
      tagMap.put(INewApplicableFilterModel.PROPERTY_TYPE, CommonConstants.TAG);
      tagMap.put(INewApplicableFilterModel.TYPE, tagMap.remove(ITag.TAG_TYPE));
      filterData.add(tagMap);
    }
    return filterData;
  }
  
  protected void kpiHandling(String kpiId, Map<String, Object> mapToReturn) throws Exception
  {
    if(kpiId==null || kpiId.isEmpty())
    {
      return;
    }
    Vertex kpiNode = UtilClass.getVertexById(kpiId, VertexLabelConstants.GOVERNANCE_RULE_KPI);
    
    List<String> klassIds = new ArrayList<String>();
    Iterable<Vertex> klassVertices = kpiNode.getVertices(Direction.OUT, RelationshipLabelConstants.LINK_KLASS);
    for (Vertex klassNode : klassVertices) {
      String klassId = UtilClass.getCId(klassNode);
      klassIds.add(klassId);
    }
    
    List<String> taxonomyIds = new ArrayList<String>();
    Iterable<Vertex> taxonomyVertices = kpiNode.getVertices(Direction.OUT, RelationshipLabelConstants.LINK_TAXONOMY);
    for (Vertex taxonomyNode : taxonomyVertices) {
      String taxonomyId = UtilClass.getCId(taxonomyNode);
      taxonomyIds.add(taxonomyId);
    }
    
    Set<String> klassIdsHavingRP = (Set<String>) mapToReturn.get(IConfigDetailsForGetNewInstanceTreeModel.KLASS_IDS_HAVING_RP);
    if(klassIdsHavingRP.isEmpty()) {
      klassIdsHavingRP.addAll(klassIds);
    } else if (!klassIds.isEmpty()) {
      klassIdsHavingRP.retainAll(klassIds);
    }
    
    Set<String> taxonomyIdsHavingRP = (Set<String>) mapToReturn.get(IConfigDetailsForGetNewInstanceTreeModel.TAXONOMY_IDS_HAVING_RP);
    if(taxonomyIdsHavingRP.isEmpty()) {
      taxonomyIdsHavingRP.addAll(taxonomyIds);
    } else if (!taxonomyIds.isEmpty()) {
      taxonomyIdsHavingRP.retainAll(taxonomyIds);
    }
  }
  
  /**
   * @param relationshipId
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillRelationshipConfig(String relationshipId, Map<String, Object> mapToReturn)
      throws Exception
  {
    if(relationshipId == null || relationshipId.isEmpty()) {
      return;
    }
    
    Vertex relationshipNode = null;
    try {
      relationshipNode = UtilClass.getVertexById(relationshipId,
          VertexLabelConstants.ROOT_RELATIONSHIP);
    }
    catch (NotFoundException e) {
      throw new NotFoundException();
    }
    List<String> relationshipFieldToFetch = Arrays.asList(IRelationship.ID,
        IRelationship.PROPERTY_IID, IRelationship.CODE, IRelationship.IS_NATURE,
        IRelationship.TYPE, IRelationship.SIDE1, IRelationship.SIDE2);
    Map<String, Object> referencedElement = UtilClass.getMapFromVertex(relationshipFieldToFetch,
        relationshipNode);
    mapToReturn.put(IConfigDetailsForRelationshipQuicklistResponseModel.RELATIONSHIP_CONFIG, referencedElement);
  }
}

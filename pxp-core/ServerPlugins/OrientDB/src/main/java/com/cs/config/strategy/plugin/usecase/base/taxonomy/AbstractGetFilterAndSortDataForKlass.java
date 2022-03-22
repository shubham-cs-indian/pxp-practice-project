package com.cs.config.strategy.plugin.usecase.base.taxonomy;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractGetFilterAndSortDataForKlass extends AbstractOrientPlugin {
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_SORT_DATA = Arrays.asList(
      CommonConstants.CODE_PROPERTY, IAttribute.TYPE, IAttribute.LABEL, IAttribute.CODE);
  
  public AbstractGetFilterAndSortDataForKlass(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getKlassVertexType();
  
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    String moduleId = (String) requestMap.get(IGetFilterAndSortDataRequestModel.MODULE_ID);
    String systemLevelId = moduleId != null ? EntityUtil.getSystemLevelIdByModuleId(moduleId)
        : null;
    
    Map<String, Object> response = getFilterAndSortDataForSelectedModule(systemLevelId);
    
    List<String> searchableAttributes = new ArrayList<>();
    searchableAttributes.addAll(getSearchableAttributes());
    response.put(IGetFilterInformationModel.SEARCHABLE_ATTRIBUTES, searchableAttributes);
    
    response.put(IGetFilterInformationModel.CONFIG_DETAILS, getConfigDetails(requestMap));
    
    return response;
  }
  
  private Map<String, Object> getConfigDetails(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> configDetails = new HashMap<>();
    
    List<String> attributeIds = (List<String>) requestMap
        .get(IGetFilterAndSortDataRequestModel.ATTRIBUTE_IDS);
    Map<String, Object> referencedAttributes = new HashMap<>();
    for (String attributeId : attributeIds) {
      Vertex attributeNode = null;
      try {
        attributeNode = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      }
      catch (NotFoundException e) {
        throw new AttributeNotFoundException();
      }
      Map<String, Object> referencedAttribute = AttributeUtils.getAttributeMap(attributeNode);
      referencedAttributes.put(attributeId, referencedAttribute);
    }
    configDetails.put(IConfigDetailsForFilterInfoModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    
    List<String> tagIds = (List<String>) requestMap.get(IGetFilterAndSortDataRequestModel.TAG_IDS);
    Map<String, Object> referencedTags = new HashMap<>();
    for (String tagId : tagIds) {
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
    configDetails.put(IConfigDetailsForFilterInfoModel.REFERENCED_TAGS, referencedTags);
    
    List<String> roleIds = (List<String>) requestMap
        .get(IGetFilterAndSortDataRequestModel.ROLE_IDS);
    Map<String, Object> referencedRoles = new HashMap<>();
    for (String roleId : roleIds) {
      Vertex roleNode;
      try {
        roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
      }
      catch (NotFoundException e) {
        throw new RoleNotFoundException();
      }
      Map<String, Object> referencedRole = RoleUtils.getRoleEntityMap(roleNode);
      referencedRoles.put(roleId, referencedRole);
    }
    configDetails.put(IConfigDetailsForFilterInfoModel.REFERENCED_ROLES, referencedRoles);
    
    return configDetails;
  }
  
  private List<String> getSearchableAttributes()
  {
    List<String> searchableAttributes = new ArrayList<>();
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
            + " where isSearchable = true"))
        .execute();
    for (Vertex vertex : vertices) {
      searchableAttributes.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return searchableAttributes;
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = executeInternal(requestMap);
    return responseMap;
  }
  
  public static Map<String, Object> getFilterAndSortDataForSelectedModule(String systemLevelId)
      throws Exception
  {
    Set<String> attributeIdsForSort = new HashSet<>();
    Set<String> attributeIdsForFilter = new HashSet<>();
    Set<String> tagIdsForFilter = new HashSet<>();
    Set<String> translatableAttributes = new HashSet<>();
    
    fillSortableFilterableaData(attributeIdsForSort, tagIdsForFilter, attributeIdsForFilter,
        systemLevelId, translatableAttributes);
    
    attributeIdsForSort.add(IStandardConfig.StandardProperty.nameattribute.toString());
    attributeIdsForSort.add(IStandardConfig.StandardProperty.createdonattribute.toString());
    
    Map<String, Object> sortData = TaxonomyUtil.getSortData(attributeIdsForSort);
    Map<String, Object> filterData = TaxonomyUtil.getFilterData(tagIdsForFilter,
        attributeIdsForFilter);
    List<Map<String, Object>> defaultFilterTags = TaxonomyUtil
        .getDefaultFilterTags(tagIdsForFilter);
    
    Map<String, Object> response = new HashMap<>();
    response.put(IGetFilterInfoModel.SORT_DATA, sortData);
    response.put(IGetFilterInfoModel.FILTER_DATA, filterData);
    response.put(IGetFilterInfoModel.DEFAULT_FILTER_TAGS, defaultFilterTags);
    response.put(IGetFilterInfoModel.TRANSLATABLE_ATTRIBUTES_IDS, translatableAttributes);
    
    return response;
  }
  
  private static void fillSortableFilterableaData(Set<String> klassAttributesForSort,
      Set<String> klassTagsForFilter, Set<String> klassAttributesForFilter, String systemLevelId,
      Set<String> klassTranslatableAttributes)
  {
    OrientGraph graph = UtilClass.getGraph();
    
    String availabilityQuery = " AND ( '" + systemLevelId
        + "' in availability Or availability.size() = 0 )";
    
    // filterable_ATTRIBUTE
    String query = " select " + CommonConstants.CODE_PROPERTY + " from "
        + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where " + IAttribute.IS_FILTERABLE
        + " = true ";
    if (systemLevelId != null && !systemLevelId.equals("")) {
      query += availabilityQuery;
    }
    Iterable<Vertex> filterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : filterable) {
      klassAttributesForFilter.add(UtilClass.getCodeNew(vertex));
    }
    
    // sortable_ATTRIBUTE
    query = " select " + CommonConstants.CODE_PROPERTY + " from "
        + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where " + IAttribute.IS_SORTABLE
        + " = true ";
    if (systemLevelId != null && !systemLevelId.equals("")) {
      query += availabilityQuery;
    }
    Iterable<Vertex> sortable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : sortable) {
      klassAttributesForSort.add(UtilClass.getCodeNew(vertex));
    }
    
    // filterable_TAG " where outE('Child_Of').size() = 0"
    query = " Select " + CommonConstants.CODE_PROPERTY + " from "
        + VertexLabelConstants.ENTITY_TAG + " where " + ITag.IS_ROOT + " = true AND "
        + ITag.IS_FILTERABLE + " = true ";
    if (systemLevelId != null && !systemLevelId.equals("")) {
      query += availabilityQuery;
    }
    Iterable<Vertex> sortableTag = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : sortableTag) {
      klassTagsForFilter.add(UtilClass.getCodeNew(vertex));
    }
    
    // translatable_Attribute
    query = " select " + CommonConstants.CODE_PROPERTY + " from "
        + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where " + IAttribute.IS_TRANSLATABLE
        + " = true ";
    if (systemLevelId != null && !systemLevelId.equals("")) {
      query += availabilityQuery;
    }
    Iterable<Vertex> translatableAttributes = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex translatableAttribute : translatableAttributes) {
      klassTranslatableAttributes.add(UtilClass.getCodeNew(translatableAttribute));
    }
  }
}

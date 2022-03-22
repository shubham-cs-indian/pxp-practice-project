package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityPaginationModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.klass.IGetConfigDataForRelationshipExportModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDataForRelationshipExport extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH         = Arrays.asList(
      CommonConstants.ICON_PROPERTY, CommonConstants.CID_PROPERTY, CommonConstants.LABEL_PROPERTY,
      CommonConstants.TYPE_PROPERTY, CommonConstants.CODE_PROPERTY);
  
  private static final List<String> FIELDS_TO_FETCH_FOR_TAG = Arrays.asList(
      CommonConstants.CID_PROPERTY, CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY,
      CommonConstants.ICON_PROPERTY, CommonConstants.COLOR_PROPERTY, CommonConstants.CODE_PROPERTY);
  
  public GetConfigDataForRelationshipExport(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDataForRelationshipExport/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String searchColumn = (String) requestMap.get(IGetConfigDataRequestModel.SEARCH_COLUMN);
    String searchText = (String) requestMap.get(IGetConfigDataRequestModel.SEARCH_TEXT);
    searchText = searchText.replace("'", "\\'");
    Map<String, Object> entities = (Map<String, Object>) requestMap
        .get(IGetConfigDataRequestModel.ENTITIES);
    
    String klassId = (String) requestMap.get(IGetConfigDataForRelationshipExportModel.KLASS_ID);
    Map<String, Object> responseMap = new HashMap<>();
    getConfigDataForEntity(searchColumn, searchText, entities, responseMap, klassId, requestMap);
    
    return responseMap;
  }
  
  /**
   * @param searchColumn
   * @param searchText
   * @param entities
   * @param responseMap
   * @param klassId
   * @param requestMap 
   */
  private void getConfigDataForEntity(String searchColumn, String searchText,
      Map<String, Object> entities, Map<String, Object> responseMap, String klassId, Map<String, Object> requestMap)
  {
    // Handlings for attributes.
    Map<String, Object> attributesRequestInfo = (Map<String, Object>) entities.get(IGetConfigDataEntityRequestModel.ATTRIBUTES);
    if (attributesRequestInfo != null) {
      List<String> entityTypes = Arrays.asList(VertexLabelConstants.ENTITY_STANDARD_ATTRIBUTE,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      Map<String, Object> attributeConfigMap = fetchEntities(searchColumn, searchText, klassId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, requestMap, entityTypes);
      responseMap.put(IGetConfigDataResponseModel.ATTRIBUTES, attributeConfigMap);
    }
    
    // Handling for tags.
    Map<String, Object> tagsRequestInfo = (Map<String, Object>) entities.get(IGetConfigDataEntityRequestModel.TAGS);
    if (tagsRequestInfo != null) {
      List<String> entityTypes = Arrays.asList(VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> tagConfigMap = fetchEntities(searchColumn, searchText, klassId,
          VertexLabelConstants.ENTITY_TAG, requestMap, entityTypes);
      responseMap.put(IGetConfigDataResponseModel.TAGS, tagConfigMap);
    }
    
    // Handling for relationships.
    Map<String, Object> relationshipsRequestInfo = (Map<String, Object>) entities.get(IGetConfigDataEntityRequestModel.RELATIONSHIPS);
    if (relationshipsRequestInfo != null) {
      List<String> entityTypes = Arrays.asList(VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP,
          VertexLabelConstants.NATURE_RELATIONSHIP);
      Map<String, Object> relationshipConfigMap = fetchEntities(searchColumn, searchText, klassId,
          VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP, requestMap, entityTypes);
      responseMap.put(IGetConfigDataResponseModel.RELATIONSHIPS, relationshipConfigMap);
    }
  }
  
  /**
   * @param searchColumn
   * @param searchText
   * @param entityRequestInfo
   * @param klassId
   * @param entityType
   * @param entityLabel
   * @param requestMap 
   * @param entityTypes 
   * @return
   */
  private Map<String, Object> fetchEntities(String searchColumn, String searchText,
       String klassId, String entityLabel, Map<String, Object> requestMap, List<String> entityTypes)
  {
    Long from = Long.valueOf(requestMap.get(IGetConfigDataEntityPaginationModel.FROM).toString());
    Long size = Long.valueOf(requestMap.get(IGetConfigDataEntityPaginationModel.SIZE).toString());
    String sortBy = (String) requestMap.get(IGetConfigDataEntityPaginationModel.SORT_BY);
    String sortOrder = (String) requestMap.get(IGetConfigDataEntityPaginationModel.SORT_ORDER);
    OrientGraph graph = UtilClass.getGraph();
    List<String> attributeVariantIdsList = getAttributeVariantIdsList(klassId);
    Long totalCount = getTotalCount(klassId, entityTypes,attributeVariantIdsList);
    String query = generateQuery(searchColumn, searchText, klassId, from, size, sortBy, sortOrder, entityTypes,attributeVariantIdsList);
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query)).execute();
    
    List<Map<String, Object>> entitiesList = new ArrayList<>();
    for (Vertex searchResult : searchResults) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(getFieldsToFetch(entityLabel), searchResult);
      entitiesList.add(entityMap);
    }
    
    Map<String, Object> entitiesResponse = new HashMap<>();
    entitiesResponse.put(IGetConfigDataEntityResponseModel.FROM, from);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.SIZE, size);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    entitiesResponse.put(IGetConfigDataEntityResponseModel.LIST, entitiesList);
    
    return entitiesResponse;
  }
  
  // TO DO 
  // Optimize the  Query to fetch the  List of IDS of AttributeVariant.
  
  private List<String> getAttributeVariantIdsList(String klassId)
  {
    String queryForAttributeVarinat = generateQueryToGetAttributeVarinat(klassId);
    Iterable<Vertex> searchResultsOfAttributeVariant = UtilClass.getGraph().command(new OCommandSQL(queryForAttributeVarinat)).execute();
    List<String> attributeVariantIdsList = new ArrayList<>();
    for (Vertex attributeVariantVertex : searchResultsOfAttributeVariant) {
      Iterable<Vertex> variantContextOfVertices = attributeVariantVertex.getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      for(Vertex variantContextOfVertice : variantContextOfVertices) {
        Iterable<Vertex> hasPropertyVertices = variantContextOfVertice.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY);
        for(Vertex hasPropertyVertice : hasPropertyVertices) {
          attributeVariantIdsList.add(hasPropertyVertice.getProperty(CommonConstants.CID_PROPERTY));
        }
      }
    }
    return attributeVariantIdsList;
  }

  /**
   * @param klassId
   * @param entityTypes
   * @param attributeVariantIdsList 
   * @return
   */
  private Long getTotalCount(String klassId, List<String> entityTypes, List<String> attributeVariantIdsList)
  {
    String query = "SELECT count(*) FROM (TRAVERSE out(\"" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "\"), out (\""+RelationshipLabelConstants.HAS_PROPERTY+"\") FROM (SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS 
        + " where cid = \"" + klassId + "\")) WHERE @class IN " + EntityUtil.quoteIt(entityTypes) ;
        
    if (!attributeVariantIdsList.isEmpty()) {
      query += " and cid NOT IN " + EntityUtil.quoteIt(attributeVariantIdsList);
    }
   
    return EntityUtil.executeCountQueryToGetTotalCount(query);
  }

  /**
   * @param entityLabel
   * @return
   */
  private List<String> getFieldsToFetch(String entityLabel)
  {
    switch (entityLabel) {
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
        return FIELDS_TO_FETCH;
      case VertexLabelConstants.ENTITY_TAG:
        return FIELDS_TO_FETCH_FOR_TAG;
      case VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP:
        return FIELDS_TO_FETCH;
      default:
        return FIELDS_TO_FETCH;
    }
  }

  /**
   * @param searchColumn
   * @param searchText
   * @param klassId
   * @param from
   * @param size
   * @param sortBy
   * @param sortOrder
   * @param entityTypes
   * @return
   */
  private String generateQuery(String searchColumn, String searchText, String klassId, Long from,
      Long size, String sortBy, String sortOrder, List<String> entityTypes,List<String> attributeVariantIdsList)
  {
    String query = "SELECT FROM (TRAVERSE out(\"" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "\"), out (\""+RelationshipLabelConstants.HAS_PROPERTY+"\") FROM (SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS 
        + " where cid = \"" + klassId + "\")) WHERE @class IN "+ EntityUtil.quoteIt(entityTypes);
    if (!attributeVariantIdsList.isEmpty()) {
      query += " and cid NOT IN " + EntityUtil.quoteIt(attributeVariantIdsList);
    }
    String queryParameters = prepareSearchQuery(searchColumn, searchText, from, size, sortBy, sortOrder);
    query += queryParameters;
    return query;
  }
  
  private String generateQueryToGetAttributeVarinat(String klassId) {
    String query = "SELECT FROM (TRAVERSE out(\"" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "\"), in (\""+RelationshipLabelConstants.VARIANT_CONTEXT_OF+"\") FROM (SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS 
        + " where cid = \"" + klassId + "\")) WHERE @class IN " + EntityUtil.quoteIt(VertexLabelConstants.VARIANT_CONTEXT); 

    return query;
  }
   
  /**
   * @param searchColumn
   * @param searchText
   * @param from
   * @param size
   * @param sortBy
   * @param sortOrder
   * @return
   */
  private String prepareSearchQuery(String searchColumn, String searchText, Long from,
      Long size, String sortBy, String sortOrder)
  {
    String queryParameters = "";
    
    if (!UtilClass.isStringNullOrEmpty(searchColumn)
        && !UtilClass.isStringNullOrEmpty(searchText)) {
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      queryParameters += " AND " + searchColumn + " like '%" + searchText + "%'";
    }
    
    
    if (!UtilClass.isStringNullOrEmpty(sortBy)) {
      sortBy = EntityUtil.getLanguageConvertedField(sortBy);
      if (UtilClass.isStringNullOrEmpty(sortOrder)) {
        sortOrder = "asc";
      }
      queryParameters += " order by " + sortBy + " " + sortOrder;
    }
    
    if (from != null && size != null) {
      queryParameters += " skip " + from + " limit " + size;
    }
    
    return queryParameters;
  }
}

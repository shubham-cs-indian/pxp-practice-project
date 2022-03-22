package com.cs.config.strategy.plugin.usecase.calculatedattribute;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.model.calculatedattribute.IGetAllowedAttributesForCalculatedAttributeRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataAttributeResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetAllowedAttributesForCalculatedAttribute extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH_FOR_ATTRIBUTE = Arrays.asList(
      CommonConstants.CODE_PROPERTY, IAttribute.LABEL, IAttribute.TYPE, IAttribute.ICON,
      IAttribute.CODE);
  
  public GetAllowedAttributesForCalculatedAttribute(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedAttributesForCalculatedAttribute/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long
        .valueOf(requestMap.get(IGetAllowedAttributesForCalculatedAttributeRequestModel.FROM)
            .toString());
    Long size = Long
        .valueOf(requestMap.get(IGetAllowedAttributesForCalculatedAttributeRequestModel.SIZE)
            .toString());
    
    String sortBy = (String) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.SORT_BY);
    String sortOrder = (String) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.SORT_ORDER);
    
    String searchColumn = (String) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.SEARCH_COLUMN);
    String searchText = (String) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.SEARCH_TEXT);
    
    String calculatedAttributeType = (String) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.CALCULATED_ATTRIBUTE_TYPE);
    String calculatedAttributeUnit = (String) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.CALCULATED_ATTRIBUTE_UNIT);
    
    Boolean shouldAllowSelf = (Boolean) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.SHOULD_ALLOW_SELF);
    Map<String, Map<String, Map<String, String>>> mapping = (Map<String, Map<String, Map<String, String>>>) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.MAPPING);
    
    String attributeId = (String) requestMap
        .get(IGetAllowedAttributesForCalculatedAttributeRequestModel.ATTRIBUTE_ID);
    
    Long totalCount = getTotalCount(searchColumn, searchText, calculatedAttributeType,
        calculatedAttributeUnit, mapping, attributeId, shouldAllowSelf);
    
    Iterable<Vertex> vertices = getVerticesAccordingToRequest(from, size, sortBy, sortOrder,
        searchColumn, searchText, calculatedAttributeType, calculatedAttributeUnit, mapping,
        attributeId, shouldAllowSelf);
    
    List<Map<String, Object>> attributes = new ArrayList<>();
    for (Vertex vertex : vertices) {
      Map<String, Object> attributeMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_ATTRIBUTE,
          vertex);
      String id = (String) attributeMap.get(IAttribute.ID);
      if (!shouldAllowSelf && attributeId.equals(id)) {
        totalCount--;
        continue;
      }
      attributes.add(attributeMap);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetConfigDataEntityResponseModel.FROM, from);
    returnMap.put(IGetConfigDataEntityResponseModel.SIZE, size);
    returnMap.put(IGetConfigDataEntityResponseModel.TOTAL_COUNT, totalCount);
    returnMap.put(IGetConfigDataAttributeResponseModel.LIST, attributes);
    
    return returnMap;
  }
  
  private Iterable<Vertex> getVerticesAccordingToRequest(Long from, Long size, String sortBy,
      String sortOrder, String searchColumn, String searchText, String calculatedAttributeType,
      String calculatedAttributeUnit, Map<String, Map<String, Map<String, String>>> mapping,
      String attributeId, Boolean shouldAllowSelf)
  {
    String query = getQuery(from, size, sortBy, sortOrder, searchColumn, searchText,
        calculatedAttributeType, calculatedAttributeUnit, mapping, attributeId, shouldAllowSelf);
    
    OrientGraph graph = UtilClass.getGraph();
    return graph.command(new OCommandSQL(query))
        .execute();
  }
  
  private Long getTotalCount(String searchColumn, String searchText, String calculatedAttributeType,
      String calculatedAttributeUnit, Map<String, Map<String, Map<String, String>>> mapping,
      String attributeId, Boolean shouldAllowSelf)
  {
    String totalCountQuery = getTotalCountQuery(searchColumn, searchText, calculatedAttributeType,
        calculatedAttributeUnit, mapping, attributeId, shouldAllowSelf);
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> countResult = graph.command(new OCommandSQL(totalCountQuery))
        .execute();
    Iterator<Vertex> iterator = countResult.iterator();
    return iterator.next()
        .getProperty("count");
  }
  
  private String getQuery(Long from, Long size, String sortBy, String sortOrder,
      String searchColumn, String searchText, String calculatedAttributeType,
      String calculatedAttributeUnit, Map<String, Map<String, Map<String, String>>> mapping,
      String attributeId, Boolean shouldAllowSelf)
  {
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
    
    String whereConditions = getWhereConditions(searchColumn, searchText, calculatedAttributeType,
        calculatedAttributeUnit, mapping, attributeId, shouldAllowSelf);
    
    if (!UtilClass.isStringNullOrEmpty(whereConditions)) {
      query += " WHERE " + whereConditions;
    }
    
    if (!UtilClass.isStringNullOrEmpty(sortBy)) {
      if (UtilClass.isStringNullOrEmpty(sortOrder)) {
        sortOrder = "asc";
      }
      sortBy = EntityUtil.getLanguageConvertedField(sortBy);
      query += " order by " + sortBy + " " + sortOrder;
    }
    
    if (from != null && size != null) {
      query += " skip " + from + " limit " + size;
    }
    
    return query;
  }
  
  private String getTotalCountQuery(String searchColumn, String searchText,
      String calculatedAttributeType, String calculatedAttributeUnit,
      Map<String, Map<String, Map<String, String>>> mapping, String attributeId,
      Boolean shouldAllowSelf)
  {
    String query = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
    
    String whereConditions = getWhereConditions(searchColumn, searchText, calculatedAttributeType,
        calculatedAttributeUnit, mapping, attributeId, shouldAllowSelf);
    
    if (!UtilClass.isStringNullOrEmpty(whereConditions)) {
      query += " WHERE " + whereConditions;
    }
    
    return query;
  }
  
  private String getWhereConditions(String searchColumn, String searchText,
      String calculatedAttributeType, String calculatedAttributeUnit,
      Map<String, Map<String, Map<String, String>>> mapping, String attributeId,
      Boolean shouldAllowSelf)
  {
    String allowedTypesQuery = getAllowedTypesQuery(calculatedAttributeType,
        calculatedAttributeUnit, mapping);
    
    String searchQuery = getSearchQuery(searchColumn, searchText);
    
    List<String> whereConditions = new ArrayList<>();
    if (!UtilClass.isStringNullOrEmpty(allowedTypesQuery)) {
      whereConditions.add(allowedTypesQuery);
    }
    if (!UtilClass.isStringNullOrEmpty(searchQuery)) {
      whereConditions.add(searchQuery);
    }
    if (!shouldAllowSelf) {
      whereConditions.add(CommonConstants.CODE_PROPERTY + " != '" + attributeId + "'");
    }
    String whereConditionQuery = String.join(" AND ", whereConditions);
    if (shouldAllowSelf) {
      return "(" + whereConditionQuery + ") OR " + CommonConstants.CODE_PROPERTY + " = '"
          + attributeId + "'";
    }
    else {
      return whereConditionQuery;
    }
  }
  
  private String getSearchQuery(String searchColumn, String searchText)
  {
    if (!UtilClass.isStringNullOrEmpty(searchColumn)
        && !UtilClass.isStringNullOrEmpty(searchText)) {
      searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      return searchColumn + " like '%" + searchText + "%'";
    }
    return "";
  }
  
  private String getAllowedTypesQuery(String calculatedAttributeType,
      String calculatedAttributeUnit, Map<String, Map<String, Map<String, String>>> mapping)
  {
    List<String> allowedTypesConditions = new ArrayList<>();
    if (!UtilClass.isStringNullOrEmpty(calculatedAttributeType)
        && !calculatedAttributeType.equals(CommonConstants.CUSTOM_UNIT_ATTRIBUTE_TYPE)
        && !UtilClass.isStringNullOrEmpty(calculatedAttributeUnit)) {
      Set<String> allowedAttributeTypes = mapping.get(calculatedAttributeType)
          .get(calculatedAttributeUnit)
          .keySet();
      for (String allowedAttributeType : allowedAttributeTypes) {
        allowedTypesConditions.add(IAttribute.TYPE + " = '" + allowedAttributeType + "'");
        allowedTypesConditions.add(
            ICalculatedAttribute.CALCULATED_ATTRIBUTE_TYPE + " = '" + allowedAttributeType + "'");
      }
    }
    allowedTypesConditions
        .add(IAttribute.TYPE + " = '" + CommonConstants.NUMBER_ATTRIBUTE_TYPE + "'");
    String allowedTypesQuery = String.join(" OR ", allowedTypesConditions);
    return "(" + allowedTypesQuery + ")";
  }
}

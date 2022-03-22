package com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;
import com.cs.core.config.interactor.model.tabs.IGetTabEntityModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.plugin.exception.InvalidDataException;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class GridEditUtil {
  
  protected static List<String> fieldToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IGetTabEntityModel.LABEL, IGetTabEntityModel.CODE);
  
  public static Map<String, Object> fetchGridEditData(Long from, Long size, String searchText,
      String searchColumn, String sortBy, String sortOrder, Boolean isRuntimeRequest)
      throws Exception
  {
    
    Vertex propertyListSequenceNode = GridEditUtil.getOrCreateGridEditSequenceNode();
    List<String> sequenceList = propertyListSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    
    if (isRuntimeRequest && sequenceList.isEmpty()) {
      sequenceList = fetchPropertyWhenSequencePropertyIsEmpty();
    }
    
    List<Map<String, Object>> propertyList = getAllGridEditProperties(from, size, searchText,
        searchColumn, sortBy, sortOrder, sequenceList);
    
    Long totalCountOfPropertyList= getTotalCount(sequenceList);
    List<Map<String, Object>> propetySequenceList = getAllSequenceListProperties(sequenceList);
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IGetGridEditPropertyListSuccessModel.PROPERTY_LIST, propertyList);
    returnMap.put(IGetGridEditPropertyListSuccessModel.PROPERTY_SEQUENCE_LIST, propetySequenceList);
    returnMap.put(IGetGridEditPropertyListSuccessModel.PROPERTY_LIST_TOTAL_COUNT,totalCountOfPropertyList);
    
    return returnMap;
  }
  
  public static List<Map<String, Object>> getAllSequenceListProperties(List<String> idsToExclude)
  {
    
    String quotedIdsToExlude = EntityUtil.quoteIt(idsToExclude);
    String propertySequenceQuery = GridEditUtil.prepareQueryForSequenceList(quotedIdsToExlude);
    
    Iterable<Vertex> propertySequenceVertexs = UtilClass.getGraph()
        .command(new OCommandSQL(propertySequenceQuery))
        .execute();
    
    List<Map<String, Object>> sequenceList = GridEditUtil.getSequences(idsToExclude,
        propertySequenceVertexs);
    
    return sequenceList;
  }
  
  public static List<Map<String, Object>> getAllGridEditProperties(Long from, Long size,
      String searchText, String searchColumn, String sortBy, String sortOrder,
      List<String> idsToExclude)
  {
    List<Map<String, Object>> propertyList = new ArrayList<>();
    
    List<String> sequenceListToExclude = new ArrayList<String>(idsToExclude);
    //Excluding name attribute always, as it should not be visible in grid edit properties.
   	if (!sequenceListToExclude.contains(IStandardConfig.StandardProperty.nameattribute.toString())) {
		  sequenceListToExclude.add(IStandardConfig.StandardProperty.nameattribute.toString());
	  }
    String quotedIdsToExclude = EntityUtil.quoteIt(sequenceListToExclude);
    
    String propertyQuery = GridEditUtil.prepareQuery(from, size, quotedIdsToExclude, searchText,
        searchColumn, sortBy, sortOrder);
    
    Iterable<Vertex> propertyVertexs = UtilClass.getGraph()
        .command(new OCommandSQL(propertyQuery))
        .execute();
    
    GridEditUtil.fillPropertyList(propertyVertexs, propertyList);
    return propertyList;
  }
  
  public static Map<String, Object> fetchGridEditSequenceData(List<String> sequencedPropertyCidList) throws Exception
  {
    if (sequencedPropertyCidList.size() > CommonConstants.SEQUENCE_LIST_LIMIT) {
      throw new InvalidDataException("Only a maximum of " + CommonConstants.SEQUENCE_LIST_LIMIT + " properties can be added to the sequence list");
    }
	//Removing name attribute always, as it should not be visible in grid edit properties.
    sequencedPropertyCidList.remove(IStandardConfig.StandardProperty.nameattribute.toString());
    
    String quotedSequencedPropertyCidList = EntityUtil.quoteIt(sequencedPropertyCidList);
    
    String propertySequenceQuery = GridEditUtil
        .prepareQueryForSequenceList(quotedSequencedPropertyCidList);
    
    Iterable<Vertex> propertySequenceVertexs = UtilClass.getGraph()
        .command(new OCommandSQL(propertySequenceQuery))
        .execute();
    
    List<Map<String, Object>> sequenceList = GridEditUtil.getSequences(sequencedPropertyCidList,
        propertySequenceVertexs);
    
    List<String> cleanedSequencedPropertyCidList = new ArrayList<>();
    for (Map<String, Object> propertyMap : sequenceList) {
    	cleanedSequencedPropertyCidList.add((String)propertyMap.get("code"));
    }
    
    Vertex gridEditSequenceNode = GridEditUtil.getOrCreateGridEditSequenceNode();
    gridEditSequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, cleanedSequencedPropertyCidList);
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IGetGridEditPropertyListSuccessModel.PROPERTY_SEQUENCE_LIST, sequenceList);
    return returnMap;
  }
  
  public static List<String> fetchPropertyWhenSequencePropertyIsEmpty()
  {
    StringBuilder includeQuery = getIncludeTabQuery();
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_PROPERTY + " where "
        + includeQuery + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc limit 25";
    List<String> aPropertyList = new ArrayList<String>();
    Iterable<Vertex> propertyVertexs = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex propertyVertex : propertyVertexs) {
      aPropertyList.add(UtilClass.getCodeNew(propertyVertex));
    }
    return aPropertyList;
  };
  
  public static Vertex getOrCreateGridEditSequenceNode() throws Exception
  {
    
    Vertex GridEditSequenceNode = null;
    try {
      UtilClass.getOrCreateVertexType(VertexLabelConstants.GRID_EDIT_SEQUENCE,
          CommonConstants.CODE_PROPERTY);
      GridEditSequenceNode = UtilClass.getVertexById(SystemLevelIds.GRID_EDIT_SEQUENCE_NODE_ID,
          VertexLabelConstants.GRID_EDIT_SEQUENCE);
    }
    catch (NotFoundException e) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.GRID_EDIT_SEQUENCE, CommonConstants.CODE_PROPERTY);
      Map<String, Object> gridEditMap = new HashMap<>();
      gridEditMap.put(CommonConstants.CODE_PROPERTY, SystemLevelIds.GRID_EDIT_SEQUENCE_NODE_ID);
      gridEditMap.put(CommonConstants.SEQUENCE_LIST, new ArrayList<String>());
      GridEditSequenceNode = UtilClass.createNode(gridEditMap, vertexType, new ArrayList<>());
    }
    return GridEditSequenceNode;
  }
  
  public static List<Map<String, Object>> getSequences(List<String> sequencedPropertyCidList,
      Iterable<Vertex> propertySequenceVertexs)
  {
    
    Map<Integer, Map<String, Object>> sequenceMap = new HashMap<>();
    for (Vertex propertySequenceVertex : propertySequenceVertexs) {
      Map<String, Object> propertySequenceListMap = getPropertyDetails(propertySequenceVertex);
      String propertyListId = (String) propertySequenceListMap.get(IGetTabEntityModel.ID);
      sequenceMap.put(sequencedPropertyCidList.indexOf(propertyListId), propertySequenceListMap);
    }
    
    return new ArrayList<>(new TreeMap<>(sequenceMap).values());
  }
  
  private static Map<String, Object> getPropertyDetails(Vertex propertySequenceVertex)
  {
    Map<String, Object> propertySequenceListMap = UtilClass.getMapFromVertex(fieldToFetch,
        propertySequenceVertex);
    propertySequenceListMap.put(IIdLabelTypeModel.TYPE, getPropertyType(propertySequenceVertex));
    return propertySequenceListMap;
  }
  
  public static void fillPropertyList(Iterable<Vertex> propertyVertexs,
      List<Map<String, Object>> propertyList)
  {
    for (Vertex propertyVertex : propertyVertexs) {
      Map<String, Object> propertyListMap = getPropertyDetails(propertyVertex);
      propertyList.add(propertyListMap);
    }
  }
  
  public static String prepareQueryForSequenceList(String quotedSequencedPropertyCidList)
  {
    StringBuilder includeQuery = getIncludeTabQuery();
    String propertySequenceQuery = "select from " + VertexLabelConstants.ENTITY_TYPE_PROPERTY;
    propertySequenceQuery += " where " + includeQuery + " and code in " + quotedSequencedPropertyCidList;
    return propertySequenceQuery;
  }
  
  public static String prepareQuery(Long from, Long size, String quotedSequencedPropertyCidList,
      String searchText, String searchColumn, String sortBy, String sortOrder)
  {
    StringBuilder includeQuery = getIncludeTabQuery();
    String propertyQuery = "select from " + VertexLabelConstants.ENTITY_TYPE_PROPERTY;
    propertyQuery += " where " + includeQuery + " and code not in " + quotedSequencedPropertyCidList
        + " and outE('Child_Of').size() = 0 and " + ITag.TYPE + " is not null";
    
    /*We need to replace single Quote because Orientdb Query already has single quote surrounding LIKE %% query.
     * Query will give error if encountered single quote in search text
     */
    if (searchText != null && !searchText.isEmpty()) {
      searchText = searchText.replace("'", "\\'");
      StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
      propertyQuery += " and " + searchQuery;
    }
    
    propertyQuery += " order by " + EntityUtil.getDataLanguageConvertedField(sortBy) + " "
        + sortOrder + " skip " + from + " limit " + size;
    return propertyQuery;
  }
  
  public static StringBuilder getIncludeTabQuery()
  {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("isGridEditable = true");
    return queryBuilder;
  }
  
  public static String getPropertyType(Vertex propertyVertex)
  {
    // TODO: Constants should always be used from CommonConstants or
    // EntityConstants
    String propertyType = VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
    String vertexType = propertyVertex.getProperty("@class");
    if (vertexType.equals(VertexLabelConstants.ENTITY_TAG)) {
      propertyType = vertexType;
    }
    return propertyType;
  }
  
  public static void removePropertyFromGridEditSequenceList(String propertyId) throws Exception
  {
    Vertex gridEditSequenceNode = getOrCreateGridEditSequenceNode();
    List<String> newGridEditSequenceList = gridEditSequenceNode
        .getProperty(ISaveGridEditablePropertyListModel.SEQUENCE_LIST);
    newGridEditSequenceList.remove(propertyId);
    gridEditSequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, newGridEditSequenceList);
  }
  
  public static Long getTotalCount(List<String> idsToExclude)
  {
    List<String> sequenceListToExclude = new ArrayList<String>(idsToExclude);
    if (!sequenceListToExclude.contains(IStandardConfig.StandardProperty.nameattribute.toString())) {
      sequenceListToExclude.add(IStandardConfig.StandardProperty.nameattribute.toString());
    }
    String quotedIdsToExclude = EntityUtil.quoteIt(sequenceListToExclude);
    StringBuilder includeQuery = getIncludeTabQuery();
    String query = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_PROPERTY + " where " + includeQuery + " and code not in "
        + quotedIdsToExclude;
    return EntityUtil.executeCountQueryToGetTotalCount(query);
  }
}

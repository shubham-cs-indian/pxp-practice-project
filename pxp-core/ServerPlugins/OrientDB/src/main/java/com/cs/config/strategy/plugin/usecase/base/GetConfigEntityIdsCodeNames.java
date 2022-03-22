package com.cs.config.strategy.plugin.usecase.base;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetConfigEntityIdsCodeNames extends AbstractOrientPlugin {
  
  public static final String       CODE_ID  = "codeId";
  public static final String       ID_CODE  = "idCode";
  
  private static final Set<String> ENTITIES = new HashSet<String>(Arrays.asList(new String[] {
      VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, VertexLabelConstants.ENTITY_TAG,
      VertexLabelConstants.ENTITY_TYPE_LANGUAGE, VertexLabelConstants.PROPERTY_COLLECTION, VertexLabelConstants.ATTRIBUTION_TAXONOMY,
      VertexLabelConstants.UI_TRANSLATIONS, VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL,
      VertexLabelConstants.ENTITY_TYPE_KLASS,
      VertexLabelConstants.ENTITY_TYPE_TASK, VertexLabelConstants.DATA_RULE,
      VertexLabelConstants.VARIANT_CONTEXT, VertexLabelConstants.ORGANIZATION,
      VertexLabelConstants.RULE_LIST, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
      VertexLabelConstants.TAB, VertexLabelConstants.SYSTEM, VertexLabelConstants.ENDPOINT,
      VertexLabelConstants.GOVERNANCE_RULE_KPI, VertexLabelConstants.ENTITY_TYPE_USER,
      VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP,
      VertexLabelConstants.NATURE_RELATIONSHIP, VertexLabelConstants.ROOT_KLASS_TAXONOMY,
      VertexLabelConstants.GOLDEN_RECORD_RULE, VertexLabelConstants.ENTITY_TYPE_ASSET,
      VertexLabelConstants.ENTITY_TYPE_SUPPLIER, VertexLabelConstants.ENTITY_TYPE_TARGET,
      VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET, VertexLabelConstants.ROOT_RELATIONSHIP}));
  
  public GetConfigEntityIdsCodeNames(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigEntityIdsCodeNames/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String entityType = (String) requestMap
        .get(IGetConfigEntityIdsCodeNamesRequestModel.ENTITY_TYPE);
    String mapType = (String) requestMap.get(IGetConfigEntityIdsCodeNamesRequestModel.MAP_TYPE);
    String filter = (String) requestMap.get(IGetConfigEntityIdsCodeNamesRequestModel.FILTER);
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    Iterable<Vertex> iterable;
    if (ENTITIES.contains(entityType)) {
      switch (mapType) {
        case CODE_ID:
          Map<String, Object> codeIdMap = new HashMap<String, Object>();
          List<String> codeNames = (List<String>) requestMap
              .get(IGetConfigEntityIdsCodeNamesRequestModel.LIST);
          if (codeNames.size() == 0) {
            iterable = getAllIdCodes(entityType, filter);
          }
          else {
            StringBuilder codeList = preProcessMap(codeNames);
            iterable = getVerticesByCode(codeList, entityType);
          }
          codeIdMap = getMap(iterable, mapType);
          responseMap.put(IGetConfigEntityIdsCodeNamesResponseModel.MAP, codeIdMap);
          break;
        
        case ID_CODE:
          Map<String, Object> idCodeMap = new HashMap<String, Object>();
          List<String> ids = (List<String>) requestMap
              .get(IGetConfigEntityIdsCodeNamesRequestModel.LIST);
          if (ids.size() == 0) {
            iterable = getAllIdCodes(entityType, filter);
          }
          else {
            StringBuilder idList = preProcessMap(ids);
            iterable = getVerticesById(idList, entityType);
          }
          idCodeMap = getMap(iterable, mapType);
          responseMap.put(IGetConfigEntityIdsCodeNamesResponseModel.MAP, idCodeMap);
          break;
      }
    }
    return responseMap;
  }
  
  private Iterable<Vertex> executeQuery(String query)
  {
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return iterable;
  }
  
  private Iterable<Vertex> getAllIdCodes(String entityType, String filter)
  {
    String queryString = "select " + CommonConstants.CODE_PROPERTY + ", "
        + CommonConstants.CODE_PROPERTY + " from " + entityType;
    if (!((filter == null) || filter.isEmpty())) {
      queryString += " where " + filter;
    }
    return executeQuery(queryString);
  }
  
  private StringBuilder preProcessMap(List<String> list)
  {
    StringBuilder listString = new StringBuilder();
    listString.append("[");
    for (int i = 0; i < list.size(); i++) {
      listString.append("\"");
      listString.append(list.get(i));
      listString.append("\"");
      if (i < list.size() - 1) {
        listString.append(",");
      }
    }
    listString.append("]");
    return listString;
  }
  
  private Map<String, Object> getMap(Iterable<Vertex> iterable, String mapType)
  {
    Map<String, Object> Map = new HashMap<String, Object>();
    
    for (Vertex vertex : iterable) {
      switch (mapType) {
        case CODE_ID:
          Map.put(vertex.getProperty(CommonConstants.CODE_PROPERTY)
              .toString(),
              vertex.getProperty(CommonConstants.CODE_PROPERTY)
                  .toString());
          break;
        case ID_CODE:
          Map.put(vertex.getProperty(CommonConstants.CODE_PROPERTY)
              .toString(),
              vertex.getProperty(CommonConstants.CODE_PROPERTY)
                  .toString());
          break;
      }
    }
    return Map;
  }
  
  private Iterable<Vertex> getVerticesByCode(StringBuilder codeList, String entityType)
  {
    String queryString = "select " + CommonConstants.CODE_PROPERTY + ", "
        + CommonConstants.CODE_PROPERTY + " from " + entityType + " where "
        + CommonConstants.CODE_PROPERTY + " IN " + codeList;
    return executeQuery(queryString);
  }
  
  private Iterable<Vertex> getVerticesById(StringBuilder idList, String entityType)
  {
    String queryString = "select " + CommonConstants.CODE_PROPERTY + ", "
        + CommonConstants.CODE_PROPERTY + " from " + entityType + " where "
        + CommonConstants.CODE_PROPERTY + " IN " + idList;
    return executeQuery(queryString);
  }
}

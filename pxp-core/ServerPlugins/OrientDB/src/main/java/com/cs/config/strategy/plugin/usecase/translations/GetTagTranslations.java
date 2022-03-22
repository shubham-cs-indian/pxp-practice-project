package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.translations.IGetTagTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetTagTranslations extends AbstractOrientPlugin {
  
  public GetTagTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String entityType = (String) requestMap.get(IGetTranslationsRequestModel.ENTITY_TYPE);
    String vertexLabel = EntityUtil.getVertexLabelByEntityType(entityType);
    
    Long from = Long.valueOf(requestMap.get(IGetTranslationsRequestModel.FROM)
        .toString());
    Long size = null;

    if (requestMap.get(IGetTranslationsRequestModel.SIZE) != null) {
      size = Long.valueOf(requestMap.get(IGetTranslationsRequestModel.SIZE)
          .toString());
    }

    String searchText = (String) requestMap.get(IGetTranslationsRequestModel.SEARCH_TEXT);
    String searchColumn = (String) requestMap.get(IGetTranslationsRequestModel.SEARCH_FIELD);
    String searchLang = (String) requestMap.get(IGetTranslationsRequestModel.SEARCH_LANGUAGE);
    String sortBy = (String) requestMap.get(IGetTranslationsRequestModel.SORT_BY);
    String sortOrder = (String) requestMap.get(IGetTranslationsRequestModel.SORT_ORDER);
    String sortLang = (String) requestMap.get(IGetTranslationsRequestModel.SORT_LANGUAGE);
    List<String> languages = (List<String>) requestMap.get(IGetTranslationsRequestModel.LANGUAGES);
    String parentId = (String) requestMap.get(IGetTagTranslationsRequestModel.PARENT_ID);
    
    searchColumn = TranslationsUtils.prepareSearchOrSortField(searchColumn, searchLang);
    sortBy = TranslationsUtils.prepareSearchOrSortField(sortBy, sortLang);
    
    sortOrder = TranslationsUtils.checkAndGetSortOrder(sortOrder);
    
    String query;
    String countQuery;
    Long count;
    if (parentId == null || parentId.equals("-1")) {
      query = "select from " + vertexLabel + " where " + ITag.IS_ROOT + " = true";
      if (entityType.equals(CommonConstants.TAG)) {
        query = query + " AND " + ITag.TYPE + " is not null";
      }
      if (!searchText.isEmpty()) {
        query = query + " AND " + searchColumn + " like '%" + searchText + "%'";
      }
      query = query + " order by " + sortBy + " " + sortOrder + " skip " + from;

      if(size != null) {
        query = query + " limit " + size;
      }
      
      if (!searchText.isEmpty()) {
        countQuery = "select count(*) from " + vertexLabel
            + " where " + ITag.IS_ROOT + " = true AND " + ITag.TYPE + " is not null AND "
            + searchColumn + " like '%" + searchText + "%'";
      }
      else {
        countQuery = "select count(*) from " + vertexLabel
            + " where " + ITag.IS_ROOT + " = true AND " + ITag.TYPE + " is not null";
      }
      count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    }
    else {
      StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
      StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
      
      Vertex tagNode = UtilClass.getVertexByIndexedId(parentId, vertexLabel);
      String rid = tagNode.getId()
          .toString();
      query = "select expand(in ('Child_Of')) from " + rid + conditionQuery + " order by " + sortBy
          + " " + sortOrder + " skip " + from;

      if(size != null) {
        query = query + " limit " + size;
      }
      
      countQuery = "select in('Child_Of').size() as size from " + rid;
      OrientGraph graph = UtilClass.getGraph();
      Iterable<Vertex> countResult = graph.command(new OCommandSQL(countQuery))
          .execute();
      Iterator<Vertex> iterator = countResult.iterator();
      count = Long.valueOf(iterator.next()
          .getProperty("size")
          .toString());
    }
    
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query, languages);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetTranslationsResponseModel.DATA, list);
    responseMap.put(IGetTranslationsResponseModel.COUNT, count);
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query,
      List<String> languages) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> returnList = new ArrayList<>();
    for (Vertex node : vertices) {
      Map<String, Object> map = TranslationsUtils.prepareTagTranslationsMap(languages, node);
      returnList.add(map);
    }
    return returnList;
  }
}

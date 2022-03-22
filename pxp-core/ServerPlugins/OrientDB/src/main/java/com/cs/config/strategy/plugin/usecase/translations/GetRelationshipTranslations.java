package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.translations.IGetRelationshipTranslationsResponseModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetRelationshipTranslations extends AbstractOrientPlugin {
  
  public GetRelationshipTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRelationshipTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String entityType = (String) requestMap.get(IGetTranslationsRequestModel.ENTITY_TYPE);
    String vertexLabel = EntityUtil.getVertexLabelByEntityType(entityType);
    
    Long from = Long.valueOf(requestMap.get(IGetTranslationsRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetTranslationsRequestModel.SIZE)
        .toString());
    String searchText = (String) requestMap.get(IGetTranslationsRequestModel.SEARCH_TEXT);
    String searchColumn = (String) requestMap.get(IGetTranslationsRequestModel.SEARCH_FIELD);
    String searchLang = (String) requestMap.get(IGetTranslationsRequestModel.SEARCH_LANGUAGE);
    String sortBy = (String) requestMap.get(IGetTranslationsRequestModel.SORT_BY);
    String sortOrder = (String) requestMap.get(IGetTranslationsRequestModel.SORT_ORDER);
    String sortLang = (String) requestMap.get(IGetTranslationsRequestModel.SORT_LANGUAGE);
    List<String> languages = (List<String>) requestMap.get(IGetTranslationsRequestModel.LANGUAGES);
    
    searchColumn = TranslationsUtils.prepareSearchOrSortField(searchColumn, searchLang);
    sortBy = TranslationsUtils.prepareSearchOrSortField(sortBy, sortLang);
    
    sortOrder = TranslationsUtils.checkAndGetSortOrder(sortOrder);
    
    String query = "select from " + vertexLabel + " where " + searchColumn + " like '%" + searchText
        + "%'  order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query, languages);
    
    String countQuery = "select count(*) from " + vertexLabel + " where " + searchColumn
        + " like '%" + searchText + "%'";
    Long count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetRelationshipTranslationsResponseModel.DATA, list);
    responseMap.put(IGetRelationshipTranslationsResponseModel.COUNT, count);
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
      Map<String, Object> map = TranslationsUtils.prepareRelationshipTranslationsMap(languages,
          node);
      returnList.add(map);
    }
    return returnList;
  }
}

package com.cs.config.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
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
public class GetStaticTranslations extends AbstractOrientPlugin {
  
  public GetStaticTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStaticTranslations/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
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
    
    sortOrder = TranslationsUtils.checkAndGetSortOrder(sortOrder);
    
    searchColumn = TranslationsUtils.prepareSearchOrSortField(searchColumn, searchLang);
    sortBy = TranslationsUtils.prepareSearchOrSortField(sortBy, sortLang);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    String query = "select from " + VertexLabelConstants.UI_TRANSLATIONS + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query, languages);
    
    String countQuery = "select count(*) from " + VertexLabelConstants.UI_TRANSLATIONS
        + conditionQuery;
    Long count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
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
      Map<String, Object> map = TranslationsUtils.prepareTranslationsMap(languages, node);
      returnList.add(map);
    }
    return returnList;
  }
}

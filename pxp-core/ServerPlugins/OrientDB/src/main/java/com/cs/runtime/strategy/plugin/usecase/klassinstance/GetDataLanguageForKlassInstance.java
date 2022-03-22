package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetDataLanguageForKlassInstance extends AbstractOrientPlugin {
  
  public GetDataLanguageForKlassInstance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDataLanguageForKlassInstance/*" };
  }
  
  private static final List<String> fieldsToFetch = Arrays.asList(ILanguage.ID, ILanguage.ICON,
      ILanguage.LABEL, ILanguage.CODE, ILanguage.NUMBER_FORMAT, ILanguage.DATE_FORMAT,
      ILanguage.LOCALE_ID);
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    
    List<String> languageTaxonomyIds = (List<String>) requestMap
        .get(IGetAllDataLanguagesModel.LANGUAGE_CODES);
    languageTaxonomyIds.add(SystemLevelIds.LANGUAGE_TREE);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    String conditionQueryPart = " code not in " + EntityUtil.quoteIt(languageTaxonomyIds)
        + " AND isDataLanguage = " + EntityUtil.quoteIt(true);
    StringBuilder queryPart = new StringBuilder();
    queryPart.append(conditionQueryPart);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(queryPart, searchQuery);
    
    String query = "select from " + VertexLabelConstants.LANGUAGE + conditionQuery + " order by "
        + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    ;
    Iterable<Vertex> languageTaxonomyVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    List<Map<String, Object>> taxonomyInfoList = new ArrayList<>();
    
    for (Vertex vertex : languageTaxonomyVertices) {
      Map<String, Object> languageTaxonomyInfo = UtilClass.getMapFromVertex(fieldsToFetch, vertex,
          null);
      taxonomyInfoList.add(languageTaxonomyInfo);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IListModel.LIST, taxonomyInfoList);
    
    return returnMap;
  }
}

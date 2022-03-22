package com.cs.plugin.utility;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.translations.IStandardTranslationModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.List;

public class SearchDbUtil {
  
  /**
   * @author Rohit.Raina
   * @param searchColumn
   * @param searchText
   * @return
   */
  public static String getSearchQuery(String searchColumn, String searchText)
  {
    String searchQuery = "";
    if (!searchColumn.isEmpty() && !searchText.isEmpty()) {
      if (IStandardTranslationModel.TRNASLATION_FIELDS.contains(searchColumn)) {
        searchColumn = EntityUtil.getLanguageConvertedField(searchColumn);
      }
      searchQuery += " [" + searchColumn + " like '%" + searchText + "%' ] ";
    }
    return searchQuery;
  }
  
  /**
   * @author Rohit.Raina
   * @param query
   * @return
   */
  public static List<String> executeQuery(String query)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<String> tagValueIds = new ArrayList<>();
    for (Vertex tagVertex : searchResults) {
      tagValueIds.add(UtilClass.getCodeNew(tagVertex));
    }
    
    return tagValueIds;
  }
}

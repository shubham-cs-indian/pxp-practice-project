package com.cs.plugin.utility.property.tag;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.plugin.utility.SearchDbUtil;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;

public class TagDbUtil {
  
  /**
   * @author Rohit.Raina
   * @param tag
   * @param searchColumn
   * @param searchText
   * @return
   * @throws Exception
   */
  public static List<String> getTagValueIds(String tagGroupId, String searchColumn,
      String searchText) throws Exception
  {
    String searchQuery = SearchDbUtil.getSearchQuery(searchColumn, searchText);
    
    Vertex tagNode = UtilClass.getVertexByIndexedId(tagGroupId, VertexLabelConstants.ENTITY_TAG);
    String query = "select from (select expand(in('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')" + searchQuery + ") from "
        + tagNode.getId() + ")";
    return SearchDbUtil.executeQuery(query);
  }

  public static List<String> getTagValueIds(String tagGroupId) throws Exception
  {
    Vertex tagNode = UtilClass.getVertexByIndexedId(tagGroupId, VertexLabelConstants.ENTITY_TAG);
    String query = String.format("select from (select expand(in('%s')) from %s )", RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, tagNode.getId());
    return SearchDbUtil.executeQuery(query);
  }
}

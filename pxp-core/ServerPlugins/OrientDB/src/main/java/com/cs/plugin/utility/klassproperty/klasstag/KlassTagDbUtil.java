package com.cs.plugin.utility.klassproperty.klasstag;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.plugin.utility.SearchDbUtil;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;

public class KlassTagDbUtil {
  
  /**
   * @author Rohit.Raina
   * @param tag
   * @param searchColumn
   * @param searchText
   * @return
   * @throws Exception
   */
  public static List<String> getAllowedTagValueIds(String klassTagId, String searchColumn,
      String searchText) throws Exception
  {
    String searchQuery = SearchDbUtil.getSearchQuery(searchColumn, searchText);
    Vertex klassTag = UtilClass.getVertexByIndexedId(klassTagId, VertexLabelConstants.KLASS_TAG);
    String query = "select expand(out('" + RelationshipLabelConstants.HAS_KLASS_TAG_VALUE + "')"
        + searchQuery + ") from " + klassTag.getId();
    
    return SearchDbUtil.executeQuery(query);
  }
  
  /**
   * @author Rohit.Raina
   * @param klassIds
   * @param tagGroupId
   * @return
   * @throws Exception
   */
  public static Iterable<Vertex> getKlassTagVerticesFromTagAndKlasses(String tagGroupId,
      List<String> klassIds) throws Exception
  {
    Vertex tagVertex = UtilClass.getVertexByIndexedId(tagGroupId, VertexLabelConstants.ENTITY_TAG);
    String query = "select expand(in('" + RelationshipLabelConstants.HAS_PROPERTY + "')[['"
        + String.join("','", klassIds) + "'] contains in('"
        + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').code ])" + " from "
        + tagVertex.getId();
    
    Iterable<Vertex> klassTagVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return klassTagVertices;
  }
}

package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.taxonomy.IGetChildTaxonomiesByParentIdAndSelectedTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.taxonomy.IParentChildTaxonomyIdModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetChildTaxonomiesByParentIdWithSelectedTaxonomies extends AbstractOrientPlugin {
  
  public GetChildTaxonomiesByParentIdWithSelectedTaxonomies(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetChildTaxonomiesByParentIdWithSelectedTaxonomies/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> parentIds = (List<String>) requestMap
        .get(IGetChildTaxonomiesByParentIdAndSelectedTaxonomiesRequestModel.PARENT_TAXONOMIES);
    List<String> selectedIds = (List<String>) requestMap
        .get(IGetChildTaxonomiesByParentIdAndSelectedTaxonomiesRequestModel.SELECTED_TAXONOMIES);
    List<String> selectedIdsForQuery = new ArrayList<>();
    for (String id : selectedIds) {
      selectedIdsForQuery.add("'" + id + "'");
    }
    Map<String, Object> parentChildMap = new HashMap<>();
    for (String parentId : parentIds) {
      Set<String> childIds = new HashSet<>();
      String query = "select code from Attribution_Taxonomy " + "where code in "
          + selectedIdsForQuery + " and isTaxonomy = true";
      
      Iterable<Vertex> childTaxonomies = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex child : childTaxonomies) {
        String childId = child.getProperty(CommonConstants.CODE_PROPERTY);
        childIds.add(childId);
      }
      parentChildMap.put(parentId, childIds);
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IParentChildTaxonomyIdModel.TAXONOMY_MAP, parentChildMap);
    return returnMap;
  }
}

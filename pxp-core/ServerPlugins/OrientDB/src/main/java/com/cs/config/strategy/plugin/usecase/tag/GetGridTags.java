package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagGridResponseModel;
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
public class GetGridTags extends AbstractOrientPlugin {
  
  public GetGridTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGridTags/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
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
    List<String> types = (List<String>) requestMap.get(IConfigGetAllRequestModel.TYPES);
    Long count = new Long(0);
    
    StringBuilder childrenSizeQuery = new StringBuilder();
    childrenSizeQuery.append(" outE('Child_Of').size() = 0 AND " + ITag.TYPE + " is not null ");
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    StringBuilder tagTypeQuery = UtilClass.getTypeQueryWithoutANDOperator(types, ITag.TAG_TYPE);
    
    StringBuilder condition = EntityUtil.getConditionQuery(childrenSizeQuery, searchQuery,
        tagTypeQuery);
    
    String query = "select from " + VertexLabelConstants.ENTITY_TAG + condition + " order by "
        + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    responseMap = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TAG + condition;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    responseMap.put(IGetTagGridResponseModel.COUNT, count);
    
    return responseMap;
  }
  
  private Map<String, Object> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> rootNode = new HashMap<>();
    List<Map<String, Object>> childTags = new ArrayList<>();
    Map<String, Object> referencedTags = new HashMap<>();
    rootNode.put(IGetTagGridResponseModel.TAGS_LIST, childTags);
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex searchResult : searchResults) {
      Vertex tagVertex = null;
      tagVertex = searchResult;
      HashMap<String, Object> childmap = new HashMap<String, Object>();
      childmap.putAll(TagUtils.getTagMap(tagVertex, false));
      childTags.add(childmap);
    }
    referencedTags = TagUtils.fillReferenceTags(childTags);
    rootNode.put(IGetTagGridResponseModel.REFERENCED_TAGS, referencedTags);
    return rootNode;
  }
}

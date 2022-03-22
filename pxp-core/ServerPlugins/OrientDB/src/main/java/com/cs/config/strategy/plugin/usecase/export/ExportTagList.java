package com.cs.config.strategy.plugin.usecase.export;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
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

public class ExportTagList extends AbstractOrientPlugin {
  
  public ExportTagList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportTagList/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    HashMap<String, Object> responseMap = new HashMap<>();
    
    StringBuilder childrenSizeQuery = new StringBuilder();
    childrenSizeQuery.append(" outE('Child_Of').size() = 0 AND " + ITag.TYPE + " is not null ");
    
    List<String> itemCodes = (List<String>) requestMap.get("itemCodes");
    
    Iterable<Vertex> resultIterable = null;
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(itemCodes, ITag.CODE);
    
    StringBuilder condition = EntityUtil.getConditionQuery(childrenSizeQuery, codeQuery);
    
    String query = "select from " + VertexLabelConstants.ENTITY_TAG + condition + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    
    responseMap.put("list", list);
    
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> childTags = new ArrayList<>();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex searchResult : searchResults) {
      Vertex tagVertex = null;
      tagVertex = searchResult;
      Map<String, Object> tag = TagUtils.getTagMap(tagVertex, false);
      Map<String,Object> defaultValue = (Map<String, Object>) tag.get(ITag.DEFAULT_VALUE);
      String defaultTagValueCode = null;
      if(defaultValue != null) {
         defaultTagValueCode = (String) defaultValue.get(CommonConstants.CODE_PROPERTY);
      }
      tag.put(ITag.DEFAULT_VALUE, defaultTagValueCode);
      tag.remove(CommonConstants.TAG_VALUES);
      childTags.add(tag);
    }
    return childTags;
  }
}

package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetTagValuesByTagIds extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_FETCH_FOR_TAG = Arrays.asList(CommonConstants.LABEL_PROPERTY, CommonConstants.TYPE_PROPERTY,
      ITag.ICON, ITag.COLOR, ITag.IS_MULTI_SELECT, CommonConstants.CODE_PROPERTY, ITag.TAG_TYPE);
  
  public GetTagValuesByTagIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> tagIds = (List<String>) requestMap.get("ids");
    
    List<Map<String, Object>> entitiesList = new ArrayList<Map<String, Object>>();
    for (String tagGroupId : tagIds) {
      Vertex tagGroupNode = null;
      try {
        tagGroupNode = UtilClass.getVertexById(tagGroupId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        continue;
      }
      
      List<String> tagValuesSequence  = new ArrayList<>();
      tagValuesSequence = tagGroupNode.getProperty(ITag.TAG_VALUES_SEQUENCE);
      tagValuesSequence.add(tagGroupId);
      
      String query = "select from " + VertexLabelConstants.ENTITY_TAG + " where code in ['" + String.join("','", tagValuesSequence) + "']";
      
      executeQueryAndPrepareResponse(query, VertexLabelConstants.ENTITY_TAG, entitiesList);
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", entitiesList);
    return response;
    
  }
  
  private Boolean executeQueryAndPrepareResponse(String query, String entityLabel, List<Map<String, Object>> entitiesList)
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query)).execute();
    Boolean found = false;
    for (Vertex searchResult : searchResults) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_TAG, searchResult);
      entitiesList.add(entityMap);
      found = true;
    }
    return found;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagValuesByTagIds/*" };
  }
}

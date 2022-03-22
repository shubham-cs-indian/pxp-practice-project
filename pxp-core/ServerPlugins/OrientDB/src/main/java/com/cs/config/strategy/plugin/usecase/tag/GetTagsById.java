package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTagsById extends AbstractOrientPlugin {
  
  public GetTagsById(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> tagIds = (List<String>) requestMap.get("ids");
    
    List<Map<String, Object>> tagsList = new ArrayList<>();
    
    for (String tagId : tagIds) {
      Vertex tagNode = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
      if (tagNode != null) {
        tagsList.add(TagUtils.getTagMap(tagNode, true));
      }
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", tagsList);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagsById/*" };
  }
}

package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetAllTags extends AbstractOrientPlugin {
  
  public GetAllTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    List<Map<String, Object>> tags = new ArrayList<>();
    
    Iterable<Vertex> i = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
            + " where " + ITag.IS_ROOT + " = true"))
        .execute();
    
    for (Vertex tagNode : i) {
      tags.add(TagUtils.getTagMap(tagNode, true));
    }
    
    returnMap.put("list", tags);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllTags/*" };
  }
}

/**/

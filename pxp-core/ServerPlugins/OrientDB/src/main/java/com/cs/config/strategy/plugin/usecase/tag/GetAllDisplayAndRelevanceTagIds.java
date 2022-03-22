package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllDisplayAndRelevanceTagIds extends AbstractOrientPlugin {
  
  public GetAllDisplayAndRelevanceTagIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Iterable<Vertex> i = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select code from tag where " + ITag.IS_ROOT + " = true and shouldDisplay=true"))
        .execute();
    List<String> displayTagIds = new ArrayList<>();
    for (Vertex tagNode : i) {
      displayTagIds.add(tagNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(
            "select code from tag where " + ITag.IS_ROOT + " = true and isForRelevance=true"))
        .execute();
    List<String> relevanceTagIds = new ArrayList<>();
    for (Vertex tagNode : iterable) {
      relevanceTagIds.add(tagNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("displayTagIds", displayTagIds);
    response.put("relevanceTagIds", relevanceTagIds);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllDisplayAndRelevanceTagIds/*" };
  }
}

package com.cs.config.strategy.plugin.usecase.tag;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetAllDisplayTagIds extends AbstractOrientPlugin {
  
  public GetAllDisplayTagIds(final OServerCommandConfiguration iConfiguration)
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
    return displayTagIds;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllDisplayTagIds/*" };
  }
}

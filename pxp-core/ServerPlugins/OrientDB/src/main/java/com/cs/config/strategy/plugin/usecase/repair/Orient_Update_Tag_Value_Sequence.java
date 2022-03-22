package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Update_Tag_Value_Sequence extends AbstractOrientPlugin {
  
  public Orient_Update_Tag_Value_Sequence(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Iterable<Vertex> i = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
            + " where outE('Child_Of').size() = 0 order by label asc"))
        .execute();
    
    for (Vertex tagNode : i) {
      
      String rid = tagNode.getId()
          .toString();
      
      Iterable<Vertex> resultIterable = UtilClass.getGraph()
          .command(
              new OCommandSQL("select expand(in ('Child_Of')) from " + rid + " order by label asc"))
          .execute();
      List<String> childIds = new ArrayList<>();
      for (Vertex childTagNode : resultIterable) {
        childIds.add(UtilClass.getCodeNew(childTagNode));
      }
      tagNode.setProperty(ITag.TAG_VALUES_SEQUENCE, childIds);
    }
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> returnMap = new HashMap<>();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Update_Tag_Value_Sequence/*" };
  }
}

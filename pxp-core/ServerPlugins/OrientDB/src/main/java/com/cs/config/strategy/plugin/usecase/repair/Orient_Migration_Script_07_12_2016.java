package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_07_12_2016 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_07_12_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<String, Object>();
    String query = "SELECT FROM " + RelationshipLabelConstants.PREVIOUS_SECTION;
    Iterable<Edge> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Edge previousSectionOf : resultIterable) {
      List<String> utilizingKlassIds = previousSectionOf
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      List<String> newUtilizingKlassIds = new ArrayList<>(utilizingKlassIds);
      for (String utilizingKlassId : utilizingKlassIds) {
        try {
          Vertex klassNode = UtilClass.getVertexById(utilizingKlassId,
              VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        }
        catch (NotFoundException e) {
          newUtilizingKlassIds.remove(utilizingKlassId);
        }
      }
      previousSectionOf.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
          newUtilizingKlassIds);
    }
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_07_12_2016/*" };
  }
}

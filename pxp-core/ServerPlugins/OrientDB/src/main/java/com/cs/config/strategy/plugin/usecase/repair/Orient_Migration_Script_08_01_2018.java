/*
 * (SetStandardAttributesDisabled) : This migration script dated 30-08-2016 is
 * to update flat field attributes to isDisabled true.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class Orient_Migration_Script_08_01_2018 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_08_01_2018(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> iterable = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ROLE))
        .execute();
    for (Vertex vertex : iterable) {
      vertex.setProperty(IRole.IS_DASHBOARD_ENABLE, true);
      vertex.setProperty(IRole.LANDING_SCREEN, CommonConstants.DASHBOARD);
    }
    
    UtilClass.getGraph()
        .commit();
    
    HashMap<String, Object> response = new HashMap<>();
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_08_01_2018/*" };
  }
}

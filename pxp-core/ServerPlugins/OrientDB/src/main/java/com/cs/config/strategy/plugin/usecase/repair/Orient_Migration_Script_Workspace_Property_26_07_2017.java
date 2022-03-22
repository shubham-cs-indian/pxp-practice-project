package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.Map;

public class Orient_Migration_Script_Workspace_Property_26_07_2017 extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_Workspace_Property_26_07_2017(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    OrientGraph graph = UtilClass.getGraph();
    
    // Get All Endpoints
    Iterable<Vertex> endpointVertices = graph
        .command(
            new OCommandSQL("select from " + VertexLabelConstants.ENDPOINT + " order by label asc"))
        .execute();
    
    for (Vertex profileNode : endpointVertices) {
      /*
      String indexName = profileNode.getProperty(IEndpoint.INDEX_NAME);
      if (profileNode.getProperty(IEndpoint.WORK_SPACE) == null) {
      if (indexName.equals("central_staging")) {
      profileNode.setProperty(IEndpoint.WORK_SPACE,
      CommonConstants.CENTRAL_STAGEING_INDEX_NAME);
      profileNode.setProperty(IEndpoint.ENDPOINT_INDEX,
      "endpoint" + System.currentTimeMillis());
      }
      else if (indexName.equals("cs")) {
      profileNode.setProperty(IEndpoint.WORK_SPACE, CommonConstants.MDM_STAGEING_INDEX_NAME);
      profileNode.setProperty(IEndpoint.ENDPOINT_INDEX,
      "endpoint" + System.currentTimeMillis());
      }
      else {
      profileNode.setProperty(IEndpoint.WORK_SPACE,
      CommonConstants.SUPPLIER_STAGEING_INDEX_NAME);
      profileNode.setProperty(IEndpoint.ENDPOINT_INDEX, indexName);
      }
      }
      */}
    
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Workspace_Property_26_07_2017/*" };
  }
}

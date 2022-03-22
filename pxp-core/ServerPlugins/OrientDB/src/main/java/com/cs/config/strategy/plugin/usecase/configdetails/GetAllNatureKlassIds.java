package com.cs.config.strategy.plugin.usecase.configdetails;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllNatureKlassIds extends AbstractOrientPlugin {
  
  public GetAllNatureKlassIds(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> natureKlassIds = new ArrayList<>();
    
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
            + " where isNature = true"))
        .execute();
    
    for (Vertex natureKlassVertex : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY),
          natureKlassVertex));
      natureKlassIds.add((String) map.get(CommonConstants.ID_PROPERTY));
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put(CommonConstants.IDS_PROPERTY, natureKlassIds);
    graph.commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllNatureKlassIds/*" };
  }
}

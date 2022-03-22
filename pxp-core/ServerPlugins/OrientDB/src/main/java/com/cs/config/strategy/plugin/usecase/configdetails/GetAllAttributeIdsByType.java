package com.cs.config.strategy.plugin.usecase.configdetails;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
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

public class GetAllAttributeIdsByType extends AbstractOrientPlugin {
  
  public GetAllAttributeIdsByType(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> attributeIdsByType = new ArrayList<>();
    
    String attributeType = (String) requestMap.get(IIdAndTypeModel.TYPE);
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE
            + " where type = \"" + attributeType + "\""))
        .execute();
    
    for (Vertex dateAttributeVertex : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY),
          dateAttributeVertex));
      attributeIdsByType.add((String) map.get(CommonConstants.ID_PROPERTY));
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put(IIdsListParameterModel.IDS, attributeIdsByType);
    graph.commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllAttributeIdsByType/*" };
  }
}

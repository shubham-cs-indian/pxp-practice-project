package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDataRulesByEndpointId extends AbstractOrientPlugin {
  
  public GetDataRulesByEndpointId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDataRulesByEndpointId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> dataToReturn = new HashMap<>();
    List<Map<String, Object>> dataRules = new ArrayList<>();
    String endpointId = (String) requestMap.get(IIdParameterModel.ID);
    Vertex endPoint = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
    Iterable<Edge> dataRulesEdge = endPoint.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_DATA_RULE);
    for (Edge dataRuleEdge : dataRulesEdge) {
      Vertex datarule = dataRuleEdge.getVertex(Direction.IN);
      dataRules.add(GetDataRuleUtils.getDataRuleFromNode(datarule));
    }
    dataToReturn.put("list", dataRules);
    return dataToReturn;
  }
}

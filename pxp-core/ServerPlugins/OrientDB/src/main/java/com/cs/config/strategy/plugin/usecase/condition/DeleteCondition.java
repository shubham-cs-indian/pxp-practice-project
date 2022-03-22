package com.cs.config.strategy.plugin.usecase.condition;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteCondition extends OServerCommandAuthenticatedDbAbstract {
  
  public DeleteCondition(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    String result = "";
    String model = iRequest.content.toString();
    HashMap<String, Object> map = new HashMap<String, Object>();
    List<String> dataRuleIds = new ArrayList<String>();
    
    try {
      map = ObjectMapperUtil.readValue(model, HashMap.class);
      dataRuleIds = (List<String>) map.get("ids");
      
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      List<String> deletedIds = new ArrayList<>();
      for (String id : dataRuleIds) {
        Vertex userNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.CONDITION);
        userNode.remove();
        deletedIds.add(id);
      }
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("success", deletedIds);
      
      graph.commit();
      ResponseCarrier.successResponse(iResponse, responseMap);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteConditions/*" };
  }
}

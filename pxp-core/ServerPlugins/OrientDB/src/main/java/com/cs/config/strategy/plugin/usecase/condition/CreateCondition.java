package com.cs.config.strategy.plugin.usecase.condition;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.HashMap;
import java.util.Map;

public class CreateCondition extends OServerCommandAuthenticatedDbAbstract {
  
  public CreateCondition(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    
    String result = "";
    String model = iRequest.content.toString();
    HashMap<String, Object> map = new HashMap<String, Object>();
    String id = "";
    String label = "";
    Map<String, Object> returnMap = new HashMap<>();
    HashMap<String, Object> request = new HashMap<String, Object>();
    try {
      request = ObjectMapperUtil.readValue(model, HashMap.class);
      map = (HashMap<String, Object>) request.get("condition");
      
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.CONDITION,
          CommonConstants.CODE_PROPERTY);
      
      Vertex dataRuleNode = UtilClass.createNode(map, vertexType);
      
      returnMap = UtilClass.getMapFromNode(dataRuleNode);
      graph.commit();
      
      ResponseCarrier.successResponse(iResponse, returnMap);
      
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateCondition/*" };
  }
}

package com.cs.config.strategy.plugin.usecase.condition;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.condition.ConditionNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;

public class GetCondition extends OServerCommandAuthenticatedDbAbstract {
  
  public GetCondition(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    String result = "";
    String model = iRequest.content.toString();
    HashMap<String, Object> map = new HashMap<String, Object>();
    HashMap<String, Object> request = new HashMap<String, Object>();
    String userId = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    try {
      request = ObjectMapperUtil.readValue(model, HashMap.class);
      map = (HashMap<String, Object>) request.get("condition");
      userId = (String) map.get("id");
      
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      try {
        Vertex userNode = UtilClass.getVertexByIndexedId(userId, VertexLabelConstants.CONDITION);
        returnMap.putAll(UtilClass.getMapFromNode(userNode));
      }
      catch (NotFoundException e) {
        throw new ConditionNotFoundException();
      }
      graph.commit();
      
      ResponseCarrier.successResponse(iResponse, returnMap);
      
    }
    catch (PluginException e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetCondition/*" };
  }
}

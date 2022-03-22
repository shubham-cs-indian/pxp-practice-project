package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.ImportVertexLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.sql.OCommandSQL;
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

public class AllKlass extends OServerCommandAuthenticatedDbAbstract {
  
  public AllKlass(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      List<Map<String, Object>> usersList = new ArrayList<>();
      Iterable<Vertex> resultIterable;
      if (iRequest.databaseName.equals("cs")) {
        resultIterable = graph
            .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_KLASS))
            .execute();
      }
      else {
        resultIterable = graph
            .command(new OCommandSQL(
                "select from " + ImportVertexLabelConstants.ENTITY_TYPE_IMPORT_KLASS))
            .execute();
      }
      
      for (Vertex userNode : resultIterable) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.putAll(UtilClass.getMapFromNode(userNode));
        usersList.add(map);
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("list", usersList);
      graph.commit();
      ResponseCarrier.successResponse(iResponse, response);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|AllKlass/*" };
  }
}

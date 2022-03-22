package com.cs.runtime.strategy.plugin.usecase.file.base;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.runtime.strategy.plugin.usecase.file.util.FileUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGetSupplierFileKlassMapping
    extends OServerCommandAuthenticatedDbAbstract {
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      String requestBody = iRequest.content.toString();
      Map<String, Object> requestMap = ObjectMapperUtil.readValue(requestBody, HashMap.class);
      
      Map<String, Object> responseMap = execute(requestMap);
      graph.commit();
      
      ResponseCarrier.successResponse(iResponse, responseMap);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    
    return false;
  }
  
  @SuppressWarnings("unchecked")
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> supplierKlassIdMap = (HashMap<String, Object>) requestMap.get("request");
    return FileUtil.geSupplierKlassMapping(supplierKlassIdMap);
  }
}

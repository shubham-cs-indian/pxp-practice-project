package com.cs.imprt.config.strategy.plugin.usecase.vertextypes;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.vertextypes.CreateVertexTypes;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.ImportVertexLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentEmbedded;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.tinkerpop.blueprints.Parameter;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.HashMap;

public class CreateImportVertexTypes extends CreateVertexTypes {
  
  public CreateImportVertexTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      ODatabaseDocumentEmbedded database = (ODatabaseDocumentEmbedded) getProfiledDatabaseInstance(
          iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      super.createTypes(database, graph);
      
      OrientVertexType vertexType = graph
          .getVertexType(ImportVertexLabelConstants.ENTITY_TYPE_IMPORT_KLASS);
      if (vertexType == null) {
        vertexType = graph.createVertexType(ImportVertexLabelConstants.ENTITY_TYPE_IMPORT_KLASS,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
            new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));
      }
      
      vertexType = graph.getVertexType(VertexLabelConstants.ENTITY_STANDARD_SUPPLIER);
      if (vertexType == null) {
        vertexType = graph.createVertexType(VertexLabelConstants.ENTITY_STANDARD_SUPPLIER,
            VertexLabelConstants.ONBOARDING_USER);
        graph.createKeyIndex(CommonConstants.CODE_PROPERTY, Vertex.class,
            new Parameter("type", "UNIQUE"), new Parameter("class", vertexType));
      }
      
      HashMap returnMap = new HashMap<String, Object>();
      returnMap.put("success", "vertex types created");
      
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
    return new String[] { "POST|CreateImportVertexTypes/*" };
  }
}

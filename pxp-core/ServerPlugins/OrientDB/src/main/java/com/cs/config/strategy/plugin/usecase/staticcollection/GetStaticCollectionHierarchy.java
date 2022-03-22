package com.cs.config.strategy.plugin.usecase.staticcollection;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetStaticCollectionHierarchy extends OServerCommandAuthenticatedDbAbstract {
  
  public GetStaticCollectionHierarchy(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    String model = iRequest.content.toString();
    HashMap<String, Object> requestMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<>();
    ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
    OrientGraph graph = new OrientGraph(database);
    UtilClass.setGraph(graph);
    
    try {
      requestMap = ObjectMapperUtil.readValue(model, HashMap.class);
      String id = (String) requestMap.get(IIdParameterModel.ID);
      Vertex parentNode = null;
      if (id.equals("-1")) {
        List<String> collectionIds = new ArrayList<>();
        
        Iterable<Vertex> i = graph
            .command(new OCommandSQL("select from " + VertexLabelConstants.COLLECTION
                + " where outE('Child_Of').size() = 0"))
            .execute();
        
        for (Vertex collectionNode : i) {
          collectionIds.add(collectionNode.getProperty(CommonConstants.CODE_PROPERTY));
        }
        
        returnMap.put(IIdsListParameterModel.IDS, collectionIds);
      }
      else {
        try {
          parentNode = UtilClass.getVertexById(id, VertexLabelConstants.COLLECTION);
        }
        catch (NotFoundException e) {
          throw new CollectionNodeNotFoundException();
        }
        Iterable<Vertex> childVetices = parentNode.getVertices(Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
        List<String> ids = new ArrayList<>();
        for (Vertex childNode : childVetices) {
          ids.add(childNode.getProperty(CommonConstants.CODE_PROPERTY));
        }
        returnMap.put(IIdsListParameterModel.IDS, ids);
      }
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
    return new String[] { "POST|GetStaticCollectionHierarchy/*" };
  }
}

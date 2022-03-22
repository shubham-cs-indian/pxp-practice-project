package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetTaxonomyIdsByCodeIdForOnboarding extends OServerCommandAuthenticatedDbAbstract {
  
  public GetTaxonomyIdsByCodeIdForOnboarding(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTaxonomyIdsByCodeIdForOnboarding/*" };
  }
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY = Arrays
      .asList(ITaxonomy.LABEL, CommonConstants.CODE_PROPERTY);
  
  @Override
  public boolean execute(OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      String requestBody = iRequest.content.toString();
      Map<String, Object> requestMap = ObjectMapperUtil.readValue(requestBody, HashMap.class);
      
      Map<String, Object> responseMap = getResponse(requestMap);
      ResponseCarrier.successResponse(iResponse, responseMap);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    
    return false;
  }
  
  protected Map<String, Object> getResponse(Map<String, Object> requestMap) throws Exception
  {
    List<String> taxonomyCodeList = (List<String>) requestMap.get(IListModel.LIST);
    Map<String, Object> returnMap = new HashMap<>();
    List<String> taxonomyIds = new ArrayList<>();
    
    for (String taxonomyCode : taxonomyCodeList) {
      try {
        Vertex taxonomyVertex = UtilClass.getVertexByCode(taxonomyCode,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        if (taxonomyVertex != null) {
          taxonomyIds.add(UtilClass.getCodeNew(taxonomyVertex));
        }
      }
      catch (NotFoundException e) {
        e.printStackTrace();
        //RDBMSLogger.instance().exception(e);
      }
    }
    
    returnMap.put("ids", taxonomyIds);
    
    return returnMap;
  }
}

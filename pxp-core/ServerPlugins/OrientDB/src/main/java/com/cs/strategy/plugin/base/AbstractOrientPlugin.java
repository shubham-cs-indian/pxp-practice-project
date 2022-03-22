package com.cs.strategy.plugin.base;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.model.ILanguageModel;
import com.cs.config.strategy.plugin.model.LanguageModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentEmbedded;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractOrientPlugin extends OServerCommandAuthenticatedDbAbstract {
  
  public AbstractOrientPlugin(OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      ODatabaseDocumentEmbedded database = (ODatabaseDocumentEmbedded) getProfiledDatabaseInstance(
          iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      UtilClass.setDatabase(database);
      
      String uiLanguage = iRequest.getHeader(ILanguageModel.UI_LANGUAGE);
      String dataLanguage = iRequest.getHeader(ILanguageModel.DATA_LANGUAGE);
      
      ILanguageModel languageModel = new LanguageModel();
      languageModel.setUiLanguage(uiLanguage);
      languageModel.setDataLanguage(dataLanguage);
      
      UtilClass.setLanguage(languageModel);
      
      String requestBody = iRequest.content.toString();
      Map<String, Object> requestMap = ObjectMapperUtil.readValue(requestBody, HashMap.class);
      
      Object responseMap = execute(requestMap);
      ResponseCarrier.successResponse(iResponse, responseMap);
      graph.shutdown();
    }
    catch (PluginException e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    catch (Throwable e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    return false;
  }
  
  protected abstract Object execute(Map<String, Object> requestMap) throws Exception;
}

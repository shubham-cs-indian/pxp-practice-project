package com.cs.config.strategy.plugin.usecase.user;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkCreateUsers extends OServerCommandAuthenticatedDbAbstract {
  
  public BulkCreateUsers(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    String model = iRequest.content.toString();
    List<Map<String, Object>> mapList = new ArrayList<>();
    List<Map<String, Object>> returnMapList = new ArrayList<>();
    List<Map<String, Object>> failedUserList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    try {
      HashMap<String, Object> map = ObjectMapperUtil.readValue(model, HashMap.class);
      mapList = (List<Map<String, Object>>) map.get("list");
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      for (Map<String, Object> userMap : mapList) {
        try {
          Map<String, Object> returnedUserMap = UserUtils.createUser(userMap);
          Map<String, Object> returnMap = new HashMap<>();
          returnMap.put(ISummaryInformationModel.ID, returnedUserMap.get(IUserModel.ID));
          returnMap.put(ISummaryInformationModel.LABEL, returnedUserMap.get(IUserModel.USER_NAME));
          returnMapList.add(returnMap);
        }
        catch (PluginException e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
              (String) userMap.get(IUserModel.USER_NAME));
          addToFailureIds(failedUserList, userMap);
        }
        catch (Exception e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
              (String) userMap.get(IUserModel.USER_NAME));
          addToFailureIds(failedUserList, userMap);
        }
      }
      
      Map<String, Object> result = new HashMap<>();
      result.put(IPluginSummaryModel.FAILURE, failure);
      result.put(IPluginSummaryModel.SUCCESS, returnMapList);
      result.put(IPluginSummaryModel.FAILED_IDS, failedUserList);
      ResponseCarrier.successResponse(iResponse, result);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    return false;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedUserList, Map<String, Object> user)
  {
    Map<String, Object> failedUserMap = new HashMap<>();
    failedUserMap.put(ISummaryInformationModel.ID, user.get(IUserModel.ID));
    failedUserMap.put(ISummaryInformationModel.LABEL, user.get(IUserModel.USER_NAME));
    failedUserList.add(failedUserMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateUsers/*" };
  }
}

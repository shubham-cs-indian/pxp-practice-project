package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkCreateRoles extends OServerCommandAuthenticatedDbAbstract {
  
  public BulkCreateRoles(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    try {
      String model = iRequest.content.toString();
      List<Map<String, Object>> roleList = new ArrayList<>();
      Map<String, Object> map = ObjectMapperUtil.readValue(model, Map.class);
      roleList = (List<Map<String, Object>>) map.get("roles");
      Map<String, List<String>> roleUsers = new HashMap<String, List<String>>();
      roleUsers = (Map<String, List<String>>) map.get("roleUsers");
      List<Map<String, Object>> createdRoleList = new ArrayList<>();
      List<Map<String, Object>> failedRoleList = new ArrayList<>();
      IExceptionModel failure = new ExceptionModel();
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      
      for (Map<String, Object> role : roleList) {
        try {
          Map<String, Object> roleMap = RoleUtils.createRole(role, database);
          Map<String, Object> returnRoleMap = new HashMap<>();
          Map<String, Object> roleValue = new HashMap<>();
          roleValue = (Map<String, Object>) roleMap.get("role");
          String roleId = (String) roleValue.get(IRoleModel.ID);
          returnRoleMap.put(ISummaryInformationModel.ID, roleId);
          returnRoleMap.put(ISummaryInformationModel.LABEL, roleValue.get(IRoleModel.LABEL));
          createdRoleList.add(returnRoleMap);
          RoleUtils.addUsersToRole(roleUsers.get(roleId), roleId);
        }
        catch (PluginException e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
              (String) role.get(IRoleModel.LABEL));
          addToFailureIds(failedRoleList, role);
        }
        catch (Exception e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
              (String) role.get(IRoleModel.LABEL));
          addToFailureIds(failedRoleList, role);
        }
      }
      
      Map<String, Object> result = new HashMap<>();
      result.put(IPluginSummaryModel.SUCCESS, createdRoleList);
      result.put(IPluginSummaryModel.FAILURE, failure);
      result.put(IPluginSummaryModel.FAILED_IDS, failedRoleList);
      ResponseCarrier.successResponse(iResponse, result);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    
    return false;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedRoleList, Map<String, Object> role)
  {
    Map<String, Object> failedRoleMap = new HashMap<>();
    failedRoleMap.put(ISummaryInformationModel.ID, role.get(IRoleModel.ID));
    failedRoleMap.put(ISummaryInformationModel.LABEL, role.get(IRoleModel.LABEL));
    failedRoleList.add(failedRoleMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCreateRoles/*" };
  }
}

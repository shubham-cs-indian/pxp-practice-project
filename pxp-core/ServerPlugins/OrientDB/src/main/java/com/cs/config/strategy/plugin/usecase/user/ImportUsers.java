package com.cs.config.strategy.plugin.usecase.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class ImportUsers extends AbstractOrientPlugin{
  
  private static final String MALE = "male";
  
  private static final List<String> fieldToExclude = Arrays.asList(CommonConstants.USER_NAME_PROPERTY, CommonConstants.ROLE,
      IUserModel.PASSWORD, IUserModel.USER_IID, IUserModel.USER_NAME, IUserModel.IS_BACKGROUND_USER);
  
  public ImportUsers(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ImportUsers/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> userList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> createdUserList = new ArrayList<>();
    List<Map<String, Object>> failedUserList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();

    UtilClass.setSectionElementIdMap(new HashMap<>());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_USER, CommonConstants.CODE_PROPERTY);

    for (Map<String, Object> userMap : userList) {
      try {
        Map<String, Object> usersMap = upsertUsers(userMap, vertexType);
        Map<String, Object> createdUsers = new HashMap<>();
        createdUsers.put(ISummaryInformationModel.LABEL, usersMap.get(IUserModel.USER_NAME));
        createdUserList.add(createdUsers);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, null,
            (String) userMap.get(IUserModel.USER_NAME));
        addToFailureIds(failedUserList, userMap);
      }
    }
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdUserList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedUserList);
    return result;
  }
  
  private Map<String, Object> upsertUsers(Map<String, Object> userMap, OrientVertexType vertexType) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String userCode = (String) userMap.get(CommonConstants.CODE_PROPERTY);
    String userName = (String)userMap.get("userName");
    String preferredUiLanguage = (String) userMap.remove(IUserModel.PREFERRED_UI_LANGUAGE);
    String preferredDataLanguage = (String) userMap.remove(IUserModel.PREFERRED_DATA_LANGUAGE);
    Vertex userNode = null;
    try {
      userNode = UtilClass.getVertexByCode(userCode, VertexLabelConstants.ENTITY_TYPE_USER);
      UtilClass.saveNode(userMap, userNode, fieldToExclude);
    }
    catch (NotFoundException e) { 
      userMap.put(CommonConstants.TYPE_PROPERTY, CommonConstants.USER_TYPE);
      userMap.put(IConfigResponseWithAuditLogModel.AUDIT_LOG_INFO, new ArrayList<>());
      userMap.put(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY), userMap.get(CommonConstants.CODE_PROPERTY));
      String countQuery = "SELECT  count(*) from " + VertexLabelConstants.ENTITY_TYPE_USER + " where "
          + IUser.USER_NAME + " = " + EntityUtil.quoteIt(userName);
      Long count;
      count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      if (count < 1) {
        userNode = UtilClass.createNode(userMap, vertexType, new ArrayList<String>());
      }
      if (userCode.equals(CommonConstants.ADMIN_USER_ID)) {
        userNode.setProperty(CommonConstants.CODE_PROPERTY, CommonConstants.ADMIN_USER_ID);
      }
    }  
    Map<String, Object> returnMap = new HashMap<>();
    UserUtils.addOrUpdatePreferredLanguages(preferredUiLanguage, preferredDataLanguage, userNode, returnMap);
    returnMap.putAll(UtilClass.getMapFromNode(userNode));
    graph.commit();
    return returnMap;
  }

  public void addToFailureIds(List<Map<String, Object>> failedUserList, Map<String, Object> user)
  {
    Map<String, Object> failedUserMap = new HashMap<>();
    failedUserMap.put(ISummaryInformationModel.ID, user.get(CommonConstants.ID_PROPERTY));
    failedUserMap.put(ISummaryInformationModel.LABEL, user.get(CommonConstants.LABEL_PROPERTY));
    failedUserList.add(failedUserMap);
  }
  
}

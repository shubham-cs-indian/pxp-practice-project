package com.cs.config.strategy.plugin.usecase.task;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskRoleEntity;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskContentTypeModel;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskContentTypeResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IConfigTaskReferencesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetKlassInfoOfLinkedContent extends AbstractOrientPlugin {
  
  public GetKlassInfoOfLinkedContent(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassInfoOfLinkedContent/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> contentType = new HashMap<>();
    Map<String, List<String>> mapRefInstances = (Map<String, List<String>>) requestMap
        .get(IConfigTaskReferencesModel.CONTENT_TYPES);
    List<String> userIds = mapRefInstances.remove(ITaskRoleEntity.USERS_IDS);
    List<String> roleIds = mapRefInstances.remove(ITaskRoleEntity.ROLES_IDS);
    List<String> klassIds = new ArrayList<>();
    
    for (List<String> types : mapRefInstances.values()) {
      klassIds.addAll(types);
    }
    
    Iterable<Vertex> klasses = UtilClass.getGraph()
        .command(new OCommandSQL("select code, "
            + EntityUtil.getLanguageConvertedField(IKlass.LABEL) + ", " + IKlass.PREVIEW_IMAGE
            + ", " + IKlass.CODE + " from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
            + " where isNature=true and code in " + EntityUtil.quoteIt(klassIds)))
        .execute();
    Map<String, Object> klassesInfo = new HashMap<>();
    for (Vertex klassVertex : klasses) {
      Map<String, Object> klassMap = new HashMap<>();
      klassMap.put(IKlass.LABEL,
          klassVertex.getProperty(EntityUtil.getLanguageConvertedField(IKlass.LABEL)));
      klassMap.put(IKlass.CODE,
          klassVertex.getProperty(EntityUtil.getLanguageConvertedField(IKlass.CODE)));
      klassMap.put(IKlass.PREVIEW_IMAGE,
          klassVertex.getProperty(EntityUtil.getLanguageConvertedField(IKlass.PREVIEW_IMAGE)));
      String klassId = (String) klassVertex.getProperty(CommonConstants.CODE_PROPERTY);
      klassesInfo.put(klassId, klassMap);
    }
    
    Iterable<Vertex> users = UtilClass.getGraph()
        .command(new OCommandSQL("select code, " + EntityUtil.getLanguageConvertedField(IUser.LABEL)
            + ", " + IUser.FIRST_NAME + ", " + IUser.CODE + ", " + IUser.TYPE + ", "
            + IUser.LAST_NAME + ", " + IUser.ICON + " from " + VertexLabelConstants.ENTITY_TYPE_USER
            + " where code in " + EntityUtil.quoteIt(userIds)))
        .execute();
    
    Map<String, Object> usersInfo = new HashMap<>();
    for (Vertex userVertex : users) {
      Map<String, Object> userMap = new HashMap<>();
      userMap.put(IUser.LABEL,
          userVertex.getProperty(EntityUtil.getLanguageConvertedField(IUser.FIRST_NAME)) + " "
              + userVertex.getProperty(EntityUtil.getLanguageConvertedField(IUser.LAST_NAME)));
      userMap.put(IUser.CODE,
          userVertex.getProperty(EntityUtil.getLanguageConvertedField(IUser.CODE)));
      userMap.put(IUser.ICON,
          userVertex.getProperty(EntityUtil.getLanguageConvertedField(IUser.ICON)));
      userMap.put(IUser.TYPE,
          userVertex.getProperty(EntityUtil.getLanguageConvertedField(IUser.TYPE)));
      String userId = (String) userVertex.getProperty(CommonConstants.CODE_PROPERTY);
      userMap.put(IUser.ID, userVertex.getProperty(CommonConstants.CODE_PROPERTY));
      usersInfo.put(userId, userMap);
    }
    
    Iterable<Vertex> roles = UtilClass.getGraph()
        .command(new OCommandSQL("select code, " + EntityUtil.getLanguageConvertedField(IRole.LABEL)
            + ", " + IRole.CODE + ", " + IRole.ICON + ", " + IRole.TYPE + " from "
            + VertexLabelConstants.ENTITY_TYPE_ROLE + " where code in "
            + EntityUtil.quoteIt(roleIds)))
        .execute();
    
    Map<String, Object> rolesInfo = new HashMap<>();
    for (Vertex roleVertex : roles) {
      
      Map<String, Object> roleMap = new HashMap<>();
      roleMap.put(IRole.LABEL,
          roleVertex.getProperty(EntityUtil.getLanguageConvertedField(IRole.LABEL)));
      roleMap.put(IRole.CODE,
          roleVertex.getProperty(EntityUtil.getLanguageConvertedField(IRole.CODE)));
      roleMap.put(IRole.ICON,
          roleVertex.getProperty(EntityUtil.getLanguageConvertedField(IRole.ICON)));
      roleMap.put(IRole.TYPE,
          roleVertex.getProperty(EntityUtil.getLanguageConvertedField(IRole.TYPE)));
      String roleId = (String) roleVertex.getProperty(CommonConstants.CODE_PROPERTY);
      roleMap.put(IRole.ID, roleVertex.getProperty(CommonConstants.CODE_PROPERTY));
      rolesInfo.put(roleId, roleMap);
    }
    
    List<String> natureKlassIds = new ArrayList<>();
    natureKlassIds.addAll(klassesInfo.keySet());
    
    for (String contentId : mapRefInstances.keySet()) {
      List<String> contentTypes = mapRefInstances.get(contentId);
      contentTypes.retainAll(natureKlassIds);
      contentType.put(contentId, contentTypes);
    }
    
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigTaskContentTypeModel.REFERENCED_KLASSES, klassesInfo);
    configDetails.put(IConfigTaskContentTypeModel.REFERENCED_USERS, usersInfo);
    configDetails.put(IConfigTaskContentTypeModel.REFERENCED_ROLES, rolesInfo);
    
    returnMap.put(IConfigTaskContentTypeResponseModel.CONTENT_TYPES, contentType);
    returnMap.put(IConfigTaskContentTypeResponseModel.CONFIG_DETAILS, configDetails);
    
    return returnMap;
  }
}

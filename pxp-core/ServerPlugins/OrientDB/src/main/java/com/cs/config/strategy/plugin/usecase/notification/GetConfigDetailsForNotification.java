package com.cs.config.strategy.plugin.usecase.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.notification.IGetConfigDetailsForNotificationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForNotification extends AbstractOrientPlugin {
  
  public GetConfigDetailsForNotification(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, String>> idAndTypeList = (List<Map<String, String>>) requestMap
        .get(IListModel.LIST);
    Set<String> taskIds = new HashSet<>();
    for (Map<String, String> idAndType : idAndTypeList) {
      taskIds.add(idAndType.get(IIdAndTypeModel.TYPE));
    }
    Map<String, Object> referencedTasks = getReferencedTasks(taskIds);
    
    List<Map<String, Object>> usersList = new ArrayList<>();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER
            + " order by lastname asc, firstname.toLowerCase() asc"))
        .execute();
    for (Vertex userNode : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromNode(userNode));
      usersList.add(map);
    }
    Map<String, String> roleIdLabelMap = fillReferencedRACIVSRoles();
    Map<String, Object> response = new HashMap<>();
    response.put(IGetConfigDetailsForNotificationModel.ROLE_ID_LABEL_MAP, roleIdLabelMap);
    response.put(IGetConfigDetailsForNotificationModel.USER_LIST, usersList);
    response.put(IGetConfigDetailsForNotificationModel.REFERENCED_TASKS, referencedTasks);
    response.put(IGetConfigDetailsForNotificationModel.REFERENCED_TAGS,
        getReferencedTags(referencedTasks));
    UtilClass.getGraph()
        .commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForNotification/*" };
  }
  
  private Map<String, String> fillReferencedRACIVSRoles() throws Exception
  {
    List<String> RACIVSRoles = new ArrayList<>();
    RACIVSRoles.add(SystemLevelIds.RESPONSIBLE);
    RACIVSRoles.add(SystemLevelIds.ACCOUNTABLE);
    RACIVSRoles.add(SystemLevelIds.CONSULTED);
    RACIVSRoles.add(SystemLevelIds.INFORMED);
    RACIVSRoles.add(SystemLevelIds.VERIFY);
    RACIVSRoles.add(SystemLevelIds.SIGN_OFF);
    Map<String, String> roleIdLabelMap = new HashMap<>();
    Iterable<Vertex> roleVertices = UtilClass.getVerticesByIds(RACIVSRoles,
        VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      roleIdLabelMap.put(roleVertex.getProperty(CommonConstants.CODE_PROPERTY),
          (String) UtilClass.getValueByLanguage(roleVertex, IRole.LABEL));
    }
    return roleIdLabelMap;
  }
  
  private Map<String, Object> getReferencedTasks(Set<String> taskIds) throws Exception
  {
    Map<String, Object> referencedTasks = new HashMap<>();
    Iterable<Vertex> taskVertices = UtilClass.getVerticesByIds(taskIds,
        VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex taskVertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(taskVertex);
      Map<String, Object> taskMap = TasksUtil.getTaskMapFromNode(taskVertex);
      referencedTasks.put(taskId, taskMap);
    }
    return referencedTasks;
  }
  
  private Map<String, Object> getReferencedTags(Map<String, Object> referencedTasks)
      throws Exception
  {
    Map<String, Object> referencedTags = new HashMap<>();
    Set<String> tagIds = new HashSet<>();
    for (Map.Entry<String, Object> entry : referencedTasks.entrySet()) {
      Map<String, Object> value = (Map<String, Object>) entry.getValue();
      String priorityTagId = (String) value.get(ITask.PRIORITY_TAG);
      String statusTagId = (String) value.get(ITask.STATUS_TAG);
      if (priorityTagId != null) {
        tagIds.add(priorityTagId);
      }
      if (statusTagId != null) {
        tagIds.add(statusTagId);
      }
    }
    for (String tagId : tagIds) {
      
      Vertex tagVertex = null;
      try {
        tagVertex = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
      Map<String, Object> tagMap = TagUtils.getTagMap(tagVertex, true);
      referencedTags.put(tagId, tagMap);
    }
    return referencedTags;
  }
}

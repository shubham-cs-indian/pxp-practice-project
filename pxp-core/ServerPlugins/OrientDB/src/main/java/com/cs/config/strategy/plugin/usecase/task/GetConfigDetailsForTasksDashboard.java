package com.cs.config.strategy.plugin.usecase.task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetConfigDetailsForTasksDashboardModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForTasksDashboard extends AbstractGetConfigDetails {
  
  public GetConfigDetailsForTasksDashboard(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForTasksDashboard/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return getConfigDetails(requestMap);
  }
  
  protected Map<String, Object> getConfigDetails(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = getMapToReturn();
    
    String userId = (String) requestMap.get(IIdParameterModel.CURRENT_USER_ID);
    Set<String> roleIdsForCurrentUser = new HashSet<>();
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    roleIdsForCurrentUser.add(UtilClass.getCodeNew(userInRole));
    mapToReturn.put(IGetConfigDetailsForTasksDashboardModel.ROLE_IDS_OF_CURRENT_USER,
        roleIdsForCurrentUser);
    
    fillReferencedTasks(mapToReturn, UtilClass.getCodeNew(userInRole));
    
    fillReferencedRACIVSRoles(mapToReturn);
    
    fillReferencedPermission(mapToReturn, CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE, userInRole);
    fillPersonalTaskIds(mapToReturn);
    
    return mapToReturn;
  }
  
  private Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsForTasksDashboardModel.REFERENCED_ROLES, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsForTasksDashboardModel.REFERENCED_TAGS, new HashMap<>());
    Map<String, Object> referencedPermissions = new HashMap<>();
    Map<String, Object> taskIdsForRolesHavingReadPermission = new HashMap<>();
    Set<String> taskIdsHavingReadPermissions = new HashSet<>();
    referencedPermissions.put(
        IReferencedTemplatePermissionModel.TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION,
        taskIdsForRolesHavingReadPermission);
    referencedPermissions.put(IReferencedTemplatePermissionModel.TASK_IDS_HAVING_READ_PERMISSION,
        taskIdsHavingReadPermissions);
    mapToReturn.put(IGetConfigDetailsForTasksDashboardModel.REFERENCED_PERMISSIONS,
        referencedPermissions);
    mapToReturn.put(IGetConfigDetailsForTasksDashboardModel.PERSONAL_TASK_IDS, new HashSet<>());
    return mapToReturn;
  }
  
  private void fillReferencedTasks(Map<String, Object> mapToReturn, String roleId) throws Exception
  {
    Map<String, Object> referencedTasks = new HashMap<>();
    
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForTasksDashboardModel.REFERENCED_TAGS);
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> taskVertices = graph.getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex vertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(vertex);
      Map<String, Object> task = TasksUtil.getTaskMapFromNode(vertex);
      
      String priorityTagId = (String) task.get(ITask.PRIORITY_TAG);
      if (priorityTagId != null && !referencedTags.containsKey(priorityTagId)) {
        Vertex tagNode = UtilClass.getVertexById(priorityTagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(tagNode, true);
        referencedTags.put(priorityTagId, referencedTag);
      }
      
      String statusTagId = (String) task.get(ITask.STATUS_TAG);
      if (statusTagId != null && !referencedTags.containsKey(statusTagId)) {
        Vertex tagNode = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(tagNode, true);
        referencedTags.put(statusTagId, referencedTag);
      }
      if(TasksUtil.isAnyPermissionsAvailableForTaskInstanceToUser(roleId,taskId)) {
        referencedTasks.put(taskId, task);
      }
    
    }
    
    mapToReturn.put(IGetConfigDetailsForTasksDashboardModel.REFERENCED_TASKS, referencedTasks);
  }
  
  private void fillReferencedTasks(Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedTasks = new HashMap<>();
    
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForTasksDashboardModel.REFERENCED_TAGS);
    
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> taskVertices = graph.getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex vertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(vertex);
      Map<String, Object> task = TasksUtil.getTaskMapFromNode(vertex);
      
      String priorityTagId = (String) task.get(ITask.PRIORITY_TAG);
      if (priorityTagId != null && !referencedTags.containsKey(priorityTagId)) {
        Vertex tagNode = UtilClass.getVertexById(priorityTagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(tagNode, true);
        referencedTags.put(priorityTagId, referencedTag);
      }
      
      String statusTagId = (String) task.get(ITask.STATUS_TAG);
      if (statusTagId != null && !referencedTags.containsKey(statusTagId)) {
        Vertex tagNode = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(tagNode, true);
        referencedTags.put(statusTagId, referencedTag);
      }
      
      referencedTasks.put(taskId, task);
    }
    
    mapToReturn.put(IGetConfigDetailsForTasksDashboardModel.REFERENCED_TASKS, referencedTasks);
  }
  
  protected void fillReferencedPermission(Map<String, Object> responseMap, String tabType,
      Vertex userInRole) throws Exception
  {
    
    Set<Vertex> roles = new HashSet<Vertex>();
    // Get all roles & pass to GlobalPermissionUtil to get task class Ids that
    OrientGraph graphDB = UtilClass.getGraph();
    Iterable<Vertex> roleVertices = graphDB
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      roles.add(roleVertex);
    }
    fillRoleIdsAndTaskIdsHavingReadPermission(responseMap, userInRole, roles);
  }
  
  protected void fillPersonalTaskIds(Map<String, Object> maptoReturn)
  {
    Set<String> personalTaskIds = (Set<String>) maptoReturn
        .get(IGetConfigDetailsModel.PERSONAL_TASK_IDS);
    OrientGraph graphDB = UtilClass.getGraph();
    Iterable<Vertex> taskVertices = graphDB
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex taskVertex : taskVertices) {
      if ((taskVertex.getProperty(ITask.TYPE)).equals(CommonConstants.TASK_TYPE_PERSONAL)) {
        personalTaskIds.add(UtilClass.getCodeNew(taskVertex));
      }
    }
  }
  
}

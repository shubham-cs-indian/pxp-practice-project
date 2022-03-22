package com.cs.config.strategy.plugin.usecase.task.util;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TasksUtil {
  
  public static HashMap<String, Object> getTaskMapFromNode(Vertex taskNode)
  {
    return getTaskMapFromNode(taskNode, new HashSet<>());
  }
  
  public static HashMap<String, Object> getTaskMapFromNode(Vertex taskNode,
      Set<Vertex> statusTagNodes)
  {
    HashMap<String, Object> returnMap = new HashMap<>();
    returnMap = UtilClass.getMapFromNode(taskNode);
    Iterator<Vertex> statusTagIterator = taskNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_STATUS_TAG)
        .iterator();
    if (statusTagIterator.hasNext()) {
      Vertex statusTag = statusTagIterator.next();
      returnMap.put(ITask.STATUS_TAG, statusTag.getProperty(CommonConstants.CODE_PROPERTY));
      statusTagNodes.add(statusTag);
    }
    Iterator<Vertex> priorityTagIterator = taskNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PRIORITY_TAG)
        .iterator();
    if (priorityTagIterator.hasNext()) {
      Vertex priorityTag = priorityTagIterator.next();
      returnMap.put(ITask.PRIORITY_TAG, priorityTag.getProperty(CommonConstants.CODE_PROPERTY));
      statusTagNodes.add(priorityTag);
    }
    
    return returnMap;
  }
  
  public static void fillReferencedConfigDetails(List<Map<String, Object>> list,
      Map<String, Object> referencedTags) throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        ITagModel.TAG_TYPE, ITagModel.LABEL, ITagModel.ICON, ITag.CODE);
    for (Map<String, Object> task : list) {
      String statusTagId = (String) task.get(ITaskModel.STATUS_TAG);
      if (statusTagId != null) {
        Vertex statusTagNode = UtilClass.getVertexById(statusTagId,
            VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referenceTag = UtilClass.getMapFromVertex(fieldsToFetch, statusTagNode);
        referenceTag.put(ITagModel.TYPE, referenceTag.remove(ITagModel.TAG_TYPE));
        referencedTags.put(statusTagId, referenceTag);
      }
      
      String priorityTagId = (String) task.get(ITaskModel.PRIORITY_TAG);
      
      if (priorityTagId != null) {
        Vertex priorityTagNode = UtilClass.getVertexById(priorityTagId,
            VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referenceTag = UtilClass.getMapFromVertex(fieldsToFetch,
            priorityTagNode);
        referenceTag.put(ITagModel.TYPE, referenceTag.remove(ITagModel.TAG_TYPE));
        referencedTags.put(priorityTagId, referenceTag);
      }
    }
  }
  
  public static  boolean isAnyPermissionsAvailableForTaskInstanceToUser( String roleId,String taskId) throws Exception
  {
   boolean permissionstatus =false;
    Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils
        .getDisabledGlobalPermission();
      try {
        Vertex taskNode = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
        if (taskNode.getProperty(ITask.TYPE).equals(CommonConstants.TASK_TYPE_PERSONAL)) {
          globalPermissionToReturn = getGlobalPermissionForPersonalTask(taskId, "task", roleId);
        }
        else {
          GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn, roleId);
        }
      }
      catch (TaskNotFoundException e) {
        throw new TaskNotFoundException();
      }
        Boolean canCreate = (Boolean) globalPermissionToReturn.get(GlobalPermission.CAN_CREATE);
        Boolean canRead = (Boolean) globalPermissionToReturn.get(GlobalPermission.CAN_READ);
        Boolean canEdit = (Boolean) globalPermissionToReturn.get(GlobalPermission.CAN_EDIT);
        Boolean canDelete = (Boolean) globalPermissionToReturn.get(GlobalPermission.CAN_DELETE);
        if(canCreate || canRead || canEdit ||canDelete) {
          permissionstatus = true;
        }
    
    return permissionstatus;
  }
  
  public static Map<String, Object> getGlobalPermissionForPersonalTask(String taskId, String entityType, String roleId) throws Exception
  {
    Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils.getDefaultPersonalTaskGlobalPermission();
    
    GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn, roleId);
    
    return globalPermissionToReturn;
  }
}

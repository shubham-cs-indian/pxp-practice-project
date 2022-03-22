package com.cs.config.strategy.plugin.usecase.governancetask;

import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.task.BulkSaveTaskFailedException;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.task.IBulkSaveTasksResponseModel;
import com.cs.core.config.interactor.model.task.IGetGridTasksResponseModel;
import com.cs.core.config.interactor.model.task.ISaveTaskResponseModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveGovernanceTask extends AbstractOrientPlugin {
  
  public SaveGovernanceTask(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfTasks = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSavedTasks = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    for (Map<String, Object> taskMap : listOfTasks) {
      try {
        UtilClass.setSectionElementIdMap(new HashMap<>());
        UtilClass.setSectionIdMap(new HashMap<>());
        UtilClass.setSectionPermissionIdMap(new HashMap<>());
        
        Map<String, Object> savedTaskMap = new HashMap<String, Object>();
        String taskId = (String) taskMap.get(ITaskModel.ID);
        Vertex vertex = null;
        try {
          vertex = UtilClass.getVertexById(taskId, VertexLabelConstants.GOVERNANCE_RULE_TASK);
        }
        catch (NotFoundException e) {
          throw new TaskNotFoundException();
        }
        Boolean isTypeSwitched = false;
        Map<String, Object> referencedRoles = new HashMap<>();
        String taskType = (String) taskMap.get(ITask.TYPE);
        /* if (taskType != null && !taskType.equals(vertex.getProperty(ITask.TYPE))) {
          isTypeSwitched = true;
          referencedRoles = getReferencedRACIVSRoles();
        }
        */
        manageStatusAndPriorityTags(taskMap, vertex);
        
        taskMap.remove(ITask.STATUS_TAG);
        taskMap.remove(ITask.PRIORITY_TAG);
        UtilClass.saveNode(taskMap, vertex, new ArrayList<>());
        UtilClass.getGraph()
            .commit();
        savedTaskMap = TasksUtil.getTaskMapFromNode(vertex);
        savedTaskMap.put(ISaveTaskResponseModel.REFERENCED_ROLES, referencedRoles);
        savedTaskMap.put(ISaveTaskResponseModel.IS_TYPE_SWITCHED, isTypeSwitched);
        listOfSuccessSavedTasks.add(savedTaskMap);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    if (listOfSuccessSavedTasks.isEmpty()) {
      throw new BulkSaveTaskFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    UtilClass.getGraph()
        .commit();
    Map<String, Object> successMap = new HashMap<>();
    Map<String, Object> referencedConfigDetails = new HashMap<>();
    successMap.put(IGetGridTasksResponseModel.TASKS_LIST, listOfSuccessSavedTasks);
    TasksUtil.fillReferencedConfigDetails(listOfSuccessSavedTasks, referencedConfigDetails);
    successMap.put(IGetGridTasksResponseModel.REFERENCED_TAGS, referencedConfigDetails);
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> bulkSaveTasksResponse = new HashMap<String, Object>();
    bulkSaveTasksResponse.put(IBulkSaveTasksResponseModel.SUCCESS, successMap);
    bulkSaveTasksResponse.put(IBulkSaveTasksResponseModel.FAILURE, failure);
    return bulkSaveTasksResponse;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveGovernanceTask/*" };
  }
  
  /* private Map<String,Object> getReferencedRACIVSRoles() throws Exception
  {
    List<String> RACIVSRoles = new ArrayList<>();
    RACIVSRoles.add(SystemLevelIds.RESPONSIBLE_ROLE);
    RACIVSRoles.add(SystemLevelIds.ACCOUNTABLE_ROLE);
    RACIVSRoles.add(SystemLevelIds.CONSULTED_ROLE);
    RACIVSRoles.add(SystemLevelIds.INFORMED_ROLE);
    RACIVSRoles.add(SystemLevelIds.VERIFY_ROLE);
    RACIVSRoles.add(SystemLevelIds.SIGN_OFF_ROLE);
  
    Map<String,Object> referencedRoles =  new HashMap<>();
    Iterable<Vertex> roleVertices = UtilClass.getVerticesByIds(RACIVSRoles, VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      referencedRoles.put(roleVertex.getProperty(CommonConstants.CODE_PROPERTY), RoleUtils.getRoleEntityMap(roleVertex));
    }
    return referencedRoles;
  }*/
  
  private void manageStatusAndPriorityTags(Map<String, Object> taskMap, Vertex vertex)
      throws Exception, TagNotFoundException
  {
    String priorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
    String statusTagId = (String) taskMap.get(ITask.STATUS_TAG);
    Vertex priorityTag = null;
    Vertex statusTag = null;
    try {
      Iterator<Edge> edgeIterator = vertex
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_PRIORITY_TAG)
          .iterator();
      if (edgeIterator.hasNext()) {
        Edge edge = edgeIterator.next();
        edge.remove();
      }
      if (priorityTagId != null) {
        priorityTag = UtilClass.getVertexById(priorityTagId, VertexLabelConstants.ENTITY_TAG);
        vertex.addEdge(RelationshipLabelConstants.HAS_PRIORITY_TAG, priorityTag);
      }
      Iterator<Edge> iterator = vertex
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_STATUS_TAG)
          .iterator();
      if (iterator.hasNext()) {
        Edge edge = iterator.next();
        edge.remove();
      }
      if (statusTagId != null) {
        statusTag = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
        vertex.addEdge(RelationshipLabelConstants.HAS_STATUS_TAG, statusTag);
      }
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
  }
}

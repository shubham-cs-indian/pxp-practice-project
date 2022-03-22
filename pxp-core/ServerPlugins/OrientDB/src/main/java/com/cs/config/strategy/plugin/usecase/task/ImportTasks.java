package com.cs.config.strategy.plugin.usecase.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class ImportTasks extends AbstractOrientPlugin{
  
  private static final List<String> filedToExculde = Arrays.asList(ITask.STATUS_TAG,ITask.PRIORITY_TAG, ITask.TYPE);
  
  public ImportTasks(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ImportTasks/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> taskList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> createdtaskList = new ArrayList<>();
    List<Map<String, Object>> failedtaskList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();

    UtilClass.setSectionElementIdMap(new HashMap<>());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_TASK,
        CommonConstants.CODE_PROPERTY);

    for (Map<String, Object> task : taskList) {
      try {
        Map<String, Object> taskMap = upsertTaskData(task, vertexType, failure);
        ImportUtils.addSuccessImportedInfo(createdtaskList, taskMap);
      }
      catch (Exception e) {
        ImportUtils.logExceptionAndFailureIDs(failure, failedtaskList, task, e);
      }
    }
    
    Map<String, Object> result = ImportUtils.prepareImportResponseMap(failure, createdtaskList, failedtaskList);
    return result;
  }
  
  private Map<String, Object> upsertTaskData(Map<String, Object> taskMap, OrientVertexType vertexType, IExceptionModel failure) throws Exception
  {
    Vertex taskNode;
    String taskCode = (String) taskMap.get(CommonConstants.CODE_PROPERTY);
    try {
      taskNode = UtilClass.getVertexByCode(taskCode, VertexLabelConstants.ENTITY_TYPE_TASK);
      UtilClass.saveNode(taskMap, taskNode, filedToExculde);
      manageStatusAndPriorityTags(taskMap, taskNode, failure);
    }catch (NotFoundException e) {
      String priorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
      String statusTagId = (String) taskMap.get(ITask.STATUS_TAG);
      taskMap.remove(ITask.STATUS_TAG);
      taskMap.remove(ITask.PRIORITY_TAG);
      String taskType = (String) taskMap.get(CommonConstants.TYPE_PROPERTY);
      String configType = taskType.equalsIgnoreCase(CommonConstants.TASK_TYPE_SHARED) ? CommonConstants.TASK_TYPE_SHARED : CommonConstants.TASK_TYPE_PERSONAL;
      taskMap.put(CommonConstants.TYPE_PROPERTY, configType);
     
      taskNode = UtilClass.createNode(taskMap, vertexType, new ArrayList<>());
      
      Vertex statusTag = null;
      //Status tag validation and creation
      if (configType != null && taskType.equalsIgnoreCase(CommonConstants.TASK_TYPE_PERSONAL)
          && StringUtils.isNotEmpty(statusTagId)) {
        try {
          statusTag = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
          taskNode.addEdge(RelationshipLabelConstants.HAS_STATUS_TAG, statusTag);
        }
        catch (NotFoundException ex) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, taskCode, statusTagId);
        }
      }
      else {
        statusTag = UtilClass.getVertexById(SystemLevelIds.TASK_STATUS_TAG,
            VertexLabelConstants.ENTITY_TAG);
        taskNode.addEdge(RelationshipLabelConstants.HAS_STATUS_TAG, statusTag);
      }
      
      //PriorityTag validation and creation
      try {
        if (StringUtils.isNotEmpty(priorityTagId)) {
          Vertex priorityTag = UtilClass.getVertexById(priorityTagId, VertexLabelConstants.ENTITY_TAG);
          taskNode.addEdge(RelationshipLabelConstants.HAS_PRIORITY_TAG, priorityTag);
        }
      }
      catch (NotFoundException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, taskCode, statusTagId);
      }
    }
   
    UtilClass.getGraph().commit();
    
    return taskMap;
  }

  public void addToFailureIds(List<Map<String, Object>> failedtaskList, Map<String, Object> task)
  {
    Map<String, Object> failedtaskMap = new HashMap<>();
    failedtaskMap.put(ISummaryInformationModel.ID, task.get(ITag.ID));
    failedtaskMap.put(ISummaryInformationModel.LABEL, task.get(ITag.LABEL));
    failedtaskList.add(failedtaskMap);
  }
  
  private void manageStatusAndPriorityTags(Map<String, Object> taskMap, Vertex taskVertex, IExceptionModel failure) throws Exception
  {
    String newPriorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
    String newStatusTagId = (String) taskMap.get(ITask.STATUS_TAG);
    updateTaskTag(taskVertex, newPriorityTagId, RelationshipLabelConstants.HAS_PRIORITY_TAG, failure);
    
    //status tag update only for personal type task
    String taskType = taskVertex.getProperty(CommonConstants.TYPE_PROPERTY);
    if (taskType.equals(CommonConstants.TASK_TYPE_PERSONAL)) {
      updateTaskTag(taskVertex, newStatusTagId, RelationshipLabelConstants.HAS_STATUS_TAG, failure);
    }
  }

  private void updateTaskTag(Vertex taskVertex, String newTagCode, String relationLabel, IExceptionModel failure) throws Exception
  {
    try {
      Iterator<Edge> edgeIterator = taskVertex.getEdges(Direction.OUT, relationLabel).iterator();
      if (StringUtils.isNotEmpty(newTagCode)) {
        if (edgeIterator.hasNext()) {
          Edge edge = edgeIterator.next();
          Vertex tagVertex = edge.getVertex(Direction.IN);
          String oldCode = tagVertex.getProperty(CommonConstants.CODE_PROPERTY);
          if (!oldCode.equals(newTagCode)) {
            Vertex tag = UtilClass.getVertexById(newTagCode, VertexLabelConstants.ENTITY_TAG);
            // if valid tag code is passed then only remove the previous tag
            edge.remove();
            //add new tag
            taskVertex.addEdge(relationLabel, tag);
          }
        }else {
          // Directly add if there is not tag attached previously
          Vertex tag = UtilClass.getVertexById(newTagCode, VertexLabelConstants.ENTITY_TAG);
          taskVertex.addEdge(relationLabel, tag);
        }
      }
      else {
        //Removed tag if new Code is empty or null
        if (edgeIterator.hasNext()) {
          Edge edge = edgeIterator.next();
          edge.remove();
        }
      }
    }
    catch (NotFoundException e) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, e, taskVertex.getProperty(CommonConstants.CODE_PROPERTY), newTagCode);
    }
  }
  
}

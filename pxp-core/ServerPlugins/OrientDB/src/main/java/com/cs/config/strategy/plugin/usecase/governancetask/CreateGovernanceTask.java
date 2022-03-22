package com.cs.config.strategy.plugin.usecase.governancetask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateGovernanceTask extends AbstractOrientPlugin {
  
  public CreateGovernanceTask(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> taskMap = requestMap;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    String priorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
    String statusTagId = (String) taskMap.get(ITask.STATUS_TAG);
    taskMap.remove(ITask.STATUS_TAG);
    taskMap.remove(ITask.PRIORITY_TAG);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.GOVERNANCE_RULE_TASK, CommonConstants.CODE_PROPERTY);
    Vertex taskNode = UtilClass.createNode(taskMap, vertexType, new ArrayList<>());
    
    Vertex priorityTag = null;
    Vertex statusTag = null;
    String taskType = (String) taskMap.get(ITask.TYPE);
    if (taskType != null && taskType.equals(CommonConstants.TASK_TYPE_PERSONAL)
        && statusTagId != null) {
      try {
        statusTag = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
        taskNode.addEdge(RelationshipLabelConstants.HAS_STATUS_TAG, statusTag);
      }
      catch (NotFoundException ex) {
        throw new TagNotFoundException();
      }
    }
    else {
      statusTag = UtilClass.getVertexById(SystemLevelIds.TASK_STATUS_TAG,
          VertexLabelConstants.ENTITY_TAG);
      taskNode.addEdge(RelationshipLabelConstants.HAS_STATUS_TAG, statusTag);
    }
    try {
      if (priorityTagId != null) {
        priorityTag = UtilClass.getVertexById(priorityTagId, VertexLabelConstants.ENTITY_TAG);
        taskNode.addEdge(RelationshipLabelConstants.HAS_PRIORITY_TAG, priorityTag);
      }
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    UtilClass.getGraph()
        .commit();
    returnMap = TasksUtil.getTaskMapFromNode(taskNode);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateGovernanceTask/*" };
  }
}

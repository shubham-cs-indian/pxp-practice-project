package com.cs.config.strategy.plugin.usecase.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.validationontype.InvalidNatureTypeKlassException;
import com.cs.core.config.interactor.model.task.IGetTaskModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateTask extends AbstractOrientPlugin {
  
  public CreateTask(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> taskMap = requestMap;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    try {
      UtilClass.validateOnType(Constants.TASK_TYPE_LIST, (String) taskMap.get(ITask.TYPE), true);
    }
    catch (InvalidTypeException e) {
      throw new InvalidNatureTypeKlassException(e);
    }
    
    String priorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
    String statusTagId = (String) taskMap.get(ITask.STATUS_TAG);
    taskMap.remove(ITask.STATUS_TAG);
    taskMap.remove(ITask.PRIORITY_TAG);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_TASK, CommonConstants.CODE_PROPERTY);
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
    
    Set<Vertex> statusTagNodes = new HashSet<>();
    Map<String, Object> responseTaskMap = TasksUtil.getTaskMapFromNode(taskNode, statusTagNodes);
    
    Map<String, Object> referencedTags = new HashMap<>();
    for (Vertex tagNode : statusTagNodes) {
      Map<String, Object> tagMap = new HashMap<String, Object>();
      String tagId = UtilClass.getCodeNew(tagNode);
      tagMap.put(IIdLabelTypeModel.ID, tagId);
      tagMap.put(IIdLabelTypeModel.LABEL, UtilClass.getValueByLanguage(tagNode, ITag.LABEL));
      tagMap.put(IIdLabelTypeModel.TYPE, tagNode.getProperty(ITag.TAG_TYPE));
      referencedTags.put(tagId, tagMap);
    }
    returnMap.put(IGetTaskModel.TASK, responseTaskMap);
    returnMap.put(IGetTaskModel.REFERENCED_TAGS, referencedTags);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateTask/*" };
  }
}

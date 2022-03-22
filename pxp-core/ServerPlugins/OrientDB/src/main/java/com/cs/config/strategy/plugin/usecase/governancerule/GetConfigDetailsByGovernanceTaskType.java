package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.task.TaskNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.taskinstance.ICreateTaskInstanceConfigDetailsModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetConfigDetailsByGovernanceTaskType extends AbstractOrientPlugin {
  
  public GetConfigDetailsByGovernanceTaskType(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String taskId = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Vertex vertex = null;
    try {
      vertex = UtilClass.getVertexById(taskId, VertexLabelConstants.GOVERNANCE_RULE_TASK);
    }
    catch (NotFoundException e) {
      throw new TaskNotFoundException();
    }
    Map<String, Object> taskMap = TasksUtil.getTaskMapFromNode(vertex);
    String type = (String) taskMap.get(ITask.TYPE);
    /*  if(type.equals(CommonConstants.TASK_TYPE_SHARED)){
      returnMap.put(ICreateTaskInstanceConfigDetailsModel.REFERENCED_ROLES, getReferencedRACIVSRoles());
    }
    else{
      returnMap.put(ICreateTaskInstanceConfigDetailsModel.REFERENCED_ROLES, new HashMap<>());
    }*/
    returnMap.put(ICreateTaskInstanceConfigDetailsModel.REFERENCED_TASK, taskMap);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsByGovernanceTaskType/*" };
  }
  
  /*private Map<String,Object> getReferencedRACIVSRoles() throws Exception
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
}

package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceBasedOnTaskGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForInstanceBasedOnTask extends AbstractOrientPlugin {
  
  public GetConfigDetailsForInstanceBasedOnTask(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForInstanceBasedOnTask/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> mapToReturn = new HashMap<>();
    
    getPermissionForTaskAndInstanceDetails(userId, mapToReturn);
    return mapToReturn;
  }
  
  private void getPermissionForTaskAndInstanceDetails(String userId,
      Map<String, Object> mapToReturn) throws Exception
  {
    OrientGraph graphDB = UtilClass.getGraph();
    Set<Vertex> roles = new HashSet<Vertex>();
    Iterable<Vertex> roleVertices = graphDB
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      roles.add(roleVertex);
    }
    
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    fillTaskPermissionRelatedDetails(mapToReturn, roleNode, roles);
    mapToReturn.put(IConfigDetailsForInstanceBasedOnTaskGetModel.ROLE_ID_OF_CURRENT_USER,
        UtilClass.getCodeNew(roleNode));
    managePermissionDetails(userId, mapToReturn);
  }
  
  private void managePermissionDetails(String userId, Map<String, Object> mapToReturn)
      throws Exception
  {
    Vertex userNode = null;
    try {
      userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    Set<String> allowedEntities = new HashSet<>();
    Set<String> klassIdsHavingRP = new HashSet<>();
    Set<String> taxonomyIdsHavingRP = new HashSet<>();
    Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Vertex roleNode : roleNodes) {
      
      List<String> roleEntities = (List<String>) (roleNode.getProperty(IRole.ENTITIES));
      allowedEntities.addAll(roleEntities);
      
      if (allowedEntities.isEmpty()) {
        allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
        allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
      }
      
      fillIdsHavingReadPermission(taxonomyIdsHavingRP, klassIdsHavingRP, roleNode);
    }
    
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.ALLOWED_ENTITIES, allowedEntities);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.KLASS_IDS_HAVING_RP, klassIdsHavingRP);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TAXONOMY_IDS_HAVING_RP,
        taxonomyIdsHavingRP);
  }
  
  private void fillIdsHavingReadPermission(Set<String> taxonomyIdsHavingRP,
      Set<String> klassIdsHavingRP, Vertex roleNode) throws Exception
  {
    klassIdsHavingRP.addAll(GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode));
    taxonomyIdsHavingRP.addAll(GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(roleNode));
  }
  
  protected void fillTaskPermissionRelatedDetails(Map<String, Object> mapToReturn,
      Vertex roleContainingLoginUser, Set<Vertex> roles) throws Exception
  {
    Map<String, Object> taskIdsForRoleHavingReadPermission = new HashMap<>();
    Set<String> taskIdsHavingReadPermission = new HashSet<>();
    Set<String> personalTaskIds = new HashSet<>();
    
    Iterable<Vertex> taskVertices = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex vertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(vertex);
      if ((vertex.getProperty(ITask.TYPE)).equals(CommonConstants.TASK_TYPE_PERSONAL)) {
        personalTaskIds.add(taskId);
      }
      for (Vertex role : roles) {
        String roleId = UtilClass.getCodeNew(role);
        Map<String, Object> globalPermissions = GlobalPermissionUtils
            .getKlassAndTaxonomyPermission(taskId, roleId);
        List<String> taskIdForRoleHavingReadPermission = (List<String>) taskIdsForRoleHavingReadPermission
            .get(roleId);
        if (taskIdForRoleHavingReadPermission == null) {
          taskIdForRoleHavingReadPermission = new ArrayList<>();
          taskIdsForRoleHavingReadPermission.put(roleId, taskIdForRoleHavingReadPermission);
        }
        if ((Boolean) globalPermissions.get(IGlobalPermission.CAN_READ)) {
          taskIdForRoleHavingReadPermission.add(taskId);
          if (roleContainingLoginUser.equals(role)) {
            taskIdsHavingReadPermission.add(taskId);
          }
        }
      }
    }
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION,
        taskIdsForRoleHavingReadPermission);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TASK_IDS_HAVING_READ_PERMISSION,
        taskIdsHavingReadPermission);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.PERSONAL_TASK_IDS, personalTaskIds);
  }
}

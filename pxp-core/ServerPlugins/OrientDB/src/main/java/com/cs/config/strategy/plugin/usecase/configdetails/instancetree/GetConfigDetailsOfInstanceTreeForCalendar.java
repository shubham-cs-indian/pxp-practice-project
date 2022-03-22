package com.cs.config.strategy.plugin.usecase.configdetails.instancetree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct.AbstractGetConfigDetailsForGetNewInstanceTree;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsOfInstanceTreeForCalendarResponseModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;


public class GetConfigDetailsOfInstanceTreeForCalendar
    extends AbstractGetConfigDetailsForGetNewInstanceTree {
  
  public GetConfigDetailsOfInstanceTreeForCalendar(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[]{"POST|GetConfigDetailsOfInstanceTreeForCalendar/*"};
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    String userId = (String) requestMap.get(IConfigDetailsForInstanceTreeGetRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    execute(requestMap, mapToReturn);
    manageTasksPermission(mapToReturn, roleNode);
    return mapToReturn;
  }

  protected void manageTasksPermission(Map<String, Object> mapToReturn,
      Vertex roleContainingLoginUser) throws Exception
  {
    String roleId = UtilClass.getCId(roleContainingLoginUser);
    Set<String> taskIdsHavingReadPermission = new HashSet<>();
    Set<String> personalTaskIds = new HashSet<>();
    
    Iterable<Vertex> taskVertices = UtilClass.getGraph().getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex vertex : taskVertices) {
      String taskId = UtilClass.getCId(vertex);
      if ((vertex.getProperty(ITask.TYPE)).equals(CommonConstants.TASK_TYPE_PERSONAL)) {
        personalTaskIds.add(taskId);
      }
      Map<String, Object> globalPermissions = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(taskId, roleId);
      if ((Boolean) globalPermissions.get(IGlobalPermission.CAN_READ)) {
        taskIdsHavingReadPermission.add(taskId);
      }
    }
    mapToReturn.put(IConfigDetailsOfInstanceTreeForCalendarResponseModel.TASK_IDS_HAVING_RP, taskIdsHavingReadPermission);
    mapToReturn.put(IConfigDetailsOfInstanceTreeForCalendarResponseModel.PERSONAL_TASK_IDS, personalTaskIds);
    mapToReturn.put(IConfigDetailsOfInstanceTreeForCalendarResponseModel.ROLE_ID_OF_CURRENT_USER, roleId);
  }
}

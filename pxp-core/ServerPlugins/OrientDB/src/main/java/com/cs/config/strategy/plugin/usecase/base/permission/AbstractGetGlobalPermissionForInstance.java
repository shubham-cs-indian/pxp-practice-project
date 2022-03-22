package com.cs.config.strategy.plugin.usecase.base.permission;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractGetGlobalPermissionForInstance extends AbstractOrientPlugin {
  
  public AbstractGetGlobalPermissionForInstance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected Map<String, Object> getGlobalPermissionForInstance(
      List<String> roleIdsContainingLoginUser, Set<String> allowedEntities, String taskId,
      String loginUserId) throws Exception
  {
    Map<String, Object> globalPermissionToReturn = GlobalPermissionUtils
        .getDefaultGlobalPermission();
    Vertex roleConfiguredForUser = RoleUtils.getRoleFromUser(loginUserId);
    String roleIdConfiguredForUser = UtilClass.getCodeNew(roleConfiguredForUser);
    
    GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn,
        roleIdConfiguredForUser);
    
    List<String> roleEntities = (List<String>) (roleConfiguredForUser.getProperty(IRole.ENTITIES));
    allowedEntities.addAll(roleEntities);
    
    for (String roleIdContainingLoginUser : roleIdsContainingLoginUser) {
      if (roleIdConfiguredForUser.equals(roleIdContainingLoginUser)) {
        continue;
      }
      Vertex role = UtilClass.getVertexById(roleIdContainingLoginUser,
          VertexLabelConstants.ENTITY_TYPE_ROLE);
      GlobalPermissionUtils.fillGlobalPermissionOfEntitiesForRole(taskId, globalPermissionToReturn,
          roleIdContainingLoginUser);
      
      roleEntities = (List<String>) (role.getProperty(IRole.ENTITIES));
      allowedEntities.addAll(roleEntities);
    }
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
    }
    return globalPermissionToReturn;
  }
}

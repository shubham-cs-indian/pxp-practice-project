package com.cs.config.strategy.plugin.usecase.permission;

import com.cs.config.strategy.plugin.usecase.base.permission.AbstractGetGlobalPermissionForInstance;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesRequestModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForMultipleNatureTypesResponseModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetGlobalPermissionForMultipleNatureTypes
    extends AbstractGetGlobalPermissionForInstance {
  
  public GetGlobalPermissionForMultipleNatureTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGlobalPermissionForMultipleNatureTypes/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String loginUserId = (String) requestMap
        .get(IGetGlobalPermissionForMultipleNatureTypesRequestModel.USER_ID);
    Set<String> klassIds = new HashSet<>((List<String>) requestMap
        .get(IGetGlobalPermissionForMultipleNatureTypesRequestModel.KLASS_IDS));
    Map<String, Object> response = new HashMap<>();
    
    List<String> natureKlassIds = new ArrayList<>();
    for (String klassId : klassIds) {
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
      if (isNature != null && isNature) {
        natureKlassIds.add(klassId);
      }
    }
    
    Vertex rolesConfiguredForUser = RoleUtils.getRoleFromUser(loginUserId);
    for (String natureKlassId : natureKlassIds) {
      Map<String, Object> globalPermissionMap = GlobalPermissionUtils.getDefaultGlobalPermission();
      String roleId = UtilClass.getCodeNew(rolesConfiguredForUser);
      Map<String, Object> permissionMap = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(natureKlassId, roleId);
      GlobalPermissionUtils.mergeGlobalPermissons(globalPermissionMap, permissionMap);
      response.put(natureKlassId, globalPermissionMap);
    }
    
    Iterable<Edge> clonePermissionForRoleNodes = rolesConfiguredForUser.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_CLONE_PERMISSION);
    Boolean canClone = clonePermissionForRoleNodes.iterator().hasNext() ? false : true;
    Map<String, Boolean> functionPermissionMap = new HashMap<>();
    functionPermissionMap.put(IFunctionPermissionModel.CAN_CLONE, canClone);
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IGetGlobalPermissionForMultipleNatureTypesResponseModel.GLOBAL_PERMISSION,
        response);
    mapToReturn.put(IGetGlobalPermissionForMultipleNatureTypesResponseModel.FUNCTION_PERMISSION, functionPermissionMap);
    return mapToReturn;
  }
}

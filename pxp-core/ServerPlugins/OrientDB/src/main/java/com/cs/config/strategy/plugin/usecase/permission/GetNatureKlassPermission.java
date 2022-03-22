package com.cs.config.strategy.plugin.usecase.permission;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.globalpermissions.INatureKlassWithPermissionrequestModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetNatureKlassPermission extends AbstractOrientPlugin {
  
  public GetNatureKlassPermission(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetNatureKlassPermission/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(INatureKlassWithPermissionrequestModel.KLASS_IDS);
    String loginUserId = (String) requestMap.get(INatureKlassWithPermissionrequestModel.USER_ID);
    Vertex natureKlassNode = getNatureKlassNode(klassIds);
    if (natureKlassNode == null) {
      throw new KlassNotFoundException();
    }
    Vertex userInRole = RoleUtils.getRoleFromUser(loginUserId);
    String natureKlassIds = UtilClass.getCodeNew(natureKlassNode);
    String roleId = UtilClass.getCodeNew(userInRole);
    Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils
        .getKlassIdsHavingReadPermission(userInRole);
    if (klassIdsHavingReadPermission.isEmpty()) {
      Map<String, Object> permissionMap = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(natureKlassIds, roleId);
      return permissionMap;
    }
    else {
      if (!klassIdsHavingReadPermission.contains(natureKlassIds)) {
        Map<String, Object> permissionMap = GlobalPermissionUtils.getNoRoleGlobalPermission();
        return permissionMap;
      }
      else {
        Map<String, Object> permissionMap = GlobalPermissionUtils
            .getKlassAndTaxonomyPermission(natureKlassIds, roleId);
        return permissionMap;
      }
    }
  }
  
  private Vertex getNatureKlassNode(List<String> klassIds) throws Exception
  {
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
      if (isNature) {
        return klassNode;
      }
    }
    return null;
  }
}

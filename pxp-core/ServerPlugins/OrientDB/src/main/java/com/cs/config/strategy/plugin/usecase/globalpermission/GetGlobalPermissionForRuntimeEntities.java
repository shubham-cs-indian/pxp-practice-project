package com.cs.config.strategy.plugin.usecase.globalpermission;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForRuntimeEntitiesResponseModel;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetGlobalPermissionForRuntimeEntities extends AbstractOrientPlugin {
  
  public GetGlobalPermissionForRuntimeEntities(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGlobalPermissionForRuntimeEntities/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IGetGlobalPermissionModel.TAXONOMY_IDS);
    String loginUserId = (String) requestMap.get(IGetGlobalPermissionModel.LOGIN_USER_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(loginUserId);
    String roleId = UtilClass.getCodeNew(userInRole);
    Map<String, Object> globalPermissionsToReturn = new HashMap<>();
    if (!taxonomyIds.isEmpty()) {
      Set<String> taxonomyIdsHavingReadPermission = GlobalPermissionUtils
          .getTaxonomyIdsHavingReadPermission(userInRole);
      if (taxonomyIdsHavingReadPermission.isEmpty()) {
        for (String taxonomyId : taxonomyIds) {
          Map<String, Object> permissionMap = GlobalPermissionUtils
              .getKlassAndTaxonomyPermission(taxonomyId, roleId);
          globalPermissionsToReturn.put(taxonomyId, permissionMap);
        }
      }
      else {
        for (String taxonomyId : taxonomyIds) {
          List<String> parentTaxonomyIds = new ArrayList<String>();
          Vertex rootTaxonomyNode = TaxonomyUtil
              .getRootParentVertexAndFillAllParentTaxonomyIds(taxonomyId, parentTaxonomyIds);
          String taxonomyType = rootTaxonomyNode.getProperty(ITaxonomy.TAXONOMY_TYPE);
          if (taxonomyType.equals(CommonConstants.MINOR_TAXONOMY)) {
            Map<String, Object> permissionMap = GlobalPermissionUtils
                .getAllRightsGlobalPermission();
            globalPermissionsToReturn.put(taxonomyId, permissionMap);
            continue;
          }
          parentTaxonomyIds.add(taxonomyId);
          List<String> duplicateTaxonomyIdsHavingReadPermission = new ArrayList<>(
              taxonomyIdsHavingReadPermission);
          duplicateTaxonomyIdsHavingReadPermission.retainAll(parentTaxonomyIds);
          if (duplicateTaxonomyIdsHavingReadPermission.isEmpty()) {
            Map<String, Object> permissionMap = GlobalPermissionUtils.getNoRoleGlobalPermission();
            globalPermissionsToReturn.put(taxonomyId, permissionMap);
          }
          else {
            for (String taxonomyIdHavingReadPermission : duplicateTaxonomyIdsHavingReadPermission) {
              Map<String, Object> permissionMap = GlobalPermissionUtils
                  .getKlassAndTaxonomyPermission(taxonomyIdHavingReadPermission, roleId);
              globalPermissionsToReturn.put(taxonomyId, permissionMap);
            }
          }
        }
      }
    }
    
    List<String> klassIds = (List<String>) requestMap.get(IGetGlobalPermissionModel.KLASS_IDS);
    Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils
        .getKlassIdsHavingReadPermission(userInRole);
    if (klassIdsHavingReadPermission.isEmpty()) {
      for (String klassId : klassIds) {
        Map<String, Object> permissionMap = GlobalPermissionUtils
            .getKlassAndTaxonomyPermission(klassId, roleId);
        globalPermissionsToReturn.put(klassId, permissionMap);
      }
    }
    else {
      for (String klassId : klassIds) {
        if (!klassIdsHavingReadPermission.contains(klassId)) {
          Map<String, Object> permissionMap = GlobalPermissionUtils.getNoRoleGlobalPermission();
          globalPermissionsToReturn.put(klassId, permissionMap);
        }
        else {
          Map<String, Object> permissionMap = GlobalPermissionUtils
              .getKlassAndTaxonomyPermission(klassId, roleId);
          globalPermissionsToReturn.put(klassId, permissionMap);
        }
      }
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetGlobalPermissionForRuntimeEntitiesResponseModel.GLOBAL_PERMISSIONS,
        globalPermissionsToReturn);
    
    return returnMap;
  }
}

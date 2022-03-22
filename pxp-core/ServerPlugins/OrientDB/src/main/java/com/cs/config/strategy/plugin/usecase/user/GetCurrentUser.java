package com.cs.config.strategy.plugin.usecase.user;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.user.IGetCurrentUserModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetCurrentUser extends AbstractOrientPlugin {
  
  public GetCurrentUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetCurrentUser/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    String userId = (String) requestMap.get("id");
    
    Vertex userNode = null;
    try {
      userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    OrientGraph graph = UtilClass.getGraph();
    Vertex organization = UtilClass.getInternalOrganization(graph);
    
    Map<String, Object> userMap = UtilClass.getMapFromNode(userNode);
    UserUtils.getPreferredLanguages(returnMap, userNode);
    Set<String> allowedEntities = new HashSet<>();
    Boolean isSettingAlllowed = false;
    List<String> physicalCatalogs = null;
    List<String> portals = null;
    Vertex roleNode = RoleUtils.getRoleFromUser(userNode);
    organization = OrganizationUtil.getOrganizationNodeForRole(roleNode);
    List<String> roleEntities = (List<String>) (roleNode.getProperty(IRole.ENTITIES));
    allowedEntities.addAll(roleEntities);
    String roleType = (String) roleNode.getProperty(IRole.ROLE_TYPE);
    String roleId = (String) roleNode.getProperty(CommonConstants.CODE_PROPERTY);
    Boolean isDashboardVisible = (Boolean) roleNode.getProperty(IRole.IS_DASHBOARD_ENABLE);
    String landingScreen = (String) roleNode.getProperty(IRole.LANDING_SCREEN);
    Boolean isReadOnly = (Boolean) roleNode.getProperty(IRole.IS_READ_ONLY);
    
    if (!roleType.equals(CommonConstants.SYSTEM_ADMIN_ROLE_TYPE)) {
      if (allowedEntities.isEmpty()) {
        allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
      }
      if (roleType.equals(CommonConstants.ADMIN)) {
        isSettingAlllowed = true;
      }
      
      physicalCatalogs = (List<String>) roleNode.getProperty(IRole.PHYSICAL_CATALOGS);
      if (physicalCatalogs.isEmpty()) {
        physicalCatalogs = (List<String>) organization.getProperty(IRole.PHYSICAL_CATALOGS);
        if (physicalCatalogs.isEmpty()) {
          physicalCatalogs = GlobalPermissionUtils.getDefaultPhysicalCatalogs();
        }
      }
      portals = (List<String>) roleNode.getProperty(IRole.PORTALS);
      if (portals.isEmpty()) {
        portals = (List<String>) organization.getProperty(IRole.PORTALS);
        if (portals.isEmpty()) {
          portals = GlobalPermissionUtils.getDefaultPortals();
        }
      }
      
    }
    else {
      if (allowedEntities.isEmpty()) {
        allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
      }
      physicalCatalogs = Arrays.asList(Constants.DATA_INTEGRATION_CATALOG_IDS);
      portals = GlobalPermissionUtils.getDefaultPortals();
    }
    
    returnMap.put(IGetCurrentUserModel.USER, userMap);
    returnMap.put(IGetCurrentUserModel.ENTITIES, allowedEntities);
    returnMap.put(IGetCurrentUserModel.IS_SETTING_ALLOWED, isSettingAlllowed);
    returnMap.put(IGetCurrentUserModel.ORGANIZATION_ID, UtilClass.getCodeNew(organization));
    returnMap.put(IGetCurrentUserModel.ALLOWED_PHYSICAL_CATALOG_IDS, physicalCatalogs);
    returnMap.put(IGetCurrentUserModel.ALLOWED_PORTAL_IDS, portals);
    returnMap.put(IGetCurrentUserModel.ROLE_TYPE, roleType);
    returnMap.put(IGetCurrentUserModel.IS_DASHBOARD_ENABLE, isDashboardVisible);
    returnMap.put(IGetCurrentUserModel.LANDING_SCREEN, landingScreen);
    returnMap.put(IUserInformationModel.ROLE_ID, roleId);
    returnMap.put(IGetCurrentUserModel.IS_READ_ONLY, isReadOnly);
    return returnMap;
  }
}

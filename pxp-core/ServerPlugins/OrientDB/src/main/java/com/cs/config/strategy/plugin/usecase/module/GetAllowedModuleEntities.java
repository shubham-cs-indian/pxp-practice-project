package com.cs.config.strategy.plugin.usecase.module;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GetAllowedModuleEntities extends AbstractOrientPlugin {
  
  public GetAllowedModuleEntities(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedModuleEntities/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("id");
    Vertex userNode = null;
    
    try {
      userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    Set<String> allowedEntities = RoleUtils.getAllowedEntities(userNode);
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
    }
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IListModel.LIST, allowedEntities);
    
    return returnMap;
  }
}

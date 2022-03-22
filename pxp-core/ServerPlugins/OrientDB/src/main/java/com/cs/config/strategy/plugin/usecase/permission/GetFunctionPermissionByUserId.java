package com.cs.config.strategy.plugin.usecase.permission;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetFunctionPermissionByUserId extends AbstractOrientPlugin{

  public GetFunctionPermissionByUserId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetFunctionPermissionByUserId/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IIdParameterModel.CURRENT_USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    return GlobalPermissionUtils.getFunctionPermission(roleNode);
  }
  
}

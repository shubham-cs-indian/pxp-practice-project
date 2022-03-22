package com.cs.config.strategy.plugin.usecase.permission;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetFunctionPermissionForRole extends AbstractOrientPlugin {

  public GetFunctionPermissionForRole(OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  @Override
  public String[] getNames() {
    return new String[] { "POST|GetFunctionPermissionForRole/*" };
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception {
    String roleId = (String) requestMap.get(IIdParameterModel.ID);
    Vertex roleNode = UtilClass.getVertexByIndexedId(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    Map<String, Boolean> response = GlobalPermissionUtils.getFunctionPermission(roleNode);
    
    return response;
  }

}


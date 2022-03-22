package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.role.abstrct.AbstractGetRole;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class GetRole extends AbstractGetRole {
  
  public GetRole(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    String roleId = null;
    HashMap<String, Object> roleMapToReturn = new HashMap<String, Object>();
    
    roleId = (String) map.get("id");
    roleMapToReturn = getRole(map, roleId, VertexLabelConstants.ENTITY_TYPE_ROLE,
        CommonConstants.ROLE);
    
    return roleMapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRole/*" };
  }
}

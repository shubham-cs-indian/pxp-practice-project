package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.role.abstrct.AbstractGetRolesForCurrentUser;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class GetRoleIdsForCurrentUser extends AbstractGetRolesForCurrentUser {
  
  public GetRoleIdsForCurrentUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    String userId = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    userId = (String) map.get("id");
    
    getRolesForCurrentUser(map, userId, returnMap);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRoleIdsForCurrentUser/*" };
  }
}

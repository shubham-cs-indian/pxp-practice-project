package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.role.abstrct.AbstractGetRolesAndKlassesForUser;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class GetRolesAndKlassesForUser extends AbstractGetRolesAndKlassesForUser {
  
  public GetRolesAndKlassesForUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    String loginUserId = null;
    loginUserId = (String) map.get("id");
    
    mapToReturn = getRolesAndKlassForUser(map, loginUserId);
    
    return mapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRolesAndKlassesForUser/*" };
  }
}

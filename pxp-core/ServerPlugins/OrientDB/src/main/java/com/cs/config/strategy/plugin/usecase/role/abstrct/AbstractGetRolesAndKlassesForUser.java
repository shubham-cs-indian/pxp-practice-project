package com.cs.config.strategy.plugin.usecase.role.abstrct;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractGetRolesAndKlassesForUser extends AbstractOrientPlugin {
  
  public AbstractGetRolesAndKlassesForUser(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public Map<String, Object> getRolesAndKlassForUser(Map<String, Object> requestMap,
      String loginUserId) throws Exception
  {
    List<String> klassIds = GlobalPermissionUtils.getKlassesForUser(loginUserId);
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put("klassIds", klassIds);
    
    return mapToReturn;
  }
}

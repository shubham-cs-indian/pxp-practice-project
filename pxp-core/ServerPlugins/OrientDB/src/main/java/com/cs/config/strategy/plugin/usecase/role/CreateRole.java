package com.cs.config.strategy.plugin.usecase.role;

import com.cs.config.strategy.plugin.usecase.role.abstrct.AbstractCreateRole;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class CreateRole extends AbstractCreateRole {
  
  public CreateRole(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> roleMapToReturn = new HashMap<String, Object>();
    String label = VertexLabelConstants.ENTITY_TYPE_ROLE;
    roleMapToReturn = create(map, CommonConstants.ROLE, label);
    Map<String, Object> roleMap = (Map<String, Object>) roleMapToReturn.get(CommonConstants.ROLE);
    // OnboardingRoleUtils.createEndpointForOnboardingRole((String)
    // roleMap.get(IRole.ID));
    
    return roleMapToReturn;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateRole/*" };
  }
}

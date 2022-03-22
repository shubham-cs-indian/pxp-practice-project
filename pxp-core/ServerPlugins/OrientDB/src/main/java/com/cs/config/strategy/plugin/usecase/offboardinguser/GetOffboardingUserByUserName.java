package com.cs.config.strategy.plugin.usecase.offboardinguser;

import com.cs.config.strategy.plugin.usecase.boardinguser.abstrct.AbstractGetBoardingUser;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetOffboardingUserByUserName extends AbstractGetBoardingUser {
  
  public GetOffboardingUserByUserName(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    return getBoardingUser(requestMap);
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|GetOffboardingUserByUserName/*" };
  }
}

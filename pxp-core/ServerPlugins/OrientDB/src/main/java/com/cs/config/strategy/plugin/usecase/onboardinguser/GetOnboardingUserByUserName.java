package com.cs.config.strategy.plugin.usecase.onboardinguser;

import com.cs.config.strategy.plugin.usecase.boardinguser.abstrct.AbstractGetBoardingUser;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetOnboardingUserByUserName extends AbstractGetBoardingUser {
  
  public GetOnboardingUserByUserName(final OServerCommandConfiguration iConfiguration)
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
    
    return new String[] { "POST|GetOnboardingUserByUserName/*" };
  }
}

package com.cs.core.runtime.interactor.model.offboarding;

import java.util.HashMap;
import java.util.Map;

public class OffBoardingTransferRequestModel extends OffboardingKlassInstancesRequestModel
    implements IOffBoardingTransferRequestModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected Map<String, String> userEndpointMap  = new HashMap<>();
  
  @Override
  public Map<String, String> getUserEndpointMap()
  {
    return this.userEndpointMap;
  }
  
  @Override
  public void setUserEndpointMap(Map<String, String> userEndpointMap)
  {
    this.userEndpointMap = userEndpointMap;
  }
}

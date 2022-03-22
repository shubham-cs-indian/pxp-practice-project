package com.cs.core.runtime.interactor.model.offboarding;

import java.util.Map;

public interface IOffBoardingTransferRequestModel extends IOffboardingKlassInstancesRequestModel {
  
  public static final String USER_ENDPOINT_MAP = "userEndpointMap";
  
  public Map<String, String> getUserEndpointMap();
  
  public void setUserEndpointMap(Map<String, String> userEndPointMap);
}

package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetEndpointsAndOrganisationIdRequestModel extends IModel {
  
  public static String PROCESS_IDS = "processIds";
  public static String USER_ID     = "userId";
  
  public List<String> getProcessIds();
  
  public void setProcessIds(List<String> processIds);
  
  public String getUserId();
  
  public void setUserId(String userId);
}

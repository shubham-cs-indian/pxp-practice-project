package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetGridEndpointsResponseModel extends IModel {
  
  public static final String ENDPOINTS_LIST = "endpointsList";
  public static final String COUNT          = "count";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public List<IGetEndpointModel> getEndpointsList();
  
  public void setEndpointsList(List<IGetEndpointModel> taskList);
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public IConfigDetailsForGridEndpointsModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGridEndpointsModel configDetails);
}

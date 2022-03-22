package com.cs.core.config.interactor.model.role;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;

import java.util.List;

public interface ISystemsVsEndpointsModel extends IIdLabelCodeModel {
  
  public static final String ENDPOINT_IDS = "endpointIds";
  
  public List<String> getEndpointIds();
  
  public void setEndpointIds(List<String> endpointIds);
}

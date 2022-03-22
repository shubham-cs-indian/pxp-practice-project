package com.cs.core.config.interactor.model.role;

import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;

import java.util.ArrayList;
import java.util.List;

public class SystemsVsEndpointsModel extends IdLabelCodeModel implements ISystemsVsEndpointsModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    endpointIds      = new ArrayList<>();
  
  @Override
  public List<String> getEndpointIds()
  {
    return endpointIds;
  }
  
  @Override
  public void setEndpointIds(List<String> endpointIds)
  {
    this.endpointIds = endpointIds;
  }
}

package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

public class GetEntityConfigurationRequestModel implements IGetEntityConfigurationRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    ids;
  
  public GetEntityConfigurationRequestModel()
  {
  }
  
  public GetEntityConfigurationRequestModel(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public List<String> getIds()
  {
    return ids != null ? ids : new ArrayList<String>();
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
}

package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.interactor.model.organization.OrganizationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllOrganizationResponseModel implements IGetAllOrganizationResponseModel {
  
  private static final long          serialVersionUID = 1L;
  protected List<IOrganizationModel> list;
  protected Long                     count;
  
  public List<IOrganizationModel> getList()
  {
    return list;
  }
  
  @JsonDeserialize(contentAs = OrganizationModel.class)
  public void setList(List<IOrganizationModel> list)
  {
    this.list = list;
  }
  
  public Long getCount()
  {
    return count;
  }
  
  public void setCount(Long count)
  {
    this.count = count;
  }
}

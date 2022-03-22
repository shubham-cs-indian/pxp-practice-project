package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;

import java.util.ArrayList;
import java.util.List;

public class GetConfigDetailsForMatchedInstancesRequestModel extends MulticlassificationRequestModel
    implements IGetConfigDetailsForMatchedInstancesRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    organizationIds;
  
  @Override
  public List<String> getOrganizationIds()
  {
    if (organizationIds == null) {
      organizationIds = new ArrayList<>();
    }
    return organizationIds;
  }
  
  @Override
  public void setOrganizationIds(List<String> organizationIds)
  {
    this.organizationIds = organizationIds;
  }
}

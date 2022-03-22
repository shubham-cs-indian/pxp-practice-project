package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

import java.util.List;

public interface IGetConfigDetailsForMatchedInstancesRequestModel
    extends IMulticlassificationRequestModel {
  
  public static final String ORGANIZATION_IDS = "organizationIds";
  
  public List<String> getOrganizationIds();
  
  public void setOrganizationIds(List<String> organizationIds);
}

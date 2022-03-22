package com.cs.core.config.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllRelationshipsResponseModel extends IModel {
  
  public static final String LIST           = "list";
  public static final String COUNT          = "count";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public Long getCount();
  
  public void setCount(Long count);
  
  public List<IRelationshipInformationModel> getList();
  
  public void setList(List<IRelationshipInformationModel> list);
  
  public IConfigDetailsForRelationshipModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForRelationshipModel configDetails);
}

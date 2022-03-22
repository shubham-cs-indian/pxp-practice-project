package com.cs.core.config.interactor.model.summary;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISummaryModel extends IModel {
  
  public static final String CREATED_ATTRIBUTES = "createdAttributes";
  public static final String CREATED_TAGS       = "createdTags";
  public static final String CREATED_USERS      = "createdUsers";
  public static final String CREATED_ROLES      = "createdRoles";
  
  public List<ISummaryInformationModel> getCreatedAttributes();
  
  public void setCreatedAttributes(List<ISummaryInformationModel> createdAttributes);
  
  public List<ISummaryInformationModel> getCreatedTags();
  
  public void setCreatedTags(List<ISummaryInformationModel> createdTags);
  
  public List<ISummaryInformationModel> getCreatedUsers();
  
  public void setCreatedUsers(List<ISummaryInformationModel> createdUsers);
  
  public List<ISummaryInformationModel> getCreatedRoles();
  
  public void setCreatedRoles(List<ISummaryInformationModel> createdRoles);
}

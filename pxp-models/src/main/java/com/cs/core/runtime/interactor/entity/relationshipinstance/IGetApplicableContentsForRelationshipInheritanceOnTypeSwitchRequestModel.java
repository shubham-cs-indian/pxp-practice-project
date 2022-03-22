package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetApplicableContentsForRelationshipInheritanceOnTypeSwitchRequestModel
    extends IModel {
  
  public static final String CONTENT_ID     = "contentId";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel getConfigDetails();
  
  public void setConfigDetails(
      IConfigDetailsForRelationshipInheritanceOnTypeSwitchResponseModel configDetails);
}

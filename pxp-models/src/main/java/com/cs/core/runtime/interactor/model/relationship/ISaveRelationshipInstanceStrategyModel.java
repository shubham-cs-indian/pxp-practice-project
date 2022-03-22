package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveRelationshipInstanceStrategyModel extends IModel {
  
  public static final String RELATIONSHIP_ADM = "relationshipAdm";
  public static final String CONFIG_DETAILS   = "configDetails";
  
  public ISaveRelationshipInstanceModel getRelationshipAdm();
  
  public void setRelationshipAdm(ISaveRelationshipInstanceModel relationshipAdm);
  
  public IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails();
  
  public void setConfigDetails(
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails);
}

package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configdetails.IGetConfigDetailsForSaveRelationshipInstancesResponseModel;

public class SaveRelationshipInstanceStrategyModel
    implements ISaveRelationshipInstanceStrategyModel {
  
  private static final long                                            serialVersionUID = 1L;
  protected ISaveRelationshipInstanceModel                             relationshipAdm;
  protected IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails;
  
  @Override
  public ISaveRelationshipInstanceModel getRelationshipAdm()
  {
    return relationshipAdm;
  }
  
  @Override
  public void setRelationshipAdm(ISaveRelationshipInstanceModel relationshipAdm)
  {
    this.relationshipAdm = relationshipAdm;
  }
  
  @Override
  public IGetConfigDetailsForSaveRelationshipInstancesResponseModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  public void setConfigDetails(
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails)
  {
    this.configDetails = configDetails;
  }
}

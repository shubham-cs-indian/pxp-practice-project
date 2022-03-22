package com.cs.core.config.interactor.model.relationship;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetAllRelationshipsResponseModel implements IGetAllRelationshipsResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  protected List<IRelationshipInformationModel> list;
  protected Long                                count;
  protected IConfigDetailsForRelationshipModel  configDetails;
  
  @Override
  public List<IRelationshipInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = RelationshipInformationModel.class)
  public void setList(List<IRelationshipInformationModel> list)
  {
    this.list = list;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
  
  @Override
  public IConfigDetailsForRelationshipModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForRelationshipModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForRelationshipModel configDetails)
  {
    this.configDetails = configDetails;
  }
}

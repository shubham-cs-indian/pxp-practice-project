package com.cs.core.config.interactor.model.summary;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SummaryModel implements ISummaryModel {
  
  protected List<ISummaryInformationModel> createdAttributes;
  protected List<ISummaryInformationModel> createdTags;
  protected List<ISummaryInformationModel> createdUsers;
  protected List<ISummaryInformationModel> createdRoles;
  
  @Override
  public List<ISummaryInformationModel> getCreatedAttributes()
  {
    if (createdAttributes == null) {
      createdAttributes = new ArrayList<>();
    }
    return createdAttributes;
  }
  
  @JsonDeserialize(contentAs = SummaryInformationModel.class)
  @Override
  public void setCreatedAttributes(List<ISummaryInformationModel> createdAttributes)
  {
    this.createdAttributes = createdAttributes;
  }
  
  @Override
  public List<ISummaryInformationModel> getCreatedTags()
  {
    if (createdTags == null) {
      createdTags = new ArrayList<>();
    }
    return createdTags;
  }
  
  @JsonDeserialize(contentAs = SummaryInformationModel.class)
  @Override
  public void setCreatedTags(List<ISummaryInformationModel> createdTags)
  {
    this.createdTags = createdTags;
  }
  
  @Override
  public List<ISummaryInformationModel> getCreatedUsers()
  {
    if (createdUsers == null) {
      createdUsers = new ArrayList<>();
    }
    return createdUsers;
  }
  
  @JsonDeserialize(contentAs = SummaryInformationModel.class)
  @Override
  public void setCreatedUsers(List<ISummaryInformationModel> createdUsers)
  {
    this.createdUsers = createdUsers;
  }
  
  @Override
  public List<ISummaryInformationModel> getCreatedRoles()
  {
    if (createdRoles == null) {
      createdRoles = new ArrayList<>();
    }
    return createdRoles;
  }
  
  @JsonDeserialize(contentAs = SummaryInformationModel.class)
  @Override
  public void setCreatedRoles(List<ISummaryInformationModel> createdRoles)
  {
    this.createdRoles = createdRoles;
  }
}

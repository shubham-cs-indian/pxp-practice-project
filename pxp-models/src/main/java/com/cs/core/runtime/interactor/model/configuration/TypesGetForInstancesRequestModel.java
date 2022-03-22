package com.cs.core.runtime.interactor.model.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TypesGetForInstancesRequestModel implements ITypesGetForInstancesRequestModel {
  
  private static final long       serialVersionUID = 1L;
  protected List<IIdAndTypeModel> contentInfo;
  protected Boolean               isDeleteFromArchivalPortal;
  
  @Override
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  public List<IIdAndTypeModel> getContentInfo()
  {
    if (contentInfo == null) {
      contentInfo = new ArrayList<>();
    }
    return contentInfo;
  }
  
  @Override
  public void setContentInfo(List<IIdAndTypeModel> contentInfo)
  {
    this.contentInfo = contentInfo;
  }
  
  @Override
  public Boolean getIsDeleteFromArchivalPortal()
  {
    if (isDeleteFromArchivalPortal == null) {
      isDeleteFromArchivalPortal = false;
    }
    return isDeleteFromArchivalPortal;
  }
  
  @Override
  public void setIsDeleteFromArchivalPortal(Boolean isDeleteFromArchivalPortal)
  {
    this.isDeleteFromArchivalPortal = isDeleteFromArchivalPortal;
  }
}

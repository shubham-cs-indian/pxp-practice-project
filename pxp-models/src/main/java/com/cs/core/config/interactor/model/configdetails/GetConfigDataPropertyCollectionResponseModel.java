package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.model.propertycollection.PropertyCollectionBasicInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDataPropertyCollectionResponseModel extends GetConfigDataEntityResponseModel
    implements IGetConfigDataPropertyCollectionResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  @Override
  public List<IConfigEntityInformationModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyCollectionBasicInfoModel.class)
  public void setList(List<IConfigEntityInformationModel> list)
  {
    this.list = list;
  }
}
